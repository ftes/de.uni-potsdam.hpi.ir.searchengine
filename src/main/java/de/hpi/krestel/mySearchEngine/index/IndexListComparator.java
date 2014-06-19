package de.hpi.krestel.mySearchEngine.index;

import java.util.Comparator;

/**
 * Compares terms based by comparing the string representations. Will give a random ordering
 * if both have the same term, unless they share object identity.
 * @author fredrik
 *
 */
public class IndexListComparator<K extends Comparable<K>, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> implements Comparator<L> {	

	@Override
	public int compare(L o1, L o2) {
		int result = o1.getKey().compareTo(o2.getKey());
		if (result == 0) {
			return o1 == o2 ? 0 : -1;
		}
		return result;
	}
}
