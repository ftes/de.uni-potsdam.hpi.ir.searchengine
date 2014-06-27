package de.hpi.krestel.mySearchEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SchnitzelmitkartoffelsalatArticles {

	public static void main(String[] args) throws IOException {
		String fileSource = "O:/huge.xml";
		// fileSource = "../small.xml";
		BufferedReader br = new BufferedReader(new FileReader(fileSource));
		
        String line;
        int lines = 0;
        int articles = 0;
        String idLine = "";
        String titleLine = "";
        while((line = br.readLine()) != null) {
        	if (line.contains("<title>")) {
        		titleLine = line;
        	}
        	if (line.contains("<id>")) {
        		idLine = line;
        	}
        	if (line.contains("<title>Schnitzelmitkartoffelsalat</title>")) {
        		System.out.println("================0000===================");
        		System.out.println("Schnitzelmitkartoffelsalat is Article nr.:" + articles);
        		System.out.println("-------------------> id: " + idLine);
        		System.out.println("================0000===================");
        	}
        	if (line.contains("[[Schnitzelmitkartoffelsalat")) {
        		System.out.println("=============================");
        		System.out.println(titleLine.trim());
        		System.out.println(line.trim());
        		System.out.println("=============================");
        	}
        	lines++; 
        }
        System.out.println("Total number: " + articles);
        br.close();
	}

}
