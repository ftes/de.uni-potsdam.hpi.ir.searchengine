package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ParserThread extends Thread {
	private boolean done = false;
	private final String stemmedPartialDir;
	private final String unstemmedPartialDir;
	
	private int n = 0;

	private final Stack<Page> buffer;

	public ParserThread(Stack<Page> buffer, String stemmedPartialDir, String unstemmedPartialDir) {
		this.buffer = buffer;
		this.stemmedPartialDir = stemmedPartialDir;
		this.unstemmedPartialDir = unstemmedPartialDir;
	}
	
	private String getFileName() {
		return Thread.currentThread().getId() + "-" + n;
	}

	@Override
	public void run() {
		PartialIndex unstemmedIndex = new PartialIndex(getFileName());
		PartialIndex stemmedIndex = new PartialIndex(getFileName());
		
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
			
			addToIndex(page, stemmedIndex, true);
			addToIndex(page, unstemmedIndex, false);

			if (Util.isMainMemoryFull()){
				System.out.println("MEMORY FULL!");
				System.out.println("Writing " + stemmedPartialDir + "/" + getFileName());
				System.out.println("Writing " + unstemmedPartialDir + "/" + getFileName());
				// write to disk and remove references
				try {
					stemmedIndex.store(stemmedPartialDir);
					unstemmedIndex.store(unstemmedPartialDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
				n++;
				stemmedIndex = new PartialIndex(getFileName());
				unstemmedIndex = new PartialIndex(getFileName());
				Util.runGarbageCollector();
			}
		}
		
		try {
			stemmedIndex.store(stemmedPartialDir);
			unstemmedIndex.store(unstemmedPartialDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addToIndex(Page page, PartialIndex index, boolean stem) {
		Tokenizer tokenizer = new Tokenizer(page.getText());
		ArrayList<String> tokens = tokenizer.tokenize(stem);
		
		int tokenPosition = 0;
		for (String token : tokens) {
			index.addOccurenceForTerm(token, new TermOccurrence(page.getId(), tokenPosition));
			tokenPosition++;
		}
	}

	public void sendCancellation() {
		done = true;
	}
}
