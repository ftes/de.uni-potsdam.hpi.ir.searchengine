package de.hpi.krestel.mySearchEngine.parse;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;

/**
 * This Parser parses a given wikipedia xml file.
 * 
 * @author Alexander
 *
 */
public abstract class Parser {
	protected InputStream in;
	
	/**
	 * Creates the parser
	 * @param filename the filename of the wikipedia xml file
	 */
	public Parser(InputStream in) {
		this.in = in;
	}
	/**
	 * Parses the specified XML File and request the generation of the indexes.
	 */
	public abstract void parseToPartialIndexes(String stemmedPartialDir,
			String unstemmedPartialDir, String linksPartialDir, String pageIndexFile, String pageFile)
			throws XMLStreamException, ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NumberFormatException, FactoryConfigurationError, IOException;
	
	/**
	 * Gets the title index of the parser
	 * @return the title index created during parsing or null
	 */
	public abstract TitleIndex getTitleIndex();
}
