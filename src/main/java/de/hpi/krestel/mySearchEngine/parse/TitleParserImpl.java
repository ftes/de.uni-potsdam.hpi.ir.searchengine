package de.hpi.krestel.mySearchEngine.parse;

import java.io.InputStream;

import javax.xml.stream.XMLStreamReader;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;

public class TitleParserImpl extends BaseParser {
	private final TitleIndex titleIndex;
	private boolean inRevision;
	private String title;
	private Integer id;
	
	public TitleParserImpl(InputStream in, TitleIndex titleIndex) {
		super(in);
		this.titleIndex = titleIndex;
	}
	
	private void addTitleIfPossible() {
		if (title != null && id != null) {
			titleIndex.addTitle(title, id);
			title = null;
			id = null;
		}
	}
	
	@Override
	protected void startElement(String tag, XMLStreamReader parser) throws Exception {
		if (tag.equals("revision")) {
			inRevision = true;
		} else if (!inRevision && tag.equals("title")) {
			title = parser.getElementText();
		} else if (!inRevision && tag.equals("id")) {
			id = Integer.parseInt(parser.getElementText());
		}
		addTitleIfPossible();
	}
	
	@Override
	protected void endElement(String tag, XMLStreamReader parser) {
		if (tag.equals("revision")) {
			inRevision = false;
		}
	}
}
