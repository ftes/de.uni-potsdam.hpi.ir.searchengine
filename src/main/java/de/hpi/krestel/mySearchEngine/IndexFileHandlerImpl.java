package de.hpi.krestel.mySearchEngine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.SortedSet;

public class IndexFileHandlerImpl implements IndexFileHandler {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	RandomAccessFile file;
	
	public IndexFileHandlerImpl(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		// open the file
		file = new RandomAccessFile(filename, "rw");
	}
	
	@Override
	public void close() throws IOException {
		file.close();
	}
	
	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public Term readTerm(long fileOffset) throws IOException {
		file.seek(fileOffset);
		return readNextTerm();
	}

	@Override
	public Term readNextTerm() throws IOException {
		String termString = "";
		char c = file.readChar();
		while (c != '\0') {
			termString += c;
			c = file.readChar();
		}
		
		Term term = new Term(termString);
		
		// Read the occurences
		int count = file.readInt();
		for (int i = 0; i < count; i++) {
			int documentId = file.readInt();
			int position = file.readInt();
			term.addOccurence(new TermOccurrence(documentId, position));
		}
		
		return term;
	}
	
	@Override
	public long storeTerm(Term term) throws IOException {
		// goto end of file
		file.seek( file.length() );
		
		// write contents
		file.writeChars(term.getTerm());
		file.writeChar('\0');
		
		SortedSet<TermOccurrence> occurences = term.getOccurrences();
		file.writeInt(occurences.size()); // store number of occurences
		for (TermOccurrence occurence : occurences) {
			file.writeInt(occurence.getDocumentId());
			file.writeInt(occurence.getPosition());
		}
		
		return file.getFilePointer();
	}
}
