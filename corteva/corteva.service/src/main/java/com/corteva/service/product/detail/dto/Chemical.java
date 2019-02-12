package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Chemical.
 */
public class Chemical {
	
	/** The chemical description. */
	@JsonProperty("chemical_description")
	private String chemicalDescription;
	
	/** The percentage. */
	private String percentage;
	
	/** The name. */
	private String name;
	
	/** The cas number. */
	@JsonProperty("cas_number")
	private String casNumber;
	
	/** The chemical unit. */
	@JsonProperty("chemical_unit")
	private String chemicalUnit;
	
	/** The hazardous threshold. */
	@JsonProperty("hazardous_threshold")
	private String hazardousThreshold;
	
	/**
	 * Gets the chemical description.
	 * @return the chemicalDescription
	 */
	public String getChemicalDescription() {
		return chemicalDescription;
	}
	
	/**
	 * Sets the chemical description.
	 * @param chemicalDescription the chemicalDescription to set
	 */
	public void setChemicalDescription(String chemicalDescription) {
		this.chemicalDescription = chemicalDescription;
	}
	
	/**
	 * Gets the percentage.
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}
	
	/**
	 * Sets the percentage.
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the cas number.
	 * @return the casNumber
	 */
	public String getCasNumber() {
		return casNumber;
	}
	
	/**
	 * Sets the cas number.
	 * @param casNumber the casNumber to set
	 */
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	
	/**
	 * Gets the chemical unit.
	 * @return the chemicalUnit
	 */
	public String getChemicalUnit() {
		return chemicalUnit;
	}
	
	/**
	 * Sets the chemical unit.
	 * @param chemicalUnit the chemicalUnit to set
	 */
	public void setChemicalUnit(String chemicalUnit) {
		this.chemicalUnit = chemicalUnit;
	}
	
	/**
	 * Gets the hazardous threshold.
	 * @return the hazardousThreshold
	 */
	public String getHazardousThreshold() {
		return hazardousThreshold;
	}
	
	/**
	 * Sets the hazardous threshold.
	 * @param hazardousThreshold the hazardousThreshold to set
	 */
	public void setHazardousThreshold(String hazardousThreshold) {
		this.hazardousThreshold = hazardousThreshold;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Chemical [chemicalDescription=" + chemicalDescription + ", percentage=" + percentage + ", name=" + name
				+ ", casNumber=" + casNumber + ", chemicalUnit=" + chemicalUnit + ", hazardousThreshold="
				+ hazardousThreshold + "]";
	}
}
