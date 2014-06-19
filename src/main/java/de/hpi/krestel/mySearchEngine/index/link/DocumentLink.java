package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public class DocumentLink extends IndexListSlot<String> {
	public DocumentLink() {}
	public DocumentLink(int documentId, String anchorText) {
		super(documentId, anchorText);
	}
}
