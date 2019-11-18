package org.josfranmc.gutenberg.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadHttpUrlConnection;
import org.josfranmc.gutenberg.download.engine.DownloadResult;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;
import org.junit.Test;

public class DownloadHttpUrlConnectionTest {

	/**
	 * Si la ruta donde guardar las descargas no se ha especificado, entonces la carpeta de descarga debe ser la carpeta de ejecución del programa.
	 */
	@Test
	public void givenSavePathWhenNullThenReturnUserDir() {
		IDownloadEngine dhc = DownloadHttpUrlConnection.newInstance(null, null);
		String savePath = dhc.getSavePath();
		String expectedSavePath = System.getProperty("user.dir");
		assertNotNull("Ruta donde guardar descargas es null", savePath);
		assertEquals("Ruta donde guardar descargas diferente a ruta de ejecución", expectedSavePath, savePath);
	}
	
	/**
	 * Si los parámetros de descarga (ruta del recurso a descargar y ruta donde guardar la descarga) son nulos, entonces se devuelve un objeto
	 * resultado (DownloadResult) con valores a null.
	 */
	@Test
	public void givenDownloadParametersWhenNullThenDownloadResultShoulbBeNotNull() {
		DownloadResult dr = DownloadHttpUrlConnection.newInstance(null, null).download();
		assertNotNull("No se ha obtenido objeto DownloadResult", dr);
		assertNull("El campo de cabeceras obtenidas no es null", dr.getHeaders());
		assertNull("La ruta del fichero descargado no es null", dr.getFileOutputPath());
	}
	
	/**
	 * Si se indica una dirección de descarga errónea, entonces la ruta donde se ha guardado la descarga debe ser null (en el objeto DownloadResult).
	 */
	@Test
	public void givenUrlWhenWrongAddressThenFileOutputPathShoulbBeNull() {
		try {
			DownloadResult dr = DownloadHttpUrlConnection.newInstance(new URL("http://www.qsctyhu.com/fake.img"), null).download();
			assertNull("La ruta del recurso descargado no es null", dr.getFileOutputPath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Comprobar que se ha descargado un fichero concreto en la carpeta de ejecución del programa.
	 */
	@Test
	public void testDownload() {
		try {
			URL url = new URL("http://aleph.gutenberg.org/1/0/0/0/10002/10002.zip");
			DownloadHttpUrlConnection.newInstance(url, null).download();
			File f = new File(System.getProperty("user.dir").concat(System.getProperty("file.separator")).concat("10002.zip"));
			assertTrue("No se ha obtenido el recursos en la carpeta esperada", f.exists());
			f.delete();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
