package de.hpi.krestel.mySearchEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CountArticles {

	public static void main(String[] args) throws IOException {
		String fileSource = "O:/huge.xml";
		// fileSource = "../small.xml";
		BufferedReader br = new BufferedReader(new FileReader(fileSource));
		
        String line;
        int lines = 0;
        int articles = 0;
        String idLine = "";
        while((line = br.readLine()) != null) {
        	if (line.contains("<page>")) {
        		articles++;
        		if (articles % 100000 == 0) {
        			System.out.println("Article = " + articles + " at " + lines);
        		}
        	}
        	if (line.contains("<id>")) {
        		idLine = line;
        	}
        	if (line.contains("<title>Schnitzelmitkartoffelsalat</title>")) {
        		System.out.println("Schnitzelmitkartoffelsalat is Article nr.:" + articles);
        		System.out.println("-------------------> id: " + idLine);
        	}
        	if (line.contains("[[Schnitzelmitkartoffelsalat")) {
        		System.out.println("Link in article with id:" + idLine.trim());
        	}
        	lines++; 
        }
        System.out.println("Total number: " + articles);
        br.close();
	}

}
