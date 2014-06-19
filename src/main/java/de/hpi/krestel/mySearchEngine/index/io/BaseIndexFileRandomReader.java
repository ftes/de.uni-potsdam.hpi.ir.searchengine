package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListFactory;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public abstract class BaseIndexFileRandomReader<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> extends BaseIndexFileReader<K, L, S, V>
implements IndexFileRandomReader<K, L, S, V> {
	private final RandomAccessFile file;
	
	public BaseIndexFileRandomReader(String filename, KeyValueFileHandler<K, V> keyValueFileHandler,
			IndexListFactory<K, L, S, V> indexListFactory) throws IOException {
		super(keyValueFileHandler, indexListFactory);
		file = new RandomAccessFile(filename, "rw");
		in = file;
	}
	
	@Override
	public void close() throws IOException {
		file.close();
	}

	@Override
	public L readList(long fileOffset) throws IOException {
		file.seek(fileOffset);
		return readList();
	}
}