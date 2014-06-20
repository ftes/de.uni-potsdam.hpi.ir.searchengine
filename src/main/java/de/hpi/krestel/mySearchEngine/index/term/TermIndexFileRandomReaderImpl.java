package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileRandomReader;

public class TermIndexFileRandomReaderImpl extends
		BaseIndexFileRandomReader<String, Term, TermOccurrence, Integer> {

	public TermIndexFileRandomReaderImpl(
			String filename) throws IOException {
		super(filename, new TermKeyValueFileHandler(), new TermIndexListFactory());
	}

}
