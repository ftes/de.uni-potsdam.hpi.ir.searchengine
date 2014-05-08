package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.hpi.krestel.mySearchEngine.MainIndex;
import de.hpi.krestel.mySearchEngine.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.TermLengthException;
import de.hpi.krestel.mySearchEngine.TermNotFoundException;
import de.hpi.krestel.mySearchEngine.Tokenizer;

public class TermQuery implements BooleanSetOperation<Integer> {
	private final MainIndex index;
	private final String token;
	
	public TermQuery(MainIndex index, String term) throws QueryProcessingException {
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
			return new ArrayList<Integer>(index.getTerm(token).getDocumentIds());
		} catch (TermNotFoundException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.println(indentSpace + token);
	}

}
