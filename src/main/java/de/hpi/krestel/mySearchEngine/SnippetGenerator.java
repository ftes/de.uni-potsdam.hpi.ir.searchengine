package de.hpi.krestel.mySearchEngine;

import java.util.LinkedList;

public class SnippetGenerator {
	private final int SNIPPET_SIZE = 21;
	
	private String document;
	private String userQuery;

	public SnippetGenerator(String document, String userQuery) {
		this.document = document;
		this.userQuery = userQuery;
	}
	
	public String generate() {
		String[] queryTerms = userQuery.split("[\\s.]");
		String[] doc = document.split("[\\s]");
		
		boolean foundMatch = false;
		int addAfter = SNIPPET_SIZE / 2;
		
		LinkedList<String> snippet = new LinkedList<String>();
		
		if (userQuery.isEmpty()){
			return "";
		}
		
		for(String word : doc) {
			
			if (foundMatch) {
				// only append the X words following the query term
				
				if (addAfter > 0) {
					snippet.add(word);
					addAfter--;
				} else {
					break; // snippet complete, stop searching document
				}				
				
			} else {
				// keep looking for a query term and buffer X words before potential match
				
				if (snippet.size() > SNIPPET_SIZE / 2) {
					snippet.remove(); // make space for another word
				}
				snippet.add(word); // append word to the end
				
				for (String qTerm : queryTerms) {
					if (word.equals(qTerm)) {
						// found a match, now construct snippet
						foundMatch = true;
						break;
					}	
				}
			}

		}
		
		if (!foundMatch) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < snippet.size(); i++) {
				String word = snippet.get(i);
				if (i == snippet.size() - 1) {
					builder.append(word);
				} else {
					builder.append(word + " ");
				}
			}
			
			return builder.toString();
		}
	}
}
