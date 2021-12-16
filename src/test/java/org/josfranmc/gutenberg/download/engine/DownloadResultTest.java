package org.josfranmc.gutenberg.download.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DownloadResultTest {
	
	@Test
	public void ifNoHeaderTest() {
		DownloadResult r = new DownloadResult();
		assertEquals("Valor Last-Modified incorrecto", "", r.getLastModified());
		assertEquals("Valor Content-Type incorrecto", "", r.getContentType());
		assertEquals("Valor Content-Length incorrecto", "", r.getContentLength());
		assertEquals("Valor Content-Location incorrecto", "", r.getContentLocation());
	}
}
