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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * This sling model will be used in the Article Page to display Article Header.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ArticleHeaderModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BioDetailModel.class);

	/** Constant to hold value of Image Source meta data. */
	private static final String IMAGE_SOURCE_METADATA = "imageSource";

	/** Constant to hold value of article type of Article Page. */
	private static final String PAGE_PROPERTY_ARTICLE_TYPE = "cq:articleType";

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The Article Hero Image Path. */
	@Inject
	@Optional
	@Via("resource")
	private String fileReference;

	/** The current page. */
	private Page currentPage;

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;
	
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
		LOGGER.debug("Inside init method of articleHeaderModel");
		currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
		isTimeRequired = false;
	}

	/**
	 * @return the authorText
	 */
	public String getAuthorText() {
		return currentPage.getProperties().get("author").toString();
	}

	/**
	 * @return the articleType
	 */
	public String getArticleType() {
		String articleTypeTag = currentPage.getProperties().get(PAGE_PROPERTY_ARTICLE_TYPE).toString();
		String articleType = "";
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (null != tagManager) {
			articleType = tagManager.resolve(articleTypeTag).getTitle();
		}
		return articleType;
	}

	/**
	 * @return the lastModifiedDate from Article Page Property.
	 */
	public String getLastModifiedDate() {
		Locale locale = CommonUtils.getLocaleObject(currentPage.getPath(), resourceResolver);
		Calendar calendarlastModifiedDate = (Calendar) currentPage.getProperties()
				.get(CortevaConstant.CQ_LAST_MODIFIED);
		LocalDateTime localDateTime = LocalDateTime
				.ofInstant(Instant.ofEpochMilli(calendarlastModifiedDate.getTimeInMillis()), ZoneId.systemDefault());
		return CommonUtils.getFormattedLocalizedDate(localDateTime, locale, isTimeRequired, request, baseService);
	}

	/**
	 * @return the customDisplayDate from Article Page Property.
	 */
	public String getCustomDisplayDate() {
		Locale locale = CommonUtils.getLocaleObject(currentPage.getPath(), resourceResolver);
		Calendar calendarlastModifiedDate = (Calendar) currentPage.getProperties().get(CortevaConstant.DISPLAY_DATE);
		LocalDateTime localDateTime = LocalDateTime
				.ofInstant(Instant.ofEpochMilli(calendarlastModifiedDate.getTimeInMillis()), ZoneId.systemDefault());
		return CommonUtils.getFormattedLocalizedDate(localDateTime, locale, isTimeRequired, request, baseService);
	}

	/**
	 * @return the image source from Article Page Property.
	 */
	public String getHeroImageSource() {
		String resourcePath = fileReference + CortevaConstant.FORWARD_SLASH + CortevaConstant.JCR_CONTENT
				+ CortevaConstant.FORWARD_SLASH + CortevaConstant.METADATA;
		String property = "";
		if (resourceResolver != null) {
			Resource resource = resourceResolver.getResource(resourcePath);
			if (null != resource) {
				ValueMap valueMap = resource.getValueMap();
				if (valueMap.containsKey(IMAGE_SOURCE_METADATA)) {
					property = valueMap.get(IMAGE_SOURCE_METADATA, String.class);
				}
			}
		}
		return property;
	}

}