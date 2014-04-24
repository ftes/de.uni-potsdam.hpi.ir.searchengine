package de.hpi.krestel.mySearchEngine;

/**
 * This represents a single occurence of a termin in a certain document.
 * The termoccurence is referenced in the Term.
 * 
 * @author Alexander
 *
 */
public class TermOccurrence {
	/**
	 * The ID of the document as specified in the XML
	 */
	private int documentId;
	/**
	 * The absolute position of the term in the document
	 */
	private int position;
	
	public TermOccurrence() {
	}
	public TermOccurrence(int documentId, int position) {
		super();
		this.documentId = documentId;
		this.position = position;
	}
	
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		return "TermOccurence [documentId=" + documentId + ", position="
				+ position + "]";
	}	
}
