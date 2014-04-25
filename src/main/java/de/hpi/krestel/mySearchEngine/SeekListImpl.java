package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * Attempts to cache until main memory is full. The terms for requested positions in the
 * seeklist are simply cached until main memory is full. No bookkeeping is done to determine
 * what can be removed from the cache. Instead, the behaviour of the binary search pretty much
 * garuantees that certain "pivot" elements (e.g. the n/2 element) will be accessed most often,
 * so that they will land in the cache automatically.
 * 
 * @author fredrik
 *
 */
public class SeekListImpl implements SeekList {
	private final RandomAccessFile file;
	
	private final Map<Long, String> positionToTermCache = new HashMap<>();
	
	/**
	 * In bytes.
	 */
	private long fileSize;
	private long numberOfTerms;
	
	public SeekListImpl(String filename) throws IOException {
		file = new RandomAccessFile(filename, "rw");
		fileSize = file.length();
		numberOfTerms = fileSize / SeekList.SLOT_SIZE;
		assert fileSize % SeekList.SLOT_SIZE == 0 : new IOException("Seeklist file size is not multiple of slot size");
		file.seek(fileSize);
	}
	
	private long getOffset(long position) {
		return SeekList.SLOT_SIZE * position;
	}
	
	@Override
	public long getTermOffsetInIndex(String term) throws TermLengthException, IOException {
		//TODO caching
		return binarySearch(term, 0, numberOfTerms-1);
	}
	
	/**
	 * @param searchTerm
	 * @param fromPosition
	 * @param toPosition inclusive
	 * @return
	 * @throws IOException
	 */
	private long binarySearch(String searchTerm, long fromPosition, long toPosition) throws IOException {
		//could not find term
		if (toPosition < fromPosition) {
			return -1;
		}
		
		long middlePosition = (fromPosition + toPosition) / 2;
		String termAtPos = getTermAtPosition(middlePosition);
		int comparison = searchTerm.compareTo(termAtPos);
		
		if (comparison == 0) {
			//found term
			return getOffsetAtPosition(middlePosition);
		} else if (comparison < 0) {
			//search in left half
			return binarySearch(searchTerm, fromPosition, middlePosition - 1);
		} else {
			//search in right half
			return binarySearch(searchTerm, middlePosition + 1, toPosition);
		}
	}
	
	/**
	 * Returns the term found at the given position in the seeklist so that binary search can be implemented.
	 * For this, the total number of words in the seeklist must be known, but this can be gleaned from the file
	 * size, as the line length within the seeklist is constant (different from the index file).
	 * 
	 * @param position The position in the seeklist, as an index. <b>Not</b> the byte offset!
	 * @return The term found at this position.
	 * @throws IOException 
	 */
	protected String getTermAtPosition(long position) throws IOException {
		if (positionToTermCache.containsKey(position)) {
			return positionToTermCache.get(position);
		}
		
		file.seek(getOffset(position));
		String termString = "";
		char c = file.readChar();
		int i = 0;
		while (i < SeekList.MAX_TERM_LENGTH && c != '\0') {
			termString += c;
			c = file.readChar();
			i++;
		}
		
		if (! Util.isMainMemoryFull()) {
			positionToTermCache.put(position, termString);
		}
		
		return termString;
	}
	
	protected long getOffsetAtPosition(long position) throws IOException {
		file.seek(getOffset(position) + SeekList.BYTES_PER_TERM);
		return file.readLong();
	}
	
	@Override
	public void storeTermOffset(String term, long offset) throws IOException, TermLengthException {
		//write term
		file.seek(fileSize);
		int termLength = term.length();
		if (termLength > SeekList.MAX_TERM_LENGTH) {
			throw new TermLengthException();
		}
		file.writeChars(term);
		if (termLength < SeekList.MAX_TERM_LENGTH) {
			file.writeChar('\0');
		}
		
		file.seek(fileSize + SeekListImpl.BYTES_PER_TERM);
		file.writeLong(offset);
		
		fileSize += SeekList.SLOT_SIZE;
		numberOfTerms++;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
