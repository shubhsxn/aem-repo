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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

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

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ArticleBean;
import com.corteva.model.component.utils.ArticleUtils;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * The is the sling for the Article Filter Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ArticleFilterModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleFilterModel.class);
	/** The Constant to hold value default article filter component. */
	private static final String DEFAULT_NO_OF_ARTICLES = "noOfArticlesToDisplay";
	/** The sling request. */
	private SlingHttpServletRequest slingRequest;
	/** The list of tags for Article type Filter Drop down. */
	private String dateFilterJSON;
	/** The list of tags for Article type Filter Drop down. */
	private String articleTypeFilterJSON;
	/** The list of tags for Article type Filter Drop down. */
	private String articleBeanListJSON;
	/** The list of path of articles Pages */
	private List<ArticleBean> articleBeanList;
	/** The show Load More Button */
	private Boolean showLoadMoreButton = false;
	/** The Resource Resolver. */
	@Inject
	private ResourceResolver resourceResolver;
	/** The page path. */
	/*@Inject
	private String pagePath;*/
	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The article topics. */
	@Inject
	@Optional
	@Via("resource")
	private Resource articleTopics;

	/** The logical operation within taxonomy facets. */
	@Inject
	@Optional
	@Via("resource")
	private String intraFacetLogicalOperation;

	/** The logical operation across taxonomy facets. */
	@Inject
	@Optional
	@Via("resource")
	private String interFacetLogicalOperation;

	/** The Display Filter Section Checkbox. */
	@Inject
	@Optional
	@Via("resource")
	private Boolean displayFilterSection;
	/** The Display Date Filter Section Checkbox. */
	@Inject
	@Optional
	@Via("resource")
	private Boolean displayDateFilter;
	/** The No of articles to display. */
	@Inject
	@Optional
	@Via("resource")
	private Property articleType;
	/** The Display Article Type Filter Section Checkbox. */
	@Inject
	@Optional
	@Via("resource")
	private Boolean displayArticleTypeFilter;

	/** The Article Root path. */
	private String folderPath;

	/** The article type array list */
	private List<String> articleTypeArr;

	/** The Flag to check whether timestamp needs to be displayed. */
	private boolean isTimeRequired;

	/**
	 * Instantiates a new Article Filter model.
	 *
	 * @param request
	 *            the request
	 */
	public ArticleFilterModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * The base config.
	 */
	@Inject
	private BaseConfigurationService baseService;
	
	/** The current resource. */
	@SlingObject
	private Resource currentResource;
	
	/** The current page. */
	private Page currentPage;
	
	/** The number of articles to display */
	private String noOfArticlesToDisplay;

	/**
	 * The init method.
	 * 
	 * 
	 */
	@PostConstruct
	public void init() {
		try {
			currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
			Gson gson = new Gson();
			if (null != displayFilterSection && null != displayDateFilter) {
				// Get number of articles to show from configuration
				noOfArticlesToDisplay = baseService
						.getPropValueFromConfiguration(CortevaConstant.COMPONENT_CONFIG_NAME, DEFAULT_NO_OF_ARTICLES);
				LOGGER.debug("No of articles to display is {} ", noOfArticlesToDisplay);
				isTimeRequired = false;
				articleTypeArr = ArticleUtils.getArticleTypePropertyList(articleType);
				// If Folder path is blank , fetch the default folder path
				if (StringUtils.isEmpty(folderPath)) {
					folderPath = ArticleUtils.getDefaultArticleFolderPath(CommonUtils.getPagePath(slingRequest),
							resourceResolver, getBaseConfigurationService());
				}
				LOGGER.debug("Folder Path under which to find articles is {} ", folderPath);
				if (articleTypeArr.size() == 1) {
					displayArticleTypeFilter = false;
				}
				List<String> articlePages = getArticleFeedData(gson);
				articleBeanList = ArticleUtils.setPathToPage(articlePages, resourceResolver, noOfArticlesToDisplay,
						CommonUtils.getComponentName(resourceType), slingRequest, baseService,
						CommonUtils.getPagePath(slingRequest), isTimeRequired);
				articleBeanListJSON = gson.toJson(articleBeanList);
			}
		} catch (RepositoryException e) {
			LOGGER.error("Repository Exception occured while instantiating Article Filter Model", e);
		} catch (IOException e) {
			LOGGER.error("IOException occured while fetching values from Configuration in Article Filter Model", e);
		}
	}

	/**
	 * @param gson
	 *            the gson utility
	 * @return List of articlePages
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private List<String> getArticleFeedData(Gson gson) throws RepositoryException {
		Calendar cal = null;
		Map<String, Map<String, String>> dateMap = new LinkedHashMap<>();
		List<String> articlePages = new ArrayList<>();
		List<ArticleTopicsModel> articleTopicsList = new ArrayList<>();
		if (null != articleTopics) {
			articleTopicsList = AEMUtils.getMultifieldItemList(articleTopics, ArticleTopicsModel.class);
		}
		int noOfArticles = Integer.parseInt(noOfArticlesToDisplay);
		String offset = Integer.toString((CommonUtils.getPaginationIndex(slingRequest) - 1) * noOfArticles);
		Iterator<Resource> pageIterator = ArticleUtils.findArticleFeedFilterData(resourceResolver, folderPath,
				articleTypeArr, articleTopicsList, intraFacetLogicalOperation, interFacetLogicalOperation,
				CortevaConstant.UNLIMITED_RESULT, offset, CortevaConstant.ALL, "");
		int index = 0;
		while (null != pageIterator && pageIterator.hasNext()) {
			Resource pageResource = pageIterator.next();
			if (pageResource != null) {
				Page page = pageResource.adaptTo(Page.class);
				if (page != null) {
					if (displayFilterSection && displayDateFilter && null != page.getProperties()
							&& null != page.getProperties().get(CortevaConstant.DISPLAY_DATE)) {
						cal = (Calendar) page.getProperties().get(CortevaConstant.DISPLAY_DATE);
						ArticleUtils.createDateMapFilter(dateMap, cal, index);
					}
					index++;
					articlePages.add(page.getPath());
				}
			}
		}
		if (index > noOfArticles) {
			showLoadMoreButton = true;
		}
		getArticleTypeFilterJSON();
		JsonArray array = ArticleUtils.formatDateMapResponse(dateMap);
		dateFilterJSON = gson.toJson(array);
		return articlePages;
	}

	/**
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param resourceResolver
	 *            the resourceResolver to set
	 */
	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	/**
	 * @return the dateFilterJSON
	 */
	public String getDateFilterJSON() {
		return dateFilterJSON;
	}

	/**
	 * @return the articleTypeFilterJSON
	 */
	public String getArticleTypeFilterJSON() {
		Gson gson = new Gson();
		if (displayFilterSection && displayArticleTypeFilter) {
			Map<String, String> articleTypeTagMap = new HashMap<>();
			articleTypeTagMap.put(CortevaConstant.ALL, CortevaConstant.ALL);
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			for (String tagID : articleTypeArr) {
				String tagPath = ArticleUtils.ARITCLE_L2_TAXONOMY_STRUCTURE + tagID;
				if (null != tagManager && null != tagManager.resolve(tagPath)) {
					articleTypeTagMap.put(tagID, tagManager.resolve(tagPath).getTitle());
				}
			}
			articleTypeFilterJSON = gson.toJson(articleTypeTagMap);
		}
		return articleTypeFilterJSON;
	}
	
	/**
	 * Gets the pagination url
	 * 
	 * @return paginationUrl the pagination url
	 */
	public String getPaginationUrl() {
		return CommonUtils.getPaginationUrl(resourceResolver, slingRequest, currentPage.getPath());
	}

	/**
	 * @return the articleBeanList
	 */
	public List<ArticleBean> getArticleBeanList() {
		return articleBeanList;
	}

	/**
	 * @return the articleBeanListJSON
	 */
	public String getArticleBeanListJSON() {
		return articleBeanListJSON;
	}

	/**
	 * @return the showLoadMoreButton
	 */
	public Boolean getShowLoadMoreButton() {
		return showLoadMoreButton;
	}

	/**
	 * @return the displayArticleTypeFilter
	 */
	public Boolean getDisplayArticleTypeFilter() {
		return displayArticleTypeFilter;
	}

	/**
	 * @return the displayFilterSection
	 */
	public Boolean getDisplayFilterSection() {
		return displayFilterSection;
	}

	/**
	 * @return the displayDateFilter
	 */
	public Boolean getDisplayDateFilter() {
		return displayDateFilter;
	}

	/**
	 * @return the noOfArticlesToDisplay
	 */
	public String getNoOfArticlesToDisplay() {
		return noOfArticlesToDisplay;
	}
	
	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
	}

}