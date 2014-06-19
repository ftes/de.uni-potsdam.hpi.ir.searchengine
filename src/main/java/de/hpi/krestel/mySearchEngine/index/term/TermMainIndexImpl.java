package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.MainIndex;

public class TermMainIndexImpl extends MainIndex<String, Term, TermOccurrence, Integer> {

	public TermMainIndexImpl(String indexPath, String seeklistPath)
			throws IOException {
		super(indexPath, seeklistPath, new TermIndexFileHandlerFactory());
	}

}
