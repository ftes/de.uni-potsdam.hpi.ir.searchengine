package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class Tokenizer {
	private String text;
	
	public Tokenizer(String text){
		this.text = text;
	}
	
	public ArrayList<String> tokenize(){
		SnowballStemmer stemmer = new germanStemmer();
		ArrayList<String> resultTokens = new ArrayList<String>();
		String token = null;
		
		String[] tokens = text.split("[\\s.]");
		
		for (int i = 0; i < tokens.length; i++){
			token = tokens[i].toLowerCase();
			token = token.replaceAll("[^a-zäöüß]", "");

			if (token.length() > 2) { // only index terms with minimum length 2
				stemmer.setCurrent(token);
				stemmer.stem();
				resultTokens.add(stemmer.getCurrent());
			}
		}

		return resultTokens;
	}

}
