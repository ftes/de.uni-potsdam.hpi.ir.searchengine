package de.hpi.krestel.mySearchEngine;

import java.util.Comparator;

/**
 * Compare term occurrences by first comparing the document id, and in case
 * of equals document ids then comparing the positions within the document.
 * @author fredrik
 *
 */
public enum TermOccurenceComparator implements Comparator<TermOccurence> {
	INSTANCE;
	
	@Override
	public int compare(TermOccurence o1, TermOccurence o2) {
		int result = Integer.compare(o1.getDocumentId(), o2.getDocumentId());
		if (result != 0) return result;
		
		return Integer.compare(o1.getPosition(), o2.getPosition());
	}

}
