package de.hpi.krestel.mySearchEngine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.SortedSet;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public class IndexFileHandler {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	RandomAccessFile file;
	
	public IndexFileHandler(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		// open the file
		file = new RandomAccessFile(filename, "rw");
	}
	
	/**
	 * Closes all open files
	 * @throws IOException 
	 */
	public void close() throws IOException {
		file.close();
	}
	
	public String getFilename() {
		return filename;
	}
	/**
	 * Reads the first term in the file
	 * @return
	 * @throws IOException 
	 */
	public Term readTerm() throws IOException {
		return readTerm(0);
	}
	/**
	 * Reads the term in the file at a certain offset
	 * @param fileOffset
	 * @return
	 * @throws IOException 
	 */
	public Term readTerm(long fileOffset) throws IOException {
		file.seek(fileOffset);
		return readNextTerm();
	}
	/**
	 * Reads the next term
	 * @return The next term, or {@code null} if the end of the file is reached.
	 * @throws IOException 
	 */
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
			term.addOccurence(new TermOccurence(documentId, position));
		}
		
		return term;
	}
	
	/**
	 * Stores a term to the end of the file.
	 * File Format is as follows
	 * 	- Term as String
	 *  - \0 character
	 *  - 4 bytes integer to store number of occurences that follow
	 *  - for each occurence
	 *  	- 4 byte document id
	 *  	- 4 byte position
	 *  
	 * after that the next term follows.
	 * 
	 * @param term
	 * @return
	 * @throws IOException 
	 */
	public long storeTerm(Term term) throws IOException {
		// goto end of file
		file.seek( file.length() );
		
		// write contents
		file.writeChars(term.getTerm());
		file.writeChar('\0');
		
		SortedSet<TermOccurence> occurences = term.getOccurrences();
		file.writeInt(occurences.size()); // store number of occurences
		for (TermOccurence occurence : occurences) {
			file.writeInt(occurence.getDocumentId());
			file.writeInt(occurence.getPosition());
		}
		
		return file.getFilePointer();
	}
}
