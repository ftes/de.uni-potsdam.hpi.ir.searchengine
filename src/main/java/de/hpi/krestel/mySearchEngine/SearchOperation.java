package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.List;

public interface SearchOperation<T> {
	/**
	 * 
	 * @param topK How many of the top ranked pages to return.
	 * @return
	 * @throws IOException
	 * @throws TermLengthException
	 * @throws QueryProcessingException
	 */
	List<T> execute(int topK) throws IOException, TermLengthException, QueryProcessingException;
	void print(int indent, int step);
}
