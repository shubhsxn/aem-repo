package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class Placards.
 */
public class Placards {
	
	/** The placard. */
	private List<String> placard;
	
	/**
	 * Gets the placard.
	 * @return the placard
	 */
	public List<String> getPlacard() {
		return placard;
	}
	
	/**
	 * Sets the placard.
	 * @param placard the placard to set
	 */
	public void setPlacard(List<String> placard) {
		this.placard = placard;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Placards [placard=" + placard + "]";
	}
	
}
