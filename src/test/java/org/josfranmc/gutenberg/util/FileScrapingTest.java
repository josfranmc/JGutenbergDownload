package org.josfranmc.gutenberg.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.junit.Test;

public class FileScrapingTest {
	
	@Test(expected=IllegalStateException.class)
	public void createObjectTest() {
		new FileScraping();
	}
	
	@Test
	public void getLinksWhenPathIsNull() {
		List<String> links = FileScraping.getLinks(null);
		assertTrue(links.isEmpty());
	}
	
	@Test
	public void getLinksTest() {
		URL url = FileScrapingTest.class.getClassLoader().getResource("links_to_download.html");
		List<String> links = FileScraping.getLinks(url.getPath().substring(1));
		assertTrue(!links.isEmpty());
		assertEquals("Link incorrecto", "http://aleph.gutenberg.org/1/0/2/9/10293/10293-8.zip", links.get(0));
	}
}
