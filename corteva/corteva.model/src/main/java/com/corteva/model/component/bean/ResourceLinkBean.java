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
 * The is the Resource Link List bean class.
 * 
 * @author Sapient
 */
public class ResourceLinkBean {

    /** The resource type. */
    private String resourceType;
    
    /** The resource link style. */
    private String resourceLinkStyle;

    /** The resource label. */
    private String resourceLabel;
    
    /** The destination URL. */
    private String destinationUrl;
      
    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    public String getResourceType() {
		return resourceType;
	}

	/**
	 * Sets the resource type.
	 *
	 * @param resourceType the new resource type
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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
     * Gets the resource label.
     *
     * @return the resource label
     */
    public String getResourceLabel() {
		return resourceLabel;
	}

	/**
	 * Sets the resource label.
	 *
	 * @param resourceLabel the new resource label
	 */
	public void setResourceLabel(String resourceLabel) {
		this.resourceLabel = resourceLabel;
	}

	/**
	 * Gets the destination URL.
	 *
	 * @return the destination URL
	 */
	public String getDestinationUrl() {
		return destinationUrl;
	}

	/**
	 * Sets the destination URL.
	 *
	 * @param destinationUrl the new destination URL
	 */
	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

}
