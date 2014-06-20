package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.hpi.krestel.mySearchEngine.parse.Tokenizer;
import de.hpi.krestel.mySearchEngine.search.SnippetGenerator;

public class AlexMain {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLStreamException {
//		Parser p = new ParserImpl("/small.xml");
//		p.parseToPartialIndexes("/partials", "title.dat");
		
//		IndexMerger m = new IndexMergerImpl();
		
		String document = "this is a lame document that is very short. Another sentence should make this whole thing long enough to actually show some result!";
		String query = "";
		SnippetGenerator gen = new SnippetGenerator(document, query);
		System.out.println(gen.generate());
		
		Tokenizer tk = new Tokenizer("entnommen.");
		System.out.println(tk.tokenize(true));
	}

}
