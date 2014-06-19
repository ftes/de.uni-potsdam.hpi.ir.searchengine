package de.hpi.krestel.mySearchEngine.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;
import de.hpi.krestel.mySearchEngine.util.Util;

public class ParserThread extends Thread {
	private boolean done = false;
	private boolean writeRequested = false;
	private final String stemmedPartialDir;
	private final String unstemmedPartialDir;
	private PartialIndex<String, Term, TermOccurrence, Integer> unstemmedIndex;
	private PartialIndex<String, Term, TermOccurrence, Integer> stemmedIndex;
	private final IndexFileHandlerFactory<String, Term, TermOccurrence, Integer> indexFileHandlerFactory;
	
	private int n = 0;

	private final Stack<Page> buffer;

	public ParserThread(Stack<Page> buffer, String stemmedPartialDir, String unstemmedPartialDir) {
		this.buffer = buffer;
		this.stemmedPartialDir = stemmedPartialDir;
		this.unstemmedPartialDir = unstemmedPartialDir;
		this.indexFileHandlerFactory = new TermIndexFileHandlerFactory();
	}
	
	private String getFilePath(String dir) {
		return dir + File.separator + Thread.currentThread().getId() + "-" + n + ".dat";
	}

	@Override
	public void run() {
		try {
			stemmedIndex = indexFileHandlerFactory.createPartialIndex(getFilePath(stemmedPartialDir));
			unstemmedIndex = indexFileHandlerFactory.createPartialIndex(getFilePath(unstemmedPartialDir));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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

			if (writeRequested) {
				write();
				try {
					stemmedIndex = indexFileHandlerFactory.createPartialIndex(getFilePath(stemmedPartialDir));
					unstemmedIndex = indexFileHandlerFactory.createPartialIndex(getFilePath(unstemmedPartialDir));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Util.runGarbageCollector();
				writeRequested = false;
			}
		}
		
		write();
	}
	
	private void write() {
		System.out.println("Writing " + getFilePath(stemmedPartialDir));
		System.out.println("Writing " + getFilePath(unstemmedPartialDir));
		// write to disk and remove references
		try {
			stemmedIndex.store(stemmedPartialDir);
			unstemmedIndex.store(unstemmedPartialDir);
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

	public void sendCancellation() {
		done = true;
	}
	
	public void requestWrite() {
		writeRequested = true;
	}
}
