package de.hpi.krestel.mySearchEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CountArticles {

	public static void main(String[] args) throws IOException {
		String fileSource = "C:\\Users\\Alexander\\Desktop\\000-CLEARED\\huge.xml";
		// fileSource = "../small.xml";
		BufferedReader br = new BufferedReader(new FileReader(fileSource));
		
        String line;
        int lines = 0;
        int articles = 0;
        while((line = br.readLine()) != null) {
        	if (line.contains("<page>")) {
        		articles++;
        		if (articles % 100000 == 0) {
        			System.out.println("Article = " + articles + " at " + lines);
        		}
        	}
        	lines++; 
        }
        System.out.println("Total number: " + articles);
        br.close();
	}

}
