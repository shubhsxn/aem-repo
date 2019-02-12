package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class Chemicals.
 */
public class Chemicals {
	
	/** The chemical. */
	private List<Chemical> chemical;
	
	/**
	 * Gets the chemical.
	 * @return the chemical
	 */
	public List<Chemical> getChemical() {
		return chemical;
	}
	
	/**
	 * Sets the chemical.
	 * @param chemical the chemical to set
	 */
	public void setChemical(List<Chemical> chemical) {
		this.chemical = chemical;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Chemicals [chemical=" + chemical + "]";
	}
}
