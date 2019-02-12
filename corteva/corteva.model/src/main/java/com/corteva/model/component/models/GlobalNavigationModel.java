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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;

/**
 * The is the sling model for the Global Navigation.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class GlobalNavigationModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalNavigationModel.class);

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The logo url. */
	@Inject
	@Optional
	@Via("resource")
	@Named("logoUrl")
	private String logoUrl;

	/**
	 * Instantiates a new global navigation model.
	 *
	 * @param request
	 *            the request
	 */
	public GlobalNavigationModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the logo url.
	 *
	 * @return the logo url
	 */
	public String getLogoUrl() {
		LOGGER.debug("Inside getLogoUrl() method");
		String logoPath;
		if (StringUtils.isBlank(logoUrl)) {
			logoPath = getBaseConfigurationService().getPropertyValueFromConfiguration(
					CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePath(slingRequest), getResourceResolver()),
					CortevaConstant.CORTEVA_ROOT_PATH, CortevaConstant.GLOBAL_CONFIG_NAME);
		} else {
			logoPath = logoUrl;
		}
		LOGGER.debug("Logo URL :: {}", logoPath);
		return logoPath;
	}
}
