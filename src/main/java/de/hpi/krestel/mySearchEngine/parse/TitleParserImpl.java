package de.hpi.krestel.mySearchEngine.parse;

import java.io.InputStream;
import java.util.Date;

import javax.xml.stream.XMLStreamReader;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;

public class TitleParserImpl extends BaseParser {
	private final TitleIndex titleIndex;
	private boolean inRevision;
	private String title;
	private Integer id;
	private long numArticles = 0;
	private static final long printInterval = (long) 1e5;
	private Date start;
	
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
		if (tag.equals("page")) {
			numArticles++;
			if (numArticles % printInterval == 0) {
				long elapsed = new Date().getTime() - start.getTime();
				int eta = (int) ((elapsed / (float) numArticles) * (3.3e6-numArticles) / 1000 / 60);
				System.out.printf("%d articles, %d seconds, ETA: %d minutes\n", numArticles, (int) elapsed / 1000, eta);
			}
		} else if (tag.equals("revision")) {
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
	
	@Override
	public void parse() throws Exception {
		start = new Date();
		super.parse();
	}
}
