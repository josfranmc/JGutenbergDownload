package org.josfranmc.gutenberg.download;

import static org.junit.Assert.assertEquals;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.junit.Test;

/**
 * Clase que implementa los test para probar los métodos de la clase JGutenbergDownloadTest
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public class JGutenbergDownloadTest {


	/**
	 * Comprueba si los valores por defecto de los parámetros de descarga a la hora de crear un IGutenbergDownload son los correctos
	 */
	@Test
	public void testParametersDefaultValue() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		assertEquals("Parámetro delay valor por defecto no válido", 2000, jg.getDelay());
		assertEquals("Parámetro engineType valor por defecto no válido", DownloadEngineType.HTTP_CONNECTION, jg.getEngineType());
		assertEquals("Parámetro maxFiles valor por defecto no válido", 10, jg.getMaxFilesToDownload());
		assertEquals("Parámetro savePath valor por defecto no válido", System.getProperty("user.dir").concat(System.getProperty("file.separator")), jg.getSavePath());
		assertEquals("Parámetro urlBase valor por defecto no válido", "No se ha establecido URL.", jg.getUrlBase());
		assertEquals("Parámetro unzip valor por defecto no válido", true, jg.isUnzip());
		assertEquals("Parámetro downloadMode valor por defecto no válido", DownloadMode.SOFT, jg.getDownloadMode());
	}
	
	/**
	 * Si no se ha indicado tipo de fichero e idioma para realizar las descargas, entonces lanzar excepción IllegalArgumentException y mostrar mensaje
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenUrlParametersWhenNullThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.downloadBooks();
	}

	/**
	 * Si no se ha indicado tipo de fichero e idioma para realizar las descargas, entonces lanzar excepción IllegalArgumentException y mostrar mensaje
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenUrlParametersWhenEmptyThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.setFileType("").setLanguage("");
		jg.downloadBooks();
	}
	
	/**
	 * Si la ruta indicada para guardar las descargas es null, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenSavePathWhenNullThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.setSavePath(null);
	}
	
	/**
	 * Si la ruta indicada para guardar las descargas no existe, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenSavePathWhenNoExistsThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.setSavePath("C:\\xopakeuj\\sfukeo");
	}
	
	/**
	 * Si el parámetro MaxFilesToDownload es menor de cero, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenMaxFilesToDownloadWhenMinusZeroThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.setMaxFilesToDownload(-1);
	}
	
	/**
	 * Si el parámetro DownloadMode es null, entonces lanzar excepción IllegalArgumentException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void givenDownloadModeWhenNullThenIllegalArgumentException() {
		IGutenbergDownload jg = JGutenbergDownloadFactory.create();
		jg.setDownloadMode(null);
	}	
}