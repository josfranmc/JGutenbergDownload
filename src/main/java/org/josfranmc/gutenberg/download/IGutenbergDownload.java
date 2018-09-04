package org.josfranmc.gutenberg.download;

import org.josfranmc.gutenberg.download.engine.DownloadEngineType;

/**
 * Establece el interfaz de uso que debe implementarse para llevar a cabo la gestión y desarrollo del proceso de descarga de los libros alojados
 * por el proyecto Gutenberg (<a href="http://www.gutenberg.org/">http://www.gutenberg.org/</a>)
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see DownloadMode
 * @see DownloadEngineType
 */
public interface IGutenbergDownload {

	/**
	 * Desacarga libros
	 */
	public void downloadBooks();
	
	/**
	 * @return el modo de descarga utilizado
	 */
	public DownloadMode getDownloadMode();
	
	/**
	 * Establece el mode de realizar las descargas
	 * @param mode tipo de descarga DownloadMode
	 * @see DownloadMode
	 */
	public void setDownloadMode(DownloadMode mode);
	
	/**
	 * Establece el tiempo de espera entre descargas, en milisegundos
	 * @param delay tiempo de espera entre descargas
	 */
	public void setDelay(int delay);
	
	/**
	 * @return el tiempo de espera entre descargas
	 */
	public int getDelay();
	
	/**
	 * Establece si se deben sobreescribir los ficheros ya existentes en la carpeta especificada por las nuevas descargas en caso de ser los mismos 
	 * @param value <i>true</i> si se deben sobreescribir los archivos, <i>false</i> en caso contrario
	 */
	public void setOverwrite(boolean value);
	
	/**
	 * @return si está activado/desactivado el indicador de sobreescribir ficheros ya descargados
	 */
	public boolean isOverwrite();
	
	/**
	 * @return la dirección URL desde donde realizar la descarga de los archivos
	 */
	public String getUrlBase();
	
	/**
	 * Establece la ruta de la carpeta en la que guardar los archivos descargados
	 * @param savePath ruta de la carpeta en la que guardar los archivos descargados
	 */
	public void setSavePath(String savePath);
	
	/**
	 * @return la ruta en la que guardar los archivos descargados
	 */
	public String getSavePath();

	/**
	 * Establece si se deben descomprimir los ficheros una vez descargados 
	 * @param value <i>true</i> si se deben descomprimir los archivos descargados, <i>false</i> en caso contrario
	 */
	public void setUnzip(boolean value);
	
	/**
	 * @return <i>true</i> si se deben descomprimir los archivos descargados, <i>false</i> en caso contrario
	 */
	public boolean isUnzip();
	
	/**
	 * Establece el tipo de ficheros a descargar. Tipos válidos: txt, epub, html
	 * @param fileType tipo de fichero a desrgargar
	 * @return una referencia al propio objeto que hace la llamada
	 * @see IGutenbergDownload
	 */
	public IGutenbergDownload setFileType(String fileType);
	
	/**
	 * @return el tipo de fichero configurado para descargar.
	 */
	public String getFileType();
	
	/**
	 * Establece el idioma de los ficheros a descargar, según la nomenclatura usada por el proyecto Gutenberg. Ej.: es - español, en - inglés, fr - francés<p>
	 * Más info: http://www.gutenberg.org/catalog/
	 * @param language tipod e idioma
	 * @return una referencia al propio objeto que hace la llamada
	 * @see IGutenbergDownload
	 */
	public IGutenbergDownload setLanguage(String language);
	
	/**
	 * @return el idioma configurado de los ficheros para descargar.
	 */
	public String getLanguage();

	/**
	 * @return el número máximo de ficheros a descargar
	 */
	public int getMaxFilesToDownload();
	
	/**
	 * Establece el número máximo de ficheros a descargar
	 * @param maxFilesToDownload número máximo de ficheros
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload);
	
	/**
	 * Establece el tipo de motor a utilizar para realizar las descargas
	 * @param engineType tipode motor DownloadEngineType
	 * @see DownloadEngineType
	 */
	public void setEngineType(DownloadEngineType engineType);
	
	/**
	 * @return el tipo de motor que se está utilizando para realizar las descargas
	 * @see DownloadEngineType
	 */
	public DownloadEngineType getEngineType();
}