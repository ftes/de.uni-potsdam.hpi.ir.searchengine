package de.hpi.krestel.mySearchEngine.parse;


/**
 * This Parser parses a given wikipedia xml file.
 * 
 * @author Alexander
 *
 */
public interface Parser {
	/**
	 * Parses the specified XML File and request the generation of the indexes.
	 */
	void parse()
			throws Exception;
}
