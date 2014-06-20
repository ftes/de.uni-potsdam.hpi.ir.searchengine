package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearWriter;


public class TermIndexFileLinearWriterImpl
extends BaseIndexFileLinearWriter<String, Term, TermOccurrence, Integer> {
	public TermIndexFileLinearWriterImpl(String fileName) throws IOException {
		super(fileName, new TermKeyValueFileHandler());
	}
}
