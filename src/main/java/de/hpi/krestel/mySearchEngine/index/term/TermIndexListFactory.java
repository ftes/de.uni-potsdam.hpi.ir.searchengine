package de.hpi.krestel.mySearchEngine.index.term;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexListFactory;

public class TermIndexListFactory implements
		IndexListFactory<String, Term, TermOccurrence, Integer> {

	@Override
	public Term createList(String key) {
		return new Term(key);
	}

	@Override
	public TermOccurrence createSlot(int docId, Integer value) {
		return new TermOccurrence(docId, value);
	}
	
	@Override
	public Term createList(String key, SortedSet<TermOccurrence> slots) {
		return new Term(key, slots);
	}
}
