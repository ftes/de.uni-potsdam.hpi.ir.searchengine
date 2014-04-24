package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Merge several partial index files into one large index files, while directly creating
 * the seeklist file on disk in parallel.
 * 
 * Because each of the partial index files is about as large as the allocated main memory,
 * no more than one partial index can be loaded into main memory at a single point in time.
 * Instead, the merging process steps through each partial index line by line in parallel.
 * 
 * As the partial indexes are alphabetically sorted, words occuring in several partial
 * indexes are found at the same time, can be combined, and then written directly to the
 * combined index file.
 * 
 * The word offsets within the merged index are directly written to the seeklist file while
 * merging, as the merging process steps through all words individually anyway.
 * @author fredrik
 *
 */
public class IndexMerger {
	private final SortedMap<Term, IndexFileHandler> currentTerms = new TreeMap<>(TermComparator.INSTANCE);
	private Set<IndexFileHandler> handlers;
	
	/**
	 * Merge the partial index files that are located in the {@code partialIndexDirectory} into
	 * one file located at {@code mergedIndexPath}.
	 * The seeklist is written to the {@code seekListPath}.
	 * 
	 * @param seekList The seeklist handler
	 * @param partialIndexDirectory The directory where the partial index files reside
	 * @param mergedIndexPath The path to the merged index file (is overwritten!)
	 * @param seekListPath The path to the seeklist file (is overwritten!)
	 * @throws IOException 
	 */
	public void merge(SeekList seekList, String partialIndexDirectory, String mergedIndexPath, String seekListPath) throws IOException {
		Set<IndexFileHandler> handlers = new HashSet<>();
		IndexFileHandler indexHandler = new IndexFileHandler(mergedIndexPath);		
		
		for (File file : new File(partialIndexDirectory).listFiles()) {
			if (file.isFile()) {
				handlers.add(new IndexFileHandler(file.getAbsolutePath()));
			}
		}
		
		while (true) {
			fillCurrentTermSet();
			if (handlers.isEmpty()) {
				break;
			}
			
			//find as many occurrences of the same term in the set as possible, beginning with the first
			String term = currentTerms.firstKey().getTerm();
			NavigableSet<TermOccurence> occurrences = new TreeSet<TermOccurence>(TermOccurenceComparator.INSTANCE);
			for (Iterator<Term> i = currentTerms.keySet().iterator(); i.hasNext();) {
				Term checkTerm = i.next();
				if (checkTerm.getTerm().equals(term)) {
					occurrences.addAll(checkTerm.getOccurrences());
					i.remove();
				}
			}
			
			//now these have been found, write them to the main index file and seeklist
			Term combinedTerm = new Term(term, occurrences);
			long offset = indexHandler.storeTerm(combinedTerm);
			seekList.storeTermOffset(term, offset);
		}
	}
	
	/**
	 * Reads the new terms from those files for which currently no term is placed in the
	 * {@link #currentTermMap}.
	 * If the end of one file is reached, it is removed from the {@link #handlers}.
	 * @throws IOException 
	 */
	private void fillCurrentTermSet() throws IOException {
		Set<IndexFileHandler> missingHandlers = new HashSet<>(handlers);
		missingHandlers.removeAll(currentTerms.values());
		for (IndexFileHandler handler : missingHandlers) {
			Term term = handler.readNextTerm();
			if (term == null) {
				handlers.remove(handler);
			} else {
				currentTerms.put(term, handler);
			}
		}
	}
}
