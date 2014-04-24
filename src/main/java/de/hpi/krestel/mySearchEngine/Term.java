package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;

public class Term {
	/** 
	 * the tokenized term
	 */
	private String term;
	/**
	 * a list of occurences, without any duplicates
	 */
	private ArrayList<TermOccurence> occurrences;
	public Term(String term) {
		this.term = term;
		this.occurrences = new ArrayList<TermOccurence>();
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	public ArrayList<TermOccurence> getOccurrences() {
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
