package org.josfranmc.gutenberg.download;

import java.net.URL;

import org.josfranmc.gutenberg.download.engine.DownloadResult;
import org.josfranmc.gutenberg.download.engine.IDownloadEngine;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * Encapsula el proceso que ha de seguirse para la descarga de un recurso.<br>
 * Esta clase actúa como plantilla (template), de forma que las clases que hereden de ella deben implementar los métodos indicados para realizar las descargas.
 * En concreo, se debe definir como se configuran los parámetros pasados y como se realiza el procesamiento de las páginas que exponen los enlaces a los
 * libros del proyecto Gutenberg (<a href="http://www.gutenberg.org/">http://www.gutenberg.org/</a>) 
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see DownloadParams
 * @see IDownloadEngine
 */
public abstract class AbstractDownload {
	
	/**
	 * Motor de descarga utilizado
	 */
	protected IDownloadEngine downloadEngine;
	
	/**
	 * Dirección url inicial del recurso a obtener. Se guarda la dirección inicial de descarga ya que irá variando según se vayan descargando
	 * recursos.
	 */
	private URL urlBase;
	
	/**
	 * Ruta de la carpeta en la que obtener los recursos descargados
	 */
	private String savePath;
	
	/**
	 *  Si se deben sobreescribir los archivos existentes por los descargados
	 */
	private boolean overwrite;
	
	/**
	 * Intervalo de espera entre descargas, en milisegundos
	 */
	private int delay;
	
	/**
	 * Número maximo de ficheros a descargar
	 */
	private int maxFilesToDownload;
	
	
	/**
	 * Constructor por defecto.
	 */
	protected AbstractDownload() {
		this.downloadEngine = null;
		this.urlBase = null;
		this.overwrite = false;
		this.savePath = null;
		this.delay = 2500;
	}

	/**
	 * Ejecuta el proceso de desarga.<br>Una vez realizada la configuración de los elementos necesarios, se realiza la descarga del recurso indicado
	 * en los parámetros pasados. Si el recurso obtenido es una página con enlaces de descarga se extraen estos enlaces
	 * para realizar la descarga de los recursos que se referencian.
	 * @param downloadParams parámetros de configuración de la descarga
	 * @see DownloadParams
	 * @throws IllegalStateException si se ha devuelto algún error al hacer las descargas
	 */
	public void executeDownload(DownloadParams downloadParams) {
		configDownload(downloadParams);
		DownloadResult downloadResult = downloadEngine.download();
		if (downloadResult.getError() == null) {
			if (downloadResult.getContentType() != null && downloadResult.getContentType().equals("text/html")) {
				processPage(downloadResult.getFileOutputPath());
			}
		} else {
			throw new IllegalStateException(downloadResult.getError());
		}
	}
	
	/**
	 * Realiza la configuración necesaria para realizar las descargas. Recibe un objeto DownloadParams que encapsula los parámetros para llevar a cabo
	 * la inicialización del proceso.
	 * @param dp parametros de configuración de las descargas establecidos por el cliente
	 * @see DownloadParams
	 */
	protected abstract void configDownload(DownloadParams dp);
	
	/**
	 * Procesa un archivo que es una página web que contiene los enlaces a los zips de los libros que se quieren obtener.<p>
	 * El último enlace es la dirección de la siguiente página a procesar, que contiene nuevos enlaces a libros.
	 * @param file nombre del fichero de la página web que contiene los enlaces de los zips a descargar
	 */
	protected abstract void processPage(String file);

	/**
	 * @return el tipo de algoritmo de descarga que utiliza la clase
	 * @see DownloadMode
	 */
	protected abstract DownloadMode getType();
	
	/**
	 * Comprueba si debe descargarse un determinado recurso. El recurso puede descargarse si se cumple alguna de los siguientes condiciones:
	 * <ul>
	 * <li>es la primera vez que se descarga</li>
	 * <li>el enlace del recurso contiene la palabra harvest (es por tanto una página que contiene enlaces a recursos)</li>
	 * <li>se ha activado el parametro de sobreescribir recursos ya descargados</li>
	 * </ul>
	 * @param link enlace al recurso que se quiere descargar
	 * @return <i>true</i> si debe descargarse el recurso, <i>false</i> en caso contrario
	 */
	protected boolean createResource(String link) {
		boolean result = false;
		if (!resourceAlreadyDownloaded(link) || link.contains("harvest") || isOverwrite()) {
			result = true;
		}
		return result;
	}	
	
	/**
	 * Comprueba si existe en el directorio en el que se descargan los recursos el fichero correspondiente al recurso indicado por un determinado enlace
	 * @param link enlace del recurso a descargar
	 * @return <i>true</i> si ya existe el recurso a descargar en el directorio donde se descargan los recursos, <i>false</i> en caso contrario
	 */
	protected boolean resourceAlreadyDownloaded(String link) {
		return (FileManager.fileExists(downloadEngine.getSavePath(), link));
	}
	
	/**
	 * @return el tipo de motor de descarga utilizado
	 * @see IDownloadEngine
	 */
	protected IDownloadEngine getDownloadEngine() {
		return downloadEngine;
	}

	/**
	 * @return la dirección url inicial del recurso a obtener
	 */
	protected URL getUrlBase() {
		return urlBase;
	}

	/**
	 * Establece la dirección url inicial del recurso a obtener. 
	 * @param urlBase dirección url
	 */
	protected void setUrlBase(URL urlBase) {
		this.urlBase = urlBase;
	}

	/**
	 * @return la ruta de la carpeta en la que obtener los recursos descargados
	 */
	protected String getSavePath() {
		return savePath;
	}

	/**
	 * Establece la ruta de la carpeta en la que obtener los recursos descargados.
	 * @param savePath ruta de la carpeta en la que obtener los recursos descargados
	 */
	protected void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * @return <i>true</i> si se deben sobreescribir los archivos existentes por los descargados, <i>false</i> en caso contrario
	 */
	protected boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Establece si deben sobreescribirse los recursos existentes por las nuevas descargas 
	 * @param overwrite <i>true</i> si se deben sobreescribir los archivos, <i>false</i> en caso contrario
	 */
	protected void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	/**
	 * Establece el intervalo de espera entre descargas
	 * @param delay tiempo de espera, en milisegundos
	 */
	protected void setDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * @return el intervalo de espera entre descargas, en milisegundos
	 */
	protected int getDelay() {
		return delay;
	}

	/**
	 * @return el número máximo de ficheros a descargar
	 */
	protected int getMaxFilesToDownload() {
		return maxFilesToDownload;
	}

	/**
	 * Establece el número máximo de ficheros a descargar. El valor cero indica descargar todos los ficheros disponibles.
	 * @param maxFilesToDownload número máximo de ficheros
	 */
	protected void setMaxFilesToDownload(int maxFilesToDownload) {
		this.maxFilesToDownload = (maxFilesToDownload == 0) ? Integer.MAX_VALUE : maxFilesToDownload;
	}
}
