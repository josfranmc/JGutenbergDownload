package org.josfranmc.gutenberg.download;

import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;

/**
 * Encapsula los parámetros que permiten configurar la aplicación. Son los siguientes:
 * <ul>
 * <li><b>urlBase</b>: url del recurso a descargar</li>
 * <li><b>savePath</b>: ruta local donde descargar los recursos</li>
 * <li><b>overwrite</b>: indica si se deben sobreescribir recursos que ya están descargados</li>
 * <li><b>delay</b>: tiempo de espera entre descargas</li>
 * <li><b>unzip</b>: indica si los recursos descargados en formato zip deben o no descomprimirse</li>
 * <li><b>engineType</b>: tipo de motor a utilizar para la descarga</li>
 * </ul>
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see DownloadEngineType
 */
public class DownloadParams {

	/**
	 * url del recurso a descargar
	 */
	private URL urlBase = null;
	
	/**
	 * Ruta de la carpeta en la que obtener los recursos descargados.
	 */
	private String savePath = null;
	
	/**
	 * Si deben sobreescribirse los recursos existentes por las nuevas descargas en caso de ser los mismos
	 */
	private boolean overwrite = false;
	
	/**
	 * Intervalo de espera entre descargas, en milisegundos
	 */
	private int delay = 2000;
	
	/**
	 * Si se deben descomprimir los archivos descargados
	 */
	private boolean unzip = true;
	
	/**
	 * Número maximo de ficheros a descargar. El valor cero indica descargar todos los ficheros disponibles.
	 */
	private int maxFilesToDownload = 10;
	
	/**
	 * Tipo de motor de descarga a utilizar
	 */
	DownloadEngineType engineType = DownloadEngineType.HTTP_CONNECTION;
	
	
	/**
	 * Constructor por defecto.
	 */
	public DownloadParams() { }

	/**
	 * @return la dirección url inicial del recurso a obtener
	 */
	public URL getUrlBase() {
		return urlBase;
	}

	/**
	 * Establece la dirección url inicial del recurso a obtener
	 * @param urlBase dirección url
	 */
	public void setUrlBase(URL urlBase) {
		this.urlBase = urlBase;
	}

	/**
	 * @return la ruta de la carpeta en la que obtener los recursos descargados
	 */
	public String getSavePath() {
		return savePath;
	}

	/**
	 * Establece la ruta de la carpeta en la que obtener los recursos descargados.
	 * @param savePath ruta de la carpeta en la que obtener los recursos descargados
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	/**
	 * @return <i>true</i> si se deben sobreescribir los archivos existentes por los descargados en caso de ser los mismos, <i>false</i> en caso contrario
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Establece si deben sobreescribirse los recursos existentes por las nuevas descargas en caso de ser los mismos
	 * @param overwrite <i>true</i> si se deben sobreescribir los archivos, <i>false</i> en caso contrario
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * @return el intervalo de espera entre descargas, en milisegundos
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Establece el intervalo de espera entre descargas.
	 * @param delay tiempo de espera, en milisegundos
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return <i>true</i> si se deben descomprimir los archivos descargados, <i>false</i> en caso contrario
	 */
	public boolean isUnzip() {
		return unzip;
	}

	/**
	 * Establece si se deben descomprimir los archivos descargados
	 * @param unzip <i>true</i> si se deben descomprimir los archivos descargados, <i>false</i> en caso contrario
	 */
	public void setUnzip(boolean unzip) {
		this.unzip = unzip;
	}

	/**
	 * @return el número máximo de ficheros a descargar
	 */
	public int getMaxFilesToDownload() {
		return maxFilesToDownload;
	}

	/**
	 * Establece el número máximo de ficheros a descargar. El valor cero indica descargar todos los ficheros disponibles.
	 * @param maxFilesToDownload número máximo de ficheros
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		this.maxFilesToDownload = maxFilesToDownload;
	}

	/**
	 * @return el tipo de motor de descarga a utilizar
	 */
	public DownloadEngineType getEngineType() {
		return engineType;
	}

	/**
	 * Establece el tipo de motor de descarga a utilizar
	 * @param engineType tipo de motor de descarga a utilizar
	 */
	public void setEngineType(DownloadEngineType engineType) {
		this.engineType = engineType;
	}
}
