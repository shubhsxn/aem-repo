/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.bean;

/**
 * This is the bean class for product information.
 *
 * @author Sapient
 */
public class ProductDetailBean implements Comparable<ProductDetailBean> {

	/** The product id. */
	private String productId;

	/** The product name. */
	private String productName;

	/** The product source. */
	private String productSource;

	/** The hide product. */
	private String hideProduct;

	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productId
	 *            the new product id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
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
	 * Gets the product source.
	 *
	 * @return the product source
	 */
	public String getProductSource() {
		return productSource;
	}

	/**
	 * Sets the product source.
	 *
	 * @param productSource
	 *            the new product source
	 */
	public void setProductSource(String productSource) {
		this.productSource = productSource;
	}

	/**
	 * Gets the hide product.
	 *
	 * @return the hide product
	 */
	public String getHideProduct() {
		return hideProduct;
	}

	/**
	 * Sets the hide product.
	 *
	 * @param hideProduct
	 *            the new hide product
	 */
	public void setHideProduct(String hideProduct) {
		this.hideProduct = hideProduct;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param o
	 *            the o
	 * @return the int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProductDetailBean o) {
		int flag = 0;
		if (null != this.productName && null != o.productName) {
			flag = this.productName.compareTo(o.productName);
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
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
}