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
 * The is the bean class for Anchor Navigation.
 * 
 * @author Sapient
 */
public class AnchorLinkBean {
	
	/** The anchor link label. */
	private String anchorLinkLabel;
	
	/** The anchor node name. */
	private String anchorNodeName;

	/**
	 * Gets the anchor link label.
	 *
	 * @return the anchorLinkLabel
	 */
	public String getAnchorLinkLabel() {
		return anchorLinkLabel;
	}

	/**
	 * Sets the anchor link label.
	 *
	 * @param anchorLinkLabel the anchorLinkLabel to set
	 */
	public void setAnchorLinkLabel(String anchorLinkLabel) {
		this.anchorLinkLabel = anchorLinkLabel;
	}

	/**
	 * Gets the anchor node name.
	 *
	 * @return the anchorNodeName
	 */
	public String getAnchorNodeName() {
		return anchorNodeName;
	}

	/**
	 * Sets the anchor node name.
	 *
	 * @param anchorNodeName the anchorNodeName to set
	 */
	public void setAnchorNodeName(String anchorNodeName) {
		this.anchorNodeName = anchorNodeName;
	}

	

}
