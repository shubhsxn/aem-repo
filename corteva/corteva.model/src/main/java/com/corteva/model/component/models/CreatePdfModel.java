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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.day.cq.wcm.api.Page;

/**
 * The is the sling model for downloading pdf. The getter of this model will be
 * called upon clicking on the Download Icon of the page.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class CreatePdfModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreatePdfModel.class);

	/** The current resource.  */
	@SlingObject
	private Resource currentResource;
	
	/**
	 * Gets the page pdf url.
	 *
	 * @return the page pdf url
	 */
	public String getPagePdfUrl() {
		return getPagePdfUrl(currentResource);
	}

	/**
	 * Gets the page pdf url.
	 *
	 * @param resource the resource
	 * @return the page pdf url
	 */
	public String getPagePdfUrl(Resource resource) {
		LOGGER.debug("Inside getPagePdfUrl() method");
		String pagePdfUrl = StringUtils.EMPTY;
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), resource);
		if (null != currentPage) {
			pagePdfUrl = currentPage.getPath() + CortevaConstant.DOT + CortevaConstant.PDF_EXTN;
		}
		LOGGER.debug("Page URL with pdf extension :: {}", pagePdfUrl);
		return pagePdfUrl;
	}
}