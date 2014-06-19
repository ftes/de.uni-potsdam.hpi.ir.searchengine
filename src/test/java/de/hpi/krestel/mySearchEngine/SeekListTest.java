package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.index.term.TermSeekListImpl;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public class SeekListTest {
	@Test
	public void testWritingAndReading() throws IOException, TermLengthException, KeyNotFoundException {
		String fileName = "seekListTest.dat";
		
		SeekList<String> seekList = new TermSeekListImpl(fileName);
		seekList.storeKeyOffset("a", 1);
		seekList.storeKeyOffset("b", 2);
		seekList.storeKeyOffset("c", 3);
		seekList.storeKeyOffset("delta", 4);
		seekList.storeKeyOffset("e", 5);
		seekList.storeKeyOffset("f", 6);
		seekList.storeKeyOffset("genauzwanzigzeichen.", 7);
		seekList.storeKeyOffset("h", 8);
		seekList.storeKeyOffset("i", 9);
		
		assertEquals(5, seekList.getKeyOffsetInIndex("e")); //in middle
		assertEquals(1, seekList.getKeyOffsetInIndex("a")); //very first
		assertEquals(8, seekList.getKeyOffsetInIndex("h")); //very last
		assertEquals(7, seekList.getKeyOffsetInIndex("genauzwanzigzeichen.")); //max length
//		assertEquals(-1, seekList.getTermOffsetInIndex("z")); //not in seeklist
		
		new File(fileName).delete();
	}
}
