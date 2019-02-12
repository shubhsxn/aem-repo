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
import javax.jcr.Property;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;

/**
 * The is the sling model for the Fixed Grid Tiles Container.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class FixedGridModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FixedGridModel.class);

	/** The fixed grid tiles component path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("fixedGridHidden")
	private Property fixedGridHidden;
	
	/** The background color. */
    @Inject
    @Optional
    @Via("resource")
    @Named("bgColor")
    private String bgColor;
    
    /** The Grid Layout. */
    @Inject
    @Optional
    @Via("resource")
    @Named("layout")
    private String layout;
    
    /** The Number of Expanded Tiles in Two Rows Layout. */
    @Inject
    @Optional
    @Via("resource")
    @Named("twoRowsExpandedTilesNumber")
    @Default(values = "2")
    private String twoRowsExpandedTilesNumber;
    
    /** The Number of Expanded Tiles in Three Rows Layout. */
    @Inject
    @Optional
    @Via("resource")
    @Named("threeRowsExpandedTilesNumber")
    @Default(values = "2")
    private String threeRowsExpandedTilesNumber;
    
    /** The Short Title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("shortTitle")
    private String shortTitle;

	/**
	 * This method is used to get the responsive grid node name in sightly.
	 *
	 * @return the responsive grid node name
	 */
	public String getResponsiveGridNodeName() {
		LOGGER.debug("Inside getResponsiveGridNodeName() method of Experience Fragment Model");
		return getResponsiveGridNodeName(fixedGridHidden);
	}
	
	/**
	 * Gets the background color.
	 *
	 * @return the bgColor
	 */
	public String getBgColor() {
		return bgColor;
	}

	/**
	 * Gets the grid layout.
	 *
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * Gets the Number of Expanded Tiles in Two Rows Layout.
	 *
	 * @return the twoRowsExpandedTilesNumber
	 */
	public String getTwoRowsExpandedTilesNumber() {
		return twoRowsExpandedTilesNumber;
	}

	/**
	 * Gets the Number of Expanded Tiles in Three Rows Layout.
	 *
	 * @return the threeRowsExpandedTilesNumber
	 */
	public String getThreeRowsExpandedTilesNumber() {
		return threeRowsExpandedTilesNumber;
	}
	
	/**
	 * Gets the Short Title.
	 *
	 * @return the shortTitle
	 */
	public String getShortTitle() {
		return shortTitle;
	}
	
	/**
	 * Gets the grid layout.
	 *
	 * @return the layout
	 */
	public boolean isCropFirstExpandedTile() {
		boolean cropFirstExpandedTile = false;
		if (CortevaConstant.THREE_ROWS_LAYOUT.equals(layout) && 1 == Integer.parseInt(threeRowsExpandedTilesNumber)) {
			cropFirstExpandedTile = true;
		}
		LOGGER.debug("Inside isCropFirstExpandedTile() method of Experience Fragment Model. cropFirstExpandedTile :: {}", cropFirstExpandedTile);
		return cropFirstExpandedTile;
	}
	
}