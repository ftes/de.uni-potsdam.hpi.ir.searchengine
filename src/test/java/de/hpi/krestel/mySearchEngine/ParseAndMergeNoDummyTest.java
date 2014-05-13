package de.hpi.krestel.mySearchEngine;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParseAndMergeNoDummyTest {
	@Test
	public void parseAndMerge() throws Exception {
		String sPartialIndexDir = "spartials";
		String uPartialIndexDir = "upartials";
		String sMainIndexPath = "s-index.dat";
		String uMainIndexPath = "u-index.dat";
		String sSeekListPath = "s-seeklist.dat";
		String uSeekListPath = "u-seeklist.dat";
		String pageIndexFile = "pagesIndex.dat";
		String pageFile = "pages.dat";
		
		try {
			File file = new File(sPartialIndexDir);
			file.mkdir();
			file = new File(uPartialIndexDir);
			file.mkdir();

			new ParserImpl("/small.xml").parseToPartialIndexes(sPartialIndexDir, uPartialIndexDir, pageIndexFile, pageFile);
			new IndexMergerImpl().merge(sSeekListPath, uSeekListPath, sPartialIndexDir, uPartialIndexDir,
					sMainIndexPath, uMainIndexPath);

			MainIndex index = new MainIndex(sMainIndexPath, sSeekListPath);
			PageIndex titleindex = new PageIndex();
			titleindex.importFile(pageIndexFile, pageFile);
			
			Term term = index.getTerm("auf");
			
			System.out.println("Term: " + term.getTerm());
			for (TermOccurrence occurence : term.getOccurrences()) {
				int documentId = occurence.getDocumentId();
				System.out.println("\t documentId= " + documentId + "; title= " + titleindex.getTitle(documentId) + "; position= " + occurence.getPosition());
			}
			
			index.close();
		} finally {
			FileUtils.deleteDirectory(new File(sPartialIndexDir));
			FileUtils.deleteDirectory(new File(uPartialIndexDir));
			new File(sMainIndexPath).delete();
			new File(uMainIndexPath).delete();
			new File(sSeekListPath).delete();
			new File(uSeekListPath).delete();
		}
	}
}

