package de.hpi.krestel.mySearchEngine.index.io;

import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public interface IndexFileLinearReader<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> extends FileHandler {
	/**
	 * Reads the next term
	 * @return The next term, or {@code null} if the end of the file is reached.
	 * @throws IOException 
	 */
	 L readNextList() throws EOFException, IOException;
}