package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class PlantBackRestrictions.
 */
public class PlantBackRestrictions {
	
	/** The restriction. */
	private List<Restriction> restriction;
	
	/** The available. */
	private String available;
	
	/**
	 * Gets the restriction.
	 * @return the restriction
	 */
	public List<Restriction> getRestriction() {
		return restriction;
	}
	
	/**
	 * Sets the restriction.
	 * @param restriction the restriction to set
	 */
	public void setRestriction(List<Restriction> restriction) {
		this.restriction = restriction;
	}
	
	/**
	 * Gets the available.
	 * @return the available
	 */
	public String getAvailable() {
		return available;
	}
	
	/**
	 * Sets the available.
	 * @param available the available to set
	 */
	public void setAvailable(String available) {
		this.available = available;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlantBackRestrictions [restriction=" + restriction + ", available=" + available + "]";
	}
	
}
