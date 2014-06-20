package de.hpi.krestel.mySearchEngine.index.term;

import de.hpi.krestel.mySearchEngine.index.io.IndexMergerImpl;

public class TermIndexMergerImpl extends IndexMergerImpl<String, Term, TermOccurrence, Integer> {
	public TermIndexMergerImpl() {
		super(new TermIndexFileHandlerFactory(), new TermIndexListFactory());
	}
}
