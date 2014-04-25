package de.hpi.krestel.mySearchEngine;

/**
 * Is thrown if a term is considered to be too long.
 * @author fredrik
 *
 */
public class TermLengthException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public TermLengthException(String term) {
		super("Term is too long: " + term);
	}
}
