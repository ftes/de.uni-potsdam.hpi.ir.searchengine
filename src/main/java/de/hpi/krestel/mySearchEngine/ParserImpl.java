package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import de.hpi.krestel.mySearchEngine.Log.Level;

public class ParserImpl extends Parser {

	public ParserImpl(String filename) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(filename);
	}

	@Override
	public void parseToPartialIndexes(String indexDirectory, String titleIndexPath)
			throws XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, FactoryConfigurationError, IOException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(WikipediaExport.class
						.getResourceAsStream(this.getFilename()));

		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;

		boolean inRevision = false;

		Tokenizer tokenizer = null;

		PartialIndex index = new PartialIndex();
		TitleIndex titleIndex = new TitleIndex();

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
					
					tokenizer = new Tokenizer(characters);
					ArrayList<String> tokens = tokenizer.tokenize();
					
					for (String token : tokens) {
					
						// FIXME: do something else ...
						if (token.length() < SeekList.MAX_TERM_LENGTH-2) { 
							index.addOccurenceForTerm(token, new TermOccurrence(page.getId(), tokenPosition));
						} else {
							Log.log(Level.DEBUG, "Ignored: " + token);
						}
//						System.out.println("id: " + page.getId()
//								+ ", location: " + tokenPosition + ", "
//								+ token);
						
						tokenPosition++;
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
					titleIndex.addTitle(page.getId(), page.getTitle());
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
		titleIndex.exportFile(titleIndexPath);
	}

}
