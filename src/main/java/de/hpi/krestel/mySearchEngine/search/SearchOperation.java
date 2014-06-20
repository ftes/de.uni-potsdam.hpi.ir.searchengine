package de.hpi.krestel.mySearchEngine.search;

import java.io.IOException;
import java.util.List;

public interface SearchOperation<T> {
	/**
	 * 
	 * @param topK How many of the top ranked pages to return.
	 * @return
	 * @throws IOException
	 * @throws WordLengthException
	 * @throws QueryProcessingException
	 */
	List<T> execute(int topK) throws IOException, WordLengthException, QueryProcessingException;
	void print(int indent, int step);
}
