package org.josfranmc.gutenberg.util;

/**
 * It allows to capture exceptions in the application in the form of a RuntimeException.
 * @author Jose Francisco Mena Ceca
 * @version 2.0
 */
public class GutenbergException extends RuntimeException {

	private static final long serialVersionUID = -8740400616606275937L;

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
