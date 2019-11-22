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
 *    
 *  This file includes software developed at
 *  The Apache Software Foundation (http://www.apache.org/). 
 */
package org.josfranmc.gutenberg.download;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * It allows to download books from the Gutenberg project repositories.<br>
 * (<a href="http://www.gutenberg.org/">http://www.gutenberg.org</a>)
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 * @see DownloadParams
 * @see DownloadEngineType
 * @see GutenbergException
 */
public class JGutenbergDownload {
	
	private static final Logger log = Logger.getLogger(JGutenbergDownload.class);

	private DownloadParams parameters;	
	
	
	/**
	 * Main constructor. It initializes the application with default values.
	 * @see DownloadParams
	 */
	public JGutenbergDownload() {
		parameters = new DownloadParams();
	}

	/**
	 * @return an object with the current parameters
	 * @see DownloadParams
	 */
	public DownloadParams getParameters() {
		return parameters;
	}
	
	/**
	 * Sets the application parameters through a <code>DownloadParams</code> object
	 * @param parameters application parameters
	 * @see DownloadParams
	 */
	public void setParameters(DownloadParams parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Begins the process of downloading books.<p>
	 * If there is any problem It can be thrown <code>GutenbergException</code>
	 * @throws GutenbergException
	 * @see GutenbergException
	 */
	public void downloadBooks() {
		printParameters();
		createDirectoriesForDownloads();
		log.info("BEGIN BOOKS DOWNLOAD " + getCurrentTime());
		DownloadBooks downloader = new DownloadBooks(parameters);
		log.info("Downloading...");
		downloader.executeDownload();
		if (parameters.isUnzip()) {
			log.info("Unzipping files... ");
			FileManager.unzipFiles(parameters.getZipsPath(), parameters.getSavePath());
		}
		log.info("END BOOKS DOWNLOAD " + getCurrentTime());
	}

	private void createDirectoriesForDownloads() {
		createBaseDirectory();
		createZipsDirectory();
	}
	
	private void createBaseDirectory() {
		File dirPath = new File(parameters.getSavePath());
		if (!dirPath.exists()) {
			log.warn("Path doesn't exist. Creating new directory.");
			dirPath.mkdirs();
			if (!dirPath.exists()) {
				throw new IllegalStateException("Cannot create directory for downloads");
			}
		}
	}
	
	private void createZipsDirectory() {
		File dirPath = new File(parameters.getZipsPath());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
			if (!dirPath.exists()) {
				throw new IllegalStateException("Cannot create directory for zips");	
			}
		}
	}

	/**
	 * @return the type of files to download
	 */
	public String getFileType() {
		return parameters.getFileType();
	}

	/**
	 * Sets the type of file to download. Valid types: txt, epub, html
	 * @param fileType type of file to download
	 * @throws IllegalArgumentException if an invalid file type is indicated
	 */
	public void setFileType(String fileType) {
		parameters.setFileType(fileType);
	}

	/**
	 * @return the language of files to download
	 */
	public String getLanguage() {
		return parameters.getLanguage();
	}

	/**
	 * Sets the language of the files to download acording to Gutenberg nomeclature.<br>
	 * Ej.: <i>es</i> - spanish, <i>en</i> - english, <i>fr</i> - french
	 * @param language type of language
	 */
	public void setLanguage(String language) {
		parameters.setLanguage(language);
	}
	
	/**
	 * @return initial download url
	 */
	public String getUrlBase() {
		return parameters.getUrlBase().toString();
	}
	
	/**
	 * @return folder path where to get downloaded resources
	 */
	public String getSavePath() {
		return parameters.getSavePath();
	}
	
	/**
	 * Sets folder path where to get downloaded resources
	 * @param savePath folder path where to get downloaded resources
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public void setSavePath(String savePath) {
		parameters.setSavePath(savePath);
	}
	
	/**
	 * @return <i>true</i> if the existing files must be overwritten by the downloaded ones, <i>false</i> otherwise
	 */
	public boolean isOverwrite() {
		return parameters.isOverwrite();
	}
	
	/**
	 * Sets if the existing files must be overwritten by the downloaded ones
	 * @param value <i>true</i> if the existing files must be overwritten, <i>false</i> otherwise
	 */
	public void setOverwrite(boolean value) {
		parameters.setOverwrite(value);
	}
	
	/**
	 * @return delay between downloads, in milliseconds
	 */
	public int getDelay() {
		return parameters.getDelay();
	}
	
	/**
	 * Sets delay between downloads
	 * @param delay waiting time, in milliseconds
	 */
	public void setDelay(int delay) {
		parameters.setDelay(delay);
	}

	/**
	 * @return <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public boolean isUnzip() {
		return parameters.isUnzip();
	}

	/**
	 * Sets if downloaded files must be unzipping,
	 * @param value <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public void setUnzip(boolean value) {
		parameters.setUnzip(value);
	}

	/**
	 * @return the maximum number of files to download
	 */
	public int getMaxFilesToDownload() {
		return parameters.getMaxFilesToDownload();
	}

	/**
	 * Sets the maximum number of files to download. The zero indicates downloading all available files.
	 * @param maxFilesToDownload files number
	 * @throws IllegalArgumentException if a number less than zero is indicated
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		parameters.setMaxFilesToDownload(maxFilesToDownload);
	}
	
	/**
	 * @return the type of downloading engine to use
	 * @see DownloadEngineType
	 */
	public DownloadEngineType getEngineType() {
		return parameters.getEngineType();
	}

	/**
	 * Sets the type of downloading engine to use
	 * @param engineType engine type
	 * @see DownloadEngineType
	 */
	public void setEngineType(DownloadEngineType engineType) {
		parameters.setEngineType(engineType);
	}

	private String getCurrentTime() {
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return hourFormat.format(date);
	}
	
	private void printParameters() {
		log.info("PARAMETERS:");
		log.info("  urlBase = " + parameters.getUrlBase().toString());
		log.info("  savePath = " + parameters.getSavePath());
		log.info("  overwrite = " + parameters.isOverwrite());
		log.info("  delay = " + parameters.getDelay());
		log.info("  unzip = " + parameters.isUnzip());
		String max = (parameters.getMaxFilesToDownload() == 0) ? "all" : Integer.toString(parameters.getMaxFilesToDownload());
		log.info("  maxDownloads = " + max);
	}
	
	/**
	 * Main method for running the application.
	 * @param args list of arguments with application parameters
	 */
	public static void main(String [] args){
		if (args.length == 0 || (args[0].equals("-h") || args[0].equals("-help"))) {
			showHelp();
		} else {
			DownloadParams params = getParametersFromCommandLine(args);
			JGutenbergDownload jg = new JGutenbergDownload();
			jg.setParameters(params);				
			jg.downloadBooks();
		}
	}
	
	/**
	 * Reads settings parameters from command line.<br>
	 * Throws an Exception if there is any error.
	 * @param args list of parameters obtained from the command line
	 * @return a <code>DownloadParams</code> object.
	 */
	private static DownloadParams getParametersFromCommandLine(String [] args) {
		DownloadParams params = new DownloadParams();
		int i = 0;
		int step = 0;
		while (i < args.length) {
			try {
				if (args[i].startsWith("-f")) {
					params.setFileType(args[i+1]);
					step = 2;
				} else if (args[i].equals("-l")) {
					params.setLanguage(args[i+1]);
					step = 2;
				} else if (args[i].equals("-s")) {
					params.setSavePath(args[i+1]);
					step = 2;
				} else if (args[i].equals("-d")) {
					params.setDelay(Integer.parseInt(args[i+1]));
					step = 2;
				} else if (args[i].equals("-m")) {
					params.setMaxFilesToDownload(Integer.parseInt(args[i+1]));
					step = 2;
				} else if (args[i].equals("-o")) {
					params.setOverwrite(true);
					step = 1;
				} else if (args[i].equals("-z")) {
					params.setUnzip(false);
					step = 1;					
				} else {
					throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Parameter " + args[i]);
				}
			} catch (ArrayIndexOutOfBoundsException a) {
				throw new GutenbergException("org.josfranmc.gutenberg.GutenbergException: Parameter " + args[i]);
			} catch (Exception e) {
				throw e;
			}
			i+=step;
		}
		return params;
	}
	
	private static void showHelp() {
		log.info("");
		log.info("Usage: java -jar JGutenbergDownload [options]");
		log.info("Options:");
		log.info("   -f xxx (xxx type of files to download, default: txt)");
		log.info("   -l xx  (xx  language of books to download, default: es)");
		log.info("   -s xxx (xxx download path on local machine, default: program folder)");
		log.info("   -d xxx (xxx delay between downloads in milliseconds, default 2000)");
		log.info("   -m xx  (xx  max number of downloads (default 10, 0 for dowload all)");
		log.info("   -o     (    overwrite existing files, default: false)");
		log.info("   -z     (    don't unzip downloads, default: true)");
		log.info("");
		log.info("(only -h to show options list)");
		log.info("");
	}
}
