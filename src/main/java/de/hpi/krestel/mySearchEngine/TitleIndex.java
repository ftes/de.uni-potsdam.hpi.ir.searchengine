package de.hpi.krestel.mySearchEngine;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This inmemory index stores for every document title the corresponding document id
 * 
 * @author Alexander
 *
 */
public class TitleIndex {	
	/**
	 * This key-sorted map stores the index. Key is the title,
	 * value the document id
	 */
	private Map<String, Integer> map = new TreeMap<>();
	
	public TitleIndex() {
		
	}	
	
	/**
	 * Adds a new document title to the index
	 * @param title
	 * @param docId
	 */
	public void addTitle(String title, int docId) {
		map.put(title, docId);
	}
	
	/**
	 * Returns document id of the page with the given title, or null if this title is not found. 
	 * @param title
	 * @return
	 */
	public int getDocId(String title) {
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
			s += "\ttitle: " + entry.getKey() + " -> docId: " + entry.getValue() + "\n";			
		}
		return s;
	}

}
