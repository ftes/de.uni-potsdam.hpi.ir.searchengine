package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileLinearWriter<K, L extends IndexList<K, S, V>, S extends IndexListSlot<V>,
V extends Comparable<V>> extends FileHandler {
	/**
	 * Stores a term to the end of the file.
	 * File Format is as follows
	 * 	- Term as String
	 *  - \0 character
	 *  - 4 bytes integer to store number of occurences that follow
	 *  - for each occurence
	 *  	- 4 byte document id
	 *  	- 4 byte position
	 *  
	 * after that the next term follows.
	 * 
	 * @param term
	 * @return returns the file offset of the written term
	 * @throws IOException 
	 */
	long storeList(L list) throws IOException;

}