package de.hpi.krestel.mySearchEngine.index.term;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexList;


public class Term extends IndexList<String, TermOccurrence, Integer> {
	public Term(String term) {
		super(term);
	}
	
	public Term(String term, SortedSet<TermOccurrence> occurrences) {
		super(term, occurrences);
	}
}
