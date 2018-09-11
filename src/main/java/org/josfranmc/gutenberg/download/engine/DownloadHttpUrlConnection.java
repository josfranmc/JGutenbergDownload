package org.josfranmc.gutenberg.download.engine;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * Permite realizar la descarga de un recurso identificado por una URL. Para ello se hace uso de la clase <i>java.net.HttpURLConnection</i>.<br>
 * El recurso descargado se guarda en la ruta que ha debido establecerse previamente a la descarga.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see IDownloadEngine
 */
public class DownloadHttpUrlConnection implements IDownloadEngine {
	
	private static final Logger log = Logger.getLogger(DownloadHttpUrlConnection.class);
	
	/**
	 * Dirección URL del recurso a descargar
	 */
	private URL resource;
	
	/**
	 * Ruta de la carpeta en la que obtener el recurso descargado
	 */
	private String savePath;

	/**
	 * Conexión establecida
	 */
	private HttpURLConnection httpConnection = null;

    
	/**
	 * Constructor. Incializa el objeto con la dirección URL de un recurso a descargar y la ruta de la carpeta donde obtenerlo.
	 * @param target direccion URL del recurso a descargar
	 * @param savePath ruta de la carpeta en la qu obtener la descarga
	 */
	private DownloadHttpUrlConnection(URL target, String savePath) {
    	setSavePath(savePath);
    	setResource(target);
    }
	
	/**
	 * Crea una instancia DownloadHttpUrlConnection de tipo IDownloadEngine. 
	 * @param target direccion URL del recurso a descargar
	 * @param savePath ruta de la carpeta en la qu obtener la descarga
	 * @return una instancia de tipo IDownloadEngine
	 * @see IDownloadEngine
	 */
	public static IDownloadEngine newInstance(URL target, String savePath) {
		return new DownloadHttpUrlConnection(target, savePath);
	}
	
	/**
	 * Configura los valores de la cabecera que se va a enviar en la petición.
	 */
	private void configHeader() throws ProtocolException {
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
	 * Ejecuta el proceso de descarga. Previamente se ha debido indicar la dirección del recurso a descargar y la ruta de la carpeta en la que obtenerlo.<br>
	 * Si la descarga es correcta, se obtiene en la carpeta indicada un nuevo fichero cuyo nombre es el nombre delfichero contenido en el enlace de descarga
	 * (se toma desde el último caracter separador de directorios)
	 * @return el resultado de la descarga encapsulado en un objeto de tipo DownloadResult
	 * @see DownloadResult
	 */
	@Override
	public DownloadResult download() {
		DownloadResult downloadResult = new DownloadResult();
		if (getResource() != null && getSavePath() != null) {
			BufferedOutputStream  fileOutputStream = null;
			try {
				httpConnection = (HttpURLConnection) getResource().openConnection();
				configHeader();
	            InputStream inputStream  = null;
	            int offset = 0;
	            final byte[] buffer = new byte[2048];
	        	int read = 0;
	    		
	    		String fileOutputPath = FileManager.getLocalFilePathFromURL(getSavePath(), getResource().toString());
	            inputStream = httpConnection.getInputStream();
	        	fileOutputStream = new BufferedOutputStream (new FileOutputStream(fileOutputPath));

	            log.debug("Download Desde: " + this.resource);
	            
	            while ((read = inputStream.read(buffer)) >= 0) {
	                fileOutputStream.write(buffer, offset, read);
	                fileOutputStream.flush();
	            }
	            
			    downloadResult.setHeaders(httpConnection);
				downloadResult.setFileOutputPath(fileOutputPath);	            
	            
				log.debug("Descargado \"" + FileManager.getLocalFileName(fileOutputPath) + "\" en " + getSavePath());
				log.debug("Tipo: " + downloadResult.getContentType() + "  Longitud: " + downloadResult.getContentLength());

	        } catch (ConnectException e) {
	        	log.error("Tiempo de espera superado");
	        	downloadResult.setFileOutputPath(null);
	        } catch (UnknownHostException e) {
	        	log.error("No puede obtenerse dirección IP del recurso a descarga");
	        	downloadResult.setFileOutputPath(null);
	        	downloadResult.setError("No puede obtenerse dirección IP del recurso a descarga");
	        } catch (FileNotFoundException e) {
	        	log.error("Recurso desconocido... " + getResource().toString());
	        	downloadResult.setFileOutputPath(null);
	        	downloadResult.setError("Recurso desconocido... " + getResource().toString());
			} catch (IOException e) {
	        	log.error("Error download " + resource.toString());
	        	downloadResult.setError("Error download " + resource.toString());
			} finally {
				httpConnection.disconnect();
			    if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
		} else {
			log.warn("DOWNLOAD Debe indicarse recurso y ruta donde obtener la descarga");
		}
		return downloadResult;
	}

	/**
	 * Establece la dirección del recurso a descargar, la cual se maneja como un objeto de tipo URL
	 * @param resource dirección url
	 */
	@Override
	public void setResource(URL resource) {
		this.resource = resource;
	}

	/**
	 * Obtiene la dirección del recurso a descargar como un objeto de tipo URL
	 * @return dirección del recurso a descargar
	 */
	@Override
	public URL getResource() {
		return resource;
	}

	/**
	 * Establece la ruta donde guardar el recurso a descargar
	 * @param savePath ruta de la carpeta donde guardar
	 */
	@Override
	public void setSavePath(String savePath) {
		this.savePath = (savePath == null) ? System.getProperty("user.dir") : savePath;
	}

	/**
	 * Obtiene la ruta donde guardar el recursos a descargar
	 * @return la ruta de la carpeta donde guardar el recursos a descargar
	 */
	@Override
	public String getSavePath() {
		return savePath;
	}
}
