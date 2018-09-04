package org.josfranmc.gutenberg.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

/**
 * Ofrece herramientas para analizar ficheros en busca de enlaces web.
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 */
public class FileScraping {
	
	private static final Logger log = Logger.getLogger(FileScraping.class);
	
	/**
	 * Codificación del archivo a analizar
	 */
	private static String ENCODING = "ISO-8859-1";
	
	/**
	 * Patrón de la expresión regular a utilizar para detectar enlaces web
	 */
	private static String PATTERN = ".*href=\"([^\"]*)\".*";

	
	/**
	 * Constructor por defecto.
	 */
	public FileScraping() {}
	
	/**
	 * Obtiene una lista con todos los enlaces contenidos dentro de un fichero HTML.<br>
	 * Los enlaces son extraidos de los atributos href de los elementos &lt;a&gt;.
	 * @param filePath ruta del fichero a analizar
	 * @return lista de enlaces
	 */
	public static List<String> getLinks(String filePath) {
		List<String> links = new ArrayList<String>();
		if (filePath != null) {
			Path path = Paths.get(filePath);
	    	links = new ArrayList<String>();
	    	try (Stream<String> stream = Files.lines(path, Charset.forName(ENCODING))) {
	    		Pattern pattern = Pattern.compile(PATTERN);
	    		Matcher matcher = null;
	    		
	    		Iterator<String> it = stream.iterator();
	    		while (it.hasNext()) {
	    			String line = it.next();
	    			matcher = pattern.matcher(line);
	    			if (matcher.matches()) {
	    				links.add(matcher.group(1));
	    			}
	    		}
			} catch (IOException e) {
				log.error(e);
			}
    	}
		return links;		
	}
}
