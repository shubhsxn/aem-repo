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

import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * The is the sling model for the Primary Cards. This sling model will be used to author
 * Primary card fields.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class PrimaryCardModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryCardModel.class);

	/** The Simple Card title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("simpleCardTitle")
    private String simpleCardTitle;
    
    /** The Feature Card title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("featureCardTitle")
    private String featureCardTitle;
	
	/** The CTA Card title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("ctaCardTitle")
    private String ctaCardTitle;
    
    /** The CTA Card with Arrow title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("ctaArrowCardTitle")
    private String ctaArrowCardTitle;
    
    /** The Responsive Card title. */
    @Inject
    @Optional
    @Via("resource")
    @Named("responsiveCardTitle")
    private String responsiveCardTitle;
    
    /** The Optional link label. */
    @Inject
    @Optional
    @Via("resource")
    @Named("optLinkLabel")
    private String optLinkLabel;

    /** The Optional link url. */
    @Inject
    @Optional
    @Via("resource")
    @Named("optLinkUrl")
    private String optLinkUrl;

    /** The link action. */
    @Inject
    @Optional
    @Via("resource")
    @Named("optLinkAction")
    private String optLinkAction;

	/** The link style. */
	@Inject
	@Optional
	@Via("resource")
	@Named("optLinkStyle")
	private String optLinkStyle;

	/** The feature card mobile image path. */
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

	/** The text on image view type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("viewType")
	private String viewType;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/**
	 * This method gets the Simple Card title.
	 * 
	 * @return the Simple Card title
	 */
	public String getSimpleCardTitle() {
		return simpleCardTitle;
	}
	
	/**
     * This method gets the Feature Card title.
     * @return the Feature Card title
     */
	public String getFeatureCardTitle() {
		return featureCardTitle;
	}

	/**
     * This method gets the CTA Card title.
     * @return the CTA Card title
     */
	public String getCtaCardTitle() {
		return ctaCardTitle;
	}

	/**
     * This method gets the CTA Card with Arrow title.
     * @return the CTA Card with Arrow title
     */
	public String getCtaArrowCardTitle() {
		return ctaArrowCardTitle;
	}

	/**
     * This method gets the Responsive Card title.
     * @return the Responsive Card title
     */
	public String getResponsiveCardTitle() {
		return responsiveCardTitle;
	}

	/**
     * Gets the optional link label.
     *
     * @return the optLinkLabel
     */
	public String getOptLinkLabel() {
		return optLinkLabel;
	}

	/**
     * Gets the optional link url.
     *
     * @return the optLinkUrl
     */
	public String getOptLinkUrl() {
		return LinkUtil.getHref(optLinkUrl);
	}

	/**
     * Gets the link action.
     *
     * @return the optLinkAction
     */
	public String getOptLinkAction() {
		return optLinkAction;
	}

	/**
     * Gets the link style.
     *
     * @return the optLinkStyle
     */
	public String getOptLinkStyle() {
		return optLinkStyle;
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

}
