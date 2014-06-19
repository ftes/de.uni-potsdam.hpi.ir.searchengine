package de.hpi.krestel.mySearchEngine;

import java.io.EOFException;
import java.io.IOException;

import de.hpi.krestel.mySearchEngine.index.term.TermSeekListImpl;

public class SeekListPrinter {
	public static void main(String[] args) throws IOException {
		String seekListPath = "seeklist.dat";
		
		TermSeekListImpl seekList = new TermSeekListImpl(seekListPath);
		long i = 0;
		while (true) {
			try {
				String term = seekList.getKeyAtPos(i);
				long offset = seekList.getOffsetAtPosition(i);
				System.out.print("(" + term + "," + offset + ") ");
				i++;
			} catch (EOFException e) {
				break;
			}
		}
		seekList.close();
	}
}
