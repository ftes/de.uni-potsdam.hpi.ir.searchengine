package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public class Link extends IndexListSlot<String> {
	public Link() {}
	public Link(int linkedFromDocId, String anchorText) {
		super(linkedFromDocId, anchorText);
	}
}
