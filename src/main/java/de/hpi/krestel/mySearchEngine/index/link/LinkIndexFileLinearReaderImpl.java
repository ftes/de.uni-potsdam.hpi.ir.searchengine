package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearReader;

public class LinkIndexFileLinearReaderImpl extends
		BaseIndexFileLinearReader<Integer, DocumentWithLinks, Link, Integer> {

	public LinkIndexFileLinearReaderImpl(
			String filename) throws IOException {
		super(filename, new LinkKeyValueFileHandler(), new LinkIndexListFactory());
	}

}
