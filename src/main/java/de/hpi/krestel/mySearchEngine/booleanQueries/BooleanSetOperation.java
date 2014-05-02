package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.TermLengthException;

public interface BooleanSetOperation<T> {
	Set<T> execute() throws IOException, TermLengthException, QueryProcessingException;
	void print(int indent, int step);
}
