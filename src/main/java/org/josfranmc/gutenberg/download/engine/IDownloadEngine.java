/*
 *  Copyright (C) 2018-2019 Jose Francisco Mena Ceca <josfranmc@gmail.com>
 *
 *  This file is part of JGutenbergDownload.
 *
 *  JGutenbergDownload is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JGutenbergDownload is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JGutenbergDownload.  If not, see <https://www.gnu.org/licenses/>. 
 */
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
