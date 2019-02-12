package com.corteva.service.product.detail.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class OshaHazardClasses.
 */
public class OshaHazardClasses {
	
	/** The osha hazard class. */
	@JsonProperty("osha_hazard_class")
	private List<OshaHazardClass> oshaHazardClass;
	
	/**
	 * Gets the osha hazard class.
	 * @return the oshaHazardClass
	 */
	public List<OshaHazardClass> getOshaHazardClass() {
		return oshaHazardClass;
	}
	
	/**
	 * Sets the osha hazard class.
	 * @param oshaHazardClass the oshaHazardClass to set
	 */
	public void setOshaHazardClass(List<OshaHazardClass> oshaHazardClass) {
		this.oshaHazardClass = oshaHazardClass;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OshaHazardClasses [oshaHazardClass=" + oshaHazardClass + "]";
	}
	
}
