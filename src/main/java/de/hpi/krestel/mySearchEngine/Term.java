package de.hpi.krestel.mySearchEngine;

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
}
