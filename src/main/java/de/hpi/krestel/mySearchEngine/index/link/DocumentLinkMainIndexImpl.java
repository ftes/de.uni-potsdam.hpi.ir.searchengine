package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.MainIndex;

public class DocumentLinkMainIndexImpl extends MainIndex<Integer,
DocumentWithLinks, DocumentLink, String> {

	public DocumentLinkMainIndexImpl(String indexPath, String seeklistPath)
			throws IOException {
		super(indexPath, seeklistPath, new DocumentLinkIndexFileHandlerFactory());
	}

}
