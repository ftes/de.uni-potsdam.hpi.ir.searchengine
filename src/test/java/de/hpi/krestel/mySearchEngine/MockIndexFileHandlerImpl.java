package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockIndexFileHandlerImpl implements IndexFileLinearReader, IndexFileLinearWriter, IndexFileRandomReader {
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
	public String getFilename() {
		return id;
	}

	@Override
	public Term readTerm(long fileOffset) throws IOException {
		return terms.get((int) fileOffset);
	}

	@Override
	public Term readNextTerm() throws IOException {
		return index < terms.size() ? terms.get(index++) : null;
	}

	@Override
	public long storeTerm(Term term) throws IOException {
		terms.add(term);
		return terms.indexOf(term);
	}

	public List<Term> getTerms() {
		return terms;
	}
}
