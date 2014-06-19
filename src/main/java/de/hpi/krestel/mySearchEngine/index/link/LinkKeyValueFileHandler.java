package de.hpi.krestel.mySearchEngine.index.link;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.KeyValueFileHandler;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;
import de.hpi.krestel.mySearchEngine.util.Util;

public class DocumentLinkKeyValueFileHandler implements
		KeyValueFileHandler<Integer, String> {

	@Override
	public String readValue(DataInput in) throws EOFException, IOException {
		return Util.readString(in);
	}

	@Override
	public int writeValue(DataOutput out, String value) throws WordLengthException, IOException {
		return Util.writeString(out, value);
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
