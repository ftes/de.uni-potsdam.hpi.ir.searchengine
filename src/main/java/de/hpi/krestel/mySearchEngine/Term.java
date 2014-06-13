package de.hpi.krestel.mySearchEngine;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Term {
	/** 
	 * the tokenized term
	 */
	private String term;
	/**
	 * a list of occurences, without any duplicates
	 */
	private SortedSet<TermOccurrence> occurrences;
	
	public Term(String term) {
		this(term, new TreeSet<TermOccurrence>(TermOccurrenceComparator.INSTANCE));
	}
	
	public Term(String term, SortedSet<TermOccurrence> occurrences) {
		this.term = term;
		this.occurrences = occurrences;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	/**
	 * Returns a list of occurences, sorted first by document id and then by position within
	 * that document.
	 */
	public SortedSet<TermOccurrence> getOccurrences() {
		return occurrences;
	}
	
	public int getOccurenceCountFor(int docId) {
		int count = 0;
		// TODO: Make that faster
		for (TermOccurrence occurence : occurrences) {
			if (occurence.getDocumentId() == docId) count++;
		}
		return count;
	}
	
	public Set<Integer> getDocumentIds() {
		Set<Integer> docIds = new HashSet<>();
		for (TermOccurrence occ : occurrences) {
			docIds.add(occ.getDocumentId());
		}
		return docIds;
	}
	
	/**
	 * Adds a occurence to the term, only if it doesn't exist yet.
	 * @param occurence
	 */
	public void addOccurence(TermOccurrence occurence) {
		this.occurrences.add(occurence);
	}
	
	@Override
	public String toString() {
		String s = "[Term] term=" + term;
		for (TermOccurrence occurence : occurrences) {
			s += "\n\t" + occurence.toString();
		}
		return s;
	}
	
	public void merge(Term term) {
		occurrences.addAll(term.getOccurrences());
	}
}
