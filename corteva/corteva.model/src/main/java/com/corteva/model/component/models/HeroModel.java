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

/**
 * The is the sling for the Experience Fragments. This sling model will be used
 * by all the components where we need path of experience fragments from the
 * author.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class HeroModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroModel.class);

    /** The hero image path. */
    @Inject
    @Optional
    @Via("resource")
    @Named("heroTitle")
    private String heroTitle;
    
    /** The hero image path. */
    @Inject
    @Optional
    @Via("resource")
    @Named("title")
    private String title;

	/** The hero image path. */
    @Inject
    @Optional
    @Via("resource")
    @Named("heroImagePath")
    private String heroImagePath;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The hero mobile image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("mobfileReference")
	private String mobileImagePath;

	/** The hero view type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("viewType")
	private String viewType;
	
	/** The Constant COMPONENT_NAME_HERO. */
	private static final String COMPONENT_NAME_HERO = "hero";

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public HeroModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the mobile image path.
	 *
	 * @return the mobile image path
	 */
	public String getMobileImagePath() {
		LOGGER.debug("Inside getHeroImageJson() method");

		ImageRenditionBean imgRenBean = getImageRenditionList(mobileImagePath, viewType, slingRequest);
		return imgRenBean.getMobileImagePath();
	}

	/**
	 * Gets the hero title.
	 *
	 * @return the hero title
	 */
	public String getHeroTitle() {
		return heroTitle;
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
     * This method gets the image rendition json for hero.
     *
     * @return the hero image json
     */
    public String getHeroImageJson() {
        LOGGER.debug("Inside getHeroImageJson() method");
        return getImageRenditionJson(heroImagePath, COMPONENT_NAME_HERO, slingRequest);
    }
}