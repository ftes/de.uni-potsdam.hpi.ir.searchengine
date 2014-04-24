package de.hpi.krestel.mySearchEngine;

import java.util.Comparator;

/**
 * Compares terms based by comparing the string representations. Will give a random ordering
 * if both have the same term, unless they share object identity.
 * @author fredrik
 *
 */
public enum TermComparator implements Comparator<Term> {
	INSTANCE;
	
	@Override
	public int compare(Term o1, Term o2) {
		int result = o1.getTerm().compareTo(o2.getTerm());
		if (result == 0) {
			return o1 == o2 ? 0 : -1;
		}
		return result;
	}
}
