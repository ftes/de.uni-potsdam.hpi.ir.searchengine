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

import de.hpi.krestel.mySearchEngine.Log.Level;


public class IndexMergerImpl implements IndexMerger {
	@Override
	public void merge(String seekListPath, String partialIndexDirectory, String mergedIndexPath) throws IOException {
		Set<IndexFileHandler> handlers = new HashSet<>();
		IndexFileHandler indexHandler = new IndexFileHandlerImpl(mergedIndexPath);	
		SeekList seekList = new SeekListImpl(seekListPath);
		
		for (File file : new File(partialIndexDirectory).listFiles()) {
			if (file.isFile()) {
				handlers.add(new IndexFileHandlerImpl(file.getAbsolutePath()));
			}
		}
		
		merge(seekList, handlers, indexHandler);
		
		for (IndexFileHandler handler : handlers) {
			handler.close();
		}
		indexHandler.close();seekList.close();
	}
	
//	private String getCurrentTermSet(SortedMap<Term, IndexFileHandler> currentTerms) {
//		StringBuilder sb = new StringBuilder();
//		for (Term term : currentTerms.keySet()) {
//			IndexFileHandler handler = currentTerms.get(term);
//			sb.append(handler.getFilename() + ": " + term.getTerm());
//			sb.append(", ");
//		}
//		return sb.toString();
//	}
	
	@Override
	public void merge(SeekList seekList, Set<IndexFileHandler> partialIndexHandlers,
			IndexFileHandler mainIndexHandler) throws IOException {
		SortedMap<Term, IndexFileHandler> currentTerms = new TreeMap<>(TermComparator.INSTANCE);
		
		while (true) {
			fillCurrentTermSet(currentTerms, partialIndexHandlers);
//			Log.log(Level.DEBUG, "Current Term set: " + getCurrentTermSet(currentTerms));
			if (partialIndexHandlers.isEmpty()) {
				break;
			}
			
			//find as many occurrences of the same term in the set as possible, beginning with the first
			String term = currentTerms.firstKey().getTerm();
			NavigableSet<TermOccurrence> occurrences = new TreeSet<TermOccurrence>(TermOccurrenceComparator.INSTANCE);
			for (Iterator<Term> i = currentTerms.keySet().iterator(); i.hasNext();) {
				Term checkTerm = i.next();
				if (checkTerm.getTerm().equals(term)) {
					occurrences.addAll(checkTerm.getOccurrences());
					i.remove();
				} else {
					break;
				}
			}
//			Log.log(Level.DEBUG, "Chose first " + n + " from term set");
			
			//now these have been found, write them to the main index file and seeklist
			Term combinedTerm = new Term(term, occurrences);
			long offset = mainIndexHandler.storeTerm(combinedTerm);
			try {
				seekList.storeTermOffset(term, offset);
			} catch (TermLengthException e) {
				Log.log(Level.ERROR, "Did not store term offset in seeklist because it exceeded max length: " + term);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads the new terms from those files for which currently no term is placed in the
	 * {@link #currentTermMap}.
	 * If the end of one file is reached, it is removed from the {@link #handlers}.
	 * @throws IOException 
	 */
	private void fillCurrentTermSet(SortedMap<Term, IndexFileHandler> currentTerms,
			Set<IndexFileHandler> partialIndexHandlers) throws IOException {
		Set<IndexFileHandler> missingHandlers = new HashSet<>(partialIndexHandlers);
		missingHandlers.removeAll(currentTerms.values());
		for (IndexFileHandler handler : missingHandlers) {
			Term term = handler.readNextTerm();
			if (term == null) {
				partialIndexHandlers.remove(handler);
			} else {
				currentTerms.put(term, handler);
			}
		}
	}
}
