package de.hpi.krestel.mySearchEngine;

import java.util.Comparator;

/**
 * Compares terms based by comparing the string representations.
 * @author fredrik
 *
 */
public class TermComparator implements Comparator<Term> {
	@Override
	public int compare(Term o1, Term o2) {
		return o1.getTerm().compareTo(o2.getTerm());
	}
}
