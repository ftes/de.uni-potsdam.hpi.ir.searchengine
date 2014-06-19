package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.parse.Tokenizer;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public class TermQuery implements BooleanSetOperation<Integer> {
	private final TermMainIndexImpl index;
	private final String token;
	
	public TermQuery(TermMainIndexImpl index, String term) throws QueryProcessingException {
		this.index = index;
		ArrayList<String> tokens = new Tokenizer(term).tokenize(true);
		if (tokens.size() != 1) {
			throw new QueryProcessingException(
					"Can only handle exactly one token as part of a boolean op, but got " + term);
		}
		token = tokens.get(0);
	}

	@Override
	public List<Integer> execute(int topK) throws IOException, TermLengthException {
		try {
			return new ArrayList<Integer>(index.getList(token).getDocumentIds());
		} catch (KeyNotFoundException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.println(indentSpace + token);
	}

}
