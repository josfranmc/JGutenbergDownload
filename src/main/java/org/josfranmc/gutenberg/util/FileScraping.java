/*
 *  Copyright (C) 2018-2019 Jose Francisco Mena Ceca <josfranmc@gmail.com>
 *
 *  This file is part of JGutenbergDownload.
 *
 *  JGutenbergDownload is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JGutenbergDownload is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JGutenbergDownload.  If not, see <https://www.gnu.org/licenses/>.
 *    
 *  This file includes software developed at
 *  The Apache Software Foundation (http://www.apache.org/). 
 */ 
package org.josfranmc.gutenberg.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
 * Tools for searching web links in files.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
public class FileScraping {
	
	private static final Logger log = Logger.getLogger(FileScraping.class);
	
	/**
	 * Coding of the file to analyze
	 */
	private static final Charset ENCODING = StandardCharsets.ISO_8859_1;
	
	/**
	 * Regular expression pattern for detecting web links
	 */
	private static final String PATTERN = ".*href=\"([^\"]*)\".*";

	
	FileScraping() {
		throw new IllegalStateException("Cannot instantiate class");
	}
	
	/**
	 * Returns a list with all links within an html file.<br>
	 * The links are extracted from href attributes of <code>&lt;a&gt;</code> elements.
	 * @param filePath path of file to analyze
	 * @return a <code>List</code> element with links
	 */
	public static List<String> getLinks(String filePath) {
		List<String> links = new ArrayList<>();
		if (filePath != null) {
			Path path = Paths.get(filePath);
			try (Stream<String> stream = Files.lines(path, ENCODING)) {
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
