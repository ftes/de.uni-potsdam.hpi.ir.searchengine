package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

public class AlexMain {

	public static void main(String[] args) throws IOException {
		IndexFileHandler fileHandler = new IndexFileHandlerImpl("test.dat");
		
		Term term = new Term("Testing");
		term.addOccurence(new TermOccurrence(3, 5));
		term.addOccurence(new TermOccurrence(4, 1));
		term.addOccurence(new TermOccurrence(4, 1001));
		
		Term term2 = new Term("HPI");
		term2.addOccurence(new TermOccurrence(1003, 5));
		term2.addOccurence(new TermOccurrence(3000, 1));
		term2.addOccurence(new TermOccurrence(3000, 1001));
		
		fileHandler.storeTerm(term);
		fileHandler.storeTerm(term2);
		fileHandler.close();
		fileHandler = null;
		
		System.out.println("[DONE]");
		
		IndexFileHandler fileHandler2 = new IndexFileHandlerImpl("test.dat");
		Term t;
		t = fileHandler2.readNextTerm();
		System.out.println(t.toString());
		t = fileHandler2.readNextTerm();
		System.out.println(t.toString());
		t = fileHandler2.readNextTerm();
	}

}
