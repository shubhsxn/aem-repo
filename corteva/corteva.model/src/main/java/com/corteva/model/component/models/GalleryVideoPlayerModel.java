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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.VideoBean;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonObject;

/**
 * The is the sling model for Gallery Video Player component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class GalleryVideoPlayerModel extends AbstractSlingModel {

	/** The Constant VIDEO_THUMBNAIL. */
	private static final String VIDEO_THUMBNAIL = "videoThumbnail";

	/** The Constant VIDEO_ALT_TEXT. */
	private static final String VIDEO_ALT_TEXT = "videoAltText";

	/** The Constant VIDEO_TITLE. */
	private static final String VIDEO_TITLE = "videoTitle";

	/** The Constant VIDEO_ID. */
	private static final String VIDEO_ID = "videoId";

	/** The Constant VIDEO_PATH. */
	private static final String VIDEO_PATH = "videoPath";

	/** The Constant VIDEO_TITLE_S7. */
	private static final String VIDEO_TITLE_S7 = "videoTitleS7";

	/** The Constant VIDEO_DESCRIPTION_S7. */
	private static final String VIDEO_DESCRIPTION_S7 = "videoDescriptionS7";

	/** The Constant VIDEO_DESCRIPTION. */
	private static final String VIDEO_DESCRIPTION = "videoDescription";

	/** The Constant VIDEO_ALT_TEXT_S7. */
	private static final String VIDEO_ALT_TEXT_S7 = "videoAltTextS7";

	/** The Constant VIDEO_THUMBNAIL_S7. */
	private static final String VIDEO_THUMBNAIL_S7 = "videoThumbnailS7";

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GalleryVideoPlayerModel.class);

	/** The video details. */
	@Inject
	@Optional
	@Via("resource")
	private Property videoDetails;

	/** The Scene7 video details. */
	@Inject
	@Optional
	@Via("resource")
	private Property videoDetailsS7;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The brand name. */
	private String brandName;

	/**
	 * The init method.
	 */
	@PostConstruct
	public void init() {
		ResourceResolver resolver = getResourceResolver();
		Page currentPage = CommonUtils.getPageFromResource(resolver, currentResource);
		brandName = CommonUtils.getRegionCountryLanguage(currentPage.getPath(), resolver).get(CortevaConstant.BRAND);
		LOGGER.debug("Brand Name is ::: {}", brandName);
	}

	/**
	 * @param thumbnail
	 *            the thumbnail
	 * @param dto
	 *            the video bean
	 */
	private void populateVideoThumbnail(String thumbnail, VideoBean dto) {
		if (thumbnail.contains(CortevaConstant.CONTENT_DAM_PATH)) {
			if (StringUtils.equalsIgnoreCase(brandName, CortevaConstant.BRAND_NAME_BREVANT)) {
				dto.setVideoThumbnailBean(
						getImageRenditionList(thumbnail, getComponentName(resourceType), slingRequest));
			} else {
				dto.setVideoThumbnail(getSceneSevenImagePath(thumbnail, slingRequest));
			}
		} else {
			dto.setVideoThumbnail(thumbnail);
		}
	}

	/**
	 * Gets the youtube video details.
	 *
	 * @return the youtube video details
	 */
	public List<VideoBean> getYoutubeVideoDetails() {
		LOGGER.info("Inside getYoutubeVideoDetails() method of GalleryVideoPlayerModel");
		List<VideoBean> videosList = new ArrayList<>();
		try {
			List<JsonObject> videosJsonList = AEMUtils.getJSONListfromProperty(videoDetails);
			VideoBean videoDto;
			for (JsonObject video : videosJsonList) {
				videoDto = new VideoBean();
				if (video.has(VIDEO_ID)) {
					videoDto.setVideoId(video.get(VIDEO_ID).getAsString());
				}
				if (video.has(VIDEO_TITLE)) {
					videoDto.setVideoTitle(video.get(VIDEO_TITLE).getAsString());
				}
				if (video.has(VIDEO_DESCRIPTION)) {
					videoDto.setVideoDescription(video.get(VIDEO_DESCRIPTION).getAsString());
				}
				if (video.has(VIDEO_ALT_TEXT)) {
					videoDto.setVideoAltText(video.get(VIDEO_ALT_TEXT).getAsString());
				}
				if (video.has(VIDEO_THUMBNAIL)) {
					String videoThumbnail = video.get(VIDEO_THUMBNAIL).getAsString();
					populateVideoThumbnail(videoThumbnail, videoDto);
				}
				videosList.add(videoDto);
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getYoutubeVideoDetails() of GalleryVideoPlayerModel", e);
		}
		return videosList;
	}

	/**
	 * Gets the scene seven video details.
	 *
	 * @return the scene seven video details
	 */
	public List<VideoBean> getSceneSevenVideoDetails() {
		LOGGER.info("Inside getSceneSevenVideoDetails() method of GalleryVideoPlayerModel");
		List<VideoBean> videosList = new ArrayList<>();
		try {
			List<JsonObject> videosJsonList = AEMUtils.getJSONListfromProperty(videoDetailsS7);
			VideoBean videoDto;
			for (JsonObject video : videosJsonList) {
				videoDto = new VideoBean();
				if (video.has(VIDEO_PATH)) {
					videoDto.setVideoId(getSceneSevenVideoPath(video.get(VIDEO_PATH).getAsString(), slingRequest));
				}
				if (video.has(VIDEO_TITLE_S7)) {
					videoDto.setVideoTitle(video.get(VIDEO_TITLE_S7).getAsString());
				}
				if (video.has(VIDEO_DESCRIPTION_S7)) {
					videoDto.setVideoDescription(video.get(VIDEO_DESCRIPTION_S7).getAsString());
				}
				if (video.has(VIDEO_ALT_TEXT_S7)) {
					videoDto.setVideoAltText(video.get(VIDEO_ALT_TEXT_S7).getAsString());
				}
				if (video.has(VIDEO_THUMBNAIL_S7)) {
					String videoThumbnail = video.get(VIDEO_THUMBNAIL_S7).getAsString();
					populateVideoThumbnail(videoThumbnail, videoDto);
				}
				videosList.add(videoDto);
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getSceneSevenVideoDetails() of GalleryVideoPlayerModel", e);
		}
		return videosList;
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
