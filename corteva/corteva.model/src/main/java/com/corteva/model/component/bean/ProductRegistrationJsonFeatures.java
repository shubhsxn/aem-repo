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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the bean class for product information.
 *
 * @author Sapient
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRegistrationJsonFeatures {

	/**
	 * variable type.
	 * 
	 */
	@JsonProperty("type")
	private String type;

	/**
	 * variable id.
	 * 
	 */
	@JsonProperty("id")
	private Integer id;

	/**
	 * variable properties.
	 * 
	 */
	@JsonProperty("properties")
	private ProductRegistrationJsonProp properties;

	/**
	 * variable geometry.
	 * 
	 */
	@JsonProperty("geometry")
	private ProductRegistrationJsonGeometry geometry;

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
	 * @return the id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the product id.
	 *
	 * @param id
	 *            the new product id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the properties
	 */
	@JsonProperty("properties")
	public ProductRegistrationJsonProp getProperties() {
		return properties;
	}

	/**
	 * Sets the product properties.
	 *
	 * @param properties
	 *            the new product id
	 */
	@JsonProperty("properties")
	public void setProperties(ProductRegistrationJsonProp properties) {
		this.properties = properties;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the geometry
	 */
	@JsonProperty("geometry")
	public ProductRegistrationJsonGeometry getGeometry() {
		return geometry;
	}

	/**
	 * Sets the product id.
	 *
	 * @param geometry
	 *            the new product id
	 */
	@JsonProperty("geometry")
	public void setGeometry(ProductRegistrationJsonGeometry geometry) {
		this.geometry = geometry;
	}

}