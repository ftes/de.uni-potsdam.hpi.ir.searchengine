package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;
import de.hpi.krestel.mySearchEngine.util.Util;

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
public abstract class BaseSeekList<K extends Comparable<K>> implements SeekList<K> {
	private final RandomAccessFile file;
	
	private final Map<Long, K> positionToKeyCache = new HashMap<>();
	private final KeyValueFileHandler<K, ?> keyValueFileHandler;
	
	/**
	 * In bytes.
	 */
	private long fileSize;
	private long numberOfTerms;
	
	public BaseSeekList(String filename, KeyValueFileHandler<K, ?> keyValueFileHandler) throws IOException {
		file = new RandomAccessFile(filename, "rw");
		fileSize = file.length();
		numberOfTerms = fileSize / SeekList.SLOT_SIZE;
		assert fileSize % SeekList.SLOT_SIZE == 0 : new IOException("Seeklist file size is not multiple of slot size");
		file.seek(fileSize);
		this.keyValueFileHandler = keyValueFileHandler;
	}
	
	private long getOffset(long position) {
		return SeekList.SLOT_SIZE * position;
	}
	
	@Override
	public long getKeyOffsetInIndex(K key) throws WordLengthException, KeyNotFoundException, IOException {
		return getOffsetAtPosition(binarySearch(key, 0, numberOfTerms-1, false));
	}
	
	/**
	 * @param key
	 * @param fromPosition
	 * @param toPosition inclusive
	 * @param findClosest If {@code true}, return the closest term even if the exact term isn't found.
	 * Then the position of the first term naturally ordered after the {@code searchTerm} is returned.
	 * @return
	 * @throws IOException
	 */
	private long binarySearch(K key, long fromPosition, long toPosition, boolean findClosest)
			throws KeyNotFoundException, IOException {
		//could not find term
		if (toPosition < fromPosition) {
			if (findClosest) {
				return fromPosition;
			} else {
				throw new KeyNotFoundException(key);
			}
		}
		
		long middlePosition = (fromPosition + toPosition) / 2;
		K keyAtPos = getKeyAtPos(middlePosition);
		int comparison = key.compareTo(keyAtPos);
		
		if (comparison == 0) {
			//found term
			return middlePosition;
		} else if (comparison < 0) {
			//search in left half
			return binarySearch(key, fromPosition, middlePosition - 1, findClosest);
		} else {
			//search in right half
			return binarySearch(key, middlePosition + 1, toPosition, findClosest);
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
	public K getKeyAtPos(long position) throws IOException {
		if (positionToKeyCache.containsKey(position)) {
			return positionToKeyCache.get(position);
		}
		
		file.seek(getOffset(position));
		K key = keyValueFileHandler.readKey(file);
		
		if (! Util.isMainMemoryFull()) {
			positionToKeyCache.put(position, key);
		}
		
		return key;
	}
	
	public long getOffsetAtPosition(long position) throws IOException {
		file.seek(getOffset(position) + SeekList.BYTES_PER_TERM);
		return file.readLong();
	}
	
	@Override
	public void storeKeyOffset(K key, long offset) throws IOException, WordLengthException {
		//write term
		file.seek(fileSize);
		keyValueFileHandler.writeKey(file, key);
		
		file.seek(fileSize + BaseSeekList.BYTES_PER_TERM);
		file.writeLong(offset);
		
		fileSize += SeekList.SLOT_SIZE;
		numberOfTerms++;
	}
	
	@Override
	public void close() throws IOException {
		file.close();
	}

	protected abstract boolean stop(K prefix, K current);
	
	@Override
	public Set<K> getKeysBeginningWith(K prefix) throws IOException {
		long pos = numberOfTerms;
		try {
			pos = binarySearch(prefix, 0, numberOfTerms-1, true);
		} catch (KeyNotFoundException e) {} //shouldn't occur
		Set<K> result = new HashSet<>();
		for (long i=pos; i<numberOfTerms; i++) {
			K key = getKeyAtPos(i);
			if (stop(prefix, key)) {
				break;
			}
			result.add(key);
		}
		return result;
	}
}
