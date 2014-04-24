package de.hpi.krestel.mySearchEngine;

import java.util.Iterator;

public class Util {
	public static <T> T get(Iterable<T> iterable, int i) {
		Iterator<T> iter = iterable.iterator();
		T t = iter.next();
		for (; i>0; i--) {
			t = iter.next();
		}
		return t;
	}
	
	public static <T> String toString(Iterable<T> iterable) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (T t : iterable) {
			sb.append(t.toString());
			sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
}
