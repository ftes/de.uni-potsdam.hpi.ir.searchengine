package de.hpi.krestel.mySearchEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hpi.krestel.mySearchEngine.Util.Pair;

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

	private static final Pattern linkPattern = Pattern.compile("\\[\\[([^\\|\\]]*)\\|?([^\\]]*)?\\]\\]");
	/**
	 * Finds all links in the text
	 * @return A List of Pairs containing (linkText, linkTargetPageTitle)
	 */
	public List<Pair<String, String>> getLinks() {
		List<Pair<String, String>> links = new ArrayList<>();
		Matcher linkMatcher = linkPattern.matcher(text);
		while (linkMatcher.find()) {
			String linkText = linkMatcher.group(1);
			String linkTargetPageTitle = linkMatcher.group(2);
			if (linkTargetPageTitle.equals("")) {
				linkTargetPageTitle = linkText;
			}
			Pair<String, String> link = new Pair<String, String>(linkText, linkTargetPageTitle);
			links.add(link);
		}
		
		return links;
	}
}
