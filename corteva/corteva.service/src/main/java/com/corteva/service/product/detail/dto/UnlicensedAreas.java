package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class UnlicensedAreas.
 */
public class UnlicensedAreas {
	
	/** The counties. */
	@JsonIgnore
	private Counties counties;
	
	/** The states. */
	private States states;
	
	/**
	 * Gets the counties.
	 * @return the counties
	 */
	public Counties getCounties() {
		return counties;
	}
	
	/**
	 * Sets the counties.
	 * @param counties the counties to set
	 */
	public void setCounties(Counties counties) {
		this.counties = counties;
	}
	
	/**
	 * Gets the states.
	 * @return the states
	 */
	public States getStates() {
		return states;
	}
	
	/**
	 * Sets the states.
	 * @param states the states to set
	 */
	public void setStates(States states) {
		this.states = states;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UnlicensedAreas [counties=" + counties + ", states=" + states + "]";
	}
}
