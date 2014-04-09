package de.hpi.krestel.mySearchEngine;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class WikipediaExport {
	public static void main(String[] args) throws XMLStreamException,
			FileNotFoundException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
				.createXMLStreamReader(WikipediaExport.class
						.getResourceAsStream("/small.xml"));
		
		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		String characters = null;
		String tag = null;
		
		boolean inRevision = false;
		

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
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				characters = parser.getText();
				break;

			case XMLStreamConstants.END_ELEMENT:
				tag = parser.getLocalName();
				if (! inRevision && tag.equals("id")) {
					page.setId(Integer.parseInt(characters));
				} else if (! inRevision && tag.equals("title")) {
					page.setTitle(characters);
				} else if (tag.equals("page")) {
					pages.add(page);
					System.out.println(page);
				} else if (tag.equals("revision")) {
					inRevision = false;
				}
				break;

			default:
				break;
			}
			parser.next();
		}
	}
}
