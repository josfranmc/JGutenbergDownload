package org.josfranmc.gutenberg.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

public class FileManagerTest {
	
	@Test(expected=IllegalStateException.class)
	public void createObjectTest() {
		new FileManager();
	}
	
	@Test
	public void fileExistsTest() {
		File zipFolder = null;
		try {
			zipFolder = new File(FileManagerTest.class.getResource("/zips").toURI());
			assertTrue(FileManager.fileExists(zipFolder.toString(), "http://aleph.gutenberg.org/1/0/2/9/10293/10293-8.zip"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Comprobar si es correcta la ruta que debe tener en el equipo local un fichero que va a descargarse en dicho equipo.
	 */
	@Test
	public void getLocalFilePathFromURLTest() {
		String pathToSave = System.getProperty("user.dir");
		String resource = "10002.zip";
		String urlTest = "http://aleph.gutenberg.org/1/0/0/0/10002/";
		
		String path = FileManager.getLocalFilePathFromURL(pathToSave, urlTest + resource);
		String expected = pathToSave + System.getProperty("file.separator") + resource;
		assertEquals("Ruta inválida", expected, path);
	}
	
	/**
	 * Comprobar si es correcto el nombre del archivo contenido en una ruta.
	 */
	@Test
	public void getLocalFileNameTest() {
		String testCad = "test" + System.getProperty("file.separator") + "book.txt";
		String file = FileManager.getLocalFileName(testCad);
		String expected = "book.txt";
		assertEquals("Ruta inválida", expected, file);
		
		testCad = "test" + System.getProperty("file.separator") + "book?one&amp;two.txt";
		file = FileManager.getLocalFileName(testCad);
		expected = "book_one&two.txt";
		assertEquals("Ruta inválida", expected, file);
	}
	
	/**
	 * Si la ruta del directorio donde se encuentran los ficheros zip o la ruta del directorio en el que descomprimir los ficheros son null,
	 * entonces el método FileManager.unzipFiles no debe lanzar NullPointerException
	 */
	@Test
	public void givenInputPathAndOutputPathWhenNullThenAvoidNullPointerException() {
		try {
			FileManager.unzipFiles(null, null);
		} catch (NullPointerException e) {
			fail("Las rutas de los directorios son null");
		}
		
		try {
			FileManager.unzipFiles("zips", null);
		} catch (NullPointerException e) {
			fail("Las rutas de los directorios son null");
		}
	}
	
	@Test
	public void unzipFilesTest() {
		try {
			File zipFolder = new File(FileManagerTest.class.getResource("/zips").toURI());
			File unzipFolder = new File(FileManagerTest.class.getResource("/zips").toURI());
			FileManager.unzipFiles(zipFolder.toString(), unzipFolder.toString());
			
			File unzipFile = new File(FileManagerTest.class.getResource("/zips/10293-8.txt").toURI());
			assertTrue(unzipFile.exists());
			unzipFile.delete();
			
			unzipFile = new File(FileManagerTest.class.getResource("/zips/10506-8.txt").toURI());
			assertTrue(unzipFile.exists());
			unzipFile.delete();
		} catch (NullPointerException e) {
			fail("Las rutas de los directorios son null");
		} catch (URISyntaxException e) {	
			fail("URISyntaxException");
		}
	}
}
