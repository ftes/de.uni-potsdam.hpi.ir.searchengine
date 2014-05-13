package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.util.ArrayList;

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
	
	private void addToIndex(String characters, Page page, PartialIndex index, boolean stem) {
		Tokenizer tokenizer = new Tokenizer(characters);
		ArrayList<String> tokens = tokenizer.tokenize(stem);
		
		int tokenPosition = 0;
		for (String token : tokens) {
			// FIXME: do something else ...
			//TODO why -2?
			if (token.length() <= SeekList.MAX_TERM_LENGTH-2) { 
				index.addOccurenceForTerm(token, new TermOccurrence(page.getId(), tokenPosition));
			} else {
				Log.log(Level.DEBUG, "Ignored: " + token);
			}
			tokenPosition++;
		}
	}

	@Override
	public void parseToPartialIndexes(String stemmedPartialDir, String unstemmedPartialDir, String pageIndexFile, String pageFile)
			throws XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, FactoryConfigurationError, IOException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(WikipediaExport.class
						.getResourceAsStream(this.getFilename()));

//		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;

		boolean inRevision = false;

		PartialIndex unstemmedIndex = new PartialIndex();
		PartialIndex stemmedIndex = new PartialIndex();
		PageIndexWriter pageWriter = new PageIndexWriter(pageFile);
		PageIndex pageIndex = new PageIndex();

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
					
					addToIndex(characters, page, stemmedIndex, true);
					addToIndex(characters, page, unstemmedIndex, false);
					
					long offset = pageWriter.store(page);
					pageIndex.addOffset(page.getId(), offset);
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
//				} else if (tag.equals("page")) {
//					pages.add(page);
//					// System.out.println(page);
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
				stemmedIndex.store(stemmedPartialDir);
				unstemmedIndex.store(unstemmedPartialDir);
				stemmedIndex = new PartialIndex();
				unstemmedIndex = new PartialIndex();
				Util.runGarbageCollector();
			}
			
			parser.next();
		}
		stemmedIndex.store(stemmedPartialDir);
		unstemmedIndex.store(unstemmedPartialDir);
		pageIndex.exportFile(pageIndexFile);
	}

}
