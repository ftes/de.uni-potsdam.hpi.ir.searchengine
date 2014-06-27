package de.hpi.krestel.mySearchEngine.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This inmemory index stores for every document title the corresponding
 * document id
 * 
 * @author Alexander
 * 
 */
public class TitleIndex {
	/**
	 * This key-sorted map stores the index. Key is the title, value the
	 * document id
	 */
	private final Map<String, Integer> map = new HashMap<>();

	public TitleIndex() {

	}

	/**
	 * Adds a new document title to the index
	 * 
	 * @param title
	 * @param docId
	 */
	public void addTitle(String title, int docId) {
		title = title.toLowerCase().trim();
		map.put(title, docId);
	}

	/**
	 * Returns document id of the page with the given title, or null if this
	 * title is not found.
	 * 
	 * @param title
	 * @return
	 */
	public Integer getDocId(String title) {
		title = title.toLowerCase().trim();
		return map.get(title);
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
		for (Entry<String, Integer> entry : map.entrySet()) {
			s += "\ttitle: " + entry.getKey() + " -> docId: "
					+ entry.getValue() + "\n";
		}
		return s;
	}

	public void exportFile(String filename) throws IOException {
		// open the file
		File f = new File(filename);
		f.delete();
		RandomAccessFile file = new RandomAccessFile(filename, "rw");

		file.seek(file.length()); // goto end of file

		for (Entry<String, Integer> entry : map.entrySet()) {
			String title = entry.getKey();
			Integer docId = entry.getValue();
			file.writeUTF(title);
			file.writeInt(docId);
		}

		file.close();
	}

	public void importFile(String filename) throws IOException {
		// open the file
		RandomAccessFile file = new RandomAccessFile(filename, "r");

		while (file.getFilePointer() < file.length()) {
			String title = file.readUTF();
			Integer docId = file.readInt();

			// add to the index
			map.put(title, docId);
		}

		file.close();
	}

}
