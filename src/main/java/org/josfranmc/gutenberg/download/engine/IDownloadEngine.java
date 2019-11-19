package org.josfranmc.gutenberg.download.engine;

import java.net.URL;

import org.josfranmc.gutenberg.util.GutenbergException;

/**
 * Defines the behavior that a class have to implement in order to have the ability to download resources on net.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 * @see IDownloadEngine
 * @see GutenbergException
 */
public interface IDownloadEngine {

	/**
	 * Runs a download.
	 * @return the result of the download in the form of a DownloadResult object
	 * @throws GutenbergException if there is any error downloading
	 * @see DownloadResult
	 */
	public DownloadResult download();
	
	/**
	 * Sets the resource of the url to download, in the form of a URL object
	 * @param resource url address
	 */
	public void setResource(URL resource);
    
	/**
	 * @return the resource of the url to download, in the form of a URL object
	 */
	public URL getResource();

	/**
	 * Sets the local path where to save the resource to be download
	 * @param savePath local path
	 */
	public void setSavePath(String savePath); 
	
	/**
	 * @return the local path where to save the resource to be download
	 */
	public String getSavePath();

}
