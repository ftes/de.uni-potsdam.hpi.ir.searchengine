package de.hpi.krestel.mySearchEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ReadFile {

	public static void main(String[] args) throws IOException {
		String fileSource = "../dewiki-20140216-pages-articles-multistream.xml";
		String fileTarget = "../small.xml";
		BufferedReader br = new BufferedReader(new FileReader(fileSource));
		PrintWriter writer = new PrintWriter(fileTarget, "UTF-8");
		
        String line;
        
        int maxLines = 10000;
        int i = 0;
        while((line = br.readLine()) != null) {
        	if (i > maxLines) break;
        	writer.println(line);
        	i++; 
        }
        br.close();
        writer.close();
	}
}
