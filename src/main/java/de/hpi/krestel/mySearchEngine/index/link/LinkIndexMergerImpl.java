package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexMergerImpl;

public class LinkIndexMergerImpl extends IndexMergerImpl<Integer,
DocumentWithLinks, Link, String> {

	public LinkIndexMergerImpl(TitleIndex titleIndex) {
		super(new LinkIndexFileHandlerFactory(), new LinkIndexListFactory(), titleIndex);
	}

	public LinkIndexMergerImpl() {
		super(new LinkIndexFileHandlerFactory(), new LinkIndexListFactory());
	}
}
