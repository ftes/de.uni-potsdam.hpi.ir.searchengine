package de.hpi.krestel.mySearchEngine.index.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

/**
 * TODO compression / encoding (delta, ...gamma) *
 */
public abstract class BaseIndexFileLinearWriter<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> implements IndexFileLinearWriter<K, L, S, V> {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	private DataOutputStream out;
	protected long length;
	private final KeyValueFileHandler<K, V> keyValueFileHandler;
	
	public BaseIndexFileLinearWriter(String filename, KeyValueFileHandler<K, V> keyValueFileHandler) throws IOException {
		this.filename = filename;
		
		File file = new File(filename);
		length = file.length();
		// open the file
		out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(file)));
		this.keyValueFileHandler = keyValueFileHandler;
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
	@Override
	public String getFileName() {
		return filename;
	}
	
	@Override
	public long storeList(L list) throws IOException {
		long offset = length;
		
		// write contents
		length += keyValueFileHandler.writeKey(out, list.getKey());
		
		SortedSet<? extends IndexListSlot<V>> slots = list.getSlots();
		out.writeInt(slots.size()); // store number of occurences
		length += 4;
		for (IndexListSlot<V> slot : slots) {
			out.writeInt(slot.getDocumentId());
			length += 4;
			length += keyValueFileHandler.writeValue(out, slot.getValue());
		}
		
		return offset;
	}
}
