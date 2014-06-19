package de.hpi.krestel.mySearchEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;

import de.hpi.krestel.mySearchEngine.booleanQueries.QueryParser;
import de.hpi.krestel.mySearchEngine.index.PageIndex;
import de.hpi.krestel.mySearchEngine.index.TitleIndex;
import de.hpi.krestel.mySearchEngine.index.link.LinkIndexMergerImpl;
import de.hpi.krestel.mySearchEngine.index.link.LinkMainIndexImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexMergerImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.parse.Parser;
import de.hpi.krestel.mySearchEngine.parse.ParserImpl;
import de.hpi.krestel.mySearchEngine.search.NdcgComputer;
import de.hpi.krestel.mySearchEngine.search.PseudoRelevanceFeedback;
import de.hpi.krestel.mySearchEngine.search.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.search.SearchOperation;
import de.hpi.krestel.mySearchEngine.search.SnippetGenerator;

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
	public static final String linkSeeklistFile = dir + File.separator + "link-seeklist.dat";
	
	public static final String stemmedIndexFile = dir + File.separator + "stemmed-index.dat";
	public static final String unstemmedIndexFile = dir + File.separator + "unstemmed-index.dat";
	public static final String linkIndexFile = dir + File.separator + "link-index.dat";
	
	public static final String pageFile = dir + File.separator + "pages.dat";
	public static final String pageIndexFile = dir + File.separator + "pagesIndex.dat";
	
	public static final String stemmedPartialDir = partialDir + File.separator + "stemmed";
	public static final String unstemmedPartialDir = partialDir + File.separator + "unstemmed";
	public static final String linksPartialDir = partialDir + File.separator + "links";
	
	private TermMainIndexImpl stemmedMainIndex;
	private TermMainIndexImpl unstemmedMainIndex;
	private LinkMainIndexImpl linkMainIndex;
	private PageIndex pageIndex;
	
	// Replace 'Y' with your search engine name
	public SearchEngineFAP() {
		// This should stay as is! Don't add anything here!
		super();	
	}

	@Override
	void index(String directory) {
		InputStream in = null;
		try {
			in = new FileInputStream(new File(directory));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			return;
		}
		try {
			FileUtils.deleteDirectory(new File(SearchEngineFAP.dir));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new File(stemmedPartialDir).mkdirs();
		new File(unstemmedPartialDir).mkdirs();
		new File(linksPartialDir).mkdirs();
		try {
			Parser parser = new ParserImpl(in);
			parser.parseToPartialIndexes(stemmedPartialDir, unstemmedPartialDir, linksPartialDir, pageIndexFile, pageFile);
			TitleIndex titleIndex = parser.getTitleIndex();
			
			TermIndexMergerImpl termMerger = new TermIndexMergerImpl();
			termMerger.merge(stemmedSeeklistFile, stemmedPartialDir, stemmedIndexFile);
			termMerger.merge(unstemmedSeeklistFile, unstemmedPartialDir, unstemmedIndexFile);
			
			LinkIndexMergerImpl linkMerger = new LinkIndexMergerImpl(titleIndex);
			linkMerger.merge(linkSeeklistFile, linksPartialDir, linkIndexFile);
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
			stemmedMainIndex = new TermMainIndexImpl(stemmedIndexFile, stemmedSeeklistFile);
			unstemmedMainIndex = new TermMainIndexImpl(unstemmedIndexFile, unstemmedSeeklistFile);
			linkMainIndex = new LinkMainIndexImpl(linkIndexFile, linkSeeklistFile);
			pageIndex = new PageIndex();
			pageIndex.importFile(pageIndexFile, pageFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	ArrayList<String> search(String query, int topK, int prf) {
		try {
			SearchOperation<Integer> op;
			if (prf > 0) {
				op = new PseudoRelevanceFeedback(stemmedMainIndex, pageIndex, query, prf);
			} else {
				op = new QueryParser( stemmedMainIndex, unstemmedMainIndex, pageIndex, query).parse();
			}
			List<Integer> docIds = op.execute(topK);
			ArrayList<String> results = new ArrayList<>();
			
			for (Integer docId : docIds) {
				String result = pageIndex.getTitle(docId) + "\n";
				result += new SnippetGenerator(pageIndex.getText(docId), query).generate() + "\n";
				results.add(result);
			}
			return results;
		} catch (IOException | QueryProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	Double computeNdcg(ArrayList<String> goldRanking, ArrayList<String> myRanking, int at) {
		return NdcgComputer.computeNdcg(goldRanking, myRanking, at);
	}
}
