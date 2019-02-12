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
 * The is the Link List bean class.
 * 
 * @author Sapient
 */
public class LinkListBean {

    /** The link label. */
    private String linkLabel;

    /** The menu has sub-nav dropdown. */
    private String hasDropdown;
    
    
    /** The country sel. */
    private String countrySel;
    
	/** The link style. */
    private String linkStyle;
    
    /** The link color. */
    private String linkColor;
      
    /**
     * Gets the link style.
     *
     * @return the link style
     */
    public String getLinkStyle() {
		return linkStyle;
	}

	/**
	 * Sets the link style.
	 *
	 * @param linkStyle the new link style
	 */
	public void setLinkStyle(String linkStyle) {
		this.linkStyle = linkStyle;
	}
	
	 /**
     * Gets the country sel.
     *
     * @return the country sel
     */
    public String getCountrySel() {
		return countrySel;
	}

	/**
	 * Sets the country sel.
	 *
	 * @param countrySel the new country sel
	 */
	public void setCountrySel(String countrySel) {
		this.countrySel = countrySel;
	}

	/**
	 * Gets the link color.
	 *
	 * @return the link color
	 */
	public String getLinkColor() {
		return linkColor;
	}

	/**
	 * Sets the link color.
	 *
	 * @param linkColor the new link color
	 */
	public void setLinkColor(String linkColor) {
		this.linkColor = linkColor;
	}

	/**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Sets the link label.
     *
     * @param linkLabel the new link label
     */
    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    /**
     * Gets the link url.
     *
     * @return the link url
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * Sets the link url.
     *
     * @param linkUrl the new link url
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * Gets the link action.
     *
     * @return the link action
     */
    public String getLinkAction() {
        return linkAction;
    }

    /**
     * Sets the link action.
     *
     * @param linkAction the new link action
     */
    public void setLinkAction(String linkAction) {
        this.linkAction = linkAction;
    }

    /**
     * Gets the checks for dropdown.
     *
     * @return the checks for dropdown
     */
    public String getHasDropdown() {
        return hasDropdown;
    }

    /**
     * Sets the checks for dropdown.
     *
     * @param hasDropdown the new checks for dropdown
     */
    public void setHasDropdown(String hasDropdown) {
        this.hasDropdown = hasDropdown;
    }

    /** The link url. */
    private String linkUrl;

    /** The link action. */
    private String linkAction;

}
