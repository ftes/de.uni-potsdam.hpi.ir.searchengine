package de.hpi.krestel.mySearchEngine.index.term;

import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

/**
 * This represents a single occurence of a termin in a certain document.
 * The termoccurence is referenced in the Term.
 * 
 * @author Alexander
 *
 */
public class TermOccurrence extends IndexListSlot<Integer> {
	public TermOccurrence() {}
	public TermOccurrence(int documentId, int position) {
		super(documentId, position);
	}
}
