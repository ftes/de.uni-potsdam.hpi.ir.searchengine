package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseSeekList;

public class LinkSeekListImpl extends BaseSeekList<Integer> {
	public LinkSeekListImpl(String filename) throws IOException {
		super(filename, new LinkKeyValueFileHandler());
	}

	protected boolean stop(Integer docId, Integer current) {
		return docId != current;
	}
}
