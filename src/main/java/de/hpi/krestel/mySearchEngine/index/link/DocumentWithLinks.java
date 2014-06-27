package de.hpi.krestel.mySearchEngine.index.link;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexList;


public class DocumentWithLinks extends IndexList<Integer, Link, Integer> {
	public DocumentWithLinks(Integer linkedToDocId) {
		super(linkedToDocId);
	}
	
	public DocumentWithLinks(Integer linkedToDocId, SortedSet<Link> links) {
		super(linkedToDocId, links);
	}
}
