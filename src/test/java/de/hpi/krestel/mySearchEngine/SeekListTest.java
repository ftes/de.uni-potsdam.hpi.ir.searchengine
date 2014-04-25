package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SeekListTest {
	@Test
	public void testWritingAndReading() throws IOException, TermLengthException, TermNotFoundException {
		String fileName = "seekListTest.dat";
		
		SeekList seekList = new SeekListImpl(fileName);
		seekList.storeTermOffset("a", 1);
		seekList.storeTermOffset("b", 2);
		seekList.storeTermOffset("c", 3);
		seekList.storeTermOffset("delta", 4);
		seekList.storeTermOffset("e", 5);
		seekList.storeTermOffset("f", 6);
		seekList.storeTermOffset("genauzwanzigzeichen.", 7);
		seekList.storeTermOffset("h", 8);
		seekList.storeTermOffset("i", 9);
		
		assertEquals(5, seekList.getTermOffsetInIndex("e")); //in middle
		assertEquals(1, seekList.getTermOffsetInIndex("a")); //very first
		assertEquals(8, seekList.getTermOffsetInIndex("h")); //very last
		assertEquals(7, seekList.getTermOffsetInIndex("genauzwanzigzeichen.")); //max length
		assertEquals(-1, seekList.getTermOffsetInIndex("z")); //not in seeklist
		
		new File(fileName).delete();
	}
}
