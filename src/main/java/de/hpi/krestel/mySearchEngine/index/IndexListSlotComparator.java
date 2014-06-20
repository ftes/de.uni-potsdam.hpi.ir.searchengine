package de.hpi.krestel.mySearchEngine.index;

import java.util.Comparator;

/**
 * Compare term occurrences by first comparing the document id, and in case
 * of equals document ids then comparing the positions within the document.
 * @author fredrik
 *
 */
public class IndexListSlotComparator<S extends IndexListSlot<V>, V extends Comparable<V>>
implements Comparator<S> {
	@Override
	public int compare(S o1, S o2) {
		int result = Integer.compare(o1.getDocumentId(), o2.getDocumentId());
		if (result != 0) return result;
		
		return o1.getValue().compareTo(o2.getValue());
	}
}
