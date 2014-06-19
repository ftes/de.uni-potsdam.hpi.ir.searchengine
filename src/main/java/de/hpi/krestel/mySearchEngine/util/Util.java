package de.hpi.krestel.mySearchEngine.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class Util {
	public static class Pair<A, B> {
		public final A a;
		public final B b;
		public Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair<?, ?> other = (Pair<?, ?>) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}
	}
	
	public static class PairAComparator <C extends Comparable<C>, T extends Pair<C, ?>> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			return o1.a.compareTo(o2.a);
		}
	}
	
	public static class PairBComparator <C extends Comparable<C>, T extends Pair<?, C>> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			return o1.b.compareTo(o2.b);
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
	 * Leave at least 500 MBytes of memory free.
	 */
	public static final long MIN_FREE_MEMORY_BYTES = 100 * 1024 * 1024;
	public static final long MAX_USED_MEMORY_BYTES = 1600 * 1024 * 1024;
	/**
	 * Determine whether the main memory can be considered full.
	 * If so, delete all references to large objects no longer needed (e.g. partial index) and
	 * run the {@link #runGarbageCollector()}.<p>
	 * TODO caching
	 */
	public static boolean isMainMemoryFull() {
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		return usedMemory > MAX_USED_MEMORY_BYTES;
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
	
	public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
		if (! map.containsKey(key)) {
			return defaultValue;
		}
		return map.get(key);
	}
	
	public static <K> void increment(Map<K, Integer> map, K key) {
		map.put(key, get(map, key, 0) + 1);
	}
	
	public static <T extends Comparable<T>> T min(T one, T two) {
		return one.compareTo(two) < 0 ? one : two;
	}
}
