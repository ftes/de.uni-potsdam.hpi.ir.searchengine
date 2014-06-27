package de.hpi.krestel.mySearchEngine.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.link.DocumentWithLinks;
import de.hpi.krestel.mySearchEngine.index.link.LinkMainIndexImpl;

public class LinkToQuery implements SearchOperation<Integer> {	
	private final String queryString;
	private final LinkMainIndexImpl linkMainIndex;
	private final TitleIndex titleIndex;
	
	public LinkToQuery(LinkMainIndexImpl linkMainIndex, TitleIndex titleIndex,
			String query) {
		this.linkMainIndex = linkMainIndex;
		this.titleIndex = titleIndex;
		this.queryString = query;
	}

	@Override
	public List<Integer> execute(int topK) throws WordLengthException, IOException {
		//only use the top prf documents for expanding the query
		List<Integer> docIds = new ArrayList<>();
		
		String title = queryString.toLowerCase().replaceFirst("linkto", "");
		title = title.trim();
		System.out.println("Searching for links to \"" + title + "\"");
		
		// get document id
		Integer targetID = titleIndex.getDocId(title);
		if (targetID == null) {
			System.out.println("No page with this title found");
			return docIds;
		}
		
		DocumentWithLinks doc;
		try {
			doc = linkMainIndex.getList(targetID);
		} catch (KeyNotFoundException e) {
			System.out.println("No Artikel with this title found in the index :(");
			return docIds;
		}
		Set<Integer> resultIds = doc.getDocumentIds();
		for (Integer id : resultIds) {
			docIds.add(id);
		}
				
		return docIds;
	}

	@Override
	public void print(int indent, int step) {
		// TODO Auto-generated method stub
		
	}
}
