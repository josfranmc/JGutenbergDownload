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
 * @version 2.0
 * @see DownloadEngineType
 * @see JGutenbergDownload
 */
public class DownloadParams {

	/**
	 * Base URL of the Gutenberg project to download
	 */
	public static final String URL_BOOKS = "http://www.gutenberg.org/robot/";
	
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
		savePath = System.getProperty("user.dir") + System.getProperty("file.separator");
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
	 * @throws IllegalArgumentException if an invalid file type is indicated
	 */
	public void setFileType(String fileType) {
		if (!fileType.equals("txt") && !fileType.equals("epub") && !fileType.equals("html")) {
			throw new IllegalArgumentException("Invalid file type");
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
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public void setSavePath(String savePath) {
		if (savePath == null) {
			throw new IllegalArgumentException("The directory path where to save downloads cannot be null");
		}
		String fileSeparator = System.getProperty("file.separator");
		String path = (!savePath.endsWith(fileSeparator)) ? savePath.concat(fileSeparator) : savePath;
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
		this.zipsPath = savePath + "zips" + System.getProperty("file.separator");
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
	 * @throws IllegalArgumentException if a number less than zero is indicated
	 */
	public void setDelay(int delay) {
		if (delay < 0) {
			throw new IllegalArgumentException("delay cannot be less than zero");
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
	 * @throws IllegalArgumentException if a number less than zero is indicated
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		if (maxFilesToDownload < 0) {
			throw new IllegalArgumentException("maxFilesToDownload cannot be less than zero");
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
			throw new IllegalStateException("Wrong base URL: " + url);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create base URL: " + url);
		}
	}
}
