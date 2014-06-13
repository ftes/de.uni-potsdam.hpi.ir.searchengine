package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileRandomReader {

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

}