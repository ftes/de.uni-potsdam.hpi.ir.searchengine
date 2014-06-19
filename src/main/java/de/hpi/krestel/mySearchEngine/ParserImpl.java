package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ParserImpl extends Parser {
	public static final int N_THREADS = 4;
	
	private static final Pattern redirect = Pattern.compile("\\A#REDIRECT \\[\\[.*\\]\\]\\z");
	private TitleIndex titleIndex = new TitleIndex();
	
	public ParserImpl(InputStream in) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(in);
	}

	@Override
	public void parseToPartialIndexes(String stemmedPartialDir, String unstemmedPartialDir, String pageIndexFile, String pageFile)
			throws XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, FactoryConfigurationError, IOException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(in);

//		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;

		boolean inRevision = false;

		PageIndexWriter pageWriter = new PageIndexWriter(pageFile);
		PageIndex pageIndex = new PageIndex();

		long numArticles = 0;
		long printInterval = 10;
		Date start = new Date();
		
		final Stack<Page> buffer = new Stack<>();
		List<ParserThread> threads = new ArrayList<>();
		for (int i=0; i<N_THREADS; i++) {
			threads.add(new ParserThread(buffer, stemmedPartialDir, unstemmedPartialDir));
			threads.get(i).start();
		}
		
		// read from stream
		boolean done = false;
		while (! done && parser.hasNext()) {
			switch (parser.getEventType()) {
			case XMLStreamConstants.START_DOCUMENT:
				break;

			case XMLStreamConstants.END_DOCUMENT:
				parser.close();
				break;

			case XMLStreamConstants.START_ELEMENT:
				tag = parser.getLocalName();
				if (tag.equals("page")) {
					page = new Page();
					numArticles++;
					if (numArticles % printInterval == 0) {
						long elapsed = new Date().getTime() - start.getTime();
						int eta = (int) ((elapsed / numArticles) * 3.3e6 / 1000 / 60 / 60);
						System.out.printf("%d articles, %d seconds, ETA: %d hours\n", numArticles, (int) elapsed / 1000, eta);
						
						if (elapsed > 5 * 1000) {
							done = true;
						}
					}
				} else if (tag.equals("revision")) {
					inRevision = true;
				} else if (tag.equals("text")) {
					
					// parses whole text and checks for end event
					characters = parser.getElementText();
					
					page.setText(characters);
					
					long offset = pageWriter.store(page);
					pageIndex.addOffset(page.getId(), offset);
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				characters = parser.getText();
				break;

			case XMLStreamConstants.END_ELEMENT:
				tag = parser.getLocalName();
				if (!inRevision && tag.equals("id")) {
					page.setId(Integer.parseInt(characters));
				} else if (!inRevision && tag.equals("title")) {
					page.setTitle(characters);
				} else if (tag.equals("page")) {
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
						titleIndex.addTitle(page.getTitle(), page.getId());
					}
				} else if (tag.equals("revision")) {
					inRevision = false;
				}
				break;

			default:
				break;
			}
			
			
			parser.next();
		}

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
	
	@Override
	public TitleIndex getTitleIndex() {
		return titleIndex;
	}

}
