package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileHandler {

	/**
	 * Closes all open files
	 * @throws IOException 
	 */
	void close() throws IOException;

	String getFilename();

	/**
	 * Reads the term in the file at a certain offset
	 * @param fileOffset
	 * @return
	 * @throws IOException 
	 */
	Term readTerm(long fileOffset) throws IOException;

	/**
	 * Reads the next term
	 * @return The next term, or {@code null} if the end of the file is reached.
	 * @throws IOException 
	 */
	Term readNextTerm() throws IOException;

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
	 * @return
	 * @throws IOException 
	 */
	long storeTerm(Term term) throws IOException;

}