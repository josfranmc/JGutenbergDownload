package org.josfranmc.gutenberg.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.junit.Test;

/**
 * Clase que implementa los test para probar los métodos de la clase JGutenbergDownload
 * @author Jose Francisco Mena Ceca
 * @version 2.1
 */
public class JGutenbergDownloadTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * Comprueba los valores por defecto de la aplicación
	 */
	@Test
	public void parametersDefaultValueTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		assertEquals("Parámetro fileType valor por defecto no válido", "txt", jg.getFileType());
		assertEquals("Parámetro language valor por defecto no válido", "es", jg.getLanguage());
		assertEquals("Parámetro delay valor por defecto no válido", 2000, jg.getDelay());
		assertEquals("Parámetro engineType valor por defecto no válido", DownloadEngineType.HTTP_CONNECTION, jg.getEngineType());
		assertEquals("Parámetro maxFiles valor por defecto no válido", 10, jg.getMaxFilesToDownload());
		assertEquals("Parámetro savePath valor por defecto no válido", System.getProperty("user.dir") + FILE_SEPARATOR + "books" + FILE_SEPARATOR, jg.getSavePath());
		assertEquals("Parámetro urlBase valor por defecto no válido", "https://www.gutenberg.org/robot/harvest?filetypes[]=txt&langs[]=es", jg.getUrlBase());
		assertEquals("Parámetro unzip valor por defecto no válido", true, jg.isUnzip());
		assertEquals("Parámetro unzip valor por defecto no válido", false, jg.isOverwrite());
	}
	

	@Test
	public void getParametersTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		assertNotNull(jg.getParameters());
	}
	
	@Test
	public void setParametersTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		DownloadParams dp = new DownloadParams();
		dp.setOverwrite(true);
		jg.setParameters(dp);
		assertTrue(jg.isOverwrite());
	}
	
	/**
	 * Si el parámetro fileType es un valor no válido, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=GutenbergException.class)
	public void givenFileTypeWhenWrongValueThenIllegalArgumentException() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setFileType("bad");
	}	
	
	/**
	 * Si la ruta indicada para guardar las descargas es null, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=GutenbergException.class)
	public void givenSavePathWhenNullThenIllegalArgumentException() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath(null);
	}
	
	@Test
	public void savePathTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("books");
		assertEquals("Ruta no correcta [1]", "books" + FILE_SEPARATOR, jg.getSavePath());
		jg.setSavePath("temp\\");
		assertEquals("Ruta no correcta [2]", "temp" + FILE_SEPARATOR, jg.getSavePath());
	}
	
	@Test
	public void givenLanguageThenUrlTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setLanguage("en");
		assertEquals("URL no correcta", DownloadParams.URL_BOOKS + "harvest?filetypes[]=" + jg.getFileType() + "&langs[]=" + jg.getLanguage(), jg.getUrlBase());
	}
	
	@Test
	public void givenFileTypeThenUrlTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setFileType("html");
		assertEquals("URL no correcta", DownloadParams.URL_BOOKS + "harvest?filetypes[]=" + jg.getFileType() + "&langs[]=" + jg.getLanguage(), jg.getUrlBase());
	}
	
	/**
	 * Si el parámetro MaxFilesToDownload es menor de cero, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=GutenbergException.class)
	public void givenMaxFilesToDownloadWhenMinusZeroThenIllegalArgumentException() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setMaxFilesToDownload(-1);
	}
	
	@Test
	public void downloadOneBookTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload");
		jg.setDelay(0);
		jg.setMaxFilesToDownload(1);
		jg.downloadBooks();
		
		File dirZip = new File(jg.getSavePath() + "zips");
		assertEquals("Número de archivos descargados incorrectos", 2, dirZip.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}
	
	@Test
	public void downloadOneBookWithMainMethodTest() {
		String [] args = {"-o", "-z", "-d", "0", "-m", "1", "-s", "testdownload2"};
		
		JGutenbergDownload.main(args); 
		
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload2");
		jg.setDelay(0);
		jg.setMaxFilesToDownload(1);
		
		File dirZip = new File(jg.getSavePath() + "zips");
		assertEquals("Número de archivos descargados incorrectos [2]", 2, dirZip.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}
	
	@Test(expected=GutenbergException.class)
	public void wrongParameterskWithMainMethodTest() {
		String [] args = {"-f", "html","-d", "0", "-l", "en", "-w", "1", "-s", "testdownload3"};
		
		JGutenbergDownload.main(args); 
		
		File dirSave = new File("testdownload3");
		assertTrue(!dirSave.exists());
	}
	/*
	@Test(expected=GutenbergException.class)
	public void overwriteParameterWithMainMethodTest() {
		String [] args = {"-d", "0", "-o", "true", "-s", "testdownload6"};
		
		JGutenbergDownload.main(args); 
		
		File dirSave = new File("testdownload6");
		assertTrue(!dirSave.exists());
	}	*/
	
	@Test
	public void downloadTwoPagesTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testpages");
		jg.setDelay(0);
		jg.setMaxFilesToDownload(110);
		jg.downloadBooks();
		
		deleteDownloadedFiles(jg);
		assertTrue(true);
	}
	
	@Test
	public void noUnzipTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload3");
		jg.setDelay(0);
		jg.setUnzip(false);
		jg.setMaxFilesToDownload(1);
		jg.downloadBooks();
		
		File dir = new File(jg.getSavePath());
		assertEquals("Número de archivos descargados incorrectos [3]", 1, dir.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}

	@Test
	public void pageWithNoLinksTest() {
		DownloadParams dp = new DownloadParams();
		dp.setDelay(0);
		dp.setMaxFilesToDownload(1);
		dp.setUnzip(true);
		dp.setUrl("http://www.gutenberg.org/robot/");
			
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setParameters(dp);
		jg.downloadBooks();	
		
		File dir = new File(jg.getSavePath());
		assertEquals("Número de archivos descargados incorrectos [4]", 1, dir.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}
	
	@Test
	public void downloadWithDelayTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload4");
		jg.setDelay(1000);
		jg.setMaxFilesToDownload(2);
		jg.downloadBooks();
		
		File dirZip = new File(jg.getSavePath() + "zips");
		assertEquals("Número de archivos descargados incorrectos [5]", 3, dirZip.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}	
	
	@Test
	public void overWriteTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload4");
		jg.setDelay(1000);
		jg.setMaxFilesToDownload(2);
		jg.downloadBooks();
		
		File dirZip = new File(jg.getSavePath() + "zips");
		assertEquals("Número de archivos descargados incorrectos [5]", 3, dirZip.listFiles().length);
		
		deleteDownloadedFiles(jg);
	}		
	
	@Test
	public void showHelpWithMainMethodTest() {
		String [] args = {"-h"};
		JGutenbergDownload.main(args);
		assertTrue(true);
	}

	@Test(expected=GutenbergException.class)
	public void wrongUrlTest() {
		JGutenbergDownload jg = new JGutenbergDownload();
		jg.setSavePath("testdownload5");
		jg.setLanguage("http://");
		jg.setDelay(0);
		jg.setUnzip(false);
		jg.setMaxFilesToDownload(1);
		try {
			jg.downloadBooks();
		} catch (GutenbergException e) {
			File dir = new File(jg.getSavePath() + "zips");
			dir.delete();
			dir = new File(jg.getSavePath());
			dir.delete();
			throw e;
		}
	}
	
	private void deleteDownloadedFiles(JGutenbergDownload jg) {
		String saveDir = jg.getSavePath();
		String pageWithLinks = "harvest_filetypes[]=" + jg.getFileType() + "&langs[]=" + jg.getLanguage();
		
		File page = new File(saveDir + "zips" + FILE_SEPARATOR + pageWithLinks);
		assertTrue(page.exists());
		
		File dirZip = new File(saveDir + "zips");
		File[] filesInZipDirectory = dirZip.listFiles();
		for(File f : filesInZipDirectory) {
			if(f.exists()) {
				f.delete();
			}
		}
		dirZip.delete();
		
		File dirSave = new File(saveDir);
		File[] filesInSaveDirectory = dirSave.listFiles();
		for(File f : filesInSaveDirectory) {
			if(f.exists()) {
				f.delete();
			}
		}
		dirSave.delete();
	}	
}