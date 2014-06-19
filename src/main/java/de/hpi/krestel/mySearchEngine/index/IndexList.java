package de.hpi.krestel.mySearchEngine.index;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class IndexList<K, S extends IndexListSlot<V>, V extends Comparable<V>> {
	/** 
	 * the tokenized term
	 */
	private K key;
	/**
	 * a list of occurences, without any duplicates
	 */
	private SortedSet<S> slots;
	
	public IndexList(K key) {
		this(key, new TreeSet<S>(new IndexListSlotComparator<S, V>()));
	}
	
	public IndexList(K key, SortedSet<S> slots) {
		this.key = key;
		this.slots = slots;
	}
	
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	
	/**
	 * Returns a list of occurences, sorted first by document id and then by position within
	 * that document.
	 */
	public SortedSet<S> getSlots() {
		return slots;
	}
	
	public int getOccurenceCountFor(int docId) {
		int count = 0;
		// TODO: Make that faster
		for (S slot : slots) {
			if (slot.getDocumentId() == docId) count++;
		}
		return count;
	}
	
	public Set<Integer> getDocumentIds() {
		Set<Integer> docIds = new HashSet<>();
		for (S slot : slots) {
			docIds.add(slot.getDocumentId());
		}
		return docIds;
	}
	
	/**
	 * Adds a occurence to the term, only if it doesn't exist yet.
	 * @param occurence
	 */
	public void addSlot(S slot) {
		this.slots.add(slot);
	}
	
	@Override
	public String toString() {
		String s = "[Key] key=" + key;
		for (S slot : slots) {
			s += "\n\t" + slot.toString();
		}
		return s;
	}
	
	public void merge(IndexList<K, S, V> indexList) {
		slots.addAll(indexList.getSlots());
	}
}
