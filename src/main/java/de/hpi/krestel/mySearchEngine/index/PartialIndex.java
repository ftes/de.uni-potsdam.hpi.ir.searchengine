package de.hpi.krestel.mySearchEngine.index;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;
import de.hpi.krestel.mySearchEngine.util.Log;
import de.hpi.krestel.mySearchEngine.util.Log.Level;

/**
 * The index stores terms and their occurrences in main memory. At any given time we will only have parts
 * of the whole index in main memory, so this class will for example only contain the terms for the first
 * few articles.
 * 
 * @author Alexander
 *
 */
public abstract class PartialIndex<K, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> {
	private final IndexFileLinearWriter<K, L, S, V> writer;
	private final IndexListFactory<K, L, S, V> indexListFactory;
	
	/**
	 * This key-sorted map stores the partial index. Keys are the Terms as Strings.
	 * Values are the Term objects.
	 */
	private Map<K, L> map = new TreeMap<>();
	
	public PartialIndex(IndexFileLinearWriter<K, L, S, V> writer,
			IndexListFactory<K, L, S, V> indexListFactory) {
		this.writer = writer;
		this.indexListFactory = indexListFactory;
	}
	
	/**
	 * Adds a certain term with all it's occurrences to the index. If the term already exists,
	 * Occurrences are merged.
	 * @param term
	 */
	public void addList(L list) {
		if (map.containsKey(list.getKey())) {
			L existingList = map.get(list.getKey());
			existingList.merge(list);
		} else {
			map.put(list.getKey(), list);
		}
	}
	
	public void addSlotForKey(K key, S slot) {
		if (map.containsKey(key)) {
			map.get(key).addSlot(slot);
		} else {
			L list = indexListFactory.createList(key);
			list.addSlot(slot);
			map.put(key, list);
		}
	}
	
	/**
	 * Stores the whole index to disk to a file named "[getID()].dat"
	 * @param the directory to store the file in (no trailing slash)
	 * @throws IOException 
	 */
	public void store(String directory) throws IOException {		
		for (L list : map.values()) {
			try {
				writer.storeList(list);
			} catch (WordLengthException e) {
				Log.log(Level.DEBUG, e.getMessage());
			}
		}
		writer.close();		
	}
	
	/**
	 * Print the whole index
	 */
	@Override
	public String toString() {
		String s = "";
		for (Entry<K, L> entry : map.entrySet()) {
			s += entry.getValue().toString() + "\n";			
		}
		return s;
	}

}
