package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hpi.krestel.mySearchEngine.MainIndex;
import de.hpi.krestel.mySearchEngine.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.RankedQuery;
import de.hpi.krestel.mySearchEngine.SearchOperation;
import de.hpi.krestel.mySearchEngine.PageIndex;

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
	private final MainIndex stemmedIndex;
	private final MainIndex unstemmedIndex;
	private final PageIndex titleIndex;
	
	public QueryParser(MainIndex stemmedIndex, MainIndex unstemmedIndex, PageIndex titleIndex, String queryString) {
		this.stemmedIndex = stemmedIndex;
		this.unstemmedIndex = unstemmedIndex;
		this.queryString = queryString;
		this.titleIndex = titleIndex;
	}
	
	public SearchOperation<Integer> parse() throws IOException, QueryProcessingException {
		Matcher m = PATTERN.matcher(queryString); 
		if (m.find() || queryString.contains("*") || queryString.contains(PhraseQuery.SYMBOL)) {
			// Boolean Query
			return recursiveParse(queryString);
		} else {
			return new RankedQuery(stemmedIndex, titleIndex, queryString);
		}
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
			if (query.startsWith(PhraseQuery.SYMBOL) && query.endsWith(PhraseQuery.SYMBOL)) {
				return new PhraseQuery(unstemmedIndex, query.substring(1, query.length() - 1));
			} else if (query.endsWith("*")) {
				return new PrefixQuery(stemmedIndex, unstemmedIndex, query.substring(0, query.length() - 1));					
			} else {
				return new TermQuery(stemmedIndex, query);
			}
		}
	}
}
