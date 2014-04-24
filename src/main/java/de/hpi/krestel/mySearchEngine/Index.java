package de.hpi.krestel.mySearchEngine;

/**
 * The index stores terms and their occurencies in main memory. At any given time we will only have parts
 * of the whole index in main memory, so this class will for example only contain the terms for the first
 * few articles
 * 
 * @author Alexander
 *
 */
public class Index {
	public Index() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * returns a globally unique and constant ID that can be savely used for the filename.
	 * @return
	 */
	public int getID() {
		return 0;
	}
	
	/**
	 * Adds a certain term with all it's occurencies to the index. If the term already exists,
	 * occurencies are merged.
	 * @param term
	 */
	public void addTerm(Term term) {
		
	}
	
	/**
	 * Adds an occurence for a certain term.
	 * @param term
	 * @param occurence
	 */
	public void addOccurenceForTerm(String term, TermOccurence occurence) {
		
	}
	
	/**
	 * Stores the whole index to disk
	 */
	public void store() {
		
	}
	
	/**
	 * Deletes the contents of the index to free up memory space.
	 */
	public void clear() {
		
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
