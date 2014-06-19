package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;

public class DocumentPartialIndex extends
		PartialIndex<Integer, DocumentWithLinks, DocumentLink, String> {
	public DocumentPartialIndex(IndexFileLinearWriter<Integer, DocumentWithLinks, DocumentLink, String> writer) {
		super(writer, new DocumentLinkIndexListFactory());
	}
}
