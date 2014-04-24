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
	private SortedSet<TermOccurence> occurrences;
	
	public Term(String term) {
		this(term, new TreeSet<TermOccurence>(TermOccurenceComparator.INSTANCE));
	}
	
	public Term(String term, SortedSet<TermOccurence> occurrences) {
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
	public SortedSet<TermOccurence> getOccurrences() {
		return occurrences;
	}
	
	public void addOccurence(TermOccurence occurence) {
		this.occurrences.add(occurence);
		// TODO: sort
	}
	
	@Override
	public String toString() {
		return "Term [term=" + term + ", occurrences=" + occurrences + "]";
	}
}
