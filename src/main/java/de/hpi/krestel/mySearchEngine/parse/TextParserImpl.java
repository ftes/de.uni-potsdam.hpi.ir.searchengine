package de.hpi.krestel.mySearchEngine.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamReader;

import de.hpi.krestel.mySearchEngine.index.PageIndex;
import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.io.PageIndexWriter;
import de.hpi.krestel.mySearchEngine.util.Util;

public class TextParserImpl extends BaseParser {
	public static final int N_THREADS = 4;
	
	private static final Pattern redirect = Pattern.compile("\\A#REDIRECT \\[\\[.*\\]\\]\\z");
	private static final int maxSeconds = 60;
	
	private Page page;
	private boolean inRevision;
	private long numArticles = 0;
	private static final long printInterval = 10;
	private Date start;
	private List<ParserThread> threads;
	private Stack<Page> buffer;
	private PageIndexWriter pageWriter;
	private PageIndex pageIndex;
	
	private final String stemmedPartialDir;
	private final String unstemmedPartialDir;
	private final String linksPartialDir;
	private final String pageIndexFile;
	private final String pageFile;
	
	private final TitleIndex titleIndex;
	
	public TextParserImpl(InputStream in, TitleIndex titleIndex,
			String stemmedPartialDir, String unstemmedPartialDir,
			String linksPartialDir, String pageIndexFile, String pageFile) {
		super(in);
		this.stemmedPartialDir = stemmedPartialDir;
		this.unstemmedPartialDir = unstemmedPartialDir;
		this.linksPartialDir = linksPartialDir;
		this.pageIndexFile = pageIndexFile;
		this.pageFile = pageFile;
		this.titleIndex = titleIndex;
	}
	
	@Override
	protected void startElement(String tag, XMLStreamReader parser) throws Exception {
		if (tag.equals("page")) {
			page = new Page();
			numArticles++;
			if (numArticles % printInterval == 0) {
				long elapsed = new Date().getTime() - start.getTime();
				int eta = (int) ((elapsed / numArticles) * 3.3e6 / 1000 / 60 / 60);
				System.out.printf("%d articles, %d seconds, ETA: %d hours\n", numArticles, (int) elapsed / 1000, eta);
				
				if (maxSeconds != -1 && elapsed > maxSeconds * 1000) {
					done = true;
				}
			}
		} else if (tag.equals("revision")) {
			inRevision = true;
		} else if (tag.equals("text")) {
			String text = parser.getElementText();
			page.setText(text);
			long offset = pageWriter.store(page);
			pageIndex.addOffset(page.getId(), offset);
		} else if (!inRevision && tag.equals("id")) {
			page.setId(Integer.parseInt(parser.getElementText()));
		} else if (!inRevision && tag.equals("title")) {
			page.setTitle(parser.getElementText());
		}
	}
	
	@Override
	protected void endElement(String tag, XMLStreamReader parser) {
		if (tag.equals("page")) {
			if (numArticles % 50 == 0) {
				if (Util.isMainMemoryFull()){
					System.out.println("MEMORY FULL!");
					for (ParserThread thread : threads) {
						thread.requestWrite();
					}
				}
			}
			
			//don't include redirect pages
			if (! redirect.matcher(page.getText()).matches()) {
				synchronized (buffer) {
					while (buffer.size() >= N_THREADS*2) {
						try {
							buffer.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					buffer.add(page);
					buffer.notifyAll();
				}
			}
		} else if (tag.equals("revision")) {
			inRevision = false;
		}
	}

	@Override
	public void parse() throws Exception {
		pageWriter = new PageIndexWriter(pageFile);
		pageIndex = new PageIndex();
		start = new Date();
		
		buffer = new Stack<>();
		threads = new ArrayList<>();
		for (int i=0; i<N_THREADS; i++) {
			threads.add(new ParserThread(buffer, titleIndex, stemmedPartialDir,
					unstemmedPartialDir, linksPartialDir));
			threads.get(i).start();
		}
		
		super.parse();

		int bufferSize = 0;
		do {
			synchronized (buffer) {
				bufferSize = buffer.size();
				if (bufferSize > 0) {
					try {
						buffer.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} while (bufferSize > 0);
		
		for (ParserThread thread : threads) {
			thread.sendCancellation();
		}
		
		for (ParserThread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		pageIndex.exportFile(pageIndexFile);
		pageWriter.close();
	}
}
