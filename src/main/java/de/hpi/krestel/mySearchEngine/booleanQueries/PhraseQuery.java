package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.MainIndex;
import de.hpi.krestel.mySearchEngine.TermLengthException;

public class PhraseQuery implements BooleanSetOperation<Integer> {
	private final MainIndex index;
	private final String phrase;
	
	public PhraseQuery(MainIndex index, String phrase) throws IOException {
		this.index = index;
		this.phrase = phrase.toLowerCase();
	}

	@Override
	public Set<Integer> execute() throws IOException, TermLengthException {
		return null;
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.print(indentSpace + "\"" + phrase + "\"");
	}

}
