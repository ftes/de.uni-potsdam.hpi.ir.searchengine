package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParseAndMergeNoDummyTest {
	@Test
	public void parseAndMerge() throws Exception {
		String partialIndexDir = "partials";
		String mainIndexPath = "index.dat";
		String seekListPath = "seeklist.dat";
		
		try {
			File file = new File(partialIndexDir);
			file.mkdir();

			new ParserImpl("/small.xml").parseToPartialIndexes(partialIndexDir);
			new IndexMergerImpl().merge(seekListPath, partialIndexDir, mainIndexPath);

			MainIndex index = new MainIndex(mainIndexPath, seekListPath);
			TitleIndex titleindex = new TitleIndex();
			titleindex.importFile("titles.dat");
			
			Term term = index.getTerm("auf");
			
			System.out.println("Term: " + term.getTerm());
			for (TermOccurrence occurence : term.getOccurrences()) {
				int documentId = occurence.getDocumentId();
				System.out.println("\t documentId= " + documentId + "; title= " + titleindex.getTitle(documentId) + "; position= " + occurence.getPosition());
			}
			
			index.close();
		} finally {
			FileUtils.deleteDirectory(new File(partialIndexDir));
			new File(mainIndexPath).delete();
			new File(seekListPath).delete();
		}
	}
}

