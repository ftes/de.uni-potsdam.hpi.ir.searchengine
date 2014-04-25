package de.hpi.krestel.mySearchEngine;

public class TermNotFoundException extends Exception {
	private static final long serialVersionUID = -1120938673909668444L;

	public TermNotFoundException(String term) {
		super("Could not find term: " + term);
	}
}
