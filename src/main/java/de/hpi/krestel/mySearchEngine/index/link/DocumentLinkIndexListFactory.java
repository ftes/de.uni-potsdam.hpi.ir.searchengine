package de.hpi.krestel.mySearchEngine.index.link;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexListFactory;

public class DocumentLinkIndexListFactory implements
		IndexListFactory<Integer, DocumentWithLinks, DocumentLink, String> {

	@Override
	public DocumentWithLinks createList(Integer key) {
		return new DocumentWithLinks(key);
	}

	@Override
	public DocumentLink createSlot(int docId, String value) {
		return new DocumentLink(docId, value);
	}
	
	@Override
	public DocumentWithLinks createList(Integer key, SortedSet<DocumentLink> slots) {
		return new DocumentWithLinks(key, slots);
	}
}
