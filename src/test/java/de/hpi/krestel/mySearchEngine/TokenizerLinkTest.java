package de.hpi.krestel.mySearchEngine;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.parse.Tokenizer;
import de.hpi.krestel.mySearchEngine.util.Util;

public class TokenizerLinkTest {
	private static final String text = "'''Alan Smithee''' steht als [[Pseudonym]] für einen fiktiven Regisseur, der Filme verantwortet, bei denen der eigentliche [[Regisseur]] seinen Namen nicht mit dem Werk in Verbindung gebracht haben möchte. Von 1968 bis 2000 wurde es von der [[Directors Guild of America]] (DGA) für solche Situationen empfohlen, seither ist es '''Thomas Lee'''.&lt;ref&gt;[[Los Angeles Times|latimes]].com: [http://articles.latimes.com/2000/jan/15/entertainment/ca-54271 ''Name of Director Smithee Isn't What It Used to Be''], zuletzt geprüft am 2. April 2011&lt;/ref&gt; ''Alan Smithee'' ist jedoch weiterhin in Gebrauch.:";
	
	@Test
	public void extractLinks() {
		for (Util.Pair<String, String> link : new Tokenizer(text).getLinks()) {
			System.out.println(link);
		}
	}
}
