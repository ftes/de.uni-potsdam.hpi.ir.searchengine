package de.hpi.krestel.mySearchEngine;

import de.hpi.krestel.mySearchEngine.util.Log;
import de.hpi.krestel.mySearchEngine.util.Log.Level;


public class Week2Assignment4 {
	public static void main(String[] args) {
		Log.LEVEL = Level.ERROR;
		SearchEngine searchEngine = new SearchEngineFAP();
		
		searchEngine.index("../small.xml");
		searchEngine.loadIndex(null);
		
		System.out.println("Result for query \"Uranisotope\":");
		System.out.println(searchEngine.search("Uranisotope", 1000, 100));
		System.out.println();
		System.out.println("Result for query \"Artikel\":");
		System.out.println(searchEngine.search("Artikel", 1000, 100));
	}
}
