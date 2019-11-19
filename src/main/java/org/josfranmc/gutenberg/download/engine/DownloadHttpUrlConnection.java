package org.josfranmc.gutenberg.download.engine;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.util.GutenbergException;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * Allows to download a resource identified by an url and save it in a local folder.<br>
 * It makes use of the <code>java.net.HttpURLConnection</code> class for downloading.<br>
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 * @see IDownloadEngine
 * @see GutenbergException
 */
public class DownloadHttpUrlConnection implements IDownloadEngine {
	
	private static final Logger log = Logger.getLogger(DownloadHttpUrlConnection.class);
	
	/**
	 * the url of the resource to download
	 */
	private URL resource;
	
	/**
	 * local path where to save the resource to be download
	 */
	private String savePath;

    
	/**
	 * Initializes an object with the url of the resource to download and the path where to save it.
	 * @param target resource url to download
	 * @param savePath path where to save the download
	 */
	private DownloadHttpUrlConnection(URL target, String savePath) {
    	setSavePath(savePath);
    	setResource(target);
    }
	
	/**
	 * Creates a <code>DownloadHttpUrlConnection</code> object which is an <code>IDownloadEngine</code> type instance.
	 * @param target resource url to download
	 * @param savePath path where to save the download
	 * @return an <code>IDownloadEngine</code> instance in the form of a <code>DownloadHttpUrlConnection</code>
	 * @see IDownloadEngine
	 */
	public static IDownloadEngine newInstance(URL target, String savePath) {
		return new DownloadHttpUrlConnection(target, savePath);
	}
	
	/**
	 * Sets up the header values that will be sended along with the request.
	 * @throws ProtocolException
	 */
	private void configHeader(HttpURLConnection httpConnection) throws ProtocolException {
		httpConnection.setRequestMethod("GET");
		httpConnection.setRequestProperty("Content-Type", "");
		httpConnection.setRequestProperty("Host", getResource().getHost());
		httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
		httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpConnection.setRequestProperty("Accept-Language", "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3");
		httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		httpConnection.setRequestProperty("Connection", "keep-alive");
		httpConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
		httpConnection.setRequestProperty("Cookie", "d404660c5ddf0ca6f3e484fe85677673c4a94196");
		httpConnection.setConnectTimeout(5000);
	}
	
	/**
	 * Runs a download.<p>
	 * Previously, it has been necessary to indicate the url address of the resource to download and the path of the folder in which to save it.<br>
     * If the download is correct a new file in the indicated folder is obtained. The file name is extracted from download link,
     * beginning from the last directory separator character.
	 * @return the result of the download in the form of a DownloadResult object
	 * @throws GutenbergException if there is any error downloading
	 * @see DownloadResult
	 */
	@Override
	public DownloadResult download() {
		
		DownloadResult downloadResult = new DownloadResult();
		
		if (getResource() != null && getSavePath() != null) {
			
			HttpURLConnection httpConnection = null;
			try {
				httpConnection = (HttpURLConnection) getResource().openConnection();
				configHeader(httpConnection);
				
				String outputFilePath = FileManager.getLocalFilePathFromURL(getSavePath(), getResource().toString());

				try (BufferedOutputStream outputFileStream = new BufferedOutputStream (new FileOutputStream(outputFilePath))) {

					InputStream inputStream = httpConnection.getInputStream();

					copyResource(inputStream, outputFileStream);

					downloadResult.setHeaders(httpConnection);
					downloadResult.setSavedFilePath(outputFilePath);	            
				}
				log.debug("Descargado \"" + FileManager.getLocalFileName(outputFilePath) + "\" en " + getSavePath());
				log.debug("Tipo: " + downloadResult.getContentType() + "  Longitud: " + downloadResult.getContentLength());

			} catch (ConnectException e) {
				log.warn("Download timeout exceeded");
			} catch (UnknownHostException e) {
				throw new GutenbergException("Download UnknownHostException", e);
			} catch (IOException e) {
				throw new GutenbergException("Download IOException", e);
			} finally {
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
			}
		} else {
			log.warn("DOWNLOAD It must be indicated resource and route where to obtain the download");
		}
		return downloadResult;
	}

	private void copyResource(InputStream inputStream, BufferedOutputStream outputFileStream) throws IOException {
		int offset = 0;
		final byte[] buffer = new byte[2048];
		int read = 0;
		while ((read = inputStream.read(buffer)) >= 0) {
			outputFileStream.write(buffer, offset, read);
			outputFileStream.flush();
		}
	}
	
	/**
	 * Sets the resource of the url to download, in the form of a URL object
	 * @param resource url address
	 */
	@Override
	public void setResource(URL resource) {
		this.resource = resource;
	}

	/**
	 * @return the resource of the url to download, in the form of a URL object
	 */
	@Override
	public URL getResource() {
		return resource;
	}

	/**
	 * Sets the local path where to save the resource to be download
	 * @param savePath local path
	 */
	@Override
	public void setSavePath(String savePath) {
		this.savePath = (savePath == null) ? System.getProperty("user.dir") : savePath;
	}

	/**
	 * @return the local path where to save the resource to be download
	 */
	@Override
	public String getSavePath() {
		return savePath;
	}
}
