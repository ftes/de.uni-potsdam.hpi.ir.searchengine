package de.hpi.krestel.mySearchEngine.parse;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public abstract class BaseParser implements Parser {
	private final InputStream in;
	protected abstract void startElement(String tag, XMLStreamReader parser) throws Exception;
	protected abstract void endElement(String tag, XMLStreamReader parser) throws Exception;
	
	public BaseParser(InputStream in) {
		this.in = in;
	}
	
	protected boolean done = false;
	
	@Override
	public void parse()
			throws Exception {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory
						.createXMLStreamReader(in);
		
		// read from stream
		while (! done && parser.hasNext()) {
			switch (parser.getEventType()) {
			case XMLStreamConstants.START_ELEMENT:
				String tag = parser.getLocalName();
				startElement(tag, parser);
				break;

			case XMLStreamConstants.END_ELEMENT:
				tag = parser.getLocalName();
				endElement(tag, parser);
				break;
			}
			
			parser.next();
		}
		parser.close();
	}
}
