/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.bean;

/**
 * The is the Video bean class.
 * 
 * @author Sapient
 */
public class VideoBean {

    /** The video id. */
    private String videoId;

    /** The video title. */
    private String videoTitle;

    /** The video description. */
    private String videoDescription;

    /** The video alt text. */
    private String videoAltText;

    /** The video thumbnail. */
    private String videoThumbnail;
    
    /** The video thumbnail bean. */
    private ImageRenditionBean videoThumbnailBean;

    /**
     * Gets the video thumbnail bean.
     * 
	 * @return the videoThumbnailBean
	 */
	public ImageRenditionBean getVideoThumbnailBean() {
		return videoThumbnailBean;
	}

	/**
	 * 
	 * Sets the video thumbnail bean
	 * 
	 * @param videoThumbnailBean the videoThumbnailBean to set
	 */
	public void setVideoThumbnailBean(ImageRenditionBean videoThumbnailBean) {
		this.videoThumbnailBean = videoThumbnailBean;
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
     * Sets the video id.
     *
     * @param videoId the videoId to set
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
     * Sets the video title.
     *
     * @param videoTitle the videoTitle to set
     */
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
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
     * Sets the video description.
     *
     * @param videoDescription the videoDescription to set
     */
    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
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
     * Sets the video alt text.
     *
     * @param videoAltText the videoAltText to set
     */
    public void setVideoAltText(String videoAltText) {
        this.videoAltText = videoAltText;
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
     * Sets the video thumbnail.
     *
     * @param videoThumbnail the videoThumbnail to set
     */
    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

}
