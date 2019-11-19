package org.josfranmc.gutenberg.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.junit.Test;

public class DownloadParamsTest {
	
	@Test
	public void savePathTest() {
		DownloadParams p = new DownloadParams();
		assertEquals("Valor Last-Modified incorrecto", p.getSavePath() + "zips" + System.getProperty("file.separator"), p.getZipsPath());

		p.setSavePath("test");
		assertEquals("Valor Last-Modified incorrecto", p.getSavePath() + "zips" + System.getProperty("file.separator"), p.getZipsPath());
	}
	
	@Test
	public void maxDownloadasTest() {
		DownloadParams p = new DownloadParams();
		p.setMaxFilesToDownload(0);
		assertEquals("Valor Last-Modified incorrecto", Integer.MAX_VALUE, p.getMaxFilesToDownload());
	}
	
	@Test
	public void engineTypeTest() {
		DownloadParams p = new DownloadParams();
		p.setEngineType(DownloadEngineType.HTTP_CONNECTION); 
		assertEquals("Engine type incorrecto", DownloadEngineType.HTTP_CONNECTION, p.getEngineType());
	}
	
	@Test
	public void overwriteTest() {
		DownloadParams p = new DownloadParams();
		p.setOverwrite(true);
		assertTrue(p.isOverwrite());
	}
	
	@Test
	public void unzipTest() {
		DownloadParams p = new DownloadParams();
		p.setUnzip(false);
		assertFalse(p.isUnzip());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void delayExceptionTest() {
		DownloadParams p = new DownloadParams();
		p.setDelay(-1);
	}
	
	@Test
	public void delayTest() {
		DownloadParams p = new DownloadParams();
		p.setDelay(5000);
		assertEquals("Delay incorrecto", 5000, p.getDelay());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fileTypeExceptionTest() {
		DownloadParams p = new DownloadParams();
		p.setFileType("doc");
	}
}
