package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.krestel.mySearchEngine.Util.Pair;
import de.hpi.krestel.mySearchEngine.Util.PairBComparator;

public class PseudoRelevanceFeedback implements SearchOperation<Integer> {
	/**
	 * As suggested by wikipedia.
	 */
	public static final int NUM_QUERY_TERMS_TO_ADD = 20;
	
	private final String queryString;
	private final MainIndex stemmedIndex;
	private final PageIndex pageIndex;
	
	/**
	 * How many of the top ranked pages from the initial search to use for pseudo relevance feedback.
	 * If {@code 0}, then no pseudo relevance feedback is performed.
	 */
	private final int prf;
	
	public PseudoRelevanceFeedback(MainIndex stemmedIndex, PageIndex pageIndex, String queryString, int prf) {
		this.stemmedIndex = stemmedIndex;
		this.queryString = queryString;
		this.pageIndex = pageIndex;
		this.prf = prf;
	}
	
	@Override
	public List<Integer> execute(int topK) throws IOException,
			TermLengthException, QueryProcessingException {
		//only use the top prf documents for expanding the query
		List<Integer> docIds = new RankedQuery(stemmedIndex, pageIndex, queryString).execute(prf);
		
		Map<String, Integer> termFrequencies = new HashMap<>();
		for (int docId : docIds) {
			String text = pageIndex.getText(docId);
			for (String token : new Tokenizer(text).tokenize(true)) {
				Util.increment(termFrequencies, token);
			}
		}
		
		ArrayList<Pair<String, Double>> termsWithTfIdf = new ArrayList<>();
		for (String token : termFrequencies.keySet()) {
			try {
				int docFrequency = stemmedIndex.getTerm(token).getDocumentIds().size();
				double idf = Math.log(pageIndex.getSize() / docFrequency);
				double tfIdf = termFrequencies.get(token) * idf;
				termsWithTfIdf.add(new Pair<String, Double>(token, tfIdf));
			} catch (TermNotFoundException e) {
				// can't happen, because this term originates from one of the docs in our index
				e.printStackTrace();
			}
		}
		
		Collections.sort(termsWithTfIdf, Collections.reverseOrder(
				new PairBComparator<Double, Pair<String, Double>>()));

		ArrayList<String> newQuery = new Tokenizer(queryString).tokenize(true);
		for (int i=0; i<NUM_QUERY_TERMS_TO_ADD; i++) {
			newQuery.add(termsWithTfIdf.get(i).a);
		}
		
		return new RankedQuery(stemmedIndex, pageIndex, newQuery).execute(topK);
	}

	@Override
	public void print(int indent, int step) {
		// TODO Auto-generated method stub
	}
}
