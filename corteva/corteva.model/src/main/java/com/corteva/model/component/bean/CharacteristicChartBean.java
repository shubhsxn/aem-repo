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
 * The is the sling model for getting the states list for different country.
 * author.
 * 
 * @author Sapient
 */
public class CharacteristicChartBean {
	
	/**
	 * variable labelSidebar.
	 * 
	 */
	private String labelSidebar;
	
	/**
	 * variable valueSidebar.
	 * 
	 */
	private String valueSidebar;

	/**
	 * Gets the Product Registered States US.
	 *
	 * 
	 * @return the labelSidebar
	 */
	public String getLabelSidebar() {
		return labelSidebar;
	}

	/**
	 * Process.
	 *
	 * @param labelSidebar
	 *            the state list
	 */
	public void setLabelSidebar(String labelSidebar) {
		this.labelSidebar = labelSidebar;
	}

	/**
	 * Gets the Product Registered States US.
	 *
	 * 
	 * @return the us states json
	 */
	public String getValueSidebar() {
		return valueSidebar;
	}

	/**
	 * Process.
	 *
	 * @param valueSidebar
	 *            the state list
	 */
	public void setValueSidebar(String valueSidebar) {
		this.valueSidebar = valueSidebar;
	}

}
