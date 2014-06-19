package de.hpi.krestel.mySearchEngine.index;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileRandomReader;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

/**
 * This class represents the main index that is used for searching. It is backed by one
 * index file and one seeklist file.
 * 
 * @author Alexander
 *
 */
public abstract class MainIndex<K extends Comparable<K>, L extends IndexList<K, S, V>, S extends IndexListSlot<V>,
V extends Comparable<V>> {
	private final IndexFileRandomReader<K, L, S, V> indexFile;
	private final SeekList<K> seeklist;
	
	/**
	 * 
	 * @param indexPath
	 * @param seeklistPath
	 * @throws IOException 
	 */
	public MainIndex(String indexPath, String seeklistPath,
			IndexFileHandlerFactory<K, L, S, V> indexFileHandlerFactory) throws IOException {
		indexFile = indexFileHandlerFactory.createRandomReader(indexPath);
		seeklist = indexFileHandlerFactory.createSeekList(seeklistPath);
	}
	
	/**
	 * Finds the term object for a certain term string in the index
	 * @param term The term (e.g. tokenized form of a word)
	 * @return the term object or null if non was found
	 * @throws IOException 
	 * @throws TermLengthException 
	 * @throws KeyNotFoundException 
	 * @throws Exception 
	 */
	public L getList(K key) throws IOException, TermLengthException, KeyNotFoundException {
		// find offset
		long offset = seeklist.getKeyOffsetInIndex(key);
		// find term
		return indexFile.readList(offset);
	}
	
	public void close() throws IOException {
		indexFile.close();
		seeklist.close();
	}
	
	public SeekList<K> getSeekList() {
		return seeklist;
	}
}
