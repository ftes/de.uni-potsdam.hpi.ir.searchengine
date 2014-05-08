package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The title index stores for every document id the corresponding title
 * 
 * @author Alexander
 *
 */
public class TitleIndex {	
	/**
	 * This key-sorted map stores the title index. Key is the document id
	 * value is the title
	 */
	private Map<Integer, String> map = new TreeMap<>();
	
	public TitleIndex() {
		
	}	
	
	/**
	 * Adds a title for a specific document id
	 * @param term
	 */
	public void addTitle(Integer documentId, String title) {
		map.put(documentId, title);
	}
	
	/**
	 * Gets the title for a certain document ID
	 * @param documentId
	 * @return the title or null if not found
	 */
	public String getTitle(Integer documentId) {
		return map.get(documentId);
	}
	
	/**
	 * Exports the whole index to a certain file
	 * @param the filename to export the file to (the file is deleted if exists)
	 * @throws IOException 
	 */
	public void exportFile(String filename) throws IOException {
		// open the file
		File f = new File(filename);
		f.delete();
		RandomAccessFile file = new RandomAccessFile(filename, "rw");
				
		file.seek( file.length() ); // goto end of file
		
		for (Entry<Integer, String> entry : map.entrySet()) {
			Integer documentId = entry.getKey();
			String title = entry.getValue();
			file.writeInt(documentId);
			file.writeChars(title);
			file.writeChar('\0');
		}

		file.close();
	}
	
	/**
	 * Imports the whole index from a certain file
	 * @param the filename to import the file from
	 * @throws IOException 
	 */
	public void importFile(String filename) throws IOException {
		// open the file
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		
		while (file.getFilePointer() < file.length()) {
			int documentId = file.readInt();
			String title = "";		
			char c = file.readChar();		
			while (c != '\0') {
				title += c;
				c = file.readChar();
			}
			
			// add to the index
			map.put(documentId, title);
		}
		
		file.close();
	}
	
	/**
	 * Return size of Map / number of documents
	 */
	public int getSize() {
		return map.size();
	}
	
	/**
	 * Print the whole index
	 */
	@Override
	public String toString() {
		String s = "[TitleIndex]\n";
		for (Entry<Integer, String> entry : map.entrySet()) {
			s += "\tdoc: " + entry.getKey() + " -> title: " + entry.getValue() + "\n";			
		}
		return s;
	}

}
