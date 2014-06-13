package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * TODO compression / encoding (delta, ...gamma) *
 */
public class IndexFileRandomReaderImpl implements IndexFileRandomReader {
	/**
	 * The name of the file that is used for storage
	 */
	private String filename;
	RandomAccessFile file;
	
	public IndexFileRandomReaderImpl(String filename) throws IOException {
		this.filename = filename;
		
		// open the file
		file = new RandomAccessFile(filename, "rw");
	}
	
	@Override
	public void close() throws IOException {
		file.close();
	}
	
	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public Term readTerm(long fileOffset) throws IOException {
		file.seek(fileOffset);
		return IndexFileLinearReaderImpl.readTerm(file);
	}
}
