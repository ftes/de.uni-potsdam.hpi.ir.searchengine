package de.hpi.krestel.mySearchEngine;

import java.io.EOFException;
import java.io.IOException;

public class SeekListPrinter {
	public static void main(String[] args) throws IOException {
		String seekListPath = "seeklist.dat";
		
		SeekListImpl seekList = new SeekListImpl(seekListPath);
		long i = 0;
		while (true) {
			try {
				String term = seekList.getTermAtPosition(i);
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
