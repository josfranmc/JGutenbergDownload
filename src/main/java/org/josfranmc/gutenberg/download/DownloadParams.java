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
package org.josfranmc.gutenberg.download;

import java.net.MalformedURLException;
import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;

/**
 * Parameters used in the application. They are:
 * <ul>
 * <li><b>fileType</b>: the type of files to download (default <i>txt</i>)</li>
 * <li><b>language</b>: the language of files to download (default <i>es</i>)</li>
 * <li><b>urlBase</b>: initial download url</li>
 * <li><b>savePath</b>: path where to get downloaded resources (default <code>System.getProperty("user.dir")</code>)</li>
 * <li><b>overwrite</b>: if the existing files must be overwritten by the downloaded ones (default <i>false</i>)</li>
 * <li><b>delay</b>: delay between downloads, in milliseconds (default 2000)</li>
 * <li><b>unzip</b>: if downloaded files must be unzipping (default <i>true</i>)</li>
 * <li><b>maxFilesToDownload</b>: the maximum number of files to download (default 10, 0 for download all existing)</li>
 * <li><b>engineType</b>: the type of downloading engine to use (default <code>DownloadEngineType.HTTP_CONNECTION</code>)</li>
 * </ul>
 * @author Jose Francisco Mena Ceca
 * @version 2.0.2
 * @see DownloadEngineType
 * @see JGutenbergDownload
 */
public class DownloadParams {

	/**
	 * Base URL of the Gutenberg project to download
	 */
	public static final String URL_BOOKS = "https://www.gutenberg.org/robot/";
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator"); 
	
	private String fileType;
	
	private String language;
	
	private URL urlBase;
	
	private String savePath;
	
	private String zipsPath;
	
	private boolean overwrite;

	private int delay;
	
	private boolean unzip;

	private int maxFilesToDownload;
	
	private DownloadEngineType engineType;
	

	/**
	 * Main constructor. It initializes the object with default values:
	 * <ul>
	 * <li><b>fileType</b>: <i>txt</i></li>
	 * <li><b>language</b>: <i>es</i></li>
	 * <li><b>savePath</b>: <code>System.getProperty("user.dir")</code></li>
	 * <li><b>overwrite</b>: <i>false</i></li>
	 * <li><b>delay</b>: 2000 milliseconds</li>
	 * <li><b>unzip</b>: <i>true</i></li>
	 * <li><b>maxFilesToDownload</b>: 10 (0 for download all existing)</li>
	 * <li><b>engineType</b>: <code>DownloadEngineType.HTTP_CONNECTION</code></li>
	 * </ul>
	 */
	public DownloadParams() { 
		fileType = "txt";
		language = "es";
		buildHarvestURL();
		savePath = System.getProperty("user.dir") + FILE_SEPARATOR + "books" + FILE_SEPARATOR;
		setZipsPath();
		overwrite = false;
		delay = 2000;
		unzip = true;
		maxFilesToDownload = 10;
		engineType = DownloadEngineType.HTTP_CONNECTION;
	}

	
	/**
	 * @return the type of files to download
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Sets the type of file to download. Valid types: txt, epub, html
	 * @param fileType type of file to download
	 * @throws GutenbergException if an invalid file type is indicated
	 */
	public void setFileType(String fileType) {
		if (!fileType.equals("txt") && !fileType.equals("epub") && !fileType.equals("html")) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Wrong file type parameter");
		}
		this.fileType = fileType;
		buildHarvestURL();
	}

	/**
	 * @return the language of files to download
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language of the files to download acording to Gutenberg nomeclature.<br>
	 * Ej.: <i>es</i> - spanish, <i>en</i> - english, <i>fr</i> - french
	 * @param language type of language
	 */
	public void setLanguage(String language) {
		this.language = language;
		buildHarvestURL();
	}
	
	/**
	 * @return initial download url
	 */
	public URL getUrlBase() {
		return urlBase;
	}

	/**
	 * Sets initial download url
	 * @param urlBase url address
	 */
	private void setUrlBase(URL urlBase) {
		this.urlBase = urlBase;
	}

	/**
	 * @return folder path where to get downloaded resources
	 */
	public String getSavePath() {
		return savePath;
	}

	/**
	 * Sets folder path where to get downloaded resources
	 * @param savePath folder path where to get downloaded resources
	 * @throws GutenbergException if the parameter is null
	 */
	public void setSavePath(String savePath) {
		if (savePath == null) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: The directory path where to save downloads cannot be null");
		}
		String path = (!savePath.endsWith(FILE_SEPARATOR)) ? savePath.concat(FILE_SEPARATOR) : savePath;
		this.savePath = path;
		setZipsPath();
	}
	
	/**
	 * @return folder path where to get downloaded zips
	 */
	public String getZipsPath() {
		return zipsPath;
	}
	
	private void setZipsPath() {
		this.zipsPath = savePath + "zips" + FILE_SEPARATOR;
	}
	
	/**
	 * @return <i>true</i> if the existing files must be overwritten by the downloaded ones, <i>false</i> otherwise
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Sets if the existing files must be overwritten by the downloaded ones
	 * @param overwrite <i>true</i> if the existing files must be overwritten, <i>false</i> otherwise
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * @return delay between downloads, in milliseconds
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Sets delay between downloads
	 * @param delay waiting time, in milliseconds
	 * @throws GutenbergException if a number less than zero is indicated
	 */
	public void setDelay(int delay) {
		if (delay < 0) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Delay parameter cannot be less than zero");
		}
		this.delay = delay;
	}

	/**
	 * @return <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public boolean isUnzip() {
		return unzip;
	}

	/**
	 * Sets if downloaded files must be unzipping,
	 * @param unzip <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public void setUnzip(boolean unzip) {
		this.unzip = unzip;
	}

	/**
	 * @return the maximum number of files to download
	 */
	public int getMaxFilesToDownload() {
		return maxFilesToDownload;
	}

	/**
	 * Sets the maximum number of files to download. The zero indicates downloading all available files.
	 * @param maxFilesToDownload files number
	 * @throws GutenbergException if a number less than zero is indicated
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		if (maxFilesToDownload < 0) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: MaxFilesToDownload parameter cannot be less than zero");
		}
		this.maxFilesToDownload = (maxFilesToDownload == 0) ? Integer.MAX_VALUE : maxFilesToDownload;
	}

	/**
	 * @return the type of downloading engine to use
	 * @see DownloadEngineType
	 */
	public DownloadEngineType getEngineType() {
		return engineType;
	}

	/**
	 * Sets the type of downloading engine to use
	 * @param engineType engine type
	 * @see DownloadEngineType
	 */
	public void setEngineType(DownloadEngineType engineType) {
		this.engineType = engineType;
	}
	
	/**
	 * Build the main url from which to download the books
	 */
	private void buildHarvestURL() {	
		String queryParams = "harvest?filetypes[]=" + getFileType() + "&langs[]=" + getLanguage();
		String url = URL_BOOKS + queryParams;
		try {
			setUrlBase(new URL(url));
		} catch (MalformedURLException e) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Wrong base URL: " + url);
		} catch (Exception e) {
			throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Cannot create base URL: " + url);
		}
	}
}
