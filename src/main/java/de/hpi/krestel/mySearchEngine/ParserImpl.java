package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class ParserImpl extends Parser {

	public ParserImpl(String filename) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(filename);
	}

	@Override
	public void parseToPartialIndexes(String indexDirectory) throws XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, FactoryConfigurationError, IOException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(WikipediaExport.class
						.getResourceAsStream(this.getFilename()));

		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;

		boolean inRevision = false;

		// initialize german stemmer
		SnowballStemmer stemmer = new germanStemmer();
		
		PartialIndex index = new PartialIndex();

		// read from stream
		while (parser.hasNext()) {

			switch (parser.getEventType()) {
			case XMLStreamConstants.START_DOCUMENT:
				break;

			case XMLStreamConstants.END_DOCUMENT:
				parser.close();
				break;

			case XMLStreamConstants.START_ELEMENT:
				tag = parser.getLocalName();
				if (tag.equals("page")) {
					page = new Page();
				} else if (tag.equals("revision")) {
					inRevision = true;
				} else if (tag.equals("text")) {
					
					// parses whole text and checks for end event
					characters = parser.getElementText();
					
					page.setText(characters);
					
					int tokenPosition = 0;

					// filter text
					String[] tokens = characters.split("[\\s.]");

					for (String token : tokens) {

						token = token.toLowerCase();
						token = token.replaceAll("[^a-zäöüß]", "");

						if (token.length() > 2) { // only index terms with minimum length 2
							stemmer.setCurrent(token);
							stemmer.stem();
							token = stemmer.getCurrent();

							index.addOccurenceForTerm(token, new TermOccurrence(page.getId(), tokenPosition));
							System.out.println("id: " + page.getId()
									+ ", location: " + tokenPosition + ", "
									+ token);
							
							tokenPosition++;
						}
					}
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				characters = parser.getText();
				break;

			case XMLStreamConstants.END_ELEMENT:
				tag = parser.getLocalName();
				if (!inRevision && tag.equals("id")) {
					page.setId(Integer.parseInt(characters));
				} else if (!inRevision && tag.equals("title")) {
					page.setTitle(characters);
				} else if (tag.equals("page")) {
					pages.add(page);
					// System.out.println(page);
				} else if (tag.equals("revision")) {
					inRevision = false;
				}
				break;

			default:
				break;
			}

			if (Util.isMainMemoryFull()){
				System.out.println("MEMORY FULL!");
				// write to disk and remove references
				index.store(indexDirectory);
				index = new PartialIndex();
			}
			
			parser.next();
		}
		index.store(indexDirectory);
	}

}
