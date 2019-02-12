package com.corteva.model.component.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the bean class for product information.
 *
 * @author Sapient
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRegistrationJsonBean {

	/**
	 * variable type.
	 * 
	 */
	@JsonProperty("type")
	private String type;

	/**
	 * variable features.
	 * 
	 */
	@JsonProperty("features")
	private List<ProductRegistrationJsonFeatures> features = null;

	/**
	 * Gets the product id.
	 *
	 * @return the type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Sets the product id.
	 *
	 * @param type
	 *            the new product id
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the type
	 */
	@JsonProperty("features")
	public List<ProductRegistrationJsonFeatures> getFeatures() {
		return features;
	}

	/**
	 * Sets the product id.
	 *
	 * @param features
	 *            the new product id
	 */
	@JsonProperty("features")
	public void setFeatures(List<ProductRegistrationJsonFeatures> features) {
		this.features = features;
	}

}