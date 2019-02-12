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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.LinkUtil;

/**
 * The is the sling model for Related Content Full Width component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class RelatedContentFullWidthModel extends AbstractSlingModel {

	/** The content type. */
	@Inject
	@Optional
	@Via("resource")
	private String contentType;

	/** The page path. */
	@Inject
	@Optional
	@Via("resource")
	private String pagePath;

	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	private String title;

	/** The short description. */
	@Inject
	@Optional
	@Via("resource")
	private String shortDescription;

	/** The video type. */
	@Inject
	@Optional
	@Via("resource")
	private String videoType;

	/** The video id. */
	@Inject
	@Optional
	@Via("resource")
	private String videoId;

	/** The video title. */
	@Inject
	@Optional
	@Via("resource")
	private String videoTitle;

	/** The video description. */
	@Inject
	@Optional
	@Via("resource")
	private String videoDescription;

	/** The video alt text. */
	@Inject
	@Optional
	@Via("resource")
	private String videoAltText;

	/** The video thumbnail. */
	@Inject
	@Optional
	@Via("resource")
	private String videoThumbnail;

	/** The video path. */
	@Inject
	@Optional
	@Via("resource")
	private String videoPath;

	/** The video title S 7. */
	@Inject
	@Optional
	@Via("resource")
	private String videoTitleS7;

	/** The video description S 7. */
	@Inject
	@Optional
	@Via("resource")
	private String videoDescriptionS7;

	/** The video alt text S 7. */
	@Inject
	@Optional
	@Via("resource")
	private String videoAltTextS7;

	/** The video thumbnail S 7. */
	@Inject
	@Optional
	@Via("resource")
	private String videoThumbnailS7;

	/** The cta label. */
	@Inject
	@Optional
	@Via("resource")
	private String ctaLabel;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/**
	 * Gets the content type.
	 *
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

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
	 * Gets the short description.
	 *
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Gets the video type.
	 *
	 * @return the videoType
	 */
	public String getVideoType() {
		return videoType;
	}

	/**
	 * Gets the video id.
	 *
	 * @return the videoId
	 */
	public String getVideoId() {
		return videoId;
	}

	/**
	 * Gets the video title.
	 *
	 * @return the videoTitle
	 */
	public String getVideoTitle() {
		return videoTitle;
	}

	/**
	 * Gets the video description.
	 *
	 * @return the videoDescription
	 */
	public String getVideoDescription() {
		return videoDescription;
	}

	/**
	 * Gets the video alt text.
	 *
	 * @return the videoAltText
	 */
	public String getVideoAltText() {
		return videoAltText;
	}

	/**
	 * Gets the video thumbnail.
	 *
	 * @return the videoThumbnail
	 */
	public String getVideoThumbnail() {
		return videoThumbnail;
	}

	/**
	 * Gets the video path.
	 *
	 * @return the videoPath
	 */
	public String getVideoPath() {
		return getSceneSevenVideoPath(videoPath, slingRequest);
	}

	/**
	 * Gets the video title S 7.
	 *
	 * @return the videoTitleS7
	 */
	public String getVideoTitleS7() {
		return videoTitleS7;
	}

	/**
	 * Gets the video description S 7.
	 *
	 * @return the videoDescriptionS7
	 */
	public String getVideoDescriptionS7() {
		return videoDescriptionS7;
	}

	/**
	 * Gets the video alt text S 7.
	 *
	 * @return the videoAltTextS7
	 */
	public String getVideoAltTextS7() {
		return videoAltTextS7;
	}

	/**
	 * Gets the video thumbnail S 7.
	 *
	 * @return the videoThumbnailS7
	 */
	public String getVideoThumbnailS7() {
		return videoThumbnailS7;
	}

	/**
	 * Gets the cta label.
	 *
	 * @return the ctaLabel
	 */
	public String getCtaLabel() {
		return ctaLabel;
	}

	/**
	 * Gets the youtube api url.
	 *
	 * @return the youtube api
	 */
	@Override
	public String getYoutubeApi() {
		return getYoutubeApi(slingRequest);
	}

	/**
	 * Gets the youtube api key.
	 *
	 * @return the youtube key
	 */
	@Override
	public String getYoutubeKey() {
		return getYoutubeKey(slingRequest);
	}
}
