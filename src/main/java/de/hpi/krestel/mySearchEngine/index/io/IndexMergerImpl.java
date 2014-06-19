package de.hpi.krestel.mySearchEngine.index.io;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListComparator;
import de.hpi.krestel.mySearchEngine.index.IndexListFactory;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;
import de.hpi.krestel.mySearchEngine.index.IndexListSlotComparator;
import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;
import de.hpi.krestel.mySearchEngine.util.Log;
import de.hpi.krestel.mySearchEngine.util.Log.Level;


public abstract class IndexMergerImpl<K extends Comparable<K>, L extends IndexList<K, S, V>, S extends IndexListSlot<V>,
V extends Comparable<V>> implements IndexMerger<K, L, S, V> {
	private final IndexFileHandlerFactory<K, L, S, V> indexFileHandlerFactory;
	private final IndexListFactory<K, L, S, V> indexListFactory;
	private final TitleIndex titleIndex;
	
	public IndexMergerImpl(IndexFileHandlerFactory<K, L, S, V> indexFileHandlerFactory,
			IndexListFactory<K, L, S, V> indexListFactory) {
		this(indexFileHandlerFactory, indexListFactory, new TitleIndex());
	}
	
	public IndexMergerImpl(IndexFileHandlerFactory<K, L, S, V> indexFileHandlerFactory,
			IndexListFactory<K, L, S, V> indexListFactory, TitleIndex titleIndex) {
		this.indexFileHandlerFactory = indexFileHandlerFactory;
		this.indexListFactory = indexListFactory;
		this.titleIndex = titleIndex;
	}
	
	@Override
	public void merge(String stemmedSeekListPath, String unstemmedSeekListPath,
			String stemmedPartialIndexDirectory, String unstemmedPartialIndexDirectory,
			String stemmedMergedIndexPath, String unstemmedMergedIndexPath) throws IOException {
		IndexFileLinearWriter<K, L, S, V> stemmedIndexHandler = indexFileHandlerFactory.createLinearWriter(stemmedMergedIndexPath);
		IndexFileLinearWriter<K, L, S, V> unstemmedIndexHandler = indexFileHandlerFactory.createLinearWriter(unstemmedMergedIndexPath);
		SeekList<K> stemmedSeekList = indexFileHandlerFactory.createSeekList(stemmedSeekListPath);
		SeekList<K> unstemmedSeekList = indexFileHandlerFactory.createSeekList(unstemmedSeekListPath);
		
		merge(stemmedPartialIndexDirectory, stemmedIndexHandler, stemmedSeekList);
		merge(unstemmedPartialIndexDirectory, unstemmedIndexHandler, unstemmedSeekList);
	}
	
	private void merge(String partialIndexDirectory, IndexFileLinearWriter<K, L, S, V> indexHandler,
			SeekList<K> seekList) throws IOException {
		Set<IndexFileLinearReader<K, L, S, V>> handlers = new HashSet<>();
		
		for (File file : new File(partialIndexDirectory).listFiles()) {
			if (file.isFile()) {
				handlers.add(indexFileHandlerFactory.createLinearReader(file.getAbsolutePath()));
			}
		}
		
		merge(seekList, handlers, indexHandler);
		
		for (IndexFileLinearReader<K, L, S, V> handler : handlers) {
			handler.close();
		}
		indexHandler.close();
		seekList.close();
	}
	
	@Override
	public void merge(SeekList<K> seekList, Set<IndexFileLinearReader<K, L, S, V>> partialIndexHandlers,
			IndexFileLinearWriter<K, L, S, V> mainIndexHandler) throws IOException {
		SortedMap<L, IndexFileLinearReader<K, L, S, V>> currentLists =
				new TreeMap<>(new IndexListComparator<K, L, S, V>());
		
		while (true) {
			fillCurrentListsSet(currentLists, partialIndexHandlers);
//			Log.log(Level.DEBUG, "Current Term set: " + getCurrentTermSet(currentTerms));
			if (partialIndexHandlers.isEmpty()) {
				break;
			}
			
			//find as many occurrences of the same term in the set as possible, beginning with the first
			K key = currentLists.firstKey().getKey();
			NavigableSet<S> slots = new TreeSet<S>(
					new IndexListSlotComparator<S, V>());
			for (Iterator<L> i = currentLists.keySet().iterator(); i.hasNext();) {
				L checkList = i.next();
				if (checkList.getKey().equals(key)) {
					slots.addAll(checkList.getSlots());
					i.remove();
				} else {
					break;
				}
			}
//			Log.log(Level.DEBUG, "Chose first " + n + " from term set");
			
			//now these have been found, write them to the main index file and seeklist
			L combinedList = indexListFactory.createList(key, slots);
			long offset = mainIndexHandler.storeList(combinedList);
			try {
				seekList.storeKeyOffset(key, offset);
			} catch (TermLengthException e) {
				Log.log(Level.ERROR, "Did not store term offset in seeklist because it exceeded max length: " + key);
				e.printStackTrace();
			}
		}
		
		// TODO: rewrite Linklist
	}
	
	/**
	 * Reads the new terms from those files for which currently no term is placed in the
	 * {@link #currentTermMap}.
	 * If the end of one file is reached, it is removed from the {@link #handlers}.
	 * @throws IOException 
	 */
	private void fillCurrentListsSet(SortedMap<L, IndexFileLinearReader<K, L, S, V>> currentLists,
			Set<IndexFileLinearReader<K, L, S, V>> partialIndexHandlers) throws IOException {
		Set<IndexFileLinearReader<K, L, S, V>> missingHandlers = new HashSet<>(partialIndexHandlers);
		missingHandlers.removeAll(currentLists.values());
		for (IndexFileLinearReader<K, L, S, V> handler : missingHandlers) {
			try {
				L list = handler.readNextList();
				currentLists.put(list, handler);
			} catch (EOFException e) {
				partialIndexHandlers.remove(handler);
			}
		}
	}
}
