package com.corteva.service.product.detail.dto;

/**
 * The Class Registrations.
 */
public class Registrations {
	
	/** The registration. */
	private Registration registration;
	
	/**
	 * Gets the registration.
	 * @return the registration
	 */
	public Registration getRegistration() {
		return registration;
	}
	
	/**
	 * Sets the registration.
	 * @param registration the registration to set
	 */
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Registrations [registration=" + registration + "]";
	}
}
