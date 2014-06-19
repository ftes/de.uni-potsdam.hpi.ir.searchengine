package de.hpi.krestel.mySearchEngine.index.term;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;

public class TermPartialIndex extends
		PartialIndex<String, Term, TermOccurrence, Integer> {
	public TermPartialIndex(IndexFileLinearWriter<String, Term, TermOccurrence, Integer> writer) {
		super(writer, new TermIndexListFactory());
	}
}
