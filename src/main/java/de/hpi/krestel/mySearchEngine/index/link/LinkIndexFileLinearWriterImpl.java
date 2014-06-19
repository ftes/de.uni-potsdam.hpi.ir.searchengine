package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseIndexFileLinearWriter;


public class LinkIndexFileLinearWriterImpl
extends BaseIndexFileLinearWriter<Integer, DocumentWithLinks, Link, String> {
	public LinkIndexFileLinearWriterImpl(String fileName) throws IOException {
		super(fileName, new LinkKeyValueFileHandler());
	}
}
