package de.hpi.krestel.mySearchEngine.index.term;

import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.PartialIndex;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileHandlerFactory;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearReader;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileLinearWriter;
import de.hpi.krestel.mySearchEngine.index.io.IndexFileRandomReader;
import de.hpi.krestel.mySearchEngine.index.io.SeekList;

public class TermIndexFileHandlerFactory implements
		IndexFileHandlerFactory<String, Term, TermOccurrence, Integer> {

	@Override
	public IndexFileLinearReader<String, Term, TermOccurrence, Integer>
	createLinearReader(String fileName) throws IOException {
		return new TermIndexFileLinearReaderImpl(fileName);
	}

	@Override
	public IndexFileRandomReader<String, Term, TermOccurrence, Integer>
	createRandomReader(String fileName) throws IOException {
		return new TermIndexFileRandomReaderImpl(fileName);
	}

	@Override
	public IndexFileLinearWriter<String, Term, TermOccurrence, Integer> createLinearWriter(String fileName)
	throws IOException{
		return new TermIndexFileLinearWriterImpl(fileName);
	}

	@Override
	public SeekList<String> createSeekList(String fileName) throws IOException {
		return new TermSeekListImpl(fileName);
	}
	
	@Override
	public PartialIndex<String, Term, TermOccurrence, Integer> createPartialIndex(
			String fileName) throws IOException {
		return new TermPartialIndex(createLinearWriter(fileName));
	}
}
