package org.josfranmc.gutenberg.download;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineFactory;
import org.josfranmc.gutenberg.download.engine.DownloadResult;
import org.josfranmc.gutenberg.util.FileScraping;

/**
 * Realiza el proceso de descarga de los libros de forma "avariciosa". La estrategia utilizada es la de ejecutar diversos hilos para realizar
 * la descarga de los libros de forma concurrrente y sin realizar ninguna pausa entre las descargas.<br>
 * Este método de descarga es más eficiente, desde el punto de vista de la velocidad de las descargas, que el usado por el tipo DownloadSoft
 * pero no respeta las condiciones de uso del servicio ofrecido por el proyecto Gutenberg ya que se hace un uso abusivo de las mismas. Utilizar este
 * modo de descarga puede suponer que nuestra dirección ip sea bloqueada, impidiéndonos el acceso a los servidores.<p>
 * Más info: <a href="http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages">http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages</a>
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see AbstractDownload
 * @see DownloadParams
 */
public class DownloadGreedy extends AbstractDownload implements Runnable {
	
	private static final Logger log = Logger.getLogger(DownloadGreedy.class);
	
	/**
	 * Control de los hilos a ejecutar.
	 */
	private static ExecutorService executorService;

	/**
	 * Conjunto de parámetros recibido
	 */
	private DownloadParams parameters;
	
	/**
	 * Número de libros descargados
	 */
	private AtomicInteger finishedDownload;
	
	
	/**
	 * Constructor por defecto.
	 */
	public DownloadGreedy() {
		super();
		finishedDownload = new AtomicInteger();
	}
	
	/**
	 * Constructor. Se usa para inicializar, desde dentro de la clase, los objetos creados para ejecutarse en un nuevo hilo. 
	 * @param target dirección URL del recurso a descarga
	 * @param dp conjunto de parámetros de configuración
	 */
	private DownloadGreedy(URL target, DownloadParams dp) {
		this.parameters = dp;
		downloadEngine = DownloadEngineFactory.create(dp.getEngineType());
		downloadEngine.setResource(target);
		downloadEngine.setSavePath(dp.getSavePath());
		setUrlBase(dp.getUrlBase());
		setSavePath(dp.getSavePath());
		setOverwrite(dp.isOverwrite());
	}

	/**
	 * Realiza la configuración inicial necesaria para realizar las descargas.<br>
	 * <ul>
	 * <li>Se crea un motor de descarga y se le indica la url a descargar y la ruta de la carpeta en la que obtener el recuro.</li>
	 * <li>Se guarda la url inicial a descargar como url base para el resto de descargas si las hubiera</li>
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
		setSavePath(dp.getSavePath());
		setOverwrite(dp.isOverwrite());
		setMaxFilesToDownload(dp.getMaxFilesToDownload());
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);
		this.parameters = dp; // guardamos los parámetros para poder pasárselos al próximo hilo
	}
	
	/**
	 * Procesa un fichero que es una página web que contiene los enlaces a los zips de los libros a obtener.<p>Recupera en una lista los enlaces de la
	 * página y luego los va descargando uno a uno. El último enlace leido es la dirección de la siguiente página a procesar. Cuando se lee este enlace
	 * se descaraga la siguiente página con enlaces a libros y se llama recursivamente al método para realizar su descargaa. El proceso continua 
	 * hasta que no existen más páginas con enlaces que procesar.<p>
	 * La descarga de cada enlace es realizada por un nuevo hilo de ejecución.
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
					if (!isNewPage) {
						DownloadGreedy dg = new DownloadGreedy(newLinkToProcess, this.parameters);
						executorService.submit(dg);
						incrementFinishedDownload();
					} else {
						// obtenemos la nueva página que contiene más enlaces
						downloadEngine.setResource(newLinkToProcess);
						downloadResult = downloadEngine.download();
						processPage(downloadResult.getFileOutputPath());
						
					}
				}
			} else {
				log.debug("Ya existe el recurso " + link + ". Omitiendo la descarga...");
			}
		}
		waitEnd();
	}
	
	/**
	 * Inicia un proceso de descarga.
	 */
	@Override
	public void run() {
		downloadEngine.download();
	}
	
	/**
	 * Espera a que terminen todos los hilos ejecutados
	 */
	private void waitEnd() {
	    try {
	    	executorService.shutdown();
			while (!executorService.awaitTermination(2, TimeUnit.MINUTES)) {}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return el número de libros descargados
	 */
	protected int getFinishedDownload() {
		return finishedDownload.get();
	}

	/**
	 * @param finishedDownload the finishedDownload to set
	 */
	protected void incrementFinishedDownload() {
		this.finishedDownload.incrementAndGet();
	}
	
	@Override
	protected DownloadMode getType() {
		return DownloadMode.GREEDY;
	}
}
