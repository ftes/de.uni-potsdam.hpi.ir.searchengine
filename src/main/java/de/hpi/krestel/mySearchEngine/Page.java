package de.hpi.krestel.mySearchEngine;

/**
 * Representation of a MediaWiki Page
 * @author Alexander
 *
 */
public class Page {
	private String title;
	private int id;
	private String text;
	
	public String getText() {
		return text;
	}
	
	public String getCleanText() {
		String text = getText();
		// remove html escape sequencies
		text = text.replaceAll("&\\w*?;", " ");
		//remove html tags
		text = text.replaceAll("(<.*?>)", ""); 
		// remove non word characters
		text = text.replaceAll("[^a-zA-ZäöüÄÖÜ0-9.,!:()-]+", " "); 
		// remove unnecessary whitespace
		text = text.replaceAll("\\s+", " "); 
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Page() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "[Page] id=" + id + "; title=\"" + title + "\""
				+ "; text=\"" + text + "\"";
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

}
