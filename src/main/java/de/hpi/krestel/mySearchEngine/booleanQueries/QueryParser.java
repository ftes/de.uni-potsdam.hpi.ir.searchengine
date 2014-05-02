package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hpi.krestel.mySearchEngine.MainIndex;
import de.hpi.krestel.mySearchEngine.QueryProcessingException;

public class QueryParser {
	private static final Map<String, Class<?>> BINARY_OPS;
	static {
		BINARY_OPS = new HashMap<>();
		BINARY_OPS.put("AND", And.class);
		BINARY_OPS.put("OR", Or.class);
		BINARY_OPS.put("BUT NOT", ButNot.class);
	}
	private static final Pattern PATTERN = Pattern.compile("AND|BUT NOT|OR");

	private final String queryString;
	private final MainIndex index;
	
	public QueryParser(MainIndex index, String queryString) {
		this.index = index;
		this.queryString = queryString;
	}
	
	public BooleanSetOperation<Integer> parse() throws IOException, QueryProcessingException {
		return recursiveParse(queryString);
	}
	
	@SuppressWarnings("unchecked")
	private BooleanSetOperation<Integer> recursiveParse(String query) throws IOException, QueryProcessingException {
		Matcher m = PATTERN.matcher(query);
		if (m.find()) {
			String left = query.substring(0, m.start()).trim();
			String middle = query.substring(m.start(), m.end());
			String right = query.substring(m.end()).trim();
			
			BooleanSetOperation<Integer> l = recursiveParse(left);
			BooleanSetOperation<Integer> r = recursiveParse(right);
			try {
				Constructor<?> constr = BINARY_OPS.get(middle).getConstructor(
						BooleanSetOperation.class, BooleanSetOperation.class);
				return (BooleanSetOperation<Integer>) constr.newInstance(l, r);
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else {
			if (query.endsWith("*")) {
				return new PrefixQuery(index, query.substring(0, query.length() - 1));
			} else if (query.contains(" ")) {
				return new PhraseQuery(index, query);
			} else {
				return new TermQuery(index, query);
			}
		}
	}
}
