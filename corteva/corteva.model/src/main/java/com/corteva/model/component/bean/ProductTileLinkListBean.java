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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the bean class for Product Tile Link List.
 * 
 * @author Sapient
 */
@JsonInclude(Include.NON_EMPTY)
public class ProductTileLinkListBean {
	
	/** Image Logo Path. */
	private String imageLogoPath;
	
	/** Image Logo altText. */
	private String logoAltText;
	
	/** Image Logo destination URL. */
	private String imageLogoUrl;
	
	/** Call to Action Logo URL. */
    private String logoCallToAction;
	
    /** The resource section label. */
    private String sectionLabel;

    /** The resource section description. */
    private String sectionDescription;
    
    /** Resource Link Type. */
	private String resourceLinkType;
	
	/** Resource Link Label. */
	private String resourceLinkLabel;
	
	/** Resource Link Destination URL. */
	private String resourceLinkDestinationUrl;
	
	/** The resource link style. */
    private String resourceLinkStyle;
    
	/** The ResourceLink Bean Array list . */
	private List<ResourceLinkBean> resourceLinkList = new ArrayList<>();
	
	/**
	 * Gets the Image Logo Path.
	 *
	 * @return the imageLogoPath
	 */
	public String getImageLogoPath() {
		return imageLogoPath;
	}

	/**
	 * Sets the Image logo path.
	 *
	 * @param imageLogoPath the imageLogoPath to set
	 */
	public void setImageLogoPath(String imageLogoPath) {
		this.imageLogoPath = imageLogoPath;
	}
	
	/**
	 * Gets the Alternate Text.
	 *
	 * @return the altText
	 */
	public String getLogoAltText() {
		return logoAltText;
	}

	/**
	 * Sets the Alternate text.
	 *
	 * @param logoAltText the logoAltText to set
	 */
	public void setLogoAltText(String logoAltText) {
		this.logoAltText = logoAltText;
	}
	
	/**
	 * Gets the Image Destination URL.
	 *
	 * @return the imageLogoUrl
	 */
	public String getImageLogoUrl() {
		return imageLogoUrl;
	}

	/**
	 * Sets the Image Destination URL.
	 *
	 * @param imageLogoUrl the imageLogoUrl to set
	 */
	public void setImageLogoUrl(String imageLogoUrl) {
		this.imageLogoUrl = imageLogoUrl;
	}
	
	/**
	 * Gets the Logo Call to Action.
	 *
	 * @return the logoCallToAction
	 */
	public String getLogoCallToAction() {
		return logoCallToAction;
	}

	/**
	 * Sets the Logo Call to Action.
	 *
	 * @param logoCallToAction the logoCallToAction to set
	 */
	public void setLogoCallToAction(String logoCallToAction) {
		this.logoCallToAction = logoCallToAction;
	}

	/**
	 * Gets the section label .
	 *
	 * @return the sectionLabel
	 */
	public String getSectionLabel() {
		return sectionLabel;
	}

	/**
	 * Sets the section label.
	 *
	 * @param sectionLabel the sectionLabel to set
	 */
	public void setSectionLabel(String sectionLabel) {
		this.sectionLabel = sectionLabel;
	}

	/**
	 * Gets the section description.
	 *
	 * @return the sectionDescription
	 */
	public String getSectionDescription() {
		return sectionDescription;
	}

	/**
	 * Sets the section description.
	 *
	 * @param sectionDescription the sectionDescription to set
	 */
	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}
	
	/**
	 * Gets the Resource Link Type.
	 *
	 * @return the resourceLinkType
	 */
	public String getResourceLinkType() {
		return resourceLinkType;
	}

	/**
	 * Sets the Resource Link Type.
	 *
	 * @param resourceLinkType the resourceLinkType to set
	 */
	public void setResourceLinkType(String resourceLinkType) {
		this.resourceLinkType = resourceLinkType;
	}
	
	/**
	 * Gets the Resource Link Label.
	 *
	 * @return the resourceLinkLabel
	 */
	public String getResourceLinkLabel() {
		return resourceLinkLabel;
	}

	/**
	 * Sets the Resource Link Label.
	 *
	 * @param resourceLinkLabel the resourceLinkLabel to set
	 */
	public void setResourceLinkLabel(String resourceLinkLabel) {
		this.resourceLinkLabel = resourceLinkLabel;
	}
	
	/**
	 * Gets the Resource Link Destination URL.
	 *
	 * @return the resourceLinkDestinationUrl
	 */
	public String getResourceLinkDestinationUrl() {
		return resourceLinkDestinationUrl;
	}

	/**
	 * Sets the Resource Link Destination URL.
	 *
	 * @param resourceLinkDestinationUrl the resourceLinkDestinationUrl to set
	 */
	public void setResourceLinkDestinationUrl(String resourceLinkDestinationUrl) {
		this.resourceLinkDestinationUrl = resourceLinkDestinationUrl;
	}
	

	/**
     * Gets the resource link style .
     *
     * @return the resource link style
     */
    public String getResourceLinkStyle() {
		return resourceLinkStyle;
	}

	/**
	 * Sets the resource link style.
	 *
	 * @param resourceLinkStyle the new resource link style
	 */
	public void setResourceLinkStyle(String resourceLinkStyle) {
		this.resourceLinkStyle = resourceLinkStyle;
	}
	
	/**
	 * @return the resourceLinkList
	 */
	public List<ResourceLinkBean> getResourceLinkList() {
		return resourceLinkList;
	}

	/**
	 * @param resourceLinkList the resourceLinkList to set
	 */
	public void setResourceLinkList(List<ResourceLinkBean> resourceLinkList) {
		this.resourceLinkList = resourceLinkList;
	}

}