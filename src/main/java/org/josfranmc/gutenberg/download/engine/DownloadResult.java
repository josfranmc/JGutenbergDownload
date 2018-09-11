package org.josfranmc.gutenberg.download.engine;

import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Encapsula el resultado de la descarga de un recurso. Esta clase sirve para guardar juntos diferentes datos realacionados con la descarga
 * de un recurso: la cabecera devuelta en la conexión y el nombre del fichero creado, junto con su ruta. Además, si se ha producido algún error
 * puede indicarse un mensaje descriptivo del mismo.
 * 
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public class DownloadResult {

	/**
	 * Ruta del fichero descargado
	 */
	private String fileOutputPath;
	
	/**
	 * Conjunto de cabeceras obtenido como respuesta a una conexión establecida
	 */
	private Map<String, List<String>> headers;
	
	/**
	 * Mensaje de error
	 */
	private String error;
	
	/**
	 * Constructor principal.
	 */
	public DownloadResult() {
		fileOutputPath = null;
		headers = null;
		error = null;
	}

	/**
	 * Establece la colección de campos de cabecera devuelta por una conexión realizada
	 * @param urlConnection conexión
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
	 * @return la colección de las cabeceras obtenidas
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	
	/**
	 * @return el valor del campo de la cebecera "Last-Modified"
	 */
	public String getLastModified() {
		return (headers != null) ? headers.get("Last-Modified").get(0).toString() : null;
	}	
	
	/**
	 * @return el valor del campo de la cebecera "Content-Type"
	 */
	public String getContentType() {
		return (headers != null) ? headers.get("Content-Type").get(0).toString() : null;
	}	
	
	/**
	 * @return el valor del campo de la cebecera "Content-Length"
	 */
	public String getContentLength() {
		return (headers != null) ? headers.get("Content-Length").get(0).toString() : null;
	}

	/**
	 * Establece la ruta del fichero descargado.
	 * @param fileOutputPath ruta del fichero descargado
	 */
	public void setFileOutputPath(String fileOutputPath) {
		this.fileOutputPath = fileOutputPath;
	}
	
	/**
	 * @return la ruta del fichero descargado
	 */
	public String getFileOutputPath() {
		return fileOutputPath;
	}

	/**
	 * @return el emnsaje de error establecido
	 */
	public String getError() {
		return error;
	}

	/**
	 * Establece un mensaje de error.
	 * @param error mensaje
	 */
	public void setError(String error) {
		this.error = error;
	}
}
