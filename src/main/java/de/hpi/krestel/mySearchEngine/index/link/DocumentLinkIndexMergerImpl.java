package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexMergerImpl;

public class DocumentLinkIndexMergerImpl extends IndexMergerImpl<Integer,
DocumentWithLinks, DocumentLink, String> {

	public DocumentLinkIndexMergerImpl(TitleIndex titleIndex) {
		super(new DocumentLinkIndexFileHandlerFactory(), new DocumentLinkIndexListFactory(), titleIndex);
	}

	public DocumentLinkIndexMergerImpl() {
		super(new DocumentLinkIndexFileHandlerFactory(), new DocumentLinkIndexListFactory());
	}
}
