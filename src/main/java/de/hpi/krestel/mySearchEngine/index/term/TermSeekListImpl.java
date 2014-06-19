package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseSeekList;

public class TermSeekListImpl extends BaseSeekList<String> {
	public TermSeekListImpl(String filename) throws IOException {
		super(filename, new TermKeyValueFileHandler());
	}

	protected boolean stop(String prefix, String current) {
		if (!current.startsWith(prefix)) {
			return true;
		}
		return false;
	}
}
