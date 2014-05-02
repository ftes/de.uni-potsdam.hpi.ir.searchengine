package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class PrefixSearchTest {
	@Test
	public void testWritingAndReading() throws IOException,
			TermLengthException, TermNotFoundException {
		String fileName = "prefixTest.dat";

		try {
			SeekList seekList = new SeekListImpl(fileName);
			seekList.storeTermOffset("aab", 1);
			seekList.storeTermOffset("abb", 2);
			seekList.storeTermOffset("abbaa", 3);
			seekList.storeTermOffset("abbklf", 4);
			seekList.storeTermOffset("abbklff", 5);
			seekList.storeTermOffset("abc", 6);
			seekList.storeTermOffset("ba", 7);
			seekList.storeTermOffset("bbb", 8);
			seekList.storeTermOffset("bc", 9);

			Set<String> result = seekList.getTermsBeginningWith("abb");
			assertEquals(4, result.size());
			assertTrue(result.containsAll(Arrays.asList("abb", "abbaa",
					"abbklf", "abbklff")));

			assertEquals(3, seekList.getTermsBeginningWith("b").size());

			assertEquals(0, seekList.getTermsBeginningWith("c").size());
			assertEquals(0, seekList.getTermsBeginningWith("aaaa").size());
			assertEquals(0, seekList.getTermsBeginningWith("aaa").size());
		} finally {
			new File(fileName).delete();
		}
	}
	@Test
	public void test2() throws IOException,
			TermLengthException, TermNotFoundException {
		String fileName = "prefixTest.dat";

		try {
			SeekList seekList = new SeekListImpl(fileName);
			seekList.storeTermOffset("anja", 1);
			seekList.storeTermOffset("anna", 2);
			seekList.storeTermOffset("anton", 3);
			seekList.storeTermOffset("ars", 4);
			seekList.storeTermOffset("artikel", 5);
			seekList.storeTermOffset("bee", 6);

			Set<String> result = seekList.getTermsBeginningWith("art");
			assertEquals(1, result.size());
			assertTrue(result.containsAll(Arrays.asList("artikel")));
		} finally {
			new File(fileName).delete();
		}
	}
}
