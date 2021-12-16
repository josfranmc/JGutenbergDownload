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

import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;

/**
 * Downloads a resource.<br>
 * Objects of this class can be runned in separate threads.
 * @author Jose Francisco Mena Ceca
 * @version 2.1
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
