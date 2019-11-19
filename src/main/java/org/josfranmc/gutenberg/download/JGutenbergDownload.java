package org.josfranmc.gutenberg.download;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;
import org.josfranmc.gutenberg.util.GutenbergException;
import org.josfranmc.gutenberg.util.FileManager;

/**
 * It allows to download books from the Gutenberg project repositories.<br>
 * (<a href="http://www.gutenberg.org/">http://www.gutenberg.org</a>)
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 * @see DownloadParams
 * @see DownloadEngineType
 */
public class JGutenbergDownload {
	
	private static final Logger log = Logger.getLogger(JGutenbergDownload.class);

	private DownloadParams parameters;	
	
	
	/**
	 * Main constructor. It initializes the application with default values.
	 * @see DownloadParams
	 */
	public JGutenbergDownload() {
		parameters = new DownloadParams();
	}

	/**
	 * @return an object with the current parameters
	 * @see DownloadParams
	 */
	public DownloadParams getParameters() {
		return parameters;
	}
	
	/**
	 * Sets the application parameters through a <code>DownloadParams</code> object
	 * @param parameters application parameters
	 * @see DownloadParams
	 */
	public void setParameters(DownloadParams parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Begins the download books process.
	 * @throws GutenbergException if there is any error in the download
	 */
	public void downloadBooks() throws GutenbergException {
		try {
			printParameters();
			createDirectoriesForDownloads();
			log.info("BEGIN BOOKS DOWNLOAD " + getCurrentTime());
			DownloadBooks downloader = new DownloadBooks(parameters);
			log.info("Downloading...");
			downloader.executeDownload();
			if (parameters.isUnzip()) {
				unzipFiles();
			}
			log.info("END BOOKS DOWNLOAD " + getCurrentTime());
		} catch (GutenbergException e) {
			log.error(e.getCause());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDirectoriesForDownloads() {
		createBaseDirectory();
		createZipsDirectory();
	}
	
	private void createBaseDirectory() {
		File dirPath = new File(parameters.getSavePath());
		if (!dirPath.exists()) {
			log.warn("Path doesn't exist. Creating new directory.");
			dirPath.mkdirs();
			if (!dirPath.exists()) {
				throw new IllegalStateException("Cannot create directory for downloads");
			}
		}
	}
	
	private void createZipsDirectory() {
		File dirPath = new File(parameters.getZipsPath());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
			if (!dirPath.exists()) {
				throw new IllegalStateException("Cannot create directory for zips");	
			}
		}
	}
	
	private void unzipFiles() {
		String zipPath = parameters.getZipsPath();
		String unZipPath = parameters.getSavePath();
		log.info("UNZIP FILES");
		log.info("Zip files path: " + zipPath);
		log.info("Unzipping path: " + unZipPath);
		log.info("Unzipping... ");
		FileManager.unzipFiles(zipPath, unZipPath);
	}

	/**
	 * @return the type of files to download
	 */
	public String getFileType() {
		return parameters.getFileType();
	}

	/**
	 * Sets the type of file to download. Valid types: txt, epub, html
	 * @param fileType type of file to download
	 * @throws IllegalArgumentException if an invalid file type is indicated
	 */
	public void setFileType(String fileType) {
		parameters.setFileType(fileType);
	}

	/**
	 * @return the language of files to download
	 */
	public String getLanguage() {
		return parameters.getLanguage();
	}

	/**
	 * Sets the language of the files to download acording to Gutenberg nomeclature.<br>
	 * Ej.: <i>es</i> - spanish, <i>en</i> - english, <i>fr</i> - french
	 * @param language type of language
	 */
	public void setLanguage(String language) {
		parameters.setLanguage(language);
	}
	
	/**
	 * @return initial download url
	 */
	public String getUrlBase() {
		return parameters.getUrlBase().toString();
	}
	
	/**
	 * @return folder path where to get downloaded resources
	 */
	public String getSavePath() {
		return parameters.getSavePath();
	}
	
	/**
	 * Sets folder path where to get downloaded resources
	 * @param savePath folder path where to get downloaded resources
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public void setSavePath(String savePath) {
		parameters.setSavePath(savePath);
	}
	
	/**
	 * @return <i>true</i> if the existing files must be overwritten by the downloaded ones, <i>false</i> otherwise
	 */
	public boolean isOverwrite() {
		return parameters.isOverwrite();
	}
	
	/**
	 * Sets if the existing files must be overwritten by the downloaded ones
	 * @param value <i>true</i> if the existing files must be overwritten, <i>false</i> otherwise
	 */
	public void setOverwrite(boolean value) {
		parameters.setOverwrite(value);
	}
	
	/**
	 * @return delay between downloads, in milliseconds
	 */
	public int getDelay() {
		return parameters.getDelay();
	}
	
	/**
	 * Sets delay between downloads
	 * @param delay waiting time, in milliseconds
	 */
	public void setDelay(int delay) {
		parameters.setDelay(delay);
	}

	/**
	 * @return <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public boolean isUnzip() {
		return parameters.isUnzip();
	}

	/**
	 * Sets if downloaded files must be unzipping,
	 * @param value <i>true</i> if downloaded files must be unzipping, <i>false</i> otherwise
	 */
	public void setUnzip(boolean value) {
		parameters.setUnzip(value);
	}

	/**
	 * @return the maximum number of files to download
	 */
	public int getMaxFilesToDownload() {
		return parameters.getMaxFilesToDownload();
	}

	/**
	 * Sets the maximum number of files to download. The zero indicates downloading all available files.
	 * @param maxFilesToDownload files number
	 * @throws IllegalArgumentException if a number less than zero is indicated
	 */
	public void setMaxFilesToDownload(int maxFilesToDownload) {
		parameters.setMaxFilesToDownload(maxFilesToDownload);
	}
	
	/**
	 * @return the type of downloading engine to use
	 * @see DownloadEngineType
	 */
	public DownloadEngineType getEngineType() {
		return parameters.getEngineType();
	}

	/**
	 * Sets the type of downloading engine to use
	 * @param engineType engine type
	 * @see DownloadEngineType
	 */
	public void setEngineType(DownloadEngineType engineType) {
		parameters.setEngineType(engineType);
	}

	private String getCurrentTime() {
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return hourFormat.format(date);
	}
	
	private void printParameters() {
		log.info("Parameters:");
		log.info("  urlBase = " + parameters.getUrlBase().toString());
		log.info("  savePath = " + parameters.getSavePath());
		log.info("  overwrite = " + parameters.isOverwrite());
		log.info("  delay = " + parameters.getDelay());
		log.info("  unzip = " + parameters.isUnzip());
		String max = (parameters.getMaxFilesToDownload() == 0) ? "all" : Integer.toString(parameters.getMaxFilesToDownload());
		log.info("  maxDownloads = " + max);
	}
	
	/**
	 * Main method for running the application.
	 * @param args list of arguments with application parameters
	 */
	public static void main(String [] args){
		if (args.length == 0 || (args[0].equals("-h") || args[0].equals("-help"))) {
			showHelp();
		} else {
			DownloadParams params = getParametersFromCommandLine(args);
			if (params != null) {
				JGutenbergDownload jg = new JGutenbergDownload();
				jg.setParameters(params);				
				jg.downloadBooks();
			}
		}
		//System.exit(0);
	}
	
	/**
	 * Reads settings parameters from command line. 
	 * @param args list of parameters obtained from the command line
	 * @return a <code>DownloadParams</code> object or null if there is any error
	 */
	private static DownloadParams getParametersFromCommandLine(String [] args) {
		log.debug("Total parámetros: " + args.length);
		DownloadParams params = null;
		params = new DownloadParams();
		for (int i = 0; i < args.length; i+=2) {
			try {
				log.debug("argumento " + args[i] + " valor " + args[i+1]);
				if (args[i].startsWith("-f")) {
					params.setFileType(args[i+1]);
				} else if (args[i].equals("-l")) {
					params.setLanguage(args[i+1]);
				} else if (args[i].equals("-s")) {
					params.setSavePath(args[i+1]);
				} else if (args[i].equals("-o")) {
					params.setOverwrite(Boolean.valueOf(args[i+1]));
				} else if (args[i].equals("-d")) {
					params.setDelay(Integer.parseInt(args[i+1]));
				} else if (args[i].equals("-z")) {
					params.setUnzip(Boolean.valueOf(args[i+1]));
				} else if (args[i].equals("-m")) {
					params.setMaxFilesToDownload(Integer.parseInt(args[i+1]));
				///} else if (args[i].equals("-e")) {
				//	params.setEngineType(DownloadEngineType.valueOf(args[i+1].toUpperCase()));
				} else {
					System.out.println("Parameter: " + args[i] + " unrecognized. Run JGutenbergDownload -h to show options.");
					params = null;
					break;
				}
			} catch (ArrayIndexOutOfBoundsException a) {
				params = null;
				System.out.println("[ERROR] Incorrect number of parameters");
				break;
			} 
			catch (Exception e) {
				params = null;
				System.out.println("[ERROR] reading parameter " + i + ". Parameter = " + args[i] + ", Value = " + args[i+1]);
				log.error(e);
				break;
			}
		}
		return params;
	}
	
	private static void showHelp() {
		System.out.println("");
		System.out.println("Options:");
		System.out.println("   -f type of files to download (default txt)");
		System.out.println("   -l language of books to download (default es)");
		System.out.println("   -s download path on local machine (default program's folder)");
		System.out.println("   -o overwrite existing files (default false)");
		System.out.println("   -d delay between downloads in milliseconds (default 2000)");
		System.out.println("   -z unzip downloads (default true)");
		System.out.println("   -m max number of downloads (default 10, 0 for dowload all)");
		System.out.println("");
		System.out.println("(only -h to show options list)");
		System.out.println("");
	}
}
