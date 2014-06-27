package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.MainIndex;

public class LinkMainIndexImpl extends MainIndex<Integer,
DocumentWithLinks, Link, Integer> {

	public LinkMainIndexImpl(String indexPath, String seeklistPath)
			throws IOException {
		super(indexPath, seeklistPath, new LinkIndexFileHandlerFactory());
	}

}
