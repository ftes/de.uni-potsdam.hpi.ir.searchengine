package de.hpi.krestel.mySearchEngine.search;

import java.io.IOException;

/**
 * Is thrown if a term is considered to be too long.
 * @author fredrik
 *
 */
public class WordLengthException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public WordLengthException(String term) {
		super("Word is too long: " + term);
	}
}
