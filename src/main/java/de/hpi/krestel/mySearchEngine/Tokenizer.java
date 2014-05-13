package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class Tokenizer {
	private String text;
	
	public Tokenizer(String text){
		this.text = text;
	}
	
	public ArrayList<String> tokenize(boolean stem){
		SnowballStemmer stemmer = new germanStemmer();
		ArrayList<String> resultTokens = new ArrayList<String>();
		
		String[] tokens = text.split("[\\s.]");
		
		for (String token : tokens){
			token = token.toLowerCase();
			token = token.replaceAll("[^a-zäöüß]", "");

			if (token.length() > SeekList.MAX_TERM_LENGTH) {
				continue;
			}
			
			if (stem) {
				if (token.length() > 2) { // only index terms with minimum length 2
					stemmer.setCurrent(token);
					stemmer.stem();
					resultTokens.add(stemmer.getCurrent());
				}
			} else {
				resultTokens.add(token);
			}
		}

		return resultTokens;
	}

}
