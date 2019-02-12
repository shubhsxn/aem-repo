package com.corteva.service.product.detail.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class HazardClasses.
 */
public class HazardClasses {
	
	/** The hazard class. */
	@JsonProperty("hazard_classes")
	private List<String> hazardClass;
	
	/**
	 * Gets the hazard class.
	 * @return the hazardClass
	 */
	public List<String> getHazardClass() {
		return hazardClass;
	}
	
	/**
	 * Sets the hazard class.
	 * @param hazardClass the hazardClass to set
	 */
	public void setHazardClass(List<String> hazardClass) {
		this.hazardClass = hazardClass;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HazardClasses [hazardClass=" + hazardClass + "]";
	}
	
}
