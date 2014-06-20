package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.index.term.Term;
import de.hpi.krestel.mySearchEngine.index.term.TermIndexMergerImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.parse.TextParserImpl;

public class ParseAndMergeTest {
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

			new TextParserImpl(this.getClass().getResourceAsStream("/small.xml"), null,
					stemmedPartialDir, unstemmedPartialDir, null, pageIndexFile, pageFile).
					parse();
			new TermIndexMergerImpl().merge(stemmedSeeklistFile, stemmedPartialDir, stemmedIndexFile);
			new TermIndexMergerImpl().merge(unstemmedSeeklistFile, unstemmedPartialDir, unstemmedIndexFile);			

			TermMainIndexImpl stemmedMainIndex = new TermMainIndexImpl(stemmedIndexFile, stemmedSeeklistFile);
			TermMainIndexImpl unstemmedMainIndex = new TermMainIndexImpl(unstemmedIndexFile, unstemmedSeeklistFile);
			
			Term term = stemmedMainIndex.getList("weit");
			assertEquals(8, term.getSlots().size());
			term = stemmedMainIndex.getList("blindtext");
			assertEquals(7, term.getSlots().size());
			
			stemmedMainIndex.close();
			unstemmedMainIndex.close();
		} finally {
		}
	}
}

