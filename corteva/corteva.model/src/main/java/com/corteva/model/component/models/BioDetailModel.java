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

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * This sling model will be used by the components to display details of
 * leadership bio cards.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class BioDetailModel extends AbstractSlingModel {

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The bio Head shot image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("bioHeadShotImagePath")
	private String bioHeadShotImagePath;

	/** The biography File path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("biographyFile")
	private String biographyFile;

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public BioDetailModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets Locale value
	 *
	 * @return Locale the locale
	 */
	public Locale getLocale() {
		return slingRequest.getLocale();
	}

	/**
	 * This method gets the image rendition json for hero.
	 *
	 * @return the hero image json
	 */
	public ImageRenditionBean getBioHeadShotImage() {
		return getImageRenditionList(bioHeadShotImagePath, CommonUtils.getComponentName(resourceType), slingRequest);
	}

	/**
	 * @return the biographyFile
	 */
	public String getBiographyFile() {
		return biographyFile;
	}
	
	/**
	 * Gets the locale for internationalization.
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
	}

}