package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class HazardCodes.
 */
public class HazardCodes {
	
	/** The hazard code. */
	@JsonProperty("hazard_code")
	private String hazardCode;
	
	/**
	 * Gets the hazard code.
	 * @return the hazardCode
	 */
	public String getHazardCode() {
		return hazardCode;
	}
	
	/**
	 * Sets the hazard code.
	 * @param hazardCode the hazardCode to set
	 */
	public void setHazardCode(String hazardCode) {
		this.hazardCode = hazardCode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HazardCodes [hazardCode=" + hazardCode + "]";
	}
	
}
