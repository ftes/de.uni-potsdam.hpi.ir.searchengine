package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.search.QueryProcessingException;


public class Week5Assignment3 {	
	public static void main(String[] args) throws IOException, QueryProcessingException {
		SearchEngine searchEngine = new SearchEngineFAP();
		
		searchEngine.index("/small.xml");
		searchEngine.loadIndex(null);
		
		System.out.println();
		
		String query = "deutsch";
		System.out.println(searchEngine.search(query, 5, 5));
		System.out.println();
		
		query = "artikel";
		System.out.println(searchEngine.search(query, 5, 5));
		System.out.println();
	}
}
