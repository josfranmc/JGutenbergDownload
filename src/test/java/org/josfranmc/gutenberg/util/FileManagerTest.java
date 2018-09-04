package org.josfranmc.gutenberg.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class FileManagerTest {
	
	/**
	 * Comprobar si es correcta la ruta que debe tener en el equipo local un fichero que va a descargarse en dicho equipo.
	 */
	@Test
	public void testGetLocalFilePathFromURL() {
		String pathToSave = System.getProperty("user.dir");
		String resource = "10002.zip";
		String urlTest = "http://aleph.gutenberg.org/1/0/0/0/10002/";
		String path = FileManager.getLocalFilePathFromURL(pathToSave, urlTest.concat(resource));
		String expected = pathToSave.concat(System.getProperty("file.separator")).concat(resource);
		assertEquals("Ruta inválida", expected, path);
	}
	
	/**
	 * Comprobar si es correcto el nombre del archivo contenido en una ruta.
	 */
	@Test
	public void testGetLocalFileName() {
		String file = FileManager.getLocalFileName("C:\\text\\book.txt");
		String expected = "book.txt";
		assertEquals("Ruta inválida", expected, file);
		
		file = FileManager.getLocalFileName("C:\\text\\book?one&amp;two.txt");
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
	}
	
}
