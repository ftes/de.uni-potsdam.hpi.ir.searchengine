package de.hpi.krestel.mySearchEngine.index;

import java.util.SortedSet;

public interface IndexListFactory<K, L extends IndexList<K, S, V>, S extends IndexListSlot<V>, V extends Comparable<V>> {
	L createList(K key);
	L createList(K key, SortedSet<S> slots);
	S createSlot(int docId, V value);
}
