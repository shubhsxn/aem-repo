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
 * The is the sling model for the Cards Container. This sling model will be used
 * to author cards container fields.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class CardsContainerModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CardsContainerModel.class);

	/** The hidden field for the component resource path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("cardResourceHidden")
	private Property cardResourceHidden;

	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;
	
	/** The optional title. */
	@Inject
	@Optional
	@Via("resource")
	@Named("optionalTitle")
	private String optionalTitle;

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
	@Named("cardType")
	private String cardType;

	/** The bg color. */
	@Inject
	@Optional
	@Via("resource")
	@Named("bgColor")
	private String bgColor;

	/**
	 * This method is used to get the responsive grid node name in sightly.
	 *
	 * @return the responsive grid node name
	 */
	public String getResponsiveGridNodeName() {
		LOGGER.debug("Inside getResponsiveGridNodeName() method of Cards Container Model");
		return getResponsiveGridNodeName(cardResourceHidden);
	}

	/**
	 * Gets the card type.
	 *
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * Gets the bg color.
	 *
	 * @return the bgColor
	 */
	public String getBgColor() {
		return bgColor;
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
	 * Gets the optional title.
	 *
	 * @return the optional title
	 */
	public String getOptionalTitle() {
		return optionalTitle;
	}

	/**
	 * Gets the body text.
	 *
	 * @return the body text
	 */
	public String getBodyText() {
		return bodyText;
	}
	
}