package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import de.hpi.krestel.mySearchEngine.booleanQueries.QueryParser;

/* This is your file! implement your search engine here!
 * 
 * Describe your search engine briefly:
 *  - multi-threaded?
 *  - stemming?
 *  - stopword removal?
 *  - index algorithm?
 *  - etc.  
 * 
 */

// Replace 'Y' with your search engine name
public class SearchEngineFAP extends SearchEngine {
	public static final String dir = "data";
	public static final String partialDir = dir + File.separator + "partial";
	
	public static final String stemmedSeeklistFile = dir + File.separator + "stemmed-seeklist.dat";
	public static final String unstemmedSeeklistFile = dir + File.separator + "unstemmed-seeklist.dat";
	public static final String stemmedIndexFile = dir + File.separator + "stemmed-index.dat";
	public static final String unstemmedIndexFile = dir + File.separator + "unstemmed-index.dat";
	public static final String titleFile = dir + File.separator + "titles.dat";
	
	public static final String stemmedPartialDir = partialDir + File.separator + "stemmed";
	public static final String unstemmedPartialDir = partialDir + File.separator + "unstemmed";
	
	private MainIndex stemmedMainIndex;
	private MainIndex unstemmedMainIndex;
	private TitleIndex titleIndex;
	
	// Replace 'Y' with your search engine name
	public SearchEngineFAP() {
		// This should stay as is! Don't add anything here!
		super();	
	}

	@Override
	void index(String dir) {
		new File(partialDir).mkdirs();
		try {
			new ParserImpl(dir).parseToPartialIndexes(stemmedPartialDir, unstemmedPartialDir, titleFile);
			new IndexMergerImpl().merge(stemmedSeeklistFile, unstemmedSeeklistFile, stemmedPartialDir, 
					unstemmedPartialDir, stemmedIndexFile, unstemmedIndexFile);
		} catch (NumberFormatException | ClassNotFoundException
				| InstantiationException | IllegalAccessException
				| XMLStreamException | FactoryConfigurationError | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	boolean loadIndex(String directory) {
		//TODO right now {@code directory} is ignored, what is it good for?
		try {
			stemmedMainIndex = new MainIndex(stemmedIndexFile, stemmedSeeklistFile);
			unstemmedMainIndex = new MainIndex(unstemmedIndexFile, unstemmedSeeklistFile);
			titleIndex = new TitleIndex();
			titleIndex.importFile(titleFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	ArrayList<String> search(String query, int topK, int prf) {
		SearchOperation<Integer> op;
		try {
			op = new QueryParser(stemmedMainIndex, unstemmedMainIndex, titleIndex, query).parse();
			op.print(0, 3);
			List<Integer> docIds = op.execute(topK);
			ArrayList<String> titles = new ArrayList<>();
			for (Integer docId : docIds) {
				titles.add(titleIndex.getTitle(docId));
			}
			return titles;
		} catch (IOException | TermLengthException | QueryProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	Double computeNdcg(String query, ArrayList<String> ranking, int ndcgAt) {
	
		// TODO Auto-generated method stub
		return null;
	}
}
