package com.corteva.service.api;

/**
 * The Class ApiResponse.
 * @param <T> the generic type
 */
public class ApiResponse<T> {
	
	/** The data. */
	private final T data;
	
	/**
	 * Instantiates a new api response.
	 * @param data the data
	 */
	public ApiResponse(T data) {
		this.data = data;
	}
	
	/**
	 * Gets the data.
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	
}
