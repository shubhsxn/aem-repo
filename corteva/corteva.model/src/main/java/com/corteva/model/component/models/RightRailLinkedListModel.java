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
import java.util.Iterator;
import java.util.List;

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
import com.day.cq.wcm.api.Page;

/**
 * The is the sling for the Right Rail Linked list Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class RightRailLinkedListModel extends AbstractSlingModel {
	/** COnstant to hold value of Linked List Feed type. */
	private static final String LINKED_LIST_TYPE_FEED = "feed";
	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RightRailLinkedListModel.class);
	/** The Resource Resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/**
	 * The base config.
	 */
	@Inject
	private BaseConfigurationService baseService;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The Article root path. */
	private String folderPath;

	/** The Article File path. */
	@Inject
	@Optional
	@Via("resource")
	private Property articlePagePaths;

	/** The Link List Type - Feed / Featured Articles. */
	@Inject
	@Optional
	@Via("resource")
	private String linkListType;

	/** The No of articles to display. */
	@Inject
	@Optional
	@Via("resource")
	private String noofArticlesDisplayed;

	/** The No of articles to display. */
	@Inject
	@Optional
	@Via("resource")
	private Property articleType;

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

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The current page. */
	private Page currentPage;

	/** The list of path of articles Pages */
	private List<ArticleBean> articleBeanList;

	/** The Constant PROPERTY_ARTICLE_PAGE_PATH. */
	private static final String PROPERTY_ARTICLE_PAGE_PATH = "articlePagePath";

	/** The Flag to check whether timestamp needs to be displayed. */
	private boolean isTimeRequired;

	/**
	 * Instantiates a new Article Filter model.
	 *
	 * @param request
	 *            the request
	 */
	public RightRailLinkedListModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * The init method.
	 * 
	 * @throws RepositoryException
	 * 
	 * 
	 */
	@PostConstruct
	public void init() {
		try {

			currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
			isTimeRequired = false;
			List<String> articlePages = new ArrayList<>();
			if (null != linkListType) {
				if (linkListType.equalsIgnoreCase(LINKED_LIST_TYPE_FEED)) {
					articlePages = getFeed();
				} else {
					if (null != articlePagePaths) {
						articlePages = AEMUtils.getListFromMultiFieldJSONProperty(articlePagePaths,
								PROPERTY_ARTICLE_PAGE_PATH);
					}
					noofArticlesDisplayed = String.valueOf(articlePages.size());
				}
			}
			if (null != currentPage) {
				articlePages.remove(currentPage.getPath());
			}
			articleBeanList = ArticleUtils.setPathToPage(articlePages, resourceResolver, noofArticlesDisplayed,
					CommonUtils.getComponentName(resourceType), slingRequest, baseService,
					CommonUtils.getPagePath(slingRequest), isTimeRequired);

		} catch (RepositoryException e) {
			LOGGER.error("Repository Exception occured while instantiating Article Filter Model", e);
		}

	}

	/**
	 * @return List of string
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private List<String> getFeed() throws RepositoryException {
		List<String> articlePages = new ArrayList<>();
		List<ArticleTopicsModel> articleTopicsList = new ArrayList<>();
		List<String> articleTypeArr = ArticleUtils.getArticleTypePropertyList(articleType);
		if (null != articleTopics) {
			articleTopicsList = AEMUtils.getMultifieldItemList(articleTopics, ArticleTopicsModel.class);
		}
		if (StringUtils.isEmpty(folderPath)) {
			folderPath = ArticleUtils.getDefaultArticleFolderPath(CommonUtils.getPagePath(slingRequest),
					resourceResolver, baseService);
		}
		Iterator<Resource> pageIterator = ArticleUtils.findArticleFeedFilterData(resourceResolver, folderPath,
				articleTypeArr, articleTopicsList, intraFacetLogicalOperation, interFacetLogicalOperation,
				noofArticlesDisplayed, "0", CortevaConstant.ALL, "");
		while (null != pageIterator && pageIterator.hasNext()) {
			Resource pageResource = pageIterator.next();
			if (pageResource != null) {
				Page page = pageResource.adaptTo(Page.class);
				if (page != null) {
					articlePages.add(page.getPath());
				}
			}
		}
		return articlePages;

	}

	/**
	 * @param resourceResolver
	 *            the resourceResolver to set
	 */
	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	/**
	 * @return the articleBeanList
	 */
	public List<ArticleBean> getArticleBeanList() {
		return articleBeanList;
	}

}