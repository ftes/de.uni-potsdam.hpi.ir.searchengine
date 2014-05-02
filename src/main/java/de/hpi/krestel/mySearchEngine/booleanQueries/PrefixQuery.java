package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.MainIndex;
import de.hpi.krestel.mySearchEngine.TermLengthException;
import de.hpi.krestel.mySearchEngine.TermNotFoundException;

public class PrefixQuery implements BooleanSetOperation<Integer> {
	private final MainIndex index;
	private final String prefix;
	private final Set<String> terms;
	
	public PrefixQuery(MainIndex index, String prefix) throws IOException {
		this.index = index;
		this.prefix = prefix.toLowerCase();
		terms = index.getSeekList().getTermsBeginningWith(this.prefix);
	}

	@Override
	public Set<Integer> execute() throws IOException, TermLengthException {
		Set<Integer> result = new HashSet<>();
		for (String term : terms) {
			try {
				result.addAll(index.getTerm(term).getDocumentIds());
			} catch (TermNotFoundException e) {} //shouldn't happen
		}
		return result;
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.print(indentSpace + prefix + "* ( ");
		for (String term : terms) {
			System.out.print(term + " ");
		}
		System.out.println(")");
	}

}
