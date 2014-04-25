package de.hpi.krestel.mySearchEngine;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.Log.Level;

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
	 * @throws FileNotFoundException
	 */
	public MainIndex(String indexPath, String seeklistPath) throws FileNotFoundException {
		indexFile = new IndexFileHandlerImpl(indexPath);
		seeklist = new SeekListImpl(seeklistPath);
	}
	
	/**
	 * Finds the term object for a certain term string in the index
	 * @param term The term (e.g. tokenized form of a word)
	 * @return the term object or null if non was found
	 */
	public Term getTerm(String term) {
		try {
			// find offset
			long offset = seeklist.getTermOffsetInIndex(term);
			// find term
			Term t = indexFile.readTerm(offset);
			return t;
		} catch (TermLengthException | IOException e) {
			Log.log(Level.ERROR, "Exception in MainIndex:getTerm(): " + e.toString());
			return null;
		}		
	}
}
