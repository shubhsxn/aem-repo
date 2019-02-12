package com.corteva.service.product.dto;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The Class Agrian_products.
 */
@JacksonXmlRootElement(localName = "agrian_products")
public class AgrianProducts {
	
	/** The product. */
	private List<Product> product;
	
	/**
	 * Gets the product.
	 *
	 * @return the product
	 */
	public List<Product> getProduct() {
		return product;
	}
	
	/**
	 * Sets the product.
	 *
	 * @param product the new product
	 */
	public void setProduct(List<Product> product) {
		this.product = product;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[product = " + product + "]";
	}
}
