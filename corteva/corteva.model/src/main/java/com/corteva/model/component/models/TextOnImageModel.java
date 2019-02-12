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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.core.utils.CommonUtils;

/**
 * The is the sling model for the TextOnImage Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class TextOnImageModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TextOnImageModel.class);

	/** The hero image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("mobfileReference")
	private String mobileImagePath;

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
	public TextOnImageModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the mobile image path.
	 *
	 * @return the mobile image path
	 */
	public String getMobileImagePath() {
		LOGGER.debug("Inside getHeroImageJson() method");
		ImageRenditionBean imgRenBean = getImageRenditionList(mobileImagePath,
				CommonUtils.getComponentName(resourceType), slingRequest);
		return imgRenBean.getMobileImagePath();
	}

}