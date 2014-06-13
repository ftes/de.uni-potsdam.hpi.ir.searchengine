package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The index stores for every document id the corresponding title and text
 * 
 * @author Alexander
 *
 */
public class PageIndex {	
	/**
	 * This key-sorted map stores the index. Key is the document id
	 * value is the offset in the pageindex file
	 */
	private Map<Integer, Long> map = new TreeMap<>();
	private RandomAccessFile pageFile = null;
	
	public PageIndex() {
		
	}	
	
	/**
	 * Adds an offset for a specific document id
	 * @param term
	 */
	public void addOffset(Integer documentId, Long offset) {
		map.put(documentId, offset);
	}
	
	/**
	 * Gets the text for a certain document ID
	 * @param documentId
	 * @return the text or null if not found
	 * @throws IOException 
	 */
	public String getText(Integer documentId) throws IOException {
		Page p = getPage(documentId);
		if (p == null) return null;
		else return p.getText();
	}
	
	/**
	 * Gets the title for a certain document ID
	 * @param documentId
	 * @return the title or null if not found
	 * @throws IOException 
	 */
	public String getTitle(Integer documentId) throws IOException {
		Page p = getPage(documentId);
		if (p == null) return null;
		else return p.getTitle();
	}
	
	/**
	 * Gets the page for a certain document ID
	 * @param documentId
	 * @return the page or null if not found
	 * @throws IOException 
	 */
	public Page getPage(Integer documentId) throws IOException {
		if (pageFile == null) {
			throw new IllegalStateException("No page file open");
		}
		Long offset = map.get(documentId);
		if (offset == null) return null;
		
		pageFile.seek(offset);
		Page page = new Page();
		page.setId(documentId);
		
		String title = pageFile.readUTF();
		page.setTitle(title);
		
		String text = pageFile.readUTF();		
		page.setText(text);
		
		return page;
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
		
		for (Entry<Integer, Long> entry : map.entrySet()) {
			Integer documentId = entry.getKey();
			long offset = entry.getValue();
			file.writeInt(documentId);
			file.writeLong(offset);
		}

		file.close();
	}
	
	/**
	 * Imports the whole index from a certain file and opens the page file
	 * @param the filename to import the file from
	 * @throws IOException 
	 */
	public void importFile(String pageIndexFile, String pageFile) throws IOException {
		// open the file
		RandomAccessFile file = new RandomAccessFile(pageIndexFile, "r");
		
		while (file.getFilePointer() < file.length()) {
			int documentId = file.readInt();
			long offset = file.readLong();		
			
			// add to the index
			map.put(documentId, offset);
		}
		
		file.close();
		
		openPageFile(pageFile);
	}
	
	public void openPageFile(String pageFilename) throws FileNotFoundException {
		pageFile = new RandomAccessFile(pageFilename, "r");
	}
	
	public void closePageFile() throws IOException {
		pageFile.close();
		pageFile = null;
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
		for (Entry<Integer, Long> entry : map.entrySet()) {
			s += "\tdoc: " + entry.getKey() + " -> offset: " + entry.getValue() + "\n";			
		}
		return s;
	}

}
