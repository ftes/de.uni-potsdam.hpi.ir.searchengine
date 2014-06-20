package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.index.IndexList;
import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

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
public interface IndexMerger<K extends Comparable<K>, L extends IndexList<K, S, V>,
S extends IndexListSlot<V>, V extends Comparable<V>> {

	/**
	 * Merge the partial index files that are located in the {@code partialIndexDirectory} into
	 * one file located at {@code mergedIndexPath}.
	 * The seeklist is written to the {@code seekListPath}.
	 * 
	 * @throws IOException 
	 */
	void merge(String seekListPath, String partialIndexDirectory, String mergedIndexPath) throws IOException;
	
	/**
	 * Exposed for testing purposes.
	 * @see IndexMerger#merge(String, String, String)
	 */
	void merge(SeekList<K> seekList, Set<IndexFileLinearReader<K, L, S, V>> partialIndexHandlers,
			IndexFileLinearWriter<K, L, S, V> mainIndexHandler)
			throws IOException;

}