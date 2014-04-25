package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParseAndMergeTest {
	@Test
	public void parseAndMerge() throws Exception {
		String partialIndexDir = "partials";
		String mainIndexPath = "index.dat";
		String seekListPath = "seeklist.dat";
		
		try {
			File file = new File(partialIndexDir);
			file.mkdir();

			new ParserDummy().parseToPartialIndexes(partialIndexDir);
			new IndexMergerImpl().merge(seekListPath, partialIndexDir, mainIndexPath);

			MainIndex index = new MainIndex(mainIndexPath, seekListPath);
			Term term = index.getTerm("weit");
			assertEquals(8, term.getOccurrences().size());
			term = index.getTerm("blindtext");
			assertEquals(7, term.getOccurrences().size());
			
			index.close();
		} finally {
			FileUtils.deleteDirectory(new File(partialIndexDir));
			new File(mainIndexPath).delete();
			new File(seekListPath).delete();
		}
	}
}

