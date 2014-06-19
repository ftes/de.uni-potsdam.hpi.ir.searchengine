package de.hpi.krestel.mySearchEngine;

import java.io.File;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.index.PageIndex;
import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexMergerImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;
import de.hpi.krestel.mySearchEngine.parse.ParserImpl;

public class ParseAndMergeNoDummyTest {
	public static final String dir = "data";
	public static final String partialDir = dir + File.separator + "partial";
	
	public static final String stemmedSeeklistFile = dir + File.separator + "stemmed-seeklist.dat";
	public static final String unstemmedSeeklistFile = dir + File.separator + "unstemmed-seeklist.dat";
	public static final String stemmedIndexFile = dir + File.separator + "stemmed-index.dat";
	public static final String unstemmedIndexFile = dir + File.separator + "unstemmed-index.dat";
	public static final String pageFile = dir + File.separator + "pages.dat";
	public static final String pageIndexFile = dir + File.separator + "pagesIndex.dat";
	
	public static final String stemmedPartialDir = partialDir + File.separator + "stemmed";
	public static final String unstemmedPartialDir = partialDir + File.separator + "unstemmed";
	
	@Test
	public void parseAndMerge() throws Exception {		
		try {
			File file = new File(dir);
			file.mkdir();
			file = new File(partialDir);
			file.mkdir();

			new ParserImpl(this.getClass().getResourceAsStream("/small.xml")).
				parseToPartialIndexes(stemmedPartialDir, unstemmedPartialDir, null, pageIndexFile, pageFile);
			new TermIndexMergerImpl().merge(stemmedSeeklistFile, stemmedPartialDir, stemmedIndexFile);
			new TermIndexMergerImpl().merge(unstemmedSeeklistFile, unstemmedPartialDir, unstemmedIndexFile);

			TermMainIndexImpl stemmedMainIndex = new TermMainIndexImpl(stemmedIndexFile, stemmedSeeklistFile);
			TermMainIndexImpl unstemmedMainIndex = new TermMainIndexImpl(unstemmedIndexFile, unstemmedSeeklistFile);
			PageIndex pageIndex = new PageIndex();
			pageIndex.importFile(pageIndexFile, pageFile);
			
			Term term = stemmedMainIndex.getList("auf");
			
			System.out.println("Term: " + term.getKey());
			for (TermOccurrence occurence : term.getSlots()) {
				int documentId = occurence.getDocumentId();
				System.out.println("\t documentId= " + documentId + "; title= " + pageIndex.getTitle(documentId) + "; position= " + occurence.getValue());
			}
			
			stemmedMainIndex.close();
			unstemmedMainIndex.close();
			pageIndex.closePageFile();
		} finally {

		}
	}
}

