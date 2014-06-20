package de.hpi.krestel.mySearchEngine.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.link.DocumentWithLinks;
import de.hpi.krestel.mySearchEngine.index.link.Link;
import de.hpi.krestel.mySearchEngine.index.link.LinkIndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;
import de.hpi.krestel.mySearchEngine.util.Util;

public class ParserThread extends Thread {
	private boolean done = false;
	private boolean writeRequested = false;
	private final String stemmedPartialDir;
	private final String unstemmedPartialDir;
	private final String linksPartialDir;
	private PartialIndex<String, Term, TermOccurrence, Integer> unstemmedIndex;
	private PartialIndex<String, Term, TermOccurrence, Integer> stemmedIndex;
	private PartialIndex<Integer, DocumentWithLinks, Link, String> linkIndex;
	private final IndexFileHandlerFactory<String, Term, TermOccurrence, Integer> termFactory;
	private final IndexFileHandlerFactory<Integer, DocumentWithLinks, Link, String> linkFactory;
	private final TitleIndex titleIndex;
	
	private int n = 0;

	private final Stack<Page> buffer;

	public ParserThread(Stack<Page> buffer, TitleIndex titleIndex, String stemmedPartialDir,
			String unstemmedPartialDir, String linksPartialDir) {
		this.buffer = buffer;
		this.stemmedPartialDir = stemmedPartialDir;
		this.unstemmedPartialDir = unstemmedPartialDir;
		this.linksPartialDir = linksPartialDir;
		this.termFactory = new TermIndexFileHandlerFactory();
		this.linkFactory = new LinkIndexFileHandlerFactory();
		this.titleIndex = titleIndex;
	}
	
	private String getFilePath(String dir) {
		return dir + File.separator + Thread.currentThread().getId() + "-" + n + ".dat";
	}
	
	private void createIndexes() {
		try {
			stemmedIndex = termFactory.createPartialIndex(getFilePath(stemmedPartialDir));
			unstemmedIndex = termFactory.createPartialIndex(getFilePath(unstemmedPartialDir));
			if (linksPartialDir != null) {
				linkIndex = linkFactory.createPartialIndex(getFilePath(linksPartialDir));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		createIndexes();
		
		while (! done) {
			Page page = null;
			synchronized (buffer) {
				if (buffer.size() > 0) {
					page = buffer.pop();
					buffer.notifyAll();
				} else {
					try {
						buffer.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (page == null) {
				continue;
			}
			
			addToTermOccurrenceIndex(page, stemmedIndex, true);
			addToTermOccurrenceIndex(page, unstemmedIndex, false);
			if (linkIndex != null) {
				addToLinkIndex(page);
			}

			if (writeRequested) {
				write();
				createIndexes();
				Util.runGarbageCollector();
				writeRequested = false;
			}
		}
		
		write();
	}
	
	private void write() {
		System.out.println("Writing " + getFilePath(stemmedPartialDir));
		System.out.println("Writing " + getFilePath(unstemmedPartialDir));
		if (linkIndex != null) {
			System.out.println("Writing " + getFilePath(linksPartialDir));
		}
		// write to disk and remove references
		try {
			stemmedIndex.store();
			unstemmedIndex.store();
			if (linkIndex != null) {
				linkIndex.store();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		n++;
	}
	
	private void addToTermOccurrenceIndex(Page page, PartialIndex<String, Term, TermOccurrence, Integer> index, boolean stem) {
		Tokenizer tokenizer = new Tokenizer(page.getText());
		ArrayList<String> tokens = tokenizer.tokenize(stem);
		
		int tokenPosition = 0;
		for (String token : tokens) {
			index.addSlotForKey(token, new TermOccurrence(page.getId(), tokenPosition));
			tokenPosition++;
		}
	}
	
	private void addToLinkIndex(Page page) {
		for (Util.Pair<String, String> linkPair : new Tokenizer(page.getText()).getLinks()) {
			Integer linkedToDocId = titleIndex.getDocId(linkPair.a);
			if (linkedToDocId == null) {
				//doc id for title unknown
//				Log.log(Log.Level.DEBUG, "Unknown title \"" + linkPair.a +
//						"\" during link index generation");
				continue;
			}
			// System.out.println(linkPair);
			int linkedFromDocId = page.getId();
			String anchorText = linkPair.b;
			Link link = new Link(linkedFromDocId, anchorText);
			linkIndex.addSlotForKey(linkedToDocId, link);
		}
	}

	public void sendCancellation() {
		done = true;
	}
	
	public void requestWrite() {
		writeRequested = true;
	}
}
