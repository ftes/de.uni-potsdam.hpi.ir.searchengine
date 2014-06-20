package de.hpi.krestel.mySearchEngine;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class SnippetGenerator {
	private final int SNIPPET_SIZE = 21;
	
	private String document;
	private String userQuery;

	public SnippetGenerator(String document, String userQuery) {
		this.document = document;
		this.userQuery = userQuery;
	}
	
	public String generate() {
		SnowballStemmer stemmer = new germanStemmer();
		
		String[] queryTerms = userQuery.toLowerCase().split("[\\s.]");
		for (int i = 0; i < queryTerms.length; i++) {
			stemmer.setCurrent(queryTerms[i]);
			stemmer.stem();
			queryTerms[i] = stemmer.getCurrent();
		}
		
		// before splitting document on whitespaces, remove wikipedia markup
		document = cleanWikiFormat(document); // bold, italic, wiki internal links
		document = removeExternalLinks(document);
		document = removeCurlyBraces(document);
		
		String[] doc = document.split("[\\s]");
		
		boolean foundMatch = false;
		int addAfter = SNIPPET_SIZE / 2;
		
		LinkedList<String> snippet = new LinkedList<String>();
		
		if (userQuery.isEmpty()){
			return "";
		}
		
		for(String word : doc) {
			// after splitting remove all headers and bullet points
			word = word.replaceAll("[*=]", "").trim();
			
			if (word.length() > 0) {
				String wordCopy = word.toLowerCase();
				wordCopy = wordCopy.replaceAll("[^a-zäöüß]", "");
				stemmer.setCurrent(wordCopy);
				stemmer.stem();
				wordCopy = stemmer.getCurrent();
				
				if (foundMatch) {
					// only append the X words following the query term
					
					if (addAfter > 0) {
						snippet.add(word);
						addAfter--;
					} else {
						break; // snippet complete, stop searching document
					}				
					
				} else {
					// keep looking for a query term and buffer X words before potential match
					
					if (snippet.size() > SNIPPET_SIZE / 2) {
						snippet.remove(); // make space for another word
					}
					snippet.add(word); // append word to the end
					
					for (String qTerm : queryTerms) {
						if (wordCopy.equals(qTerm)) {
							// found a match, now construct snippet
							foundMatch = true;
							break;
						}	
					}
				}
			}

		}
		
		if (!foundMatch) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < snippet.size(); i++) {
				String word = snippet.get(i);
				if (i == snippet.size() - 1) {
					builder.append(word);
				} else {
					builder.append(word + " ");
				}
			}
			
			return builder.toString();
		}
	}
	
	// throw away anything that is inside two curly brackets {{...}}
	private String removeCurlyBraces(CharSequence sequence) {
		Pattern patt = Pattern.compile("\\{\\{.*?\\}\\}");
		Matcher m = patt.matcher(sequence);
        StringBuffer sb = new StringBuffer(sequence.length());
        while (m.find()) {
            String text = "";
            // ... possibly process 'text' ...
            m.appendReplacement(sb, Matcher.quoteReplacement(text));
        }
        m.appendTail(sb);
        return sb.toString();
	}
	
	// remove external wikipedia links but keep description text [http://www.abc.net.au/a.htm Artikel über..]
	private String removeExternalLinks(CharSequence sequence) {
		Pattern patt = Pattern.compile("[^\\[]\\[http[^\\s\\[]*\\s+([^\\]]*)\\]");
		Matcher m = patt.matcher(sequence);
        StringBuffer sb = new StringBuffer(sequence.length());
        while (m.find()) {
            String text = m.group(1);
            // ... possibly process 'text' ...
            m.appendReplacement(sb, Matcher.quoteReplacement(text));
        }
        m.appendTail(sb);
        return sb.toString();
	}
	
	private String removeItalic(CharSequence sequence) {
        Pattern patt = Pattern.compile("\\''(.+?)\\''");
        Matcher m = patt.matcher(sequence);
        StringBuffer sb = new StringBuffer(sequence.length());
        while (m.find()) {
            String text = m.group(1);
            // ... possibly process 'text' ...
            m.appendReplacement(sb, Matcher.quoteReplacement(text));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String removeBold(CharSequence sequence) {
        Pattern patt = Pattern.compile("\\'''(.+?)\\'''");
        Matcher m = patt.matcher(sequence);
        StringBuffer sb = new StringBuffer(sequence.length());
        while (m.find()) {
            String text = m.group(1);
            // ... possibly process 'text' ...
            m.appendReplacement(sb, Matcher.quoteReplacement(text));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    // remove wikipedia internal url and keep describing text
    private String removeUrl(CharSequence sequence) {
        Pattern patt = Pattern.compile("\\[\\[([^\\|\\]]*)\\|?([^\\]]*)?\\]\\]");
        Matcher m = patt.matcher(sequence);
        StringBuffer sb = new StringBuffer(sequence.length());
        while (m.find()) {
            String text = m.group(1);
            // ... possibly process 'text' ...
            m.appendReplacement(sb, Matcher.quoteReplacement(text));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String cleanWikiFormat(CharSequence sequence) {
        return removeUrl(removeBold(removeItalic(sequence)));
    }

}
