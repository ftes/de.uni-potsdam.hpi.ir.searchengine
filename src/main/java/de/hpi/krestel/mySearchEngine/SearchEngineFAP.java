package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

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
	
	public static final String seeklistFile = dir + File.separator + "seeklist.dat";
	public static final String indexFile = dir + File.separator + "index.dat";
	public static final String titleFile = dir + File.separator + "titles.dat";
	
	private MainIndex mainIndex;
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
			new ParserImpl(dir).parseToPartialIndexes(partialDir, titleFile);
			new IndexMergerImpl().merge(seeklistFile, partialDir, indexFile);
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
			mainIndex = new MainIndex(indexFile, seeklistFile);
			titleIndex = new TitleIndex();
			titleIndex.importFile(titleFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the set of document ids in which the term occurs.
	 */
	private Set<Integer> getDocumentSet(Term term) {
		Set<Integer> result = new HashSet<>();
		for (TermOccurrence occurrence : term.getOccurrences()) {
			result.add(occurrence.getDocumentId());
		}
		return result;
	}

	@Override
	ArrayList<String> search(String query, int topK, int prf) {
		List<String> tokens = new Tokenizer(query).tokenize();

		//TODO for now, just return random k documents that contain all the tokens found in the query
		Set<Integer> documentsContainingAllTokens = new HashSet<>();
		boolean firstToken = true;
		for (String token : tokens) {
			Term term = null;
			try {
				term = mainIndex.getTerm(token);
				Set<Integer> documentSet = getDocumentSet(term);
				if (firstToken) {
					firstToken = false;
					documentsContainingAllTokens.addAll(documentSet);
				} else {
					documentsContainingAllTokens.retainAll(documentSet);
				}
			} catch (IOException | TermLengthException e) {
				System.err.println(e.getMessage());
			} catch (TermNotFoundException e) {
				//do nothing, just not found
			}
		}
		
		ArrayList<String> titles = new ArrayList<>();
		int i = 0;
		for (Integer docId : documentsContainingAllTokens) {
			if (i >= topK) {
				break;
			}
			titles.add(titleIndex.getTitle(docId));
			i++;
		}
		
		return titles;
	}
	
	@Override
	Double computeNdcg(String query, ArrayList<String> ranking, int ndcgAt) {
	
		// TODO Auto-generated method stub
		return null;
	}
}
