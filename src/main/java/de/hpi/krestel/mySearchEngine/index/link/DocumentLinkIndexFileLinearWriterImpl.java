package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearWriter;


public class DocumentLinkIndexFileLinearWriterImpl
extends BaseIndexFileLinearWriter<Integer, DocumentWithLinks, DocumentLink, String> {
	public DocumentLinkIndexFileLinearWriterImpl(String fileName) throws IOException {
		super(fileName, new DocumentLinkKeyValueFileHandler());
	}
}
