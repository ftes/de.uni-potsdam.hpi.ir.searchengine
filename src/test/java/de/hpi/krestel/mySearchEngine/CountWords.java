package de.hpi.krestel.mySearchEngine;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CountWords {
	private static void add(Map<String, Integer> map, String string) {
		if (string == null || string.isEmpty()) {
			return;
		}
		
		if (!map.containsKey(string)) {
			map.put(string, 0);
		}
		map.put(string, map.get(string) + 1);
	}
	
	private static String clean(String word) {
		word = word.toLowerCase();
		word = word.replaceAll("[^a-zäöüß]", "");
		if (word.isEmpty()) {
			return null;
		}
		return word;
	}

	public static void main(String[] args) throws IOException {
		String fileName = "/krieg_und_tod_en.txt";
		String output = "krieg_und_tod_en.csv";
		InputStream s = CountWords.class.getResourceAsStream(fileName);

		BufferedReader r = new BufferedReader(new InputStreamReader(s));

		Map<String, Integer> count = new HashMap<String, Integer>();

		String line = r.readLine();
		while (line != null) {
			String[] words = line.split("\\s");
			for (String word : words) {
				add(count, clean(word));
			}
			line = r.readLine();
		}
		s.close();
		
		FileWriter out = new FileWriter(output);
		for (String word : count.keySet()) {
			line = String.format("%s,%d\n", word, count.get(word));
			out.write(line);
			System.out.print(line);
		}
		out.close();
	}
}
