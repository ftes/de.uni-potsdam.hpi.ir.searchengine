package de.hpi.krestel.mySearchEngine.search;

public class KeyNotFoundException extends Exception {
	private static final long serialVersionUID = -1120938673909668444L;

	public KeyNotFoundException(Object key) {
		super("Could not find key: " + key);
	}
}
