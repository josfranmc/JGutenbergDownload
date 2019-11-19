package org.josfranmc.gutenberg.download.engine;

import java.net.URL;

/**
 * Factory for creating <code>IDownloadEngine</code> objects.<br>
 * It make use of the <code>create</code> method. This method is overloaded, so it can be invoked combining three parameters:
 * <ul>
 * <li><i>target</i>: the url address to download</li>
 * <li><i>savePath</i>: local path where to save download</li>
 * <li><i>type</i>: engine type for downloading (for default <code>DownloadEngineType.HTTTP_CONNECTION</code> is used)</li>
 * </ul>
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
public class DownloadEngineFactory {

	DownloadEngineFactory() {
		throw new IllegalStateException("Cannot instantiate class");
	}
	
	/**
	 * Creates an engine for downloading. Engine type is <code>DownloadEngineType.HTTP_CONNECTION.</code><br>
	 * @return a download engine
	 */
	public static IDownloadEngine create() {
		return createDownloadEngine(null, null, DownloadEngineType.HTTP_CONNECTION);
	}
	
	/**
	 * Creates a download engine of the indicated type.<br>
	 * The url to download and the path where to save are not initialized.
	 * @param type engine type for downloading
	 * @return a download engine in the form of a <code>IDownloadEngine</code> object
	 */
	public static IDownloadEngine create(DownloadEngineType type) {
		return createDownloadEngine(null, null, type);
	}

	/**
	 * Creates a download engine of the indicated type for downloading an url.<br>
	 * The path where to save the download is not initialized.
	 * @param target the url address to download
	 * @param type engine type for downloading
	 * @return a download engine in the form of a <code>IDownloadEngine</code> object
	 */
	public static IDownloadEngine create(URL target, DownloadEngineType type) {
		return createDownloadEngine(target, null, type);
	}
	
	/**
	 * Creates an engine for downloading an url. Engine type is <code>DownloadEngineType.HTTP_CONNECTION.</code><br>
	 * @param target the url address to download
	 * @return a download engine in the form of a <code>IDownloadEngine</code> object
	 */
	public static IDownloadEngine create(URL target) {
		return createDownloadEngine(target, null, DownloadEngineType.HTTP_CONNECTION);
	}

	/**
	 * Creates an engine for downloading.
	 * @param target the url address to download
	 * @param savePath local path where to save download
	 * @param type engine type for downloading
	 * @return a download engine in the form of a <code>IDownloadEngine</code> object
	 */
	public static IDownloadEngine create(URL target, String savePath, DownloadEngineType type) {
		return createDownloadEngine(target, savePath, type);
	}
 	
	/**
	 * Creates an engine for downloading. Engine type is <code>DownloadEngineType.HTTP_CONNECTION.</code><br>
	 * @param target the url address to download
	 * @param savePath local path where to save download
	 * @return a download engine in the form of a <code>IDownloadEngine</code> object
	 */
	public static IDownloadEngine create(URL target, String savePath) {
		return createDownloadEngine(target, savePath, DownloadEngineType.HTTP_CONNECTION);
	}
	
	private static IDownloadEngine createDownloadEngine(URL target, String savePath, DownloadEngineType type) {
		//type = (type == null) ? DownloadEngineType.HTTP_CONNECTION : type;
		// por ahora type solo puede tomar el valor DownloadEngineType.HTTP_CONNECTION
		return DownloadHttpUrlConnection.newInstance(target, savePath);
	}
}