package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.hpi.krestel.mySearchEngine.Log.Level;

public class AlexMain {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLStreamException {
//		PartialIndex index = new PartialIndex();
//		
//		Term term = new Term("Testing");
//		term.addOccurence(new TermOccurrence(3, 5));
//		term.addOccurence(new TermOccurrence(4, 1));
//		term.addOccurence(new TermOccurrence(4, 1001));
//		
//		Term term2 = new Term("HPI");
//		term2.addOccurence(new TermOccurrence(1003, 5));
//		term2.addOccurence(new TermOccurrence(3000, 1));
//		term2.addOccurence(new TermOccurrence(3000, 1001));
//		
//		index.addTerm(term);
//		index.addTerm(term2);
//		index.addTerm(term2);
//		index.addOccurenceForTerm("HPI", new TermOccurrence(200, 4));
//
//		index.store(".");		
//		System.out.println("[DONE]");
		
		Parser p = new ParserImpl("/small.xml");
		p.parseToPartialIndexes(".");
		
		// Read whole file
		String filename = "0.dat";
		Log.log(Level.DEBUG, "Printing file " + filename);
		IndexFileHandler fileHandler = new IndexFileHandlerImpl(filename);
		Term t = fileHandler.readNextTerm();
		while (t != null) {
			System.out.println(t.toString());
			t = fileHandler.readNextTerm();
		}
	}

}
