/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.models;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * The is the sling model for the Product Tile and Linked List.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class HybridTableModel extends AbstractSlingModel {

	/** The crop icon tile. */
	@Inject
	@Optional
	@Via("resource")
	@Named("cropIconTile")
	private String cropIconTile;

	/** The table title */
	@Inject
	@Optional
	@Via("resource")
	@Named("tableTitle")
	private String tableTitle;

	/** The tabledata */
	@Inject
	@Optional
	@Via("resource")
	@Named("tableData")
	private String tableData;

	/** The padding option. */
	@Inject
	@Optional
	@Via("resource")
	@Named("paddingOptions")
	private String paddingOptions;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public HybridTableModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * This method gets the image rendition json for hero.
	 *
	 * @return the hero image json
	 */
	public ImageRenditionBean getCropIconTile() {
		return getImageRenditionList(cropIconTile, "icon", slingRequest);
	}

	/**
	 * Gets the Product Registered States US.
	 *
	 * 
	 * @return displaySidebar
	 */
	public String getTableTitle() {
		return tableTitle;
	}

	/**
	 * Process.
	 *
	 * @param tableTitle
	 *            the Table Title
	 */
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	/**
	 * Gets the Table Data.
	 *
	 * 
	 * @return tableData
	 */
	public String getTableData() {
		return tableData;
	}

	/**
	 * Process.
	 *
	 * @param tableData
	 *            the hybrid table data list
	 */
	public void setTableData(String tableData) {
		this.tableData = tableData;
	}
	
	/**
	 * Gets the Padding Options.
	 *
	 * 
	 * @return paddingOptions
	 */
	public String getPaddingOptions() {
		return paddingOptions;
	}

	/**
	 * 
	 * @param paddingOptions
	 *            the hybrid table paddingOptions list
	 */
	public void setPaddingOptions(String paddingOptions) {
		this.paddingOptions = paddingOptions;
	}

}
