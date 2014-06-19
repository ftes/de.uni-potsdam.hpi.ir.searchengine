package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearReader;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileRandomReader;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;

public class DocumentLinkIndexFileHandlerFactory implements
		IndexFileHandlerFactory<Integer, DocumentWithLinks, DocumentLink, String> {

	@Override
	public IndexFileLinearReader<Integer, DocumentWithLinks, DocumentLink, String>
	createLinearReader(String fileName) throws IOException {
		return new DocumentLinkIndexFileLinearReaderImpl(fileName);
	}

	@Override
	public IndexFileRandomReader<Integer, DocumentWithLinks, DocumentLink, String>
	createRandomReader(String fileName) throws IOException {
		return new DocumentLinkIndexFileRandomReaderImpl(fileName);
	}

	@Override
	public IndexFileLinearWriter<Integer, DocumentWithLinks, DocumentLink, String> createLinearWriter(String fileName)
	throws IOException{
		return new DocumentLinkIndexFileLinearWriterImpl(fileName);
	}

	@Override
	public SeekList<Integer> createSeekList(String fileName) throws IOException {
		return new DocumentLinkSeekListImpl(fileName);
	}
	
	@Override
	public PartialIndex<Integer, DocumentWithLinks, DocumentLink, String> createPartialIndex(
			String fileName) throws IOException {
		return new DocumentPartialIndex(createLinearWriter(fileName));
	}
}
