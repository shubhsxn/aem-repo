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
import javax.jcr.Property;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The is the sling model for the Timeline Card.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class TimelineCardModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TimelineCardModel.class);

	/** The hidden field for the component resource path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("timelineCardResourceHidden")
	private Property timelineCardResourceHidden;

	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;

	/** The teaser text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("teaserText")
	private String teaserText;

	/** The flag label. */
	@Inject
	@Optional
	@Via("resource")
	@Named("flagLabel")
	private String flagLabel;

	/** The date. */
	@Inject
	@Optional
	@Via("resource")
	@Named("timelineLabel")
	private String timelineLabel;

	/** The modal cta text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("modalCtaText")
	private String modalCtaText;
	
	/** The modal detail. */
	@Inject
	@Optional
	@Via("resource")
	@Named("modalDetail")
	private String modalDetail;
	
	/** The associate modal detail. */
	@Inject
	@Optional
	@Via("resource")
	@Named("associateModalDetail")
	private String associateModalDetail;

	/**
	 * Gets the timeline card resource hidden.
	 *
	 * @return the timelineCardResourceHidden
	 */
	public Property getTimelineCardResourceHidden() {
		return timelineCardResourceHidden;
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
	 * Gets the teaser text.
	 *
	 * @return the teaserText
	 */
	public String getTeaserText() {
		return teaserText;
	}

	/**
	 * Gets the flag label.
	 *
	 * @return the flagLabel
	 */
	public String getFlagLabel() {
		return flagLabel;
	}	

	/**
	 * Gets the timeline label.
	 *
	 * @return the timelineLabel
	 */
	public String getTimelineLabel() {
		return timelineLabel;
	}

	/**
	 * Gets the modal cta text.
	 *
	 * @return the modalCtaText
	 */
	public String getModalCtaText() {
		return modalCtaText;
	}

	/**
	 * Gets the modal detail.
	 *
	 * @return the modalDetail
	 */
	public String getModalDetail() {
		return modalDetail;
	}

	/**
	 * This method is used to get the responsive grid node name in sightly.
	 *
	 * @return the responsive grid node name
	 */
	public String getResponsiveGridNodeName() {
		LOGGER.debug("Inside getResponsiveGridNodeName() method of Timeline Card Model");
		return getResponsiveGridNodeName(timelineCardResourceHidden);
	}

	/**
	 * Gets the associate modal detail.
	 *
	 * @return the associateModalDetail
	 */
	public String getAssociateModalDetail() {
		return associateModalDetail;
	}
}