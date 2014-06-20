package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;
import de.hpi.krestel.mySearchEngine.index.PartialIndex;

public interface IndexFileHandlerFactory<K extends Comparable<K>, L extends IndexList<K,S,V>,
S extends IndexListSlot<V>, V extends Comparable<V>> {
	IndexFileLinearReader<K, L, S, V> createLinearReader(String fileName) throws IOException;
	IndexFileRandomReader<K, L, S, V> createRandomReader(String fileName) throws IOException;
	IndexFileLinearWriter<K, L, S, V> createLinearWriter(String fileName) throws IOException;
	SeekList<K> createSeekList(String fileName) throws IOException;
	PartialIndex<K, L, S, V> createPartialIndex(String fileName) throws IOException;
}
