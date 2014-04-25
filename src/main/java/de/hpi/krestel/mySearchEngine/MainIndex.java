package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * This class represents the main index that is used for searching. It is backed by one
 * index file and one seeklist file.
 * 
 * @author Alexander
 *
 */
public class MainIndex {
	IndexFileHandler indexFile;
	SeekList seeklist;
	
	/**
	 * 
	 * @param indexPath
	 * @param seeklistPath
	 * @throws IOException 
	 */
	public MainIndex(String indexPath, String seeklistPath) throws IOException {
		indexFile = new IndexFileHandlerImpl(indexPath);
		seeklist = new SeekListImpl(seeklistPath);
	}
	
	/**
	 * Finds the term object for a certain term string in the index
	 * @param term The term (e.g. tokenized form of a word)
	 * @return the term object or null if non was found
	 * @throws IOException 
	 * @throws TermLengthException 
	 * @throws TermNotFoundException 
	 * @throws Exception 
	 */
	public Term getTerm(String term) throws IOException, TermLengthException, TermNotFoundException {
		// find offset
		long offset = seeklist.getTermOffsetInIndex(term);
		// find term
		Term t = indexFile.readTerm(offset);
		return t;	
	}
	
	public void close() throws IOException {
		indexFile.close();
		seeklist.close();
	}
}
