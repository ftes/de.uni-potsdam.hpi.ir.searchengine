package de.hpi.krestel.mySearchEngine;

import java.util.Collection;
import java.util.Iterator;

public class Util {
	public static class Pair<A, B> {
		public final A a;
		public final B b;
		public Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}
	}
	
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
	 * run the {@link #runGarbageCollector()}.<p>
	 * TODO caching
	 */
	public static boolean isMainMemoryFull() {
		return Runtime.getRuntime().freeMemory() < MIN_FREE_MEMORY_BYTES;
	}
	
	/**
	 * Only suggest that the gc should run, but bettern than nothing.
	 */
	public static void runGarbageCollector() {
		Runtime.getRuntime().gc();
	}
	
	public static <T> void print(Collection<T> collection) {
		System.out.print("[");
		int i = 0;
		int size = collection.size();
		for (T t : collection) {
			System.out.print(t);
			if (i < size - 1) {
				System.out.print(", ");
			}
			i++;
		}
		System.out.println("]");
	}
}
