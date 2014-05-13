package de.hpi.krestel.mySearchEngine;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

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

			new ParserImpl("/small.xml").parseToPartialIndexes(stemmedPartialDir, unstemmedPartialDir, pageIndexFile, pageFile);
			new IndexMergerImpl().merge(stemmedSeeklistFile, unstemmedSeeklistFile, stemmedPartialDir, 
					unstemmedPartialDir, stemmedIndexFile, unstemmedIndexFile);

			MainIndex stemmedMainIndex = new MainIndex(stemmedIndexFile, stemmedSeeklistFile);
			MainIndex unstemmedMainIndex = new MainIndex(unstemmedIndexFile, unstemmedSeeklistFile);
			PageIndex pageIndex = new PageIndex();
			pageIndex.importFile(pageIndexFile, pageFile);
			
			Term term = stemmedMainIndex.getTerm("auf");
			
			System.out.println("Term: " + term.getTerm());
			for (TermOccurrence occurence : term.getOccurrences()) {
				int documentId = occurence.getDocumentId();
				System.out.println("\t documentId= " + documentId + "; title= " + pageIndex.getTitle(documentId) + "; position= " + occurence.getPosition());
			}
			
			stemmedMainIndex.close();
			unstemmedMainIndex.close();
			pageIndex.closePageFile();
		} finally {

		}
	}
}

