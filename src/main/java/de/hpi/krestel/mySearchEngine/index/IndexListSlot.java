package de.hpi.krestel.mySearchEngine.index;

/**
 * This represents a single occurence of a termin in a certain document.
 * The termoccurence is referenced in the Term.
 * 
 * @author Alexander
 *
 */
public abstract class IndexListSlot<V extends Comparable<V>> {
	/**
	 * The ID of the document as specified in the XML
	 */
	private int documentId;

	/**
	 * The value to store for that position.
	 */
	private V value;
	
	public IndexListSlot() {
	}
	
	public IndexListSlot(int documentId, V value) {
		super();
		this.documentId = documentId;
		this.value = value;
	}
	
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "TermOccurence [documentId=" + documentId + ", value="
				+ value + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentId;
		result = prime * result + value.hashCode();
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
		@SuppressWarnings("rawtypes")
		IndexListSlot other = (IndexListSlot) obj;
		if (documentId != other.documentId)
			return false;
		if (value != other.value)
			return false;
		return true;
	}	
}
