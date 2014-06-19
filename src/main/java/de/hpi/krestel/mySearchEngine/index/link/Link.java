package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public class Link extends IndexListSlot<String> {
	public Link() {}
	public Link(int documentId, String anchorText) {
		super(documentId, anchorText);
	}
}
