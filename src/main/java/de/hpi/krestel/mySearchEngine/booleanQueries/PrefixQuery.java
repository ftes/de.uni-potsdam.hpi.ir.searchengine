package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public class PrefixQuery implements BooleanSetOperation<Integer> {
	private final TermMainIndexImpl stemmedIndex;
	private final TermMainIndexImpl unstemmedIndex;
	private final String prefix;
	private final Set<String> terms;
	
	public PrefixQuery(TermMainIndexImpl stemmedIndex, TermMainIndexImpl unstemmedIndex, String prefix) throws IOException {
		this.stemmedIndex = stemmedIndex;
		this.unstemmedIndex = unstemmedIndex;
		this.prefix = prefix.toLowerCase();
		terms = stemmedIndex.getSeekList().getKeysBeginningWith(this.prefix);
		terms.addAll(unstemmedIndex.getSeekList().getKeysBeginningWith(this.prefix));
	}

	@Override
	public List<Integer> execute(int topK) throws IOException, TermLengthException {
		Set<Integer> result = new HashSet<>();
		for (String term : terms) {
			try {
				result.addAll(stemmedIndex.getList(term).getDocumentIds());
				result.addAll(unstemmedIndex.getList(term).getDocumentIds());
			} catch (KeyNotFoundException e) {} //shouldn't happen
		}
		return new ArrayList<Integer>(result);
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.print(indentSpace + prefix + "* { ");
		for (String term : terms) {
			System.out.print(term + " ");
		}
		System.out.println("}");
	}

}
