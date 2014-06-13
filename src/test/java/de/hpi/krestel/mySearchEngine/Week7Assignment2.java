package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;


public class Week7Assignment2 {	
	public static void main(String[] args) throws IOException, QueryProcessingException {
		SearchEngine searchEngine = new SearchEngineFAP();
		
		searchEngine.index("../huge.xml");
		searchEngine.loadIndex(null);
		
		System.out.println();
		
		query("Anschluss Luhmann", searchEngine);
		query("Actinium", searchEngine);
		query("Information Retrieval", searchEngine);
	}
	
	private static void query(String query, SearchEngine searchEngine) {
		ArrayList<String> myRanking = searchEngine.search(query, 50, 5);
		System.out.println(myRanking);
		ArrayList<String> goldRanking = searchEngine.getGoldRanking(query);
		System.out.println(goldRanking);
		double ndcg = searchEngine.computeNdcg(goldRanking, myRanking, 50);
		System.out.println(ndcg);
		System.out.println();
	}
}