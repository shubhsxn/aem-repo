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

import java.time.ZoneId;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.day.cq.wcm.api.Page;

/**
 * The is the sling model for the Tiles.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class TilesModel extends AbstractSlingModel {
    
    /** The impact tile title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("impactTitle")
    private String impactTitle;
    
    /** The impact expanded tile title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("impactExpandedTitle")
    private String impactExpandedTitle;
    
    /** The background color. */
    @Inject
    @Optional
    @Via("resource")
    @Named("bgColor")
    private String bgColor;
    
    /** The link url for crop icon. */
    @Inject
    @Optional
    @Via("resource")
    @Named("linkUrlCropIcon")
    private String linkUrlCropIcon;
    
    /** The link action for crop icon. */
    @Inject
    @Optional
    @Via("resource")
    @Named("linkActionCropIcon")
    private String linkActionCropIcon;
    
    /** The link style for crop icon. */
    @Inject
    @Optional
    @Via("resource")
    @Named("linkStyleCropIcon")
    private String linkStyleCropIcon;
    
    /** The impact tiles background color. */
    @Inject
    @Optional
    @Via("resource")
    @Named("impactBgColor")
    private String impactBgColor;
    
    /** The text placement to left or right of the image. */
    @Inject
    @Optional
    @Via("resource")
    @Named("textPlacement")
    private String textPlacement;
    
    /** The eyebrow label. */
    @Inject
    @Optional
    @Via("resource")
    @Named("eyebrowLabel")
    private String eyebrowLabel;
    
    /** The display publish date selection. */
    @Inject
    @Optional
    @Via("resource")
    @Named("displayPublishDate")
    private boolean displayPublishDate;
    
    /** The publish date. */
    @Inject
    @Optional
    @Via("resource")
    @Named("publishDate")
    private Date publishDate;
    
    /** The current resource. */
	@SlingObject
	private Resource currentResource;
    
    /** The current page. */
	private Page currentPage;
	
	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;
	
	/** The sling request. */
	@Inject
	private SlingHttpServletRequest request;
	
	/** The base service. */
	@Inject
	private BaseConfigurationService baseService;
	
	/** The Flag to check whether timestamp needs to be displayed. */
	private boolean isTimeRequired;
	
	/** The init method. */
	@PostConstruct
	public void init() {
		currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
		isTimeRequired = false;
	}

	/**
	 * Gets the impact tile title.
	 *
	 * @return impactTitle
	 */	
	public String getImpactTitle() {
		return impactTitle;
	}

	/**
	 * Gets the impact expanded tile title.
	 *
	 * @return impactExpandedTitle
	 */
	public String getImpactExpandedTitle() {
		return impactExpandedTitle;
	}

	/**
	 * Gets the background color.
	 *
	 * @return the bgColor
	 */
	public String getBgColor() {
		return bgColor;
	}
	
	/**
     * Gets the link url.
     *
     * @return the linkUrl
     */
    public String getLinkUrlCropIcon() {
        return LinkUtil.getHref(linkUrlCropIcon);
    }
    
    /**
     * Gets the link action.
     *
     * @return the linkAction
     */
    public String getLinkActionCropIcon() {
        return linkActionCropIcon;
    }

    /**
     * Gets the link style.
     *
     * @return the linkStyle
     */
    public String getLinkStyleCropIcon() {
        return linkStyleCropIcon;
    }
    
	/**
	 * Gets the impact tiles background color.
	 *
	 * @return impactBgColor
	 */
	public String getImpactBgColor() {
		return impactBgColor;
	}

	/**
	 * Gets the text placement.
	 *
	 * @return textPlacement
	 */
	public String getTextPlacement() {
		return textPlacement;
	}

	/**
	 * Gets the eyebrow label.
	 *
	 * @return eyebrowLabel
	 */
	public String getEyebrowLabel() {
		return eyebrowLabel;
	}

	/**
	 * Gets the display publish date selection.
	 *
	 * @return displayPublishDate
	 */
	public boolean isDisplayPublishDate() {
		return displayPublishDate;
	}

	/**
	 * Gets the publish date.
	 *
	 * @return publishDate
	 */
	public String getPublishDate() {
		return CommonUtils.getFormattedLocalizedDate(publishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
				CommonUtils.getLocaleObject(currentPage.getPath(), resourceResolver), isTimeRequired, request, baseService);
	}
	
}