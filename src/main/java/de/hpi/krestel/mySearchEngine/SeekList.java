package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

/**
 * The seeklist is a second-level index structure on top of the main {@link PartialIndex}.
 * As the entire index is too large for main memory, the locations where the information
 * for each token can be found is stored as an offset in the seeklist.<p>
 * 
 * As the seeklist itself is also most likely too large for main memory, this class provides
 * a wrapper for the seeklist file on disk.<p>
 * 
 * If the tokens are restricted to a maximum length however, the data entries to this seeklist
 * file can be of equal size, allowing for fast searching, e.g. via binary search.
 * Also, the implementing class may dynamically cache token offsets as far as main memory permits.
 * 
 * <h3>File layout</h3>
 * The file is divided into equal-length slots. Each slots contains the term encoded as 8-bit ASCII,
 * and the byte offset of the position list for this term within the main index.
 * The maximum term length is TODO XXX characters.
 * The number of bytes needed to encode the byte offset within the index file is TODO XXX byte
 * (probably 8 byte for long values).
 * 
 * @author fredrik
 */
public interface SeekList {
	/**
	 * The maximum length of a term, which is enforced to ensure equal size slots in
	 * the seeklist file, so that binary search can be performed.
	 */
	int MAX_TERM_LENGTH = 20;
	int BYTES_PER_CHAR = 2;
	int BYTES_PER_TERM = MAX_TERM_LENGTH * BYTES_PER_CHAR;
	int BYTES_PER_OFFSET = 8;
	
	/**
	 * The length of one slot (aka one term+offset entry) in bytes.
	 */
	int SLOT_SIZE = BYTES_PER_TERM + BYTES_PER_OFFSET;

	/**
	 * Returns the offset of for the term data (position list of occurences) within the main index.
	 * 
	 * @param term The term (e.g. tokenized form of a word)
	 * @return The byte offset within the index file of the line pertaining to this {@code term}.
	 */
	long getTermOffsetInIndex(String term) throws IOException, TermLengthException, TermNotFoundException;

	/**
	 * Adds the term data to the seeklist by appending it to the file.
	 * Take care to maintain the ordering of terms, as the binary search will otherwise break.
	 * @param term
	 * @param offset The byte offset of the data for this {@code term} within the index file
	 */
	void storeTermOffset(String term, long offset) throws IOException, TermLengthException;

	/**
	 * Close the file backing the seeklist and clean up.
	 */
	void close() throws IOException;
}