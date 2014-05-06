package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.booleanQueries.PhraseQuery;


public class Week3Assignment3 {	
	public static void main(String[] args) throws IOException, QueryProcessingException {
		SearchEngine searchEngine = new SearchEngineFAP();
		
		searchEngine.index("/small.xml");
		searchEngine.loadIndex(null);
		
		System.out.println();
		
		String query = "Artikel AND Smithee";
		System.out.println(searchEngine.search(query, 0, 0));
		System.out.println();
		
		query = "Artikel OR Reaktion";
		System.out.println(searchEngine.search(query, 0, 0));
		System.out.println();
		
		query = "Art* BUT NOT Artikel";
		System.out.println(searchEngine.search(query, 0, 0));
		System.out.println();
		
		query = PhraseQuery.SYMBOL + "Filmfestspiele in Venedig" + PhraseQuery.SYMBOL;
		System.out.println(searchEngine.search(query, 0, 0));
		System.out.println();
	}
}
