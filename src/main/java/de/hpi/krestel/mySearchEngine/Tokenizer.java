package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class Tokenizer {
	private String text;
	
	public Tokenizer(String text){
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public String getCleanText() {
		String text = getText();
		// remove html escape sequencies
		text = text.replaceAll("&\\w*?;", " ");
		//remove html tags
		text = text.replaceAll("(<.*?>)", ""); 
		// remove non word characters
		text = text.replaceAll("[^a-zA-ZäöüÄÖÜß0-9.,!:\\(\\)-]+", " "); 
		// remove unnecessary whitespace
		text = text.replaceAll("\\s+", " "); 
		return text;
	}
	
	public ArrayList<String> tokenize(boolean stem){
		SnowballStemmer stemmer = new germanStemmer();
		ArrayList<String> resultTokens = new ArrayList<String>();
		
		String text = getCleanText();
		String[] tokens = text.split("[\\s.]");
		
		for (String token : tokens){
			token = token.toLowerCase();

			if (token.length() > SeekList.MAX_TERM_LENGTH) {
				continue;
			}
			
			if (stem && token.length() > 2) { // only index terms with minimum length 2
				stemmer.setCurrent(token);
				stemmer.stem();
				resultTokens.add(stemmer.getCurrent());
			} else if (token.length() > 0) {
				resultTokens.add(token);
			}
		}

		return resultTokens;
	}

}
