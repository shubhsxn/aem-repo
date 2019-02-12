package com.corteva.service.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Registrations.
 */
public class Registrations {
	
	/** The registration. */
	@JsonIgnore
	private Registration registration;
	
	/**
	 * Gets the registration.
	 *
	 * @return the registration
	 */
	public Registration getRegistration() {
		return registration;
	}
	
	/**
	 * Sets the registration.
	 *
	 * @param registration the new registration
	 */
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " [registration = " + registration + "]";
	}
	
}
