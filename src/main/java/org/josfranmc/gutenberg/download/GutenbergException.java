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
 */ 
package org.josfranmc.gutenberg.download;

/**
 * It allows to capture exceptions in the application in the form of a RuntimeException.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
@SuppressWarnings("serial")
public class GutenbergException extends RuntimeException {

	public GutenbergException() {
		super();
	}
	
	public GutenbergException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GutenbergException(String message) {
		super(message);
	}
	
	public GutenbergException(Throwable cause) {
		super(cause);
	}
}
