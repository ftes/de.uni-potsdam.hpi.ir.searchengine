package de.hpi.krestel.mySearchEngine.index.link;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearReader;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileRandomReader;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;

public class LinkIndexFileHandlerFactory implements
		IndexFileHandlerFactory<Integer, DocumentWithLinks, Link, Integer> {

	@Override
	public IndexFileLinearReader<Integer, DocumentWithLinks, Link, Integer>
	createLinearReader(String fileName) throws IOException {
		return new LinkIndexFileLinearReaderImpl(fileName);
	}

	@Override
	public IndexFileRandomReader<Integer, DocumentWithLinks, Link, Integer>
	createRandomReader(String fileName) throws IOException {
		return new LinkIndexFileRandomReaderImpl(fileName);
	}

	@Override
	public IndexFileLinearWriter<Integer, DocumentWithLinks, Link, Integer> createLinearWriter(String fileName)
	throws IOException{
		return new LinkIndexFileLinearWriterImpl(fileName);
	}

	@Override
	public SeekList<Integer> createSeekList(String fileName) throws IOException {
		return new LinkSeekListImpl(fileName);
	}
	
	@Override
	public PartialIndex<Integer, DocumentWithLinks, Link, Integer> createPartialIndex(
			String fileName) throws IOException {
		return new LinkPartialIndex(createLinearWriter(fileName));
	}
}
