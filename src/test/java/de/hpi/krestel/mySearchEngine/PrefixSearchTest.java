package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.index.term.TermSeekListImpl;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public class PrefixSearchTest {
	@Test
	public void testWritingAndReading() throws IOException,
			TermLengthException, KeyNotFoundException {
		String fileName = "prefixTest.dat";

		try {
			SeekList<String> seekList = new TermSeekListImpl(fileName);
			seekList.storeKeyOffset("aab", 1);
			seekList.storeKeyOffset("abb", 2);
			seekList.storeKeyOffset("abbaa", 3);
			seekList.storeKeyOffset("abbklf", 4);
			seekList.storeKeyOffset("abbklff", 5);
			seekList.storeKeyOffset("abc", 6);
			seekList.storeKeyOffset("ba", 7);
			seekList.storeKeyOffset("bbb", 8);
			seekList.storeKeyOffset("bc", 9);

			Set<String> result = seekList.getKeysBeginningWith("abb");
			assertEquals(4, result.size());
			assertTrue(result.containsAll(Arrays.asList("abb", "abbaa",
					"abbklf", "abbklff")));

			assertEquals(3, seekList.getKeysBeginningWith("b").size());

			assertEquals(0, seekList.getKeysBeginningWith("c").size());
			assertEquals(0, seekList.getKeysBeginningWith("aaaa").size());
			assertEquals(0, seekList.getKeysBeginningWith("aaa").size());
		} finally {
			new File(fileName).delete();
		}
	}
	@Test
	public void test2() throws IOException,
			TermLengthException, KeyNotFoundException {
		String fileName = "prefixTest.dat";

		try {
			SeekList<String> seekList = new TermSeekListImpl(fileName);
			seekList.storeKeyOffset("anja", 1);
			seekList.storeKeyOffset("anna", 2);
			seekList.storeKeyOffset("anton", 3);
			seekList.storeKeyOffset("ars", 4);
			seekList.storeKeyOffset("artikel", 5);
			seekList.storeKeyOffset("bee", 6);

			Set<String> result = seekList.getKeysBeginningWith("art");
			assertEquals(1, result.size());
			assertTrue(result.containsAll(Arrays.asList("artikel")));
		} finally {
			new File(fileName).delete();
		}
	}
}
