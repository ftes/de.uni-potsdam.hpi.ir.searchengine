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

import de.hpi.krestel.mySearchEngine.index.IndexListSlotComparator;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearReader;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexMergerImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;
import de.hpi.krestel.mySearchEngine.util.Log;
import de.hpi.krestel.mySearchEngine.util.Log.Level;
import de.hpi.krestel.mySearchEngine.util.Util;

public class IndexMergerTest {
	private static Term createTerm(String term, TermOccurrence... occurrences) {
		SortedSet<TermOccurrence> set = new TreeSet<>(
				new IndexListSlotComparator<TermOccurrence, Integer>());
		set.addAll(Arrays.asList(occurrences));
		return new Term(term, set);
	}

	@Test
	public void testMerging() throws IOException {
		Log.LEVEL = Level.DEBUG;

		MockIndexFileHandlerImpl partial1 = new MockIndexFileHandlerImpl(
				"1", createTerm("anton", new TermOccurrence(1, 3),
						new TermOccurrence(1, 10)), createTerm("berta",
						new TermOccurrence(1, 4)));

		MockIndexFileHandlerImpl partial2 = new MockIndexFileHandlerImpl(
				"2", createTerm("anna", new TermOccurrence(2, 3)), createTerm(
						"carla", new TermOccurrence(2, 4)));

		MockIndexFileHandlerImpl partial3 = new MockIndexFileHandlerImpl(
				"3", createTerm("annika", new TermOccurrence(3, 3)),
				createTerm("anton", new TermOccurrence(1, 5)), createTerm(
						"berta", new TermOccurrence(3, 11)), createTerm(
						"doris", new TermOccurrence(3, 4)));

		MockIndexFileHandlerImpl partial4 = new MockIndexFileHandlerImpl(
				"4", createTerm("boris", new TermOccurrence(4, 1)), createTerm(
						"doris", new TermOccurrence(4, 2)));

		SeekList<String> seekList = new MockSeekListImpl();
		Set<IndexFileLinearReader<String, Term, TermOccurrence, Integer>> partialIndexHandlers = new HashSet<>(
				Arrays.asList((IndexFileLinearReader<String, Term, TermOccurrence, Integer>)
						partial1, partial2, partial3, partial4));
		MockIndexFileHandlerImpl mainIndexHandler =
				new MockIndexFileHandlerImpl( "main");

		new TermIndexMergerImpl().merge(seekList, partialIndexHandlers,
				mainIndexHandler);

		List<Term> terms = mainIndexHandler.getTerms();

		assertEquals("anna", terms.get(0).getKey());
		Term anton = terms.get(2);
		assertEquals("anton", anton.getKey());
		assertEquals(3, anton.getSlots().size());
		assertEquals(5, (int) Util.get(anton.getSlots(), 1).getValue());

		Term doris = terms.get(6);
		assertEquals("doris", doris.getKey());
		assertEquals(2, doris.getSlots().size());
	}
}
