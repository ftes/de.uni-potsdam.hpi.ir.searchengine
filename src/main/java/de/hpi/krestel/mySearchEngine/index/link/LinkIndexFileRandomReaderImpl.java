package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileRandomReader;

public class LinkIndexFileRandomReaderImpl extends
		BaseIndexFileRandomReader<Integer, DocumentWithLinks, Link, Integer> {

	public LinkIndexFileRandomReaderImpl(
			String filename) throws IOException {
		super(filename, new LinkKeyValueFileHandler(), new LinkIndexListFactory());
	}

}
