package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;

public class LinkPartialIndex extends
		PartialIndex<Integer, DocumentWithLinks, Link, Integer> {
	public LinkPartialIndex(IndexFileLinearWriter<Integer, DocumentWithLinks, Link, Integer> writer) {
		super(writer, new LinkIndexListFactory());
	}
}
