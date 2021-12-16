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

import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * It encapsulates the result of downloading a resource. This class serves to save together different data related to the download:
 * the header returned in the connection and the name of the file created, along with its path.
 * @author Jose Francisco Mena Ceca
 * @version 2.1
 */
public class DownloadResult {

	/**
	 *  the path of the downloaded file on the local machine.
	 */
	private String savedFilePath;
	
	/**
	 * Header obtained in response to a connection
	 */
	private Map<String, List<String>> headers;


	public DownloadResult() {
		savedFilePath = null;
		headers = null;
	}

	/**
	 * Sets the header obtained in response to a connection
	 * @param urlConnection connection made
	 */
	public void setHeaders(URLConnection urlConnection) {
		headers = urlConnection.getHeaderFields();
//        Set<String> keys = headers.keySet();
//        for (String key : keys) {
//            String val = urlConnection.getHeaderField(key);
//            System.out.println(key+"  |  "+val);
//        }
	}
	
	/**
	 * 
	 * @return the header obtained in response to a connection
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	
	/**
	 * @return the value of the header field "Last-Modified"
	 */
	public String getLastModified() {
		String lastModified = "";
		try {
			lastModified = (headers != null) ? headers.get("Last-Modified").get(0) : "";
		} catch (Exception e) {

		}
		return lastModified;
	}	
	
	/**
	 * @return the value of the header field "Content-Type"
	 */
	public String getContentType() {
		String contentType = "";
		try {
			contentType = (headers != null) ? headers.get("Content-Type").get(0) : "";
		} catch (Exception e) {

		}
		return contentType;
	}	
	
	/**
	 * @return the value of the header field "Content-Length"
	 */
	public String getContentLength() {
		String contentLength = "";
		try {
			contentLength = (headers != null) ? headers.get("Content-Length").get(0) : "";
		} catch (Exception e) {

		}
		return contentLength;
	}

	/**
	 * @return the value of the header field "Content-Location"
	 */
	public String getContentLocation() {
		String contentLocation = "";
		try {
			contentLocation = (headers != null) ? headers.get("Content-Location").get(0) : "";
		} catch (Exception e) {

		}
		return contentLocation;
	}
	
	/**
	 * Sets the path of the downloaded file on the local machine.
	 * @param fileOutputPath path of the downloaded file
	 */
	public void setSavedFilePath(String fileOutputPath) {
		this.savedFilePath = fileOutputPath;
	}
	
	/**
	 * @return the path of the downloaded file on the local machine.
	 */
	public String getSavedFilePath() {
		return savedFilePath;
	}
}
