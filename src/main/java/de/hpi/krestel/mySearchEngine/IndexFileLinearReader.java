package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileLinearReader {

	/**
	 * Closes all open files
	 * @throws IOException 
	 */
	void close() throws IOException;

	String getFilename();

	/**
	 * Reads the next term
	 * @return The next term, or {@code null} if the end of the file is reached.
	 * @throws IOException 
	 */
	Term readNextTerm() throws IOException;

}