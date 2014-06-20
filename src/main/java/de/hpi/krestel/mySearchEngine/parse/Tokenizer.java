package de.hpi.krestel.mySearchEngine.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

import de.hpi.krestel.mySearchEngine.index.io.SeekList;
import de.hpi.krestel.mySearchEngine.util.Util;

public class Tokenizer {
	private String text;
	
	public Tokenizer(String text){
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	// remove html escape sequencies
	private static final Pattern htmlToSpace = Pattern.compile("&\\w{2,6}?;");
	//remove html tags
	//private static final Pattern htmlToNone = Pattern.compile("(<.*?>)");
	// remove non word characters
	private static final Pattern nonWordToSpace = Pattern.compile("[^a-zA-ZäöüÄÖÜß0-9.,!:\\(\\)-]"); 
	// remove unnecessary whitespace
	//private static final Pattern whitespaceToSpace = Pattern.compile("\\s+"); 
	
	public String getCleanText() {
		htmlToSpace.matcher(text).replaceAll(" ");
		//htmlToNone.matcher(text).replaceAll("");
		nonWordToSpace.matcher(text).replaceAll(" ");
		//whitespaceToSpace.matcher(text).replaceAll(" ");
		return text;
	}
	
	private static final Pattern splitOnWhitespace = Pattern.compile("[\\s+.]");
	private static final Pattern removeNonAToZOrUmlauts = Pattern.compile("[^a-zäöüß]");
	
	public ArrayList<String> tokenize(boolean stem){
		SnowballStemmer stemmer = new germanStemmer();
		ArrayList<String> resultTokens = new ArrayList<String>();
		
		String text = getCleanText();
		String[] tokens = splitOnWhitespace.split(text);
		
		for (String token : tokens){
			token = token.toLowerCase();
			token = removeNonAToZOrUmlauts.matcher(token).replaceAll("");

			if (token.length() > SeekList.MAX_WORD_LENGTH) {
				continue;
			}
			
			if (stem) { // only index terms with minimum length 2
				if (token.length() > 2) {
					stemmer.setCurrent(token);
					stemmer.stem();
					resultTokens.add(stemmer.getCurrent());
				}
			} else if (token.length() > 0) {
				resultTokens.add(token);
			}
		}

		return resultTokens;
	}

	// remove html escape sequencies
	private static final Pattern links = Pattern.compile("\\[\\[([^\\]\\|]+)\\|?([^\\]]*)\\]\\]");
	public List<Util.Pair<String, String>> getLinks() {
		List<Util.Pair<String, String>> result = new ArrayList<>();
		Matcher matcher = links.matcher(text);
		while (matcher.find()) {
			String link = matcher.group(1);
			String anchorText = matcher.group(2);
			result.add(new Util.Pair<>(link, anchorText));
		}
		return result;
	}
}
