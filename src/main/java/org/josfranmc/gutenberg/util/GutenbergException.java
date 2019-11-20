package org.josfranmc.gutenberg.util;

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
