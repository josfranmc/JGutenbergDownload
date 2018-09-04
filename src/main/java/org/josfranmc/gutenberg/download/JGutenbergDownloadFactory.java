package org.josfranmc.gutenberg.download;

/**
 * Factoría que crea objetos de tipo <a>IGutenbergDownload</a>, los cuales implementan los servicios necesarios para la gestión y desarrollo 
 * del proceso de descarga de libros alojados por el proyecto Gutenberg.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see IGutenbergDownload
 * @see JGutenbergDownload
 */
public class JGutenbergDownloadFactory {

	/**
	 * Crea un objeto JGutenbergDownload que implementa toda la funcionalidad necesaria para la descarga de libros
	 * @return objeto del tipo IGutenbergDownload
	 * @see IGutenbergDownload
	 */
	public static IGutenbergDownload create() {
		return new JGutenbergDownload();
	}
}
