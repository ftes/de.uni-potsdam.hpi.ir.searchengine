package de.hpi.krestel.mySearchEngine;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * TODO compression / encoding (delta, ...gamma) *
 */
public class IndexFileLinearReaderImpl implements IndexFileLinearReader {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	private DataInputStream in;
	
	public IndexFileLinearReaderImpl(String filename) throws IOException {
		this.filename = filename;
		File file = new File(filename);
		// open the file
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	}
	
	@Override
	public void close() throws IOException {
		in.close();
	}
	
	@Override
	public String getFilename() {
		return filename;
	}
	
	public static Term readTerm(DataInput in) throws IOException {
		String termString = "";
		char c = ' ';
		try {
			c = in.readChar();
		} catch (EOFException e) {
			return null;
		}
		while (c != '\0') {
			termString += c;
			c = in.readChar();
		}
		
		Term term = new Term(termString);
		
		// Read the occurences
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			int documentId = in.readInt();
			int position = in.readInt();
			term.addOccurence(new TermOccurrence(documentId, position));
		}
		
		return term;
	}

	@Override
	public Term readNextTerm() throws IOException {	
		return readTerm(in);
	}
}