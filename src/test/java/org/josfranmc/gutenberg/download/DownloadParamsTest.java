package org.josfranmc.gutenberg.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.junit.Test;

/**
 * Clase que implementa los test para probar los métodos de la clase DownloadParams
 * @author Jose Francisco Mena Ceca
 * @version 2.1
 */
public class DownloadParamsTest {
	
	@Test
	public void savePathTest() {
		DownloadParams p = new DownloadParams();
		assertEquals("Valor Last-Modified incorrecto", p.getSavePath() + "zips" + System.getProperty("file.separator"), p.getZipsPath());

		p.setSavePath("test");
		assertEquals("Valor Last-Modified incorrecto", p.getSavePath() + "zips" + System.getProperty("file.separator"), p.getZipsPath());
	}
	
	@Test(expected=GutenbergException.class)
	public void maxDownloadsTest() {
		DownloadParams p = new DownloadParams();
		p.setMaxFilesToDownload(0);
		assertEquals("Valor Last-Modified incorrecto", Integer.MAX_VALUE, p.getMaxFilesToDownload());

		p.setMaxFilesToDownload(-1);
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
	
	@Test(expected=GutenbergException.class)
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
	
	@Test(expected=GutenbergException.class)
	public void fileTypeExceptionTest() {
		DownloadParams p = new DownloadParams();
		p.setFileType("doc");
	}
	
	@Test(expected=GutenbergException.class)
	public void urlExceptionTest() {
		DownloadParams p = new DownloadParams();
		p.setUrl("fail://www.never.com");
	}
}
