package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The index stores terms and their occurrences in main memory. At any given time we will only have parts
 * of the whole index in main memory, so this class will for example only contain the terms for the first
 * few articles.
 * 
 * @author Alexander
 *
 */
public class PartialIndex {
	private int id;
	
	/**
	 * This key-sorted map stores the partial index. Keys are the Terms as Strings.
	 * Values are the Term objects.
	 */
	private Map<String, Term> map = new TreeMap<>();
	
	public PartialIndex(int id) {
		this.id = id;
	}
	
	/**
	 * returns a globally unique and constant ID that can be safely used for the filename.
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * returns the filename this index would be written to
	 * @return
	 */
	public String getFilename() {
		return this.getID() + ".dat";
	}
	
	/**
	 * Adds a certain term with all it's occurrences to the index. If the term already exists,
	 * Occurrences are merged.
	 * @param term
	 */
	public void addTerm(Term term) {
		if (map.containsKey(term.getTerm())) {
			Term existingTerm = map.get(term.getTerm());
			for (TermOccurrence occurence : term.getOccurrences()) {
				existingTerm.addOccurence(occurence);
			}
		} else {
			map.put(term.getTerm(), term);
		}
	}
	
	/**
	 * Adds an occurrence for a certain term.
	 * @param term
	 * @param occurence
	 */
	public void addOccurenceForTerm(String term, TermOccurrence occurence) {
		Term t = new Term(term);
		t.addOccurence(occurence);
		this.addTerm(t);
	}
	
	/**
	 * Stores the whole index to disk to a file named "[getID()].dat"
	 * @param the directory to store the file in (no trailing slash)
	 * @throws IOException 
	 */
	public void store(String directory) throws IOException {
		String filename = directory + File.separator + this.getFilename();
		IndexFileLinearWriter fileHandler = new IndexFileLinearWriterImpl(filename);
		
		for (Term term : map.values()) {
			fileHandler.storeTerm(term);	
		}
		fileHandler.close();		
	}
	
	/**
	 * Print the whole index
	 */
	@Override
	public String toString() {
		String s = "";
		for (Entry<String, Term> entry : map.entrySet()) {
			s += entry.getValue().toString() + "\n";			
		}
		return s;
	}

}
