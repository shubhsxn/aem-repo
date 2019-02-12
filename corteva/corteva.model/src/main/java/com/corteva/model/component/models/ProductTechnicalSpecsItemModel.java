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
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * This the sling Model for the Product Technical Specs Item.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductTechnicalSpecsItemModel extends ProductSlingModel {

	/** The Product Technical Specs Section Label. */
	@Inject
	private String sectionLabel;

	/** The Product Technical Specs Section Detail. */
	@Inject
	@Named("bodyText")
	private String sectionDetail;

	/** The Product Technical Specs Section Media Type. */
	@Inject
	private String mediaType;

	/** The Product Technical Specs Section Desktop Image URL. */
	@Inject
	private String desktopImageUrl;

	/** The Product Technical Specs Section Image Alternate Text. */
	@Inject
	private String imgAltText;

	/** The Product Technical Specs Section YouTube Video ID. */
	@Inject
	private String videoId;

	/** The Product Technical Specs Section Scene7 Video Path. */
	@Inject
	private String videoUrl;

	/** The Product Technical Specs Section Video Title. */
	@Inject
	@Named("videoTitle")
	private String vidTitle;

	/** The Product Technical Specs Section Video Description. */
	@Inject
	@Named("videoDescription")
	private String vidDesc;

	/** The Product Technical Specs Section Video Alternate Text. */
	@Inject
	@Named("videoAltText")
	private String vidAltText;

	/** The Product Technical Specs Section Video Thumbnail Image URL. */
	@Inject
	@Named("videoThumbnail")
	private String thumbnailImage;

	/**
	 * @return the sectionLabel
	 */
	public String getSectionLabel() {
		return sectionLabel;
	}

	/**
	 * @return the sectionDetail
	 */
	public String getSectionDetail() {
		return sectionDetail;
	}

	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * @return the desktopImageUrl
	 */
	public ImageRenditionBean getDesktopImageUrl() {
		return getImageRenditionList(desktopImageUrl);
	}

	/**
	 * @return the imageAltText
	 */
	public String getImgAltText() {
		return imgAltText;
	}

	/**
	 * @return the youtube videoId
	 */
	public String getVideoId() {
		return videoId;
	}

	/**
	 * @return the scene7 videoUrl
	 */
	public String getVideoUrl() {
		return getSceneSevenVideoPath(videoUrl);
	}

	/**
	 * @return the vidTitle
	 */
	public String getVidTitle() {
		return vidTitle;
	}

	/**
	 * @return the vidDesc
	 */
	public String getVidDesc() {
		return vidDesc;
	}

	/**
	 * @return the videoAltText
	 */
	public String getVidAltText() {
		return vidAltText;
	}

	/**
	 * @return the thumbnailImage
	 */
	public ImageRenditionBean getThumbnailImage() {
		if ("scene7video".equals(mediaType)
				|| thumbnailImage.contains(CortevaConstant.CONTENT_DAM_PATH)) {
			return getImageRenditionList(thumbnailImage);
		} else {
			ImageRenditionBean youTubeThumbnailRendition = new ImageRenditionBean();
			youTubeThumbnailRendition.setDesktopImagePath(thumbnailImage);
			return youTubeThumbnailRendition;
		}
	}

}
