package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.io.BaseSeekList;

public class DocumentLinkSeekListImpl extends BaseSeekList<Integer> {
	public DocumentLinkSeekListImpl(String filename) throws IOException {
		super(filename, new DocumentLinkKeyValueFileHandler());
	}

	protected boolean stop(Integer docId, Integer current) {
		return docId != current;
	}
}
