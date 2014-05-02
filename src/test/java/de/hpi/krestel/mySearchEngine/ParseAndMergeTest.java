package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParseAndMergeTest {
	@Test
	public void parseAndMerge() throws Exception {
		String sPartialIndexDir = "spartials";
		String uPartialIndexDir = "upartials";
		String sMainIndexPath = "s-index.dat";
		String uMainIndexPath = "u-index.dat";
		String sSeekListPath = "s-seeklist.dat";
		String uSeekListPath = "u-seeklist.dat";
		String titlePath = "titles.dat";
		
		try {
			File file = new File(sPartialIndexDir);
			file.mkdir();
			file = new File(uPartialIndexDir);
			file.mkdir();

			new ParserDummy().parseToPartialIndexes(sPartialIndexDir, uPartialIndexDir, titlePath);
			new IndexMergerImpl().merge(sSeekListPath, uSeekListPath, sPartialIndexDir, uPartialIndexDir,
					sMainIndexPath, uMainIndexPath);

			MainIndex index = new MainIndex(sMainIndexPath, sSeekListPath);
			Term term = index.getTerm("weit");
			assertEquals(8, term.getOccurrences().size());
			term = index.getTerm("blindtext");
			assertEquals(7, term.getOccurrences().size());
			
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

