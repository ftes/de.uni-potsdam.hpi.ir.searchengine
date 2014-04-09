package de.hpi.krestel.mySearchEngine;

/**
 * Representation of a MediaWiki Page
 * @author Alexander
 *
 */
public class Page {
	private String title;
	private int id;
	private Revision revision;
	
	public Page() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "[Page] id=" + id + "; title=\"" + title + "\"";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Revision getRevision() {
		return revision;
	}

	public void setRevision(Revision revision) {
		this.revision = revision;
	}

}
