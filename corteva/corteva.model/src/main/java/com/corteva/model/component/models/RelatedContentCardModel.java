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

import com.corteva.core.utils.LinkUtil;

/**
 * The is the sling model for the Related Content Cards.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class RelatedContentCardModel extends AbstractSlingModel {
		
    /** The page path. */
    @Inject
    @Optional
    @Via("resource")
    @Named("pagePath")
    private String pagePath;
    
	/** The Simple Card body text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;
    
    /** The CTA Card with Arrow title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("altText")
    private String altText;
      
	/**
	 * Gets the page path.
	 *
	 * @return the pagePath
	 */
	public String getPagePath() {
		return LinkUtil.getHref(pagePath);
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
	 * Gets the alt text.
	 *
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}
}