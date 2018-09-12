package org.josfranmc.gutenberg.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * Implementa el proceso y gestión de descarga de libros alojados por el proyecto Gutenberg (<a href="http://www.gutenberg.org/">http://www.gutenberg.org</a>)
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see IGutenbergDownload
 * @see DownloadParams
 * @see DownloadMode
 * @see DownloadEngineType
 */
public class JGutenbergDownload implements IGutenbergDownload{
	
	private static final Logger log = Logger.getLogger(JGutenbergDownload.class);
	
	/**
	 * URL base del proyecto Gutenberg para realizar las descargas
	 */
	private static final String URL_BOOKS = "http://www.gutenberg.org/robot/";
	
	/**
	 * Nombre de la carpeta dentro de la carpeta base donde guardar los ficheros zips descargados
	 */
	private static final String ZIP_DIRECTORY = "zips";

	/**
	 * Tipo de los ficheros a descargar
	 */
	private String fileType;
	
	/**
	 * Idioma de los ficheros a descargar
	 */
	private String language;
	
	/**
	 * Ruta base de la carpeta pasada por parámetro en la que realizar las descargas.
	 * A lo largo del proceso de descarga la carpeta donde realizarlas puede variar. Esta variable guardar la carpeta inicial del proceso
	 */
	private String rootSavePath;

	/**
	 * Modo de realizar las descargas
	 */
	private AbstractDownload downloadMode = null;
	
	/**
	 * Encapsula los parámetros de configuración a usar.  Recoge los parámetros pasados y es enviado a los componentes
	 * encargados de realizar las descargas
	 */
	private DownloadParams parameters = null;
	
	/**
	 * Constructor principal. Establece los parámetros de configuración que usa la apliación con valores por defecto. Concretamente:
	 * <ul>
	 * <li>Se establece como carpeta base de descarga la carpeta desde la que se ejecuta el programa</li>
	 * <li>Se fija un tiempo de demora entre descargas de 2 segundos</li>
	 * <li>Se establece que no se sobreescriban ficheros previamente descargados</li>
	 * <li>Se establece que se descompriman los ficheros zips descargados</li>
	 * <li>Se indica que se utilice un motor de descarga del tipo <i>DownloadEngineType.HTTP_CONNECTION</i></li>
	 * <li>Se indica que las descargas se realicen usando un modo <i>DownloadMode.SOFT</i></li>
	 * <li>Se indica que se descarguen todos los ficheros disponibles</li>
	 * </ul>
	 * @see DownloadParams
	 * @see DownloadEngineType
	 * @see DownloadMode
	 */
	public JGutenbergDownload() {
		parameters = new DownloadParams();
		setSavePath(System.getProperty("user.dir"));
		setDelay(2000);
		setOverwrite(false);
		setUnzip(true);
		setEngineType(DownloadEngineType.HTTP_CONNECTION);
		setMaxFilesToDownload(10);
		setDownloadMode(DownloadMode.SOFT);
	}

	/**
	 * Inicia el proceso de descarga de los libros.<br>
	 * Crea una carpeta para guardar los zip descargados dentro de la carpeta indicada para almacenar descargar las descargas.
	 * 
	 * Se comprueba si se ha indicado el tipo de ficheros a descargar y el idioma de los mismos. 
	 * Si no se se han indicado no se puede realizar la descarga.
	 * @throws IllegalArgumentException si no se ha indicado tipo de fichero e idioma
	 */
	@Override
	public void downloadBooks() {
		if (isUrlParameters()) {
			buildHarvestURL();
			log.info("INICIO DESCARGA LIBROS " + getCurrentTime());
			writeParamsLog();
			download();
			if (parameters.isUnzip()) {
				unzipFiles();
			}
			log.info("FIN DESCARGA LIBROS " + getCurrentTime());
		} else {
			log.error("Es necesario indicar tipo de fichero e idioma");
			throw new IllegalArgumentException("Es necesario indicar tipo de fichero e idioma");
		}
	}

	/**
	 * Construye la URL principal a partir de la cual realizar las descargas de los libros
	 */
	private void buildHarvestURL() {	
		String queryParam = "harvest?filetypes[]=".concat(getFileType()).concat("&langs[]=").concat(getLanguage());
		String url = URL_BOOKS.concat(queryParam);
		try {
			parameters.setUrlBase(new URL(url));
		} catch (MalformedURLException e) {
			log.warn("URL base errónea: " + url);
		} catch (Exception e) {
			log.error("Imposible inicializar URL base " + url);
			e.printStackTrace();
		}
	}
	
	/**
	 * Lleva a cabo las descargas.<br>Primero crea dentro de la carpeta raíz de descargas una nueva carpeta en la que obtener los ficheros
	 * comprimidos (esta nueva carpeta es la que se le pasa al motor de descargas para guardar los ficheros descargados).
	 * Después se inicia el proceso de descargas pasándole los parámetros establecidos.
	 */
	private void download() {
		// creamos carpeta para la descarga dentro de la carpeta raíz y la establecemos como parámetro
		setSavePathParam(createDirectoryInRootPath(ZIP_DIRECTORY));
		log.info("Descargando...");
		downloadMode.executeDownload(parameters);	
		// recuperamos la carpeta raíz de descargas como parámetro 
		setSavePathParam(getRootSavePath());
	}
	
	/**
	 * Descomprime los ficheros zip obtenidos en una nueva carpeta dentro de la carpeta raíz de descargas.<br>
	 */
	private void unzipFiles() {
		String zipPath = getRootSavePath().concat(ZIP_DIRECTORY);
		String unZipPath = getRootSavePath();
		log.info("DESCOMPRIMIR FICHEROS");
		log.info("Ruta archivos zip: " + zipPath);
		log.info("Ruta donde extraer: " + unZipPath);
		log.info("Descomprimiendo... ");
		FileManager.unzipFiles(zipPath, unZipPath);
	}
	
	/**
	 * Comprueba si se han establecido los parámetros de descarga de tipo de fichero e idioma
	 * @return <i>true</i> si se han indicado ambos parámetros, <i>false</i> si alguno o ambos no se han indicado
	 */
	private boolean isUrlParameters() {
		return (fileType != null && language != null && !fileType.isEmpty() && !language.isEmpty());
	}
	
	/**
	 * @return el modo de descarga utilizado
	 */
	@Override
	public DownloadMode getDownloadMode() {
		return downloadMode.getType();
	}
	
	/**
	 * Establece el mode de realizar las descargas
	 * @see DownloadMode
	 */
	@Override
	public void setDownloadMode(DownloadMode mode) {
		if (mode == null) {
			throw new IllegalArgumentException("El modo de descargar no puede ser null");
		}
		downloadMode = DownloadFactory.create(mode);
	}

	/**
	 * Establece el tiempo de espera entre descargas, en milisegundos.
	 * @see DownloadParams
	 */
	@Override
	public void setDelay(int delay) {
		parameters.setDelay(delay);
	}
	
	/**
	 * Obtiene el tiempo de espera entre descargas establecido, en milisegundos.
	 * @return el tiempo de espera entre descargas
	 */
	@Override
	public int getDelay() {
		return parameters.getDelay();
	}
	
	@Override
	public void setOverwrite(boolean value) {
		parameters.setOverwrite(value);
	}
	
	@Override
	public boolean isOverwrite() {
		return parameters.isOverwrite();
	}

	@Override
	public String getUrlBase() {
		String url = null;
		if (parameters.getUrlBase() != null) {
			url = parameters.getUrlBase().toString();
		} else {
			url = "No se ha establecido URL.";
		}
		return url;
	}

	/**
	 * @return la ruta base de la carpeta inicial en la que realizar las descargas
	 */
	private String getRootSavePath() {
		return rootSavePath;
	}

	/**
	 * Establece la ruta base de la carpeta en la que realizar las descargas. La carpeta indicada será la carpeta raiz del proceso
	 * @param rootSavePath ruta de la carpeta
	 */
	private void setRootSavePath(String rootSavePath) {
		this.rootSavePath = rootSavePath;
	}
	
	/**
	 * Establece la ruta de la carpeta en la que guardar los archivos descargados. Se añade el caracter separador de directorios a la final de la
	 * ruta en caso de que no lo lleve.<br>Esta ruta se añade a los parámetros de la aplicación y se establece como ruta raiz del proceso.
	 * @param savePath ruta de la carpeta
	 * @throws IllegalArgumentException ruta errónea
	 */
	@Override
	public void setSavePath(String savePath) {
		if (savePath == null) {
			throw new IllegalArgumentException("La ruta del directorio donde guardar las descargas no puede ser null");
		}
		File f = new File(savePath);
		if (!f.exists()) {
			log.warn("Ruta donde guardar las descargas inexistente. Creando nuevo directorio.");
			f.mkdirs();
			if (!f.exists()) {
				throw new IllegalArgumentException("Imposible crear direcotrio para descargas");
			}
		}

		String fileSeparator = System.getProperty("file.separator");
		String path = (!savePath.endsWith(fileSeparator)) ? savePath.concat(fileSeparator) : savePath;
		setSavePathParam(path);
		setRootSavePath(path);
	}
	
	/**
	 * Asigna al objeto DownloadParams la ruta de una carpeta en la que guardar las descargas.
	 * @param path ruta de la carpeta
	 * @see DownloadParams
	 */
	private void setSavePathParam(String path) {
		parameters.setSavePath(path);
	}

	@Override
	public String getSavePath() {
		return getRootSavePath();
	}

	@Override
	public boolean isUnzip() {
		return parameters.isUnzip();
	}

	@Override
	public void setUnzip(boolean value) {
		parameters.setUnzip(value);
	}
	
	@Override
	public String getFileType() {
		return fileType;
	}

	@Override
	public JGutenbergDownload setFileType(String fileType) {
		this.fileType = fileType;
		return this;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public JGutenbergDownload setLanguage(String language) {
		this.language = language;
		return this;
	}

	/**
	 * @return el número máximo de ficheros a descargar
	 */
	@Override
	public int getMaxFilesToDownload() {
		return parameters.getMaxFilesToDownload();
	}

	/**
	 * Establece el número máximo de ficheros a descargar
	 * @param maxFilesToDownload número máximo de ficheros
	 * @throws IllegalArgumentException si se indica un número inferior a cero
	 */
	@Override
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		if (maxFilesToDownload < 0) {
			throw new IllegalArgumentException("maxFilesToDownload no puede ser inferior a cero");
		}
		parameters.setMaxFilesToDownload(maxFilesToDownload);
	}
	
	/**
	 * @see DownloadEngineType
	 */
	@Override
	public DownloadEngineType getEngineType() {
		return parameters.getEngineType();
	}

	/**
	 * Establece el tipo de motor a utilizar para realizar las descargas.<br>
	 * Lo asigna al atributo que encapsula los parámetros de la aplicación
	 * @see DownloadEngineType
	 */
	@Override
	public void setEngineType(DownloadEngineType engineType) {
		parameters.setEngineType(engineType);
	}
	
	/**
	 * Crea un nuevo directorio dentro de la carpeta base especificada para guardar las descargas.
	 * La carpeta creada se convierte en la nueva carpeta base. apuntada por el parámetro savePath y actualiza dicho parámetro
	 * con la nueva ruta
	 * @param nameDirectory nombre del directorio a crear
	 */
	private String createDirectoryInRootPath(String nameDirectory) {
		String fileSeparator = System.getProperty("file.separator");
		String savePath = getRootSavePath(); // parameters.getSavePath();
		if (savePath != null) {
			savePath = savePath.concat(nameDirectory).concat(fileSeparator);
		} else {
			savePath = System.getProperty("user.dir").concat(nameDirectory).concat(fileSeparator);
		}
		File dirPath = new File(savePath);
		dirPath.mkdirs();
		if (!dirPath.exists()) {
			log.warn("Imposible crear ruta " + savePath);	
			return null;
		}
		//parameters.setSavePath(savePath);
		return savePath;
	}
	
	private String getCurrentTime() {
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return hourFormat.format(date);
	}
	
	private void writeParamsLog() {
		log.info("Parámetros:");
		log.info(" urlBase = " + parameters.getUrlBase().toString());
		log.info(" savePath = " + parameters.getSavePath());
		log.info(" overwrite = " + parameters.isOverwrite());
		if (downloadMode.getType() == DownloadMode.SOFT) {
			log.info(" delay = " + parameters.getDelay());
		}
		log.info(" unzip = " + parameters.isUnzip());
		log.info(" engineType = " + parameters.getEngineType().toString());
		log.info(" downloadType = " + downloadMode.getType());
	}
}
