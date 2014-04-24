package de.hpi.krestel.mySearchEngine;

/**
 * Handles file access for the index files
 * 
 * @author Alexander
 *
 */
public class IndexFileHandler {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	public IndexFileHandler(String filename) {
		this.filename = filename;
		// TODO
	}
	public String getFilename() {
		return filename;
	}
	/**
	 * Reads the first term in the file
	 * @return
	 */
	public Term readTerm() {
		return readTerm(0);
	}
	/**
	 * Reads the term in the file at a certain offset
	 * @param fileOffset
	 * @return
	 */
	public Term readTerm(long fileOffset) {
		return new Term("unimplemented");
	}
	/**
	 * Reads the next term
	 * @return
	 */
	public Term readNextTerm() {
		return readTerm(0);
	}
	/**
	 * Stores a term to the end of the file.
	 * @param term
	 * @return
	 */
	public long storeTerm(Term term) {
		return 0;
	}
}
