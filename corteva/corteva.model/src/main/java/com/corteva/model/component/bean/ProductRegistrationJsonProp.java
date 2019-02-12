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
public class ProductRegistrationJsonProp {

	/**
	 * variable uf.
	 * 
	 */
	@JsonProperty("UF")
	private String uf;

	/**
	 * variable presence.
	 * 
	 */
	@JsonProperty("presence")
	private int presence;

	/**
	 * variable name.
	 * 
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * variable regiao.
	 * 
	 */
	@JsonProperty("REGIAO")
	private String regiao;

	/**
	 * Gets the product id.
	 *
	 * @return the uf
	 */
	@JsonProperty("UF")
	public String getUf() {
		return uf;
	}

	/**
	 * Sets the product id.
	 *
	 * @param uf
	 *            the new product id
	 */
	@JsonProperty("UF")
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the presence
	 */
	@JsonProperty("presence")
	public int getPresence() {
		return presence;
	}

	/**
	 * Sets the product id.
	 *
	 * @param presence
	 *            the new product id
	 */
	@JsonProperty("presence")
	public void setPresence(int presence) {
		this.presence = presence;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Sets the product id.
	 *
	 * @param name
	 *            the new product id
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the regiao.
	 *
	 * @return the regiao
	 */
	@JsonProperty("REGIAO")
	public String getRegiao() {
		return regiao;
	}

	/**
	 * Sets the product id.
	 *
	 * @param regiao
	 *            the new product id
	 */
	@JsonProperty("REGIAO")
	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

}