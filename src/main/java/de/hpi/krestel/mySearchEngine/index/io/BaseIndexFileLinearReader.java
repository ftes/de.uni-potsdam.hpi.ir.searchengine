package de.hpi.krestel.mySearchEngine.index.io;

import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListFactory;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public abstract class BaseIndexFileLinearReader<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> extends BaseIndexFileReader<K, L, S, V>
implements IndexFileLinearReader<K, L, S, V>{
	public BaseIndexFileLinearReader(String filename, KeyValueFileHandler<K, V> keyValueFileHandler,
			IndexListFactory<K, L, S, V> indexListFactory) throws IOException {
		super(filename, keyValueFileHandler, indexListFactory);
	}

	@Override
	public L readNextList() throws EOFException, IOException {	
		return readList();
	}
}