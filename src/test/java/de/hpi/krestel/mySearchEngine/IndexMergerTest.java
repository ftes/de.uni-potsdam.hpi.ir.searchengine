package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.Log.Level;

public class IndexMergerTest {
	private static Term createTerm(String term, TermOccurrence...occurrences) {
		SortedSet<TermOccurrence> set = new TreeSet<>(TermOccurrenceComparator.INSTANCE);
		set.addAll(Arrays.asList(occurrences));
		return new Term(term, set);
	}
	
	@Test
	public void testMerging() throws IOException {
		Log.LEVEL = Level.DEBUG;
		
		IndexFileLinearReader partial1 = new MockIndexFileHandlerImpl("1",
				createTerm("anton", new TermOccurrence(1, 3), new TermOccurrence(1, 10)),
				createTerm("berta", new TermOccurrence(1, 4)));
		
		IndexFileLinearReader partial2 = new MockIndexFileHandlerImpl("2",
				createTerm("anna", new TermOccurrence(2, 3)),
				createTerm("carla", new TermOccurrence(2, 4)));
		
		IndexFileLinearReader partial3 = new MockIndexFileHandlerImpl("3",
				createTerm("annika", new TermOccurrence(3, 3)),
				createTerm("anton", new TermOccurrence(1, 5)),
				createTerm("berta", new TermOccurrence(3, 11)),
				createTerm("doris", new TermOccurrence(3, 4)));
		
		IndexFileLinearReader partial4 = new MockIndexFileHandlerImpl("4",
				createTerm("boris", new TermOccurrence(4, 1)),
				createTerm("doris", new TermOccurrence(4, 2)));
		
		MockSeekListImpl seekList = new MockSeekListImpl();
		Set<IndexFileLinearReader> partialIndexHandlers = new HashSet<>(Arrays.asList(
				partial1, partial2, partial3, partial4));
		MockIndexFileHandlerImpl mainIndexHandler = new MockIndexFileHandlerImpl("main");
		
		new IndexMergerImpl().merge(seekList, partialIndexHandlers, mainIndexHandler);
		
		List<Term> terms = mainIndexHandler.getTerms();
		
		assertEquals("anna", terms.get(0).getTerm());
		Term anton = terms.get(2);
		assertEquals("anton", anton.getTerm());
		assertEquals(3, anton.getOccurrences().size());
		assertEquals(5, Util.get(anton.getOccurrences(), 1).getPosition());
		
		Term doris = terms.get(6);
		assertEquals("doris", doris.getTerm());
		assertEquals(2, doris.getOccurrences().size());
	}
}
