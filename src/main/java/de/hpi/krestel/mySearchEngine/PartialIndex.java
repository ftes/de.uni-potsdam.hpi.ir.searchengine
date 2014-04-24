package de.hpi.krestel.mySearchEngine;

/**
 * The index stores terms and their occurrences in main memory. At any given time we will only have parts
 * of the whole index in main memory, so this class will for example only contain the terms for the first
 * few articles.
 * 
 * @author Alexander
 *
 */
public class PartialIndex {
	public PartialIndex() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * returns a globally unique and constant ID that can be safely used for the filename.
	 * @return
	 */
	public int getID() {
		return 0;
	}
	
	/**
	 * Adds a certain term with all it's occurrences to the index. If the term already exists,
	 * Occurrences are merged.
	 * @param term
	 */
	public void addTerm(Term term) {
		
	}
	
	/**
	 * Adds an occurrence for a certain term.
	 * @param term
	 * @param occurence
	 */
	public void addOccurenceForTerm(String term, TermOccurence occurence) {
		
	}
	
	/**
	 * Stores the whole index to disk to a file named "[getID()].dat"
	 * @param the directory to store the file in.
	 */
	public void store(String directory) {
		
	}
	
	/**
	 * Print the whole index
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
