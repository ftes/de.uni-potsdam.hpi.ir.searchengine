package de.hpi.krestel.mySearchEngine;

import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class RedirectIgnoreTest {
	@Test
	public void test() {
		Pattern redirect = Pattern.compile("\\A#REDIRECT \\[\\[.*\\]\\]\\z");
		assertTrue(redirect.matcher("#REDIRECT [[Anschluss (Soziologie)]]").matches());
	}
}
