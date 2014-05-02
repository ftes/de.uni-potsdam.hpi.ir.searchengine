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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentId;
		result = prime * result + position;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermOccurrence other = (TermOccurrence) obj;
		if (documentId != other.documentId)
			return false;
		if (position != other.position)
			return false;
		return true;
	}	
}
