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

/**
 * The is the sling model for the one two column feature component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class TwoColumnFeatureModel extends AbstractSlingModel {

	/** The label. */
	@Inject
	@Optional
	@Via("resource")
	@Named("label")
	private String label;

	/** The link label. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;

	/** The link url. */
	@Inject
	@Optional
	@Via("resource")
	@Named("bodyText")
	private String bodyText;
	
	/** The image alignment. */
	@Inject
	@Optional
	@Via("resource")
	@Named("imageAlignment")
	private String imageAlignment;
	
	/** The text only body text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("textOnlyBodyText")
	private String textOnlyBodyText;

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
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
	 * Gets the body text.
	 *
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * Gets the image alignment.
	 *
	 * @return the imageAlignment
	 */
	public String getImageAlignment() {
		return imageAlignment;
	}

	/**
	 * Gets the text only body text.
	 *
	 * @return the textOnlyBodyText
	 */
	public String getTextOnlyBodyText() {
		return textOnlyBodyText;
	}
}