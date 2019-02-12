package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The Class AgrianRegulatoryData.
 */
@JacksonXmlRootElement(localName = "agrian_regulatory_data")
public class AgrianRegulatoryData {
	
	/** The agrian product. */
	@JsonProperty("agrian_product")
	private AgrianProduct agrianProduct;
	
	/**
	 * Gets the agrian product.
	 * @return the agrianProduct
	 */
	public AgrianProduct getAgrianProduct() {
		return agrianProduct;
	}
	
	/**
	 * Sets the agrian product.
	 * @param agrianProduct the agrianProduct to set
	 */
	public void setAgrianProduct(AgrianProduct agrianProduct) {
		this.agrianProduct = agrianProduct;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AgrianRegulatoryData [agrianProduct=" + agrianProduct + "]";
	}
}
