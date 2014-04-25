package de.hpi.krestel.mySearchEngine;

import java.util.HashMap;
import java.util.Map;

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

}
