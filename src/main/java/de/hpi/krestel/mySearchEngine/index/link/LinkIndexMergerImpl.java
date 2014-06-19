package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.io.IndexMergerImpl;

public class LinkIndexMergerImpl extends IndexMergerImpl<Integer,
DocumentWithLinks, Link, String> {
	public LinkIndexMergerImpl() {
		super(new LinkIndexFileHandlerFactory(), new LinkIndexListFactory());
	}
}
