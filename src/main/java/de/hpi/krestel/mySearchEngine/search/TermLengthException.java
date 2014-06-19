package de.hpi.krestel.mySearchEngine.search;

import java.io.IOException;

/**
 * Is thrown if a term is considered to be too long.
 * @author fredrik
 *
 */
public class TermLengthException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public TermLengthException(String term) {
		super("Term is too long: " + term);
	}
}
