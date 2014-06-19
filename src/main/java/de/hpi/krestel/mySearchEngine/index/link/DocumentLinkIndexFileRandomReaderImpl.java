package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileRandomReader;

public class DocumentLinkIndexFileRandomReaderImpl extends
		BaseIndexFileRandomReader<Integer, DocumentWithLinks, DocumentLink, String> {

	public DocumentLinkIndexFileRandomReaderImpl(
			String filename) throws IOException {
		super(filename, new DocumentLinkKeyValueFileHandler(), new DocumentLinkIndexListFactory());
	}

}
