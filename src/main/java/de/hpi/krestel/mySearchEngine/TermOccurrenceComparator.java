package de.hpi.krestel.mySearchEngine;

import java.util.Comparator;

/**
 * Compare term occurrences by first comparing the document id, and in case
 * of equals document ids then comparing the positions within the document.
 * @author fredrik
 *
 */
public enum TermOccurrenceComparator implements Comparator<TermOccurrence> {
	INSTANCE;
	
	@Override
	public int compare(TermOccurrence o1, TermOccurrence o2) {
		int result = Integer.compare(o1.getDocumentId(), o2.getDocumentId());
		if (result != 0) return result;
		
		return Integer.compare(o1.getPosition(), o2.getPosition());
	}

}
