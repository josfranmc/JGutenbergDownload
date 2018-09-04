package org.josfranmc.gutenberg.download.engine;

import java.net.URL;

/**
 * Factoría para la creación de objetos de tipo IDownloadEngine mediante el método <i>create</i>
 * Este método está sobregargado, de forma que se puede invocar combinando tres parámetros:
 * <ul>
 * <li>target: recurso a descargar</li>
 * <li>savePath: ruta local donde obtener el recurso</li>
 * <li>type: tipo de motor a utilizar para la descarga (si no se indica ninguno por defecto se usa DownloadEngineType.HTTTP_CONNECTION)</li>
 * </ul>
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public class DownloadEngineFactory {

	/**
	 * Crea un motor para realizar la descarga de recursos en red. El motor creado es de tipo DownloadEngineType.HTTP_CONNECTION
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create() {
		return createDownloadEngine(null,null,DownloadEngineType.HTTP_CONNECTION);
	}
	
	/**
	 * Crea un motor determinado para realizar la descarga de recursos en red
	 * @param type tipo de motor a crear
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create(DownloadEngineType type) {
		return createDownloadEngine(null,null,type);
	}

	/**
	 * Crea un motor determinado para realizar la descarga desde una determinada URL
	 * @param target dirección URL
	 * @param type tipo de motor a crear
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create(URL target, DownloadEngineType type) {
		return createDownloadEngine(target,null,type);
	}
	
	/**
	 * Crea un motor para realizar la descarga desde una determinada URL. El motor creado es de tipo DownloadEngineType.HTTP_CONNECTION
	 * @param target dirección URL
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create(URL target) {
		return createDownloadEngine(target,null,DownloadEngineType.HTTP_CONNECTION);
	}

	/**
	 * Crea un motor determinado para realizar la descarga desde una determinada URL, guardando lo descargado en una ruta del equipo local
	 * @param target dirección URL
	 * @param savePath ruta donde obtener las descargas
	 * @param type tipo de motor a crear
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create(URL target, String savePath, DownloadEngineType type) {
		return createDownloadEngine(target,savePath,type);
	}
	
	/**
	 * Crea un motor para realizar la descarga desde una determinada URL, guardando lo descargado en una ruta del equipo local.
	 * El motor creado es de tipo DownloadEngineType.HTTP_CONNECTION.
	 * @param target dirección URL
	 * @param savePath ruta donde obtener las descargas
	 * @return un motor de descarga
	 */
	public static IDownloadEngine create(URL target, String savePath) {
		return createDownloadEngine(target,savePath,DownloadEngineType.HTTP_CONNECTION);
	}
	
	private static IDownloadEngine createDownloadEngine(URL target, String savePath, DownloadEngineType type) {
		IDownloadEngine downloadEngine = null;
		type = (type == null) ? DownloadEngineType.HTTP_CONNECTION : type;
		
		switch (type) {
		case HTTP_CONNECTION:
			downloadEngine = DownloadHttpUrlConnection.newInstance(target, savePath);
			break;

		default:
			downloadEngine = DownloadHttpUrlConnection.newInstance(target, savePath);
			break;
		}
		return downloadEngine;
	}
}