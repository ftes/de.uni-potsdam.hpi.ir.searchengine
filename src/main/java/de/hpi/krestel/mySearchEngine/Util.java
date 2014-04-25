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
	
	/**
	 * Leave at least 50 MBytes of memory free.
	 */
	public static final long MIN_FREE_MEMORY_BYTES = 50 * 1024 * 1024;
	
	/**
	 * Determine whether the main memory can be considered full.
	 * If so, delete all references to large objects no longer needed (e.g. partial index) and
	 * run the {@link #runGarbageCollector()}.
	 */
	public static boolean isMainMemoryFull() {
		long freeMemory = Runtime.getRuntime().freeMemory();
		return freeMemory < MIN_FREE_MEMORY_BYTES;
	}
	
	/**
	 * Only suggest that the gc should run, but bettern than nothing.
	 */
	public static void runGarbageCollector() {
		Runtime.getRuntime().gc();
	}
}
