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
public class ProductRegistrationJsonGeometry {

	/**
	 * variable type.
	 * 
	 */
	@JsonProperty("type")
	private String type;

	/**
	 * variable coordinates.
	 * 
	 */
	@JsonProperty("coordinates")
	private Object coordinates;

	/**
	 * Gets the type.
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
	 * @return the coordinates
	 */
	@JsonProperty("coordinates")
	public Object getCoordinates() {
		return coordinates;
	}

	/**
	 * Sets the product id.
	 *
	 * @param coordinates
	 *            the new product id
	 */
	@JsonProperty("coordinates")
	public void setCoordinates(Object coordinates) {
		this.coordinates = coordinates;
	}

}