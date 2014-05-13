package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.FileNotFoundException;
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
	
	public PageIndexWriter(String pageFile) throws FileNotFoundException {
		// delete the file
		File f = new File(pageFile);
		f.delete();
		// open the file
		file = new RandomAccessFile(pageFile, "rw");
	}	
	
	/**
	 * Stores a certain page to the page index.
	 * @param page the page to store
	 * @return the start offset of the page
	 * @throws IOException 
	 */

	public long store(Page page) throws IOException {				
		file.seek( file.length() ); // goto end of file
		long offset = file.getFilePointer();
		
		file.writeChars(page.getTitle());
		file.writeChar('\0');
		
		String text = page.getText();
		Tokenizer t = new Tokenizer(text);
		file.writeChars(t.getCleanText());
		file.writeChar('\0');

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
