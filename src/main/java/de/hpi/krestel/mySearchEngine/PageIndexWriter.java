package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The index stores for every document id the corresponding title and text
 * 
 * @author Alexander
 *
 */
public class PageIndexWriter {	
	private RandomAccessFile file;
	private long length;
	
	public PageIndexWriter(String pageFile) throws IOException {
		// delete the file
		File f = new File(pageFile);
		f.delete();
		// open the file
		file = new RandomAccessFile(pageFile, "rw");
		length = file.length();
	}	
	
	/**
	 * Stores a certain page to the page index.
	 * @param page the page to store
	 * @return the start offset of the page
	 * @throws IOException 
	 */

	public long store(Page page) throws IOException {
		long offset = length;
		
		file.writeChars(page.getTitle());
		file.writeChar('\0');
		length += page.getTitle().length() * 2 + 2; //every char written as 2-byte val
		
		String text = page.getText();
		Tokenizer t = new Tokenizer(text);
		String cleaned = t.getCleanText();
		file.writeChars(cleaned);
		file.writeChar('\0');
		length += cleaned.length() * 2 + 2; //every char written as 2-byte val

		return offset;
	}
	
	/**
	 * Stores the file
	 * @throws IOException
	 */
	public void close() throws IOException {
		file.close();
	}

}
