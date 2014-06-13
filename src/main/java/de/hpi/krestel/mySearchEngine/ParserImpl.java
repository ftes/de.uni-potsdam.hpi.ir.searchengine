package de.hpi.krestel.mySearchEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ParserImpl extends Parser {
	public ParserImpl(InputStream in) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(in);
	}
	
	private void addToIndex(String characters, Page page, PartialIndex index, boolean stem) {
		Tokenizer tokenizer = new Tokenizer(characters);
		ArrayList<String> tokens = tokenizer.tokenize(stem);
		
		int tokenPosition = 0;
		for (String token : tokens) {
			index.addOccurenceForTerm(token, new TermOccurrence(page.getId(), tokenPosition));
			tokenPosition++;
		}
	}

	@Override
	public void parseToPartialIndexes(String stemmedPartialDir, String unstemmedPartialDir, String pageIndexFile, String pageFile)
			throws XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, FactoryConfigurationError, IOException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(in);

//		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;

		boolean inRevision = false;

		int n = 0;
		PartialIndex unstemmedIndex = new PartialIndex(n);
		PartialIndex stemmedIndex = new PartialIndex(n);
		PageIndexWriter pageWriter = new PageIndexWriter(pageFile);
		PageIndex pageIndex = new PageIndex();

		long numArticles = 0;
		long printInterval = 10;
		Date start = new Date();
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
					numArticles++;
					if (numArticles % printInterval == 0) {
						long elapsed = new Date().getTime() - start.getTime();
						System.out.printf("%d articles, %d seconds\n", numArticles, (int) elapsed / 1000);
					}
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
				System.out.println("Writing " + stemmedPartialDir + "/" + stemmedIndex.getFilename());
				System.out.println("Writing " + unstemmedPartialDir + "/" + unstemmedIndex.getFilename());
				// write to disk and remove references
				stemmedIndex.store(stemmedPartialDir);
				unstemmedIndex.store(unstemmedPartialDir);
				stemmedIndex = new PartialIndex(++n);
				unstemmedIndex = new PartialIndex(n);
				Util.runGarbageCollector();
			}
			
			parser.next();
		}
		stemmedIndex.store(stemmedPartialDir);
		unstemmedIndex.store(unstemmedPartialDir);
		pageIndex.exportFile(pageIndexFile);
		pageWriter.close();
	}

}
