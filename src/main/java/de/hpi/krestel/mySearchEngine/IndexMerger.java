package de.hpi.krestel.mySearchEngine;


/**
 * Merge several partial index files into one large index files.
 * 
 * Because each of the partial index files is about as large as the allocated main memory,
 * no more than one partial index can be loaded into main memory at a single point in time.
 * Instead, the merging process steps through each partial index line by line in parallel.
 * 
 * As the partial indexes are alphabetically sorted, words occuring in several partial
 * indexes are found at the same time, can be combined, and then written directly to the
 * combined index file.
 * @author fredrik
 *
 */
public class IndexMerger {
	/**
	 * Merge the partial index files that are located in the {@code partialIndexDirectory} into
	 * one file located at {@code mergedIndexPath}.
	 */
	public void merge(String partialIndexDirectory, String mergedIndexPath) {
		
	}
}
