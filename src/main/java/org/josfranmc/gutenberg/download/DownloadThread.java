package org.josfranmc.gutenberg.download;

import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;

/**
 * Downloads a resource.<br>
 * Objects of this class can be runned in separate threads.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
public class DownloadThread implements Runnable {

	private IDownloadEngine downloadEngine;
	
	/**
	 * @param target download url 
	 * @param parameters settings
	 */
	public DownloadThread(URL target, DownloadParams parameters) {
		downloadEngine = DownloadEngineFactory.create(target, parameters.getZipsPath(), parameters.getEngineType());
	}
	
	@Override
	public void run() {
		downloadEngine.download();
	}

}
