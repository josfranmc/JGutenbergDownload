package org.josfranmc.gutenberg.download.client;

import org.apache.log4j.Logger;
import org.josfranmc.gutenberg.download.DownloadMode;
import org.josfranmc.gutenberg.download.DownloadParams;
import org.josfranmc.gutenberg.download.IGutenbergDownload;
import org.josfranmc.gutenberg.download.JGutenbergDownloadFactory;
import org.josfranmc.gutenberg.download.engine.DownloadEngineType;

/**
 * Clase que permite ejecutar un progrma cliente para realiza descargas de libros desde los repositorios del proyecto Gutenberg.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public class JGutenbergDownloadClient {

	private static final Logger log = Logger.getLogger(JGutenbergDownloadClient.class);
	
	private static IGutenbergDownload jg = null;
	
	private static String fileType = null;
	
	private static String language = null;
	
	private static DownloadMode downloadMode = null;
	
	private static DownloadParams params = null;
	
	
	/**
	 * Método principal de ejecución.
	 * @param args lista de argumentos pasados en la invocación del programa
	 */
	public static void main(String [] args){

		if (readParameters(args)) {
			jg = JGutenbergDownloadFactory.create();
			jg.setFileType(fileType);
			jg.setLanguage(language);
			jg.setSavePath(params.getSavePath());
			jg.setOverwrite(params.isOverwrite());
			jg.setDelay(params.getDelay());
			jg.setUnzip(params.isUnzip());
			jg.setMaxFilesToDownload(params.getMaxFilesToDownload());
			jg.setEngineType(params.getEngineType());
			if (downloadMode != null) {
				jg.setDownloadMode(downloadMode);
			}
			jg.downloadBooks();
		}
		System.exit(0);
	}
	
	/**
	 * Lee los parámetros de configuración pasados como argumentos. 
	 * @param args lista de parámetros con los valores que toman
	 * @return <i>true</i> si no hay ningún error, <i>false</i> en caso contrario
	 */
	private static boolean readParameters(String [] args) {
		log.debug("Total parámetros: " + args.length);
		boolean result = true;
		if (args.length == 0 || (args[0].equals("-h") || args[0].equals("-help"))) {
			showHelp();
			result = false;
		} else {
			params = new DownloadParams();
			for (int i = 0; i < args.length; i+=2) {
				try {
					log.debug("argumento " + args[i] + " valor " + args[i+1]);
					if (args[i].startsWith("-t")) {
						fileType = args[i+1];
					} else if (args[i].equals("-i")) {
						language = args[i+1];
					} else if (args[i].equals("-d")) {
						params.setDelay(Integer.parseInt(args[i+1]));
					} else if (args[i].equals("-s")) {
						params.setSavePath(args[i+1]);
					} else if (args[i].equals("-m")) {
						params.setMaxFilesToDownload(Integer.parseInt(args[i+1]));
					} else if (args[i].equals("-z")) {
						params.setUnzip(Boolean.valueOf(args[i+1]));
					} else if (args[i].equals("-o")) {
						params.setOverwrite(Boolean.valueOf(args[i+1]));
					} else if (args[i].equals("-e")) {
						params.setEngineType(DownloadEngineType.valueOf(args[i+1].toUpperCase()));
					} else if (args[i].equals("-x")) {
						downloadMode = DownloadMode.valueOf(args[i+1].toUpperCase());
					} else {
						System.out.println("Parámetro: " + args[i] + " no reconocido. Ejecute JGutenbergDownloadClient -h para listar opciones.");
						result = false;
					}
				} catch (ArrayIndexOutOfBoundsException a) {
					result = false;
					System.out.println("Error. Número incorrecto de parámetros");
					break;
				} 
				catch (Exception e) {
					result = false;
					System.out.println("Error al leer parámetro " + i + ". Parámetro = " + args[i] + ", valor = " + args[i+1]);
					e.printStackTrace();
					break;
				}
			}
		}
		return result;
	}

	private static void showHelp() {
		System.out.println("Opciones:");
		System.out.println("   [-t tipo_fichero, por defecto txt]");
		System.out.println("   [-i idioma, por defecto en]");
		System.out.println("   [-d tiempo de espera en milisegundos, por defecto 2000]");
		System.out.println("   [-s ruta descarga]");
		System.out.println("   [-m total ficheros a descargar]");
		System.out.println("   [-z descomprimir (true/false), por defecto true]");
		System.out.println("   [-o sobreescribir existentes (true/false), por defecto false]");
		System.out.println("   [-e tipo motor de descarga]");
		System.out.println("   [-x modo de descarga, por defecto SOFT]");
		System.out.println("");
		System.out.println("(indicar solo -h para mostrar lista de opciones)");
		System.out.println("");
	}
}
