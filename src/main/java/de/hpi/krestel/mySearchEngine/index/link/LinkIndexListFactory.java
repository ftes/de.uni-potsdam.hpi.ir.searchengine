package de.hpi.krestel.mySearchEngine.index.link;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexListFactory;

public class LinkIndexListFactory implements
		IndexListFactory<Integer, DocumentWithLinks, Link, Integer> {

	@Override
	public DocumentWithLinks createList(Integer key) {
		return new DocumentWithLinks(key);
	}

	@Override
	public Link createSlot(int docId, Integer value) {
		return new Link(docId, value);
	}
	
	@Override
	public DocumentWithLinks createList(Integer key, SortedSet<Link> slots) {
		return new DocumentWithLinks(key, slots);
	}
}
