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

/**
 * Types of download engines
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
public enum DownloadEngineType {
	
	/**
	 * Downloads based on the <code>java.net.HttpURLConnection</code> object
	 */
	HTTP_CONNECTION;

}
