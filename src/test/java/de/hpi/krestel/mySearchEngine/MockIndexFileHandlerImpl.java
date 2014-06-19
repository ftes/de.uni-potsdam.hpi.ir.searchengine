package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearReader;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileRandomReader;
import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;

public class MockIndexFileHandlerImpl implements IndexFileLinearReader<String, Term, TermOccurrence, Integer>,
IndexFileLinearWriter<String, Term, TermOccurrence, Integer>, IndexFileRandomReader<String, Term, TermOccurrence, Integer> {
	private final List<Term> terms;
	private int index = 0;
	private String id;
	
	public MockIndexFileHandlerImpl(String id, Term... terms) {
		this.terms = new ArrayList<>(Arrays.asList(terms));
		this.id = id;
	}

	@Override
	public void close() throws IOException {}

	@Override
	public String getFileName() {
		return id;
	}

	@Override
	public Term readList(long fileOffset) throws IOException {
		return terms.get((int) fileOffset);
	}

	@Override
	public Term readNextList() throws IOException {
		return index < terms.size() ? terms.get(index++) : null;
	}
	
	@Override
	public long storeList(Term term) throws IOException {
		terms.add(term);
		return terms.indexOf(term);
	}

	public List<Term> getTerms() {
		return terms;
	}
}
