package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RankedQuery implements SearchOperation<Integer> {
	// for MP25
	final float r_i = 0;
	final float R = 0;
	final float k_1 = 1.2f;
	final float k_2 = 500;
	final float K = k_1; // no normalization
	
	final MainIndex index;
	final PageIndex titleIndex;
	final String query;
	
	public RankedQuery(MainIndex stemmedIndex, PageIndex titleIndex, String queryString) {
		this.index = stemmedIndex;
		this.query = queryString;
		this.titleIndex = titleIndex;
	}
	
	private static class ScoreAndDocId implements Comparable<ScoreAndDocId> {
		final float score;
		final int docId;
		
		ScoreAndDocId(float score, int docId) {
			this.score = score;
			this.docId = docId;
		}
		
		@Override
		public int compareTo(ScoreAndDocId o) {
			int floatComp = Float.compare(score, o.score);
			return floatComp == 0 ? Integer.compare(docId, o.docId) : floatComp;
		}
	}

	@Override
	public List<Integer> execute(int topK) throws IOException, TermLengthException,
			QueryProcessingException {
		ArrayList<String> tokens = new Tokenizer(query).tokenize(true);
		
		Map<String, Term> terms = new HashMap<>();
		for (String token : tokens) {
			if (! terms.containsKey(token) ) {
				try {
					terms.put(token, index.getTerm(token) );
				} catch (TermNotFoundException e) {
					// ignore that
				}
			}
		}
		Map<String, Float> queryFactor = calculateQueryFactor(tokens);
		Map<String, Float> firstFactor = calculateFirstFactor(terms);
		
		Set<Integer> documents = new HashSet<>();
		for (Term term : terms.values()) {
			documents.addAll(term.getDocumentIds());
		}
		
		Set<ScoreAndDocId> results = new TreeSet<>();
		for (Integer docId : documents) {
			float score = calculateScore(docId, firstFactor, queryFactor, terms);
			if (results.size() < topK) {
				results.add(new ScoreAndDocId(score, docId));
			} else {
				ScoreAndDocId lowestScore = results.iterator().next();
				if (score > lowestScore.score) {
					results.remove(lowestScore);
					results.add(new ScoreAndDocId(score, docId));
				}
			}
		}
		
		List<Integer> reverseList = new ArrayList<>();
		for (ScoreAndDocId s : results) {
			reverseList.add(s.docId);
		}
		Collections.reverse(reverseList);
		return reverseList;
	}

	private float calculateScore(Integer docId, Map<String, Float> firstFactor,
			Map<String, Float> queryFactor, Map<String, Term> terms) {
		double sum = 0;
		for (String term : firstFactor.keySet()) {
			Term t = terms.get(term);
			if (t == null) {
				sum += 0;
				continue;
			}
			int f_i = t.getOccurenceCountFor(docId);
			double termFactor = (k_1 + 1) * f_i / (K + f_i);
			
			sum += firstFactor.get(term) * termFactor * queryFactor.get(term);
		}
		return (float) sum;
	}

	private Map<String, Float> calculateFirstFactor(Map<String, Term> terms) {
		int N = titleIndex.getSize();
		Map<String, Float> factors = new HashMap<>();
		for (String term : terms.keySet()) {
			int n_i = terms.get(term).getDocumentIds().size();
			double numerator = (r_i + 0.5) / (R - r_i + 0.5);
			double denominator = (n_i - r_i + 0.5) / (N - n_i - R + r_i + 0.5);
			factors.put(term, (float) Math.log(numerator / denominator));
		}
		// TODO Auto-generated method stub
		return factors;
	}

	private Map<String, Float> calculateQueryFactor(ArrayList<String> tokens) {
		Map<String, Integer> count = new HashMap<>();
		
		for (String token : tokens) {
			if (count.containsKey(token)) {
				count.put(token, count.get(token) + 1);
			} else {
				count.put(token, 1);
			}
		}
		
		Map<String, Float> factors = new HashMap<>();
		for (String token : count.keySet()) {
			int qf_i = count.get(token);
			factors.put(token, (k_2 + 1) * qf_i / (k_2 + qf_i));
		}
		
		return factors;
	}

	@Override
	public void print(int indent, int step) {
		// TODO Auto-generated method stub
		
	}

}
