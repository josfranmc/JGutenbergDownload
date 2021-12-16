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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.DownloadResult;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;
import org.josfranmc.gutenberg.util.FileManager;
import org.josfranmc.gutenberg.util.FileScraping;

/**
 * Allows to perform the file download process.
 * @author Jose Francisco Mena Ceca
 * @version 2.1
 * @see JGutenbergDownload
 * @see DownloadParams
 */
public class DownloadBooks {

	private static final Logger log = LogManager.getLogger(DownloadBooks.class);
	
	private ExecutorService executorService;
	
	private DownloadParams parameters;

	private IDownloadEngine downloadEngine;
	
	private int finishedDownloads = 0;

	
	public DownloadBooks(DownloadParams parameters) {
		this.parameters = parameters;
		this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);
	}
	
	private void settingDownloadEngine() {
		downloadEngine = DownloadEngineFactory.create(parameters.getEngineType());
		downloadEngine.setResource(parameters.getUrlBase());
		downloadEngine.setSavePath(parameters.getZipsPath());
	}
	
	/**
	 * Perfoms the book download process.<p>
	 * The first download should be a file with links to the books to download. All existing links are downloaded.
	 * If there is a link to another page with more links, this page is downloaded and processed in the same way. 
	 * This process continues until there are no more pages.<p>
	 * All downloads run on separate threads.
	 */
	public boolean executeDownload() {
		boolean result = true;
		
		settingDownloadEngine();
		
		log.info("Downloading...");
		DownloadResult downloadResult = downloadEngine.download();
		
		if (isPageWithBookLinks(downloadResult)) { 
			processPage(downloadResult.getSavedFilePath());
			try {
		    	executorService.shutdown();
				while (!executorService.awaitTermination(2, TimeUnit.MINUTES)) {}
			} catch (InterruptedException e) {
				log.warn("InterruptedException shutting down executorService. " + e.getMessage());
				Thread.currentThread().interrupt();
			}	
		} else {
			log.info("No links to download");
			return result = false;
		}
		return result;
	}

	/**
	 * @param downloadResult
	 * @return <i>true</i> if the downloaded file contains links to books, <i>false</i> otherwise
	 */
	private boolean isPageWithBookLinks(DownloadResult downloadResult) {
		boolean result =false;
		if (downloadResult.getContentType().contains("text/html") &&
			downloadResult.getContentLocation().contains("harvest.php") )
		{
			result = true;
		}
		return result;
	}
	
	/**
	 * It process a file with links that are the urls to the books to download. Existing links are extracted and then downloaded.<br>
	 * The last link could be the link to another page with more links. If so, we process the new file calling this method again.
	 * @param file file with links to the books to download
	 */
	private void processPage(String file) {
		Iterator<String> it = FileScraping.getLinks(file).iterator();
		while (it.hasNext() && getFinishedDownloads() < getMaxFilesToDownload()) {
			String link = it.next();
			if (createResource(link)) {
				try {
					getBook(link);
				} catch (MalformedURLException e) {
					try {
						DownloadResult downloadResult = getNewPageWithBookLinks(link);
						if (isPageWithBookLinks(downloadResult)) {
							processPage(downloadResult.getSavedFilePath());
						}
					} catch (MalformedURLException ex) {
						log.error("Cannot get url for " + link);
						log.error("  Url Base = " + getUrlBase());
					}
				}					
			}
		}
	}
	
	/**
	 * Downloads a book. The download is runned in a new thread.
	 * @param link download url
	 * @throws MalformedURLException
	 */
	private void getBook(String link) throws MalformedURLException {
		URL newURLToDownload = new URL(link);
		delayDownload();
		executorService.submit(new DownloadThread(newURLToDownload, parameters));
		incrementFinishedDownloads();
	}
	
	private DownloadResult getNewPageWithBookLinks(String link) throws MalformedURLException {
		URL newURLToDownload = new URL(getUrlBase(), link.replace("&amp;", "&"));
		downloadEngine.setResource(newURLToDownload);
		log.info("New url " + newURLToDownload.toString());
		return downloadEngine.download();
	}
	
	/**
	 * Checks if one resource must be downloaded. The resource can be downloaded if any of the following conditions are fulfilled:
	 * <ul>
	 * <li>it is the first time the resource is downloaded</li>
	 * <li>overwrite resources parameter is active</li>
	 * <li>the resource link contains the word harvest (it is a link to a page with more book links)</li>
	 * </ul>
	 * @param link link to the resource to download
	 * @return <i>true</i> if the resource must be downloaded, <i>false</i> otherwise
	 */
	private boolean createResource(String link) {
		boolean result = false;
		if (!resourceAlreadyDownloaded(link) || isOverwrite() || link.contains("harvest")) {
			result = true;
		}
		return result;
	}	
	
	/**
	 * Checks if the file corresponding to the resource indicated by a certain link exists in the directory where the resources are downloaded
	 * @param link resource link to download
	 * @return <i>true</i> if the resource to download already exists, <i>false</i> otherwise
	 */
	private boolean resourceAlreadyDownloaded(String link) {
		return (FileManager.fileExists(downloadEngine.getSavePath(), link));
	}
	
	private boolean isOverwrite() {
		return parameters.isOverwrite();
	}
	
	private int getDelay() {
		return parameters.getDelay();
	}
	
	private URL getUrlBase() {
		return parameters.getUrlBase();
	}
	
	private int getMaxFilesToDownload() {
		return parameters.getMaxFilesToDownload();
	}

	private int getFinishedDownloads() {
		return finishedDownloads;
	}

	private void incrementFinishedDownloads() {
		this.finishedDownloads++;
	}
	
	private void delayDownload() {
		if (getDelay() > 0) {
			try {
				Thread.sleep(getDelay());
			} catch (InterruptedException e) {
				log.error("InterruptedException delayDownload. " + e);
				Thread.currentThread().interrupt();
			}
		}		
	}
}
