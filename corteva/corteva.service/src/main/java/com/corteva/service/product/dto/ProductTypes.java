
package com.corteva.service.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Product_types.
 */
public class ProductTypes {
	
	/** The product type. */
	@JsonProperty("product_type")
	private List<String> productType;

	/**
	 * @return the productType
	 */
	public List<String> getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(List<String> productType) {
		this.productType = productType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductTypes [productType=" + productType + "]";
	}
	
}
