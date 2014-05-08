package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.List;

public interface SearchOperation<T> {
	List<T> execute(int topK) throws IOException, TermLengthException, QueryProcessingException;
	void print(int indent, int step);
}
