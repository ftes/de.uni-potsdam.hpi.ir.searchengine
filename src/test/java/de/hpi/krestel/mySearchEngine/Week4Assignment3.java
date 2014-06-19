package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.search.QueryProcessingException;


public class Week4Assignment3 {	
	public static void main(String[] args) throws IOException, QueryProcessingException {
		SearchEngine searchEngine = new SearchEngineFAP();
		
		searchEngine.index("/small.xml");
		searchEngine.loadIndex(null);
		
		System.out.println();
		
		String query = "artikel regisseur";
		System.out.println(searchEngine.search(query, 5, 0));
		System.out.println();
		
		query = "regisseur";
		System.out.println(searchEngine.search(query, 5, 0));
		System.out.println();
		
		query = "deutsch";
		System.out.println(searchEngine.search(query, 5, 0));
		System.out.println();
		
		query = "anschluss";
		System.out.println(searchEngine.search(query, 5, 0));
		System.out.println();
	}
}
