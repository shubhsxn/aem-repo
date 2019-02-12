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
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the bean class for Feature Link List.
 * 
 * @author Sapient
 */
@JsonInclude(Include.NON_EMPTY)
public class FeatureLinkListBean {
	
    /** The list item path. */
    private String listItemPath;

    /** The list item title. */
    private String listItemTitle;

	/**
	 * Gets the list item path.
	 *
	 * @return the listItemPath
	 */
	public String getListItemPath() {
		return listItemPath;
	}

	/**
	 * Sets the list item path.
	 *
	 * @param listItemPath the listItemPath to set
	 */
	public void setListItemPath(String listItemPath) {
		this.listItemPath = listItemPath;
	}

	/**
	 * Gets the list item title.
	 *
	 * @return the listItemTitle
	 */
	public String getListItemTitle() {
		return listItemTitle;
	}

	/**
	 * Sets the list item title.
	 *
	 * @param listItemTitle the listItemTitle to set
	 */
	public void setListItemTitle(String listItemTitle) {
		this.listItemTitle = listItemTitle;
	}
}