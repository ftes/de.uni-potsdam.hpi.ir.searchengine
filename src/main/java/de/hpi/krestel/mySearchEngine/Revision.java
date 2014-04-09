package de.hpi.krestel.mySearchEngine;

/**
 * Represents a revision of a Page
 * @author Alexander
 *
 */
public class Revision {
	private int id;
	private int parentid;
	private String timestamp;
	private String contributor; // TODO: extend
	private String text;
	private String sha1;
	private String model;
	private String format;
	
	public Revision() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "[Revision] id = " + id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
