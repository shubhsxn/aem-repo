package com.corteva.model.component.bean;

import java.util.List;

/**
 * The Class ProductFilterBean.
 */
public class ProductFilterChildBean implements Comparable<ProductFilterChildBean> {

	/** The product name. */
	private String productName;

	/** The product analytics name. */
	private String productAnalyticsName;

	/** The product path. */
	private String productPath;

	/** The description. */
	private String description;

	/** The product type tags. */
	private List<String> productTypeTags;

	/** The all tags. */
	private List<String> allTags;

	/**
	 * Gets the product type tags.
	 * 
	 * @return the product type tags
	 */
	public List<String> getProductTypeTags() {
		return productTypeTags;
	}

	/**
	 * Sets the product type tags.
	 * 
	 * @param productTypeTags
	 *            the new product type tags
	 */
	public void setProductTypeTags(List<String> productTypeTags) {
		this.productTypeTags = productTypeTags;
	}

	/**
	 * Gets the product name.
	 * 
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Sets the product name.
	 * 
	 * @param productName
	 *            the new product name
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Gets the product analytics name.
	 *
	 * @return the product analytics name
	 */
	public String getProductAnalyticsName() {
		return productAnalyticsName;
	}

	/**
	 * Sets the product analytics name.
	 *
	 * @param productAnalyticsName
	 *            the new product analytics name
	 */
	public void setProductAnalyticsName(String productAnalyticsName) {
		this.productAnalyticsName = productAnalyticsName;
	}

	/**
	 * Gets the product path.
	 * 
	 * @return the product path
	 */
	public String getProductPath() {
		return productPath;
	}

	/**
	 * Sets the product path.
	 * 
	 * @param productPath
	 *            the new product path
	 */
	public void setProductPath(String productPath) {
		this.productPath = productPath;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProductFilterChildBean productFilterBean) {
		return this.getProductName().compareTo(productFilterBean.getProductName());
	}

	/**
	 * override java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	/**
	 * override java.lang.Object#hashCode()
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Gets the all tags.
	 *
	 * @return the allTags
	 */
	public List<String> getAllTags() {
		return allTags;
	}

	/**
	 * Sets the all tags.
	 *
	 * @param allTags
	 *            the allTags to set
	 */
	public void setAllTags(List<String> allTags) {
		this.allTags = allTags;
	}

}
