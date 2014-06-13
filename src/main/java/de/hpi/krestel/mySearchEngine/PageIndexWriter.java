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
	public static final String TERMINATOR = "DONE00";
	
	private RandomAccessFile file;
	
	public PageIndexWriter(String pageFile) throws IOException {
		// delete the file
		File f = new File(pageFile);
		f.delete();
		// open the file
		file = new RandomAccessFile(pageFile, "rw");
		// file.seek(file.length());
	}	
	
	/**
	 * Stores a certain page to the page index.
	 * @param page the page to store
	 * @return the start offset of the page
	 * @throws IOException 
	 */

	public long store(Page page) throws IOException {
		long offset = file.getFilePointer();
		
		file.writeUTF(page.getTitle());
		
		String text = page.getText();
		Tokenizer t = new Tokenizer(text);
		String cleaned = t.getCleanText();
		
		// UTF maximal 1000 Zeichen.
		int length = 1000;
		int start = 0;
		for (start = 0; start < cleaned.length() - length; start += length ) {
			String tmp = cleaned.substring(start, start + length);
			file.writeUTF(tmp);
		}
		file.writeUTF(cleaned.substring(start));
		file.writeUTF(PageIndexWriter.TERMINATOR);

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
