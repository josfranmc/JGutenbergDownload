package org.josfranmc.gutenberg.engine;

import static org.junit.Assert.fail;

import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.josfranmc.gutenberg.download.engine.DownloadHttpUrlConnection;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;
import org.junit.Test;

public class DownloadEngineFactoryTest {
	
	/**
	 * Si se indica DownloadEngineType con valor null, entonces se debe crear un IDownloadEngineType de tipo DownloadEngineType.HTTP_CONNECTION
	 */
	@Test
	public void givenUrlWhenWrongAddressThenFileOutputPathShoulbBeNull() {
		DownloadEngineType type = null;
		IDownloadEngine engine = DownloadEngineFactory.create(type);
		if (!(engine instanceof DownloadHttpUrlConnection)) {
			fail("No se ha creado el tipo de engine por defecto adecuado");
		}
	}
}
