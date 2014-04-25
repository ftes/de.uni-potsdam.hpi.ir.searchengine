package de.hpi.krestel.mySearchEngine;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

public class AlexMain {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLStreamException {
		Parser p = new ParserImpl("/small.xml");
		p.parseToPartialIndexes("/partials", "title.dat");
		
//		IndexMerger m = new IndexMergerImpl();
	}

}
