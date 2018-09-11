package org.josfranmc.gutenberg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * Ofrece herramientas para el tratamiento de ficheros.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 *
 */
public class FileManager {

	private final static Logger log = Logger.getLogger(FileManager.class);
	
	private final static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	
	/**
	 * Comprueba si existe en una ruta del equipo local el fichero que se puede obtener con la descarga de un determinado enlace.
	 * @param savePath ruta del equipo local
	 * @param link enlace del fichero
	 * @return <i>true</i> si en la ruta indicada existe el fichero del enlace indicado, <i>false</i> en caso contrario
	 */
	public static boolean fileExists(String savePath, String link) {
    	return new File(getLocalFilePathFromURL(savePath, link)).exists();
    }
	
	/**
	 * Devuelve la ruta que debe tener en el equipo local un fichero que va a descargarse en dicho equipo.<br>
	 * La ruta completa del fichero se forma concatenando dos elementos: por un lado, la ruta de la carpeta donde se va a guardar, y por otro,
	 * el nombre del fichero contenido en el enlace de descarga (el nombre de fichero es la parte final del enlace desde el último caracter separador de directorios)<br>.
	 * Si el nombre del fichero contiene los caracteres ? o &amp;amp; se sustituyen por _ y &amp; respectivamente.
	 * @param savePath ruta de la carpeta en la que obtener la descarga
	 * @param link enlace del fichero a descargar
	 * @return la ruta del fichero en el equipo local
	 */
	public static String getLocalFilePathFromURL(String savePath, String link) {
		String path = (!savePath.endsWith(FileManager.FILE_SEPARATOR)) ? savePath.concat(FileManager.FILE_SEPARATOR) : savePath;
		String fileName = link.substring(link.lastIndexOf("/")+1, link.length())
				              .replace("?", "_")
				              .replace("&amp;", "&");
		return (path + fileName);
    }
	
	/**
	 * Devuelve el nombre del archivo contenido en una ruta.
	 * @param str ruta del archivo
	 * @return el nombre del archivo
	 */
	public static String getLocalFileName(String str) {
		return str.substring(str.lastIndexOf(FileManager.FILE_SEPARATOR)+1, str.length())
	              .replace("?", "_")
	              .replace("&amp;", "&");
	}
	
	/**
	 * Descomprime los ficheros comprimidos en formato zip de una determinada carpeta.
	 * @param inputPath ruta del directorio donde se encuentran los ficheros zip
	 * @param outputPath ruta del directorio en el que descomprimir los ficheros
	 */
	public static void unzipFiles(String inputPath, String outputPath) {
		if (inputPath != null && outputPath != null) {
			FileOutputStream fos = null;
			String fileName = null;
			byte[] buffer = new byte[1024];
			int len = 0;
			int index = -1;
			if (!outputPath.endsWith(FILE_SEPARATOR)) {
				outputPath = outputPath.concat(FILE_SEPARATOR);
			}
			List<String> zipFiles = getZipFiles(inputPath);
			for (String zipFile : zipFiles) {
				log.debug("Descomprimiendo " + zipFile);
				try {
					ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
					ZipEntry zipEntry = zis.getNextEntry();
			        while (zipEntry != null) {
			            fileName = zipEntry.getName();
						if ((index = fileName.lastIndexOf("/")) != -1) {
			            	fileName = fileName.substring(index+1);
			            }
			           	fos = new FileOutputStream(new File(outputPath.concat(fileName)));
			            while ((len = zis.read(buffer)) > 0) {
			                fos.write(buffer, 0, len);
			            }
			            fos.close();
			            zipEntry = zis.getNextEntry();
			        }
			        zis.closeEntry();
			        zis.close();
			        log.debug("Obtenido " + outputPath + fileName);
				} catch (FileNotFoundException e) {
					log.error(e);
					log.error("OutputPath: " + outputPath + " FileName: " + fileName);
				} catch (IOException e) {
					log.error(e);
					log.error("OutputPath: " + outputPath + " FileName: " + fileName);
				}
			}
		} else {
			log.warn("Rutas no válidas");
		}
	}
	
	/**
	 * Devuelve una lista con las rutas de todos los ficheros con extensión .zip contenidos en un determinado directorio.
	 * @param path ruta del directorio en el que buscar los ficheros
	 * @return lista con las rutas de los ficheros
	 */
	private static List<String> getZipFiles(String path) {
		List<String> zipFiles = new ArrayList<String>();
		File folder = new File(path);
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				if (file.getName().endsWith(".zip")) {
					zipFiles.add(file.getAbsolutePath());
				}
			}
		}
		return zipFiles;
	}
}
