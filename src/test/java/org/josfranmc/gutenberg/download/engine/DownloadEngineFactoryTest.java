package org.josfranmc.gutenberg.download.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.josfranmc.gutenberg.download.engine.DownloadHttpUrlConnection;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;
import org.junit.Test;

public class DownloadEngineFactoryTest {
	
	@Test(expected=IllegalStateException.class)
	public void createObjectTest() {
		new DownloadEngineFactory();
	}
	
	/**
	 * Si se indica DownloadEngineType con valor null, entonces se debe crear un IDownloadEngineType de tipo DownloadEngineType.HTTP_CONNECTION
	 */
	@Test
	public void createEngineWithOnlyTypeParameter() {
		DownloadEngineType type = null;
		IDownloadEngine engine = DownloadEngineFactory.create(type);
		if (!(engine instanceof DownloadHttpUrlConnection)) {
			fail("No se ha creado el tipo de engine por defecto adecuado");
		}
		assertEquals("Directorio para guardar incorrecto", System.getProperty("user.dir"), engine.getSavePath());
	}
	
	/**
	 * Si se indican parámetros con DownloadEngineType, entonces se debe crear un IDownloadEngineType de tipo DownloadEngineType.HTTP_CONNECTION
	 */
	@Test
	public void createEngineWithoutParameters() {
		IDownloadEngine engine = DownloadEngineFactory.create();
		if (!(engine instanceof DownloadHttpUrlConnection)) {
			fail("No se ha creado el tipo de engine por defecto adecuado");
		}
		assertEquals("Directorio para guardar incorrecto", System.getProperty("user.dir"), engine.getSavePath());
	}
	
	/**
	 * Si se indica solo el parámetro de la url, entonces se debe crear un IDownloadEngineType de tipo DownloadEngineType.HTTP_CONNECTION
	 */
	@Test
	public void createEngineWithOnlyTargetParameter() {
		IDownloadEngine engine = null;
		try {
			engine = DownloadEngineFactory.create(new URL("http://url_test"));
			if (!(engine instanceof DownloadHttpUrlConnection)) {
				fail("No se ha creado el tipo de engine por defecto adecuado");
			}
			assertEquals("Directorio para guardar incorrecto", System.getProperty("user.dir"), engine.getSavePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Si solo se indican los parámtros de la url y carpeta de destino, entonces se debe crear un IDownloadEngineType de tipo DownloadEngineType.HTTP_CONNECTION
	 */
	@Test
	public void createEngineWithTargetAndPathParameters() {
		IDownloadEngine engine = null;
		try {
			engine = DownloadEngineFactory.create(new URL("http://url_test"), "path_test");
			if (!(engine instanceof DownloadHttpUrlConnection)) {
				fail("No se ha creado el tipo de engine por defecto adecuado");
			}			
			assertEquals("Directorio para guardar incorrecto", "path_test", engine.getSavePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Si se indican los parámtros de la url y tipo de motor, la carpeta para guardar será aquella desde donde se ejecute el programa
	 */
	@Test
	public void createEngineWithTargetAndTypeParameters() {
		IDownloadEngine engine = null;
		try {
			engine = DownloadEngineFactory.create(new URL("http://url_test"), DownloadEngineType.HTTP_CONNECTION);
			if (!(engine instanceof DownloadHttpUrlConnection)) {
				fail("No se ha creado el tipo de engine por defecto adecuado");
			}			
			assertEquals("Directorio para guardar incorrecto", System.getProperty("user.dir"), engine.getSavePath());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createEngineWithAllParameters() {
		IDownloadEngine engine = null;
		try {
			engine = DownloadEngineFactory.create(new URL("http://url_test"), "path_test", DownloadEngineType.HTTP_CONNECTION);
			if (!(engine instanceof DownloadHttpUrlConnection)) {
				fail("No se ha creado el tipo de engine por defecto adecuado");
			}			
			assertEquals("Directorio para guardar incorrecto", "path_test", engine.getSavePath());
			assertEquals("Url de descarga incorrecta", "http://url_test", engine.getResource().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
