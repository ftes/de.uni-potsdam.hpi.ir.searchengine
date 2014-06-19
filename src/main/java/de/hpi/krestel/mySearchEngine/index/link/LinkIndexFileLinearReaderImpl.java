package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearReader;

public class DocumentLinkIndexFileLinearReaderImpl extends
		BaseIndexFileLinearReader<Integer, DocumentWithLinks, DocumentLink, String> {

	public DocumentLinkIndexFileLinearReaderImpl(
			String filename) throws IOException {
		super(filename, new DocumentLinkKeyValueFileHandler(), new DocumentLinkIndexListFactory());
	}

}
