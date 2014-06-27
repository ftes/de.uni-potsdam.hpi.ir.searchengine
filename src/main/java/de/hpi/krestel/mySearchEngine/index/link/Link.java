package de.hpi.krestel.mySearchEngine.index.link;

import de.hpi.krestel.mySearchEngine.index.IndexListSlot;

public class Link extends IndexListSlot<Integer> {
	public Link() {}
	public Link(int linkedFromDocId, Integer targetPageId) {
		super(linkedFromDocId, targetPageId);
	}
}
