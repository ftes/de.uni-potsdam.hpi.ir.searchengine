package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearReader;

public class TermIndexFileLinearReaderImpl extends
		BaseIndexFileLinearReader<String, Term, TermOccurrence, Integer> {

	public TermIndexFileLinearReaderImpl(
			String filename) throws IOException {
		super(filename, new TermKeyValueFileHandler(), new TermIndexListFactory());
	}

}
