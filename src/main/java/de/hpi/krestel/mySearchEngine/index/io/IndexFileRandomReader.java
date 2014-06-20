package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileRandomReader<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> extends FileHandler {
	/**
	 * Reads the term in the file at a certain offset
	 * @param fileOffset
	 * @return
	 * @throws IOException 
	 */
	L readList(long fileOffset) throws IOException;
}