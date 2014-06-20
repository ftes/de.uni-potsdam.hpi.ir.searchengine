package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;
import de.hpi.krestel.mySearchEngine.util.NotImplementedException;

public class MockSeekListImpl implements SeekList<String> {
	private final Map<String, Long> offsets = new HashMap<>();

	@Override
	public long getKeyOffsetInIndex(String term) throws WordLengthException {
		throw new NotImplementedException();
	}

	@Override
	public void storeKeyOffset(String term, long offset) {
		offsets.put(term, offset);
	}
	
	public Map<String, Long> getOffsets() {
		return offsets;
	}

	@Override
	public void close() {}

	@Override
	public Set<String> getKeysBeginningWith(String prefix) throws IOException {
		return null;
	}

}
