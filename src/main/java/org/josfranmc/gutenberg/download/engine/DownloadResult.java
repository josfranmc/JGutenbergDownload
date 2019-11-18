package org.josfranmc.gutenberg.download.engine;

import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * It encapsulates the result of downloading a resource. This class serves to save together different data related to the download:
 * the header returned in the connection and the name of the file created, along with its path.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
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
        /*Set<String> keys = headers.keySet();
        for (String key : keys) {
            String val = urlConnection.getHeaderField(key);
            System.out.println(key+"  |  "+val);
        }*/
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
		return (headers != null) ? headers.get("Last-Modified").get(0).toString() : "";
	}	
	
	/**
	 * @return the value of the header field "Content-Type"
	 */
	public String getContentType() {
		return (headers != null) ? headers.get("Content-Type").get(0).toString() : "";
	}	
	
	/**
	 * @return the value of the header field "Content-Length"
	 */
	public String getContentLength() {
		return (headers != null) ? headers.get("Content-Length").get(0).toString() : "";
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
