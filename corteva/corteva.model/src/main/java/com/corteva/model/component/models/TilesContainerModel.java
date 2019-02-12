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
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The is the sling model for the Tiles Container.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class TilesContainerModel extends AbstractSlingModel {

    /** Logger Instantiation. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TilesContainerModel.class);

    /** The hidden field for the component resource path. */
    @Inject
    @Optional
    @Via("resource")
    @Named("tileResourceHidden")
    private Property tileResourceHidden;
    
    /** The title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("title")
    private String title;
    
    /** The expanded title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("expandedTitle")
    private String expandedTitle;

	/** The body text. */
    @Inject
    @Optional
    @Via("resource")
    @Named("bodyText")
    private String bodyText;

    /** The card type. */
    @Inject
    @Optional
    @Via("resource")
    @Named("tileType")
    private String tileType;
    
    /** The background color. */
    @Inject
    @Optional
    @Via("resource")
    @Named("bgColor")
    private String bgColor;
    
    /** The icon alignment. */
    @Inject
    @Optional
    @Via("resource")
    @Named("iconAlign")
    private String iconAlign;

    /**
     * This method is used to get the responsive grid node name in sightly.
     *
     * @return the responsive grid node name
     */
    public String getResponsiveGridNodeName() {
        LOGGER.debug("Inside getResponsiveGridNodeName() method of Cards Container Model");
        return getResponsiveGridNodeName(tileResourceHidden);
    }

	/**
	 * Gets the tile resource hidden.
	 *
	 * @return the tileResourceHidden
	 */
	public Property getTileResourceHidden() {
		return tileResourceHidden;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gets the expanded title.
	 *
	 * @return the expanded title
	 */
	public String getExpandedTitle() {
		return expandedTitle;
	}

	/**
	 * Gets the body text.
	 *
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * Gets the tile type.
	 *
	 * @return the tileType
	 */
	public String getTileType() {
		return tileType;
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
	 * Gets the icon alignment.
	 *
	 * @return the iconAlign
	 */
	public String getIconAlign() {
		return iconAlign;
	}
	
}