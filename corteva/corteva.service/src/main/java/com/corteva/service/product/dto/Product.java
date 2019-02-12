package com.corteva.service.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Product.
 */
public class Product {
	
	/** The agrian product id. */
	@JsonProperty("agrian_product_id")
	private String agrianProductId;
	
	/** The company id. */
	@JsonProperty("company_id")
	private String companyId;
	
	/** The company name. */
	@JsonProperty("company_name")
	private String companyName;
	
	/** The product name. */
	@JsonProperty("product_name")
	private String productName;
	
	/** The registrations. */
	private Registrations registrations;
	
	/** The last changed. */
	@JsonProperty("last_changed")
	private String lastChanged;
	
	/** The epa. */
	private String epa;
	
	/** The managing company id. */
	@JsonProperty("managing_company_id")
	private String managingCompanyId;
	
	/** The product types. */
	@JsonProperty("product_types")
	private ProductTypes productTypes;

	/**
	 * @return the agrianProductId
	 */
	public String getAgrianProductId() {
		return agrianProductId;
	}

	/**
	 * @param agrianProductId the agrianProductId to set
	 */
	public void setAgrianProductId(String agrianProductId) {
		this.agrianProductId = agrianProductId;
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the registrations
	 */
	public Registrations getRegistrations() {
		return registrations;
	}

	/**
	 * @param registrations the registrations to set
	 */
	public void setRegistrations(Registrations registrations) {
		this.registrations = registrations;
	}

	/**
	 * @return the lastChanged
	 */
	public String getLastChanged() {
		return lastChanged;
	}

	/**
	 * @param lastChanged the lastChanged to set
	 */
	public void setLastChanged(String lastChanged) {
		this.lastChanged = lastChanged;
	}

	/**
	 * @return the epa
	 */
	public String getEpa() {
		return epa;
	}

	/**
	 * @param epa the epa to set
	 */
	public void setEpa(String epa) {
		this.epa = epa;
	}

	/**
	 * @return the managingCompanyId
	 */
	public String getManagingCompanyId() {
		return managingCompanyId;
	}

	/**
	 * @param managingCompanyId the managingCompanyId to set
	 */
	public void setManagingCompanyId(String managingCompanyId) {
		this.managingCompanyId = managingCompanyId;
	}

	/**
	 * @return the productTypes
	 */
	public ProductTypes getProductTypes() {
		return productTypes;
	}

	/**
	 * @param productTypes the productTypes to set
	 */
	public void setProductTypes(ProductTypes productTypes) {
		this.productTypes = productTypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product [agrianProductId=" + agrianProductId + ", companyId=" + companyId + ", companyName="
				+ companyName + ", productName=" + productName + ", registrations=" + registrations + ", lastChanged="
				+ lastChanged + ", epa=" + epa + ", managingCompanyId=" + managingCompanyId + ", productTypes="
				+ productTypes + "]";
	}
	
}
