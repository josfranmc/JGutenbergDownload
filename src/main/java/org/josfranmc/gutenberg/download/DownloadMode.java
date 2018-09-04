package org.josfranmc.gutenberg.download;

/**
 * Tipos de descarga.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public enum DownloadMode {
	/**
	 * Descarga normal (lenta)
	 */
	SOFT, 
	
	/**
	 * Descarga concurrente de recursos (más rápida)
	 */
	GREEDY;
}