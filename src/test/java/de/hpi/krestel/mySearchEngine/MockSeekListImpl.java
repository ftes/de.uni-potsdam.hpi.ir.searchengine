package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MockSeekListImpl implements SeekList {
	private final Map<String, Long> offsets = new HashMap<>();

	@Override
	public long getTermOffsetInIndex(String term) throws TermLengthException {
		throw new NotImplementedException();
	}

	@Override
	public void storeTermOffset(String term, long offset) {
		offsets.put(term, offset);
	}
	
	public Map<String, Long> getOffsets() {
		return offsets;
	}

	@Override
	public void close() {}

	@Override
	public Set<String> getTermsBeginningWith(String prefix) throws IOException {
		return null;
	}

}
