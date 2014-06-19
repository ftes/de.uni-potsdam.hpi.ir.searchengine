package de.hpi.krestel.mySearchEngine.index.io;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListFactory;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public abstract class BaseIndexFileReader<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> implements FileHandler {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	protected DataInputStream inStream;
	protected DataInput in;
	private final KeyValueFileHandler<K, V> kevValueFileHandler;
	private final IndexListFactory<K, L, S, V> indexListFactory;
	
	public BaseIndexFileReader(KeyValueFileHandler<K, V> kevValueFileHandler,
			IndexListFactory<K, L, S, V> indexListFactory) throws IOException {
		this.kevValueFileHandler = kevValueFileHandler;
		this.indexListFactory = indexListFactory;
	}
	
	public BaseIndexFileReader(String filename, KeyValueFileHandler<K, V> kevValueFileHandler,
			IndexListFactory<K, L, S, V> indexListFactory) throws IOException {
		this(kevValueFileHandler, indexListFactory);
		this.filename = filename;
		File file = new File(filename);
		// open the file
		inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		in = inStream;
	}
	
	@Override
	public void close() throws IOException {
		inStream.close();
	}
	
	@Override
	public String getFileName() {
		return filename;
	}
	
	protected L readList() throws IOException {
		K key = kevValueFileHandler.readKey(in);
		L list = indexListFactory.createList(key);
		
		// Read the occurences
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			int documentId = in.readInt();
			V value = kevValueFileHandler.readValue(in);
			list.addSlot(indexListFactory.createSlot(documentId, value));
		}
		
		return list;
	}
}