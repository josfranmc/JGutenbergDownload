package org.josfranmc.gutenberg.download.engine;

import java.net.URL;

/**
 * Define el comportamiento que tiene que ofrecer toda clase que quiera implementar la capacidad de descargar
 * un recurso identificado por una URL
 * 
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public interface IDownloadEngine {

	/**
	 * Ejecuta el proceso de descarga.
	 * @return el resultado de la descarga encapsulado en un objeto de tipo DownloadResult
	 * @see DownloadResult
	 */
	public DownloadResult download();
	
	/**
	 * Establece la dirección del recurso a descargar, la cual se maneja como un objeto de tipo URL
	 * @param resource dirección del recurso que se quiere obtener
	 */
	public void setResource(URL resource);
    
	/**
	 * Obtiene la dirección del recurso a descargar como un objeto de tipo URL
	 * @return dirección del recurso a descargar
	 */
	public URL getResource();

	/**
	 * Establece la ruta donde guardar el recurso a descargar
	 * @param savePath ruta donde guardar
	 */
	public void setSavePath(String savePath); 
	
	/**
	 * Obtiene la ruta donde guardar el recursos a descargar
	 * @return ruta donde guardar
	 */
	public String getSavePath();

}
