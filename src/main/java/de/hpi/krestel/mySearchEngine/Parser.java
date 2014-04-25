package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * This Parser parses a given wikipedia xml file.
 * 
 * @author Alexander
 *
 */
public abstract class Parser {
	private String filename;
	
	/**
	 * Creates the parser
	 * @param filename the filename of the wikipedia xml file
	 */
	public Parser(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Parses the specified XML File and request the generation of the indexes.
	 * @throws IOException 
	 */
	public abstract void parseToPartialIndexes(String indexDirectory) throws IOException;
	
	public String getFilename() {
		return filename;
	}
}
