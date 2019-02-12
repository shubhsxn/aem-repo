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
public class ComparativeImageSliderModel extends AbstractSlingModel {

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;
	
	 /** The component resource type. */
    @Inject
    @Optional
    @Via("resource")
    @Named("sling:resourceType")
    private String resourceType;
    
    /** The title. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;
	
	/** The intro text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("introText")
	private String introText;

	/** The caption text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("caption")
	private String caption;


	/** The left image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("leftImage")
	private String leftImage;
	
	/** The right image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("rightImage")
	private String rightImage;	
	

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public ComparativeImageSliderModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}
	
	/**
	 * Gets Locale value
	 *
	 * @return Locale
	 *           the locale
	 */
	public Locale getLocale() {		 
		return slingRequest.getLocale();		
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
	 * Gets the Intro Text.
	 *
	 * @return the Intro Text
	 */
	public String getIntroText() {
		return introText;
	}

	/**
	 * Gets the caption.
	 *
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * This method gets the image rendition list for leftImage.
	 *
	 * @return the left image list
	 */
	public ImageRenditionBean getLeftImage() {
		return getImageRenditionList(leftImage, CommonUtils.getComponentName(resourceType), slingRequest);
	}
	
	/**
	 * This method gets the image rendition list for rightImage.
	 *
	 * @return the right image list
	 */
	public ImageRenditionBean getRightImage() {
		return getImageRenditionList(rightImage, CommonUtils.getComponentName(resourceType), slingRequest);
	}

}