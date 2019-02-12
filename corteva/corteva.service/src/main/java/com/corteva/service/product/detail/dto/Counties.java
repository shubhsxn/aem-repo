package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class Counties.
 */
public class Counties {
	/** The county. */
	private List<County> county;
	
	/**
	 * Gets the county.
	 * @return the county
	 */
	public List<County> getCounty() {
		return county;
	}
	
	/**
	 * Sets the county.
	 * @param county the new county
	 */
	public void setCounty(List<County> county) {
		this.county = county;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Counties [county=" + county + "]";
	}
	
}
