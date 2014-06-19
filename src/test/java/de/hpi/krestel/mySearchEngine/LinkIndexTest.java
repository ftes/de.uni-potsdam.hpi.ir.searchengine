package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.IOException;

import org.junit.Test;

import de.hpi.krestel.mySearchEngine.index.link.DocumentWithLinks;
import de.hpi.krestel.mySearchEngine.index.link.LinkIndexFileLinearReaderImpl;

public class LinkIndexTest {
	@Test
	public void printLinkIndex() throws IOException {
		SearchEngineFAP searchEngine = new SearchEngineFAP();
		
		searchEngine.index("src/test/resources/small.xml");
		searchEngine.loadIndex(null);
		
		LinkIndexFileLinearReaderImpl reader =
				new LinkIndexFileLinearReaderImpl(SearchEngineFAP.linkIndexFile);
		int i=0;
		while (true) {
			try {
				DocumentWithLinks doc = reader.readNextList();
				System.out.println(doc);
				i++;
			} catch (EOFException e) {
				break;
			}
		}
		assertEquals(1, i);
		reader.close();
	}
}
