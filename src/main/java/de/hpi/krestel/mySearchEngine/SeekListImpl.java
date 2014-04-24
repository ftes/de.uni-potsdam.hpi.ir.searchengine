package de.hpi.krestel.mySearchEngine;


public class SeekListImpl implements SeekList {
	/**
	 * 
	 * @param filename the filename of the seeklist file
	 */
	public SeekListImpl(String filename) {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public long getTermOffsetInIndex(String term) throws TermLengthException {
		return 0;
	}
	
	/**
	 * Returns the term found at the given position in the seeklist so that binary search can be implemented.
	 * For this, the total number of words in the seeklist must be known, but this can be gleaned from the file
	 * size, as the line length within the seeklist is constant (different from the index file).
	 * 
	 * @param positionInSeeklist The position in the seeklist, as an index. <b>Not</b> the byte offset!
	 * @return The term found at this position.
	 */
	protected String getTermAtPosition(long positionInSeeklist) {
		return null;
	}
	
	@Override
	public void storeTermOffset(String term, long offset) {
		
	}
}
