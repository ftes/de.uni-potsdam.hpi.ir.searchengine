package de.hpi.krestel.mySearchEngine.index.link;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.KeyValueFileHandler;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;

public class LinkKeyValueFileHandler implements
		KeyValueFileHandler<Integer, Integer> {

	@Override
	public Integer readValue(DataInput in) throws EOFException, IOException {
		return in.readInt();
	}

	@Override
	public int writeValue(DataOutput out, Integer value) throws WordLengthException, IOException {
		out.writeInt(value);
		return 4;
	}

	@Override
	public Integer readKey(DataInput in) throws IOException {
		return in.readInt();
	}

	@Override
	public int writeKey(DataOutput out, Integer value) throws IOException {
		out.writeInt(value);
		return 4;
	}

}
