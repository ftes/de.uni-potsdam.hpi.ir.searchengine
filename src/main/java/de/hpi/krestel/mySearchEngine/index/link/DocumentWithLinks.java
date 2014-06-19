package de.hpi.krestel.mySearchEngine.index.link;

import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexList;


public class DocumentWithLinks extends IndexList<Integer, DocumentLink, String> {
	public DocumentWithLinks(Integer docId) {
		super(docId);
	}
	
	public DocumentWithLinks(Integer docId, SortedSet<DocumentLink> occurrences) {
		super(docId, occurrences);
	}
}
