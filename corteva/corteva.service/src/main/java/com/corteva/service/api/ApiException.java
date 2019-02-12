package com.corteva.service.api;

/**
 * The Class ApiException.
 */
public class ApiException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The code. */
	private int code;
	
	/**
	 * Instantiates a new api exception.
	 * @param throwable the throwable
	 */
	public ApiException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Instantiates a new api exception.
	 * @param message the message
	 */
	public ApiException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new api exception.
	 * @param code the code
	 * @param message the message
	 */
	public ApiException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	/**
	 * Instantiates a new api exception.
	 * @param code the code
	 * @param message the message
	 * @param throwable the throwable
	 */
	public ApiException(int code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}
	
	/**
	 * Gets the code.
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
}
