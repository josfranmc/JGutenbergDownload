package org.josfranmc.gutenberg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * Tools for managing files.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 *
 */
public class FileManager {

	private static final Logger log = Logger.getLogger(FileManager.class);
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	
	FileManager() {
		throw new IllegalStateException("Cannot instantiate class");
	}
	
	/**
	 * Checks if the file indicated by an url already exists in a local path.
	 * @param localPath local path
	 * @param url download url
	 * @return <i>true</i> if the file indicated by the url exists in the local path, <i>false</i> otherwise
	 */
	public static boolean fileExists(String localPath, String url) {
    	return new File(getLocalFilePathFromURL(localPath, url)).exists();
    }
	
	/**
	 * Returns the path a file must have when downloaded on the local machine.<br>
	 * The path is formed by concatenating two elements: on the one hand, tha path of the folder where the download will be saved,
	 * on the other hand, the name of the file in the download url (file name is at the end of the url, from last separator character of folders).<br>
	 * If the name of the file contains <i>?</i> and <i>&amp;amp;</i> characters  are replaced by <i>_</i> and <i>&amp;</i> respectively.
	 * @param savePath path of the folder in which to get the download
	 * @param link download link
	 * @return la ruta del fichero en el equipo local
	 */
	public static String getLocalFilePathFromURL(String savePath, String link) {
		String path = (!savePath.endsWith(FILE_SEPARATOR)) ? savePath + FILE_SEPARATOR : savePath;
		String fileName = link.substring(link.lastIndexOf('/')+1, link.length())
				              .replace("?", "_")
				              .replace("&amp;", "&");
		return (path + fileName);
    }
	
	/**
	 * Returns the name of file inside a path.
	 * @param str file path
	 * @return the name of file inside <code>str</code> parameter
	 */
	public static String getLocalFileName(String str) {
		return str.substring(str.lastIndexOf(FILE_SEPARATOR)+1, str.length())
	              .replace("?", "_")
	              .replace("&amp;", "&");
	}
	
	/**
	 * Unzips files in format zip from a certain folder.
	 * @param inputPath folder path with zip files
	 * @param outputPath folder path where unzip files
	 */
	public static void unzipFiles(String inputPath, String outputPath) {
		if (inputPath != null && outputPath != null) {
			inputPath = checkAndFixPath(inputPath);
			outputPath = checkAndFixPath(outputPath);
			for (String zipFile : getZipFiles(inputPath)) {
				try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
					ZipEntry zipEntry = zis.getNextEntry();
			        while (zipEntry != null) {
			        	String targetFile = outputPath + getFileName(zipEntry.getName()) ;
						extracFile(zis, targetFile);
						zipEntry = zis.getNextEntry();
			        }
				} catch (IOException e) {
					log.error("IOException with zip file" + zipFile);
				}	
			}
		} else {
			log.warn("Rutas no válidas");
		}
	}
	
	private static String checkAndFixPath(String path) {
		return (!path.endsWith(FILE_SEPARATOR)) ? (path + FILE_SEPARATOR) : path;
	}
	
	/**
	 * Returns a <code>List</code> with the paths of all existing files with <i>.zip</i> extension in a given folder.
	 * @param path folder path where search files
	 * @return a <code>List</code> with the files paths
	 */
	private static List<String> getZipFiles(String path) {
		List<String> zipFiles = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), "*.zip")) {
			for (Path file: stream) {
				zipFiles.add(file.toString());
			}
	     } catch (IOException e) {
	    	 log.error(e);
	     }
		return zipFiles;
	}
	
	private static String getFileName(String entryName) {
        String fileName = entryName;
        int index = -1;
		if ((index = fileName.lastIndexOf('/')) != -1) {
			fileName = fileName.substring(index+1);
			if (fileName.contains("..")) {
				throw new GutenbergException("Entry is trying to leave the target dir: " + entryName);
			}
		}
		return fileName;
	}
	
	private static void extracFile(ZipInputStream zis, String targetFile) {
		byte[] buffer = new byte[1024];
		int len = 0;
		try (FileOutputStream fos = new FileOutputStream(new File(targetFile))) {
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
		} catch (IOException e) {
			log.error("Error unzipping file " + targetFile);
		}
	}
}
