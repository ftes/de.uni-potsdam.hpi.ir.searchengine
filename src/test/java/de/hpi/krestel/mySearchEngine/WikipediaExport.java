package de.hpi.krestel.mySearchEngine;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
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
		
		
		Runtime runtime = Runtime.getRuntime();

	    NumberFormat format = NumberFormat.getInstance();

	    StringBuilder sb1 = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

	    sb1.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
	    sb1.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
	    sb1.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
	    sb1.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
	    
	    System.out.println(sb1.toString());
	    
		boolean memoryExhausted = false;
		int exhaustion = 10000;
		
		while (!memoryExhausted){
			
			// check again
			if (exhaustion == 0){
				memoryExhausted = true;
				break;
			}
			
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
						characters = parser.getElementText(); // parses whole text and checks for end event
						page.setText(characters);
										
						// filter text
						String[] tokens = characters.split("[\\s.]");
						
						for(String token : tokens){
							token = token.toLowerCase();
							token = token.replaceAll("[^a-zäöüß]", "");
							System.out.println(token);
							
							// TODO: use stemmer
						}
						

						
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
						//System.out.println(page);
					} else if (tag.equals("revision")) {
						inRevision = false;
					}
					break;

				default:
					break;
				}
				
				// TODO: after complete page check for memory exhaustion
				
				parser.next();
			}
			
			// clean content
			
			// tokenize
			
			exhaustion--;
		}
		
	}
}
