package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import de.hpi.krestel.mySearchEngine.index.IndexListSlotComparator;
import de.hpi.krestel.mySearchEngine.index.term.TermMainIndexImpl;
import de.hpi.krestel.mySearchEngine.index.term.TermOccurrence;
import de.hpi.krestel.mySearchEngine.parse.Tokenizer;
import de.hpi.krestel.mySearchEngine.search.KeyNotFoundException;
import de.hpi.krestel.mySearchEngine.search.WordLengthException;

public class PhraseQuery implements BooleanSetOperation<Integer> {
	public static final String SYMBOL = "´";
	
	private final TermMainIndexImpl index;
	private final List<String> tokens;
	
	public PhraseQuery(TermMainIndexImpl index, String phrase) {
		this.index = index;
		tokens = new Tokenizer(phrase).tokenize(false);
	}

	/**
	 * Step through the token list, pruning down the set of
	 * phrase occurrences as we go along.
	 * Start out with all occurrences of the first token, then see
	 * for which of these there are occurrences of the second token directly
	 * afterward in the same respective documents, and so on.
	 */
	@Override
	public List<Integer> execute(int topK) throws IOException, WordLengthException {
		SortedSet<TermOccurrence> occurrencesOfPreviousToken = null;
		for (String token : tokens) {
			SortedSet<TermOccurrence> occurrences = null;
			try {
				occurrences = index.getList(token).getSlots();
			} catch (KeyNotFoundException e) {
				return Collections.emptyList();
			}

			if (occurrencesOfPreviousToken == null) {
				//first token in phrase
				occurrencesOfPreviousToken = occurrences;
			} else {
				Iterator<TermOccurrence> previousIter = occurrencesOfPreviousToken.iterator();
				Iterator<TermOccurrence> currentIter = occurrences.iterator();

				if (previousIter.hasNext() && currentIter.hasNext()) {
					TermOccurrence previous = previousIter.next();
					TermOccurrence current = currentIter.next();
					while (true) {
						try {
							int comparison = new IndexListSlotComparator<TermOccurrence, Integer>().compare(previous, current);
							if (comparison < 0) {
								//previous is before current -> check if directly next to each other
								if (previous.getDocumentId() == current.getDocumentId() &&
										previous.getValue() + 1 == current.getValue()) {
									//directly next to one another -> keep occurrence in current set
									previous = previousIter.next();
									current = currentIter.next();
									continue;
								} else {
									//previous is in front of current, but not directly
									previous = previousIter.next();
									continue;
								}
							} else {
								//previous is after current -> can't be a possible combination, remove from current set
								currentIter.remove();
								current = currentIter.next();
								continue;
							}
						} catch (NoSuchElementException e) {
							break;
						}
					}
				}
				
				//remove the remaining unchecked elements from the current set
				while (currentIter.hasNext()) {
					currentIter.next();
					currentIter.remove();
				}
			}
			
			occurrencesOfPreviousToken = occurrences;
		}
		
		Set<Integer> documentIds = new HashSet<>();
		for (TermOccurrence occurrence : occurrencesOfPreviousToken) {
			documentIds.add(occurrence.getDocumentId());
		}
		return new ArrayList<Integer>(documentIds);
	}

	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.print(indentSpace + SYMBOL + " ");
		for (String token : tokens) {
			System.out.print(token + " ");
		}
		System.out.println(SYMBOL);
	}

}
