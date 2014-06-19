package de.hpi.krestel.mySearchEngine.index.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface KeyValueFileHandler<K, V> {
	K readKey(DataInput in) throws IOException;
	/**
	 * @return length in bytes of written data
	 */
	int writeKey(DataOutput out, K key) throws IOException;
	V readValue(DataInput in) throws IOException;
	/**
	 * @return length in bytes of written data
	 */
	int writeValue(DataOutput out, V value) throws IOException;
}
