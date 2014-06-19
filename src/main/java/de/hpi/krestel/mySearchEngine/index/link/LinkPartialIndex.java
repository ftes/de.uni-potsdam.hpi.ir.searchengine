package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;

public class LinkPartialIndex extends
		PartialIndex<Integer, DocumentWithLinks, Link, String> {
	public LinkPartialIndex(IndexFileLinearWriter<Integer, DocumentWithLinks, Link, String> writer) {
		super(writer, new LinkIndexListFactory());
	}
}
