package org.josfranmc.gutenberg.download;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.DownloadResult;
import org.josfranmc.gutenberg.util.FileScraping;

/**
 * Realiza el proceso de descarga de los libros de forma no "avariciosa". Es decir, los recursos se van descargando secuencialmente dejando un tiempo
 * de espera entre cada descarga.<br>
 * Esta es la forma correcta de llevar a cabo el proceso de descarga de los libros alojados en los servidores del proyecto Gutenberg, no haciendo un
 * uso abusivo de los recursos y servicios ofrecidos.<p>
 * Más info: <a href="http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages">http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages</a>
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see AbstractDownload 
 * @see DownloadParams
 */
public class DownloadSoft extends AbstractDownload {

	private static final Logger log = Logger.getLogger(DownloadSoft.class);
	
	/**
	 * Número de libros descargados
	 */
	private int finishedDownload = 0;
	
	
	/**
	 * Constructor por defecto.
	 */
	public DownloadSoft() {
		super();
	}

	/**
	 * Realiza la configuración necesaria para realizar las descargas.<br>
	 * <ul>
	 * <li>Se crea un motor de descarga y se le indica la url a descargar y la ruta de la carpeta en la que obtener el recuro.</li>
	 * <li>Se guarda la url inicial a descargar como url base para el resto de descargas si las hubiera</li>
	 * <li>Se establece el tiempo de espera entre descargas</li>
	 * <li>Se indica si deben sobreescribirse los recursos ya existentes por los descargados en caso de ser los mismos</li>
	 * @param dp parametros de configuración de las descargas establecidos por el cliente
	 * @see DownloadParams
	 * </ul>
	 */
	@Override
	protected void configDownload(DownloadParams dp) {
		downloadEngine = DownloadEngineFactory.create(dp.getEngineType());
		downloadEngine.setResource(dp.getUrlBase());
		downloadEngine.setSavePath(dp.getSavePath());
		setUrlBase(dp.getUrlBase());
		setDelay(dp.getDelay());
		setOverwrite(dp.isOverwrite());
		setMaxFilesToDownload(dp.getMaxFilesToDownload());
	}
	
	/**
	 * Procesa un fichero que es una página web que contiene los enlaces a los zips de los libros a obtener.<p>Recupera en una lista los enlaces de la
	 * página y luego los va descargando uno a uno. El último enlace leido es la dirección de la siguiente página a procesar. Cuando se lee este enlace
	 * se descarga la siguiente página con enlaces a libros y se llama recursivamente al método para realizar su descargaa. El proceso continua 
	 * hasta que no existen más páginas con enlaces que procesar
	 * @param file nombre del fichero de la página web que contiene los enlaces de los zips a descargar
	 */
	@Override
	protected void processPage(String file) {
		boolean error = false;
		boolean isNewPage = false;
		DownloadResult downloadResult = null;
		URL newLinkToProcess = null;

		Iterator<String> it = FileScraping.getLinks(file).iterator();
		while (it.hasNext() && getFinishedDownload() < getMaxFilesToDownload()) {
			String link = it.next();
			if (createResource(link)) {
				try {
					error = false;
					isNewPage = false;
					newLinkToProcess = new URL(link);
				} catch (MalformedURLException e) {
					try {
						// nueva página que contiene más enlaces. hay que cambiar los &amp; por & para crear correctamente el nuevo enlace
						newLinkToProcess = new URL(getUrlBase(), link.toString().replace("&amp;", "&"));
						isNewPage = true;
						log.info("Nueva url " + newLinkToProcess.toString());
					} catch (MalformedURLException ex) {
						error = true;
						log.error(ex);
						log.error("Imposible obtener URL para " + link);
						log.error("newLinkToProcess = " + newLinkToProcess);
						log.error("Url Base = " + getUrlBase());
					}
				}
				if (!error) {
					sleepProcess();
					downloadEngine.setResource(newLinkToProcess);
					downloadResult = downloadEngine.download();
					if (isNewPage && (downloadResult.getContentType().equals("text/html"))) {
						processPage(downloadResult.getFileOutputPath());
					} else {
						incrementFinishedDownload();
					}
				}
			} else {
				log.debug("Ya existe el recurso " + link + ". Omitiendo la descarga...");
			}
		}
	}
	
	/**
	 * Pausa la ejecución del proceso el tiempo fijado en el atributo de la clase delay.
	 */
	private void sleepProcess() {
		if (getDelay() > 0) {
	    	try {
				Thread.sleep(getDelay());
			} catch (InterruptedException e) {
				log.error("Imposible pausar. " + e);
			}
		}		
	}

	/**
	 * @return el número de libros descargados
	 */
	protected int getFinishedDownload() {
		return finishedDownload;
	}

	/**
	 * @param finishedDownload the finishedDownload to set
	 */
	protected void incrementFinishedDownload() {
		this.finishedDownload++;
	}
	
	/**
	 * @see DownloadMode
	 */
	@Override
	protected DownloadMode getType() {
		return DownloadMode.SOFT;
	}
}