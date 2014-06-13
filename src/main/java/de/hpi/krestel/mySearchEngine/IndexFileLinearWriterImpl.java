package de.hpi.krestel.mySearchEngine;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.SortedSet;

/**
 * TODO compression / encoding (delta, ...gamma) *
 */
public class IndexFileLinearWriterImpl implements IndexFileLinearWriter {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	private DataOutputStream out;
	private long length;
	
	public IndexFileLinearWriterImpl(String filename) throws IOException {
		this.filename = filename;
		
		File file = new File(filename);
		length = file.length();
		// open the file
		out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(file)));
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
	@Override
	public String getFilename() {
		return filename;
	}
	
	@Override
	public long storeTerm(Term term) throws IOException {
		long offset = length;
		
		// write contents
		out.writeChars(term.getTerm());
		out.writeChar('\0');
		length += term.getTerm().length() * 2 + 2; //every char written as 2-byte val
		
		SortedSet<TermOccurrence> occurences = term.getOccurrences();
		out.writeInt(occurences.size()); // store number of occurences
		length += 4;
		for (TermOccurrence occurence : occurences) {
			out.writeInt(occurence.getDocumentId());
			out.writeInt(occurence.getPosition());
			length += 8;
		}
		
		return offset;
	}
}
