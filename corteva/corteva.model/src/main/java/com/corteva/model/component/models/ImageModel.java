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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

/**
 * The is the sling model for the Image.
 * 
 * @author Sapient
 */
@Model(adaptables = { Resource.class })
public class ImageModel extends AbstractSlingModel {
	
	/** The image title. */
	@Inject
	@Optional
	@Named("imageTitle")
	private String imageTitle;
	
	/** The image caption. */
	@Inject
	@Optional
	@Via("resource")
	@Named("imageCaption")
	private String imageCaption;

	/**
	 * Gets the image title.
	 *
	 * @return the imageTitle
	 */
	public String getImageTitle() {
		return imageTitle;
	}

	/**
	 * Gets the image caption.
	 *
	 * @return the imageCaption
	 */
	public String getImageCaption() {
		return imageCaption;
	}
}