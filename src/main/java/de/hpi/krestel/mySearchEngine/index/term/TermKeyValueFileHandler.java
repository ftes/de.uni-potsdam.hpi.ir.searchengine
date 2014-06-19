package de.hpi.krestel.mySearchEngine.index.term;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.KeyValueFileHandler;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public class TermKeyValueFileHandler implements
		KeyValueFileHandler<String, Integer> {

	@Override
	public String readKey(DataInput in) throws IOException {
		String termString = "";
		char c = ' ';
		try {
			c = in.readChar();
		} catch (EOFException e) {
			return null;
		}
		int i = 0;
		while (i < SeekList.MAX_TERM_LENGTH && c != '\0') {
			termString += c;
			c = in.readChar();
			i++;
		}
		return termString;
	}

	@Override
	public int writeKey(DataOutput out, String key) throws IOException {
		if (key.length() >= SeekList.MAX_TERM_LENGTH) {
			throw new TermLengthException(key);
		}
		out.writeChars(key);
		out.writeChar('\0');
		return key.length() * 2 + 2; // every char written as 2-byte val
	}

	@Override
	public Integer readValue(DataInput in) throws IOException {
		return in.readInt();
	}

	@Override
	public int writeValue(DataOutput out, Integer value) throws IOException {
		out.writeInt(value);
		return 4;
	}

}
