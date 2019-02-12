/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.ArticleBean;
import com.corteva.model.component.models.ImageUtil;
import com.corteva.model.component.models.ArticleTopicsModel;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The is a utility class that contain methods that perform operations and
 * fetched articles in the repository.
 * 
 * @author Sapient
 */
public final class ArticleUtils {
	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleUtils.class);
	/** Constant to Hold format for numeric month */
	private static final String NUMBERIC_MONTH = "MM";
	/** Constant to Hold format for Month name */
	private static final String MONTH_NAME = "MMMMMMMM";
	/** Constant to Hold format for Year - YYYY. */
	private static final String YYYY = "yyyy";
	/** Constant to hold property name of external URL Label */
	private static final String PROP_ARTICLE_EXTERNALURL_LABEL = "externalurllabel";
	/** Constant to hold property name of external URL */
	private static final String PROP_ARTICLE_EXTERNALURL = "externalurl";
	/** Constant to hold property name of short description */
	private static final String PROP_ARTICLE_SHORT_DESCRIPTION = "shortDescription";
	/** Constant to hold property name of primaryimage authored */
	private static final String PROP_PRIMARY_IMAGE_MOBILE = "primaryImageMobile";
	/** Constant to hold property name of primaryimage authored for mobile */
	private static final String PROP_ARTICLE_PRIMARY_IMAGE = "primaryImage";
	/** Constant to hold property name of display date */
	private static final String PROP_ARTICLE_DISPLAY_DATE = "displayDate";
	/** Constant to hold property name of page title */
	private static final String PROP_ARTICLE_PAGE_TITLE = "pageTitle";
	/** The Constant to hold value default article filter component. */
	private static final String DEFAULT_CORPORATE_SITE_PATH = "defaultCorporateSitePath";
	/** The Constant to hold value default article filter component. */
	private static final String DEFAULT_COMMERCIAL_SITE_PATH = "defaultCommericalSitePath";
	/** The Constant to hold value of constant offset */
	private static final String PROPERTY_OFFSET = "p.offset";
	/** The Constant to hold value of order by clause */
	private static final String PROPERTY_ORDER_BY = "_orderby";
	/** The Constant to hold value of order by Sorting */
	private static final String PROPERTY_ORDERBY_SORT = "_orderby.sort";
	/** The Constant to hold value of like operator */
	private static final String PROPERTY_LIKE_OPERATOR = "%";
	/** The Constant to hold value of property like */
	private static final String PROPERTY_LIKE = "like";
	/** The Constant to hold value of property group by */
	private static final String PROPERTY_GROUP_BY = "group";
	/** The Constant to hold value of @ operator */
	private static final String PROPERTY_OPERATOR_AMPERSAND = "@";
	/** The Constant to hold value of default dropdown name for months */
	private static final String DEFAULT_SELECT_VALUE_ALL_MONTHS = "All Months";
	/** Constant to Hold article type page property name */
	private static final String PAGE_PROPERTY_CQ_ARTICLE_TYPE = "cq:articleType";
	/** Taxonomy Structure for L2 level article tags */
	public static final String ARITCLE_L2_TAXONOMY_STRUCTURE = "/etc/tags/corteva/contentTypes/article/";

	/**
	 * @param page
	 *            the page retrieved from query
	 * @param bean
	 *            articleBean to set
	 * @param resourceResolver
	 *            the resource resolver from request
	 * @param slingRequest
	 *            the slingRequest
	 * @param componentName
	 *            the componentName
	 * @param baseService
	 *            the baseService
	 * @param pagePath
	 *            the pagePath
	 * @param displayTime
	 *            the flag displayTime
	 */
	public static void getArticlePageData(Page page, ArticleBean bean, ResourceResolver resourceResolver,
			SlingHttpServletRequest slingRequest, String componentName, BaseConfigurationService baseService,
			String pagePath, boolean displayTime) {

		if (null != page.getProperties().get(PROP_ARTICLE_PAGE_TITLE)) {
			bean.setArticleTitle(page.getProperties().get(PROP_ARTICLE_PAGE_TITLE).toString());
		}
		if (null != page.getProperties().get(ArticleUtils.PAGE_PROPERTY_CQ_ARTICLE_TYPE)) {
			String articleTypeTag = page.getProperties().get(ArticleUtils.PAGE_PROPERTY_CQ_ARTICLE_TYPE).toString();
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			if (null != tagManager && null != tagManager.resolve(articleTypeTag)) {
				bean.setArticleType(tagManager.resolve(articleTypeTag).getTitle());
			}
		}
		if (null != page.getProperties().get(PROP_ARTICLE_DISPLAY_DATE)) {
			Calendar calendarlastModifiedDate = (Calendar) page.getProperties().get(PROP_ARTICLE_DISPLAY_DATE);
			Locale locale = CommonUtils.getLocaleObject(pagePath, resourceResolver);
			LocalDateTime localDateTime = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(calendarlastModifiedDate.getTimeInMillis()), ZoneId.systemDefault());
			bean.setCustomDisplayDate(CommonUtils.getFormattedLocalizedDate(localDateTime, locale, displayTime, slingRequest, baseService));
		}
		Map<String, String> regCountryLangMap = null;
		if (null != page.getProperties().get(PROP_ARTICLE_PRIMARY_IMAGE)
				|| null != page.getProperties().get(PROP_PRIMARY_IMAGE_MOBILE)) {
			regCountryLangMap = CommonUtils.getRegionCountryLanguage(pagePath, resourceResolver);
		}
		if (null != page.getProperties().get(PROP_ARTICLE_PRIMARY_IMAGE)) {
			bean.setPrimaryImage(
					ImageUtil.getImageRenditionList(page.getProperties().get(PROP_ARTICLE_PRIMARY_IMAGE).toString(),
							componentName, slingRequest, baseService, regCountryLangMap));
		}
		if (null != page.getProperties().get(PROP_PRIMARY_IMAGE_MOBILE)) {
			bean.setPrimaryImageMobile(
					ImageUtil.getImageRenditionList(page.getProperties().get(PROP_PRIMARY_IMAGE_MOBILE).toString(),
							componentName, slingRequest, baseService, regCountryLangMap));
		}
		if (null != page.getProperties().get(PROP_ARTICLE_SHORT_DESCRIPTION)) {
			bean.setShortDescription(page.getProperties().get(PROP_ARTICLE_SHORT_DESCRIPTION).toString());
		}
		if (null != page.getProperties().get(PROP_ARTICLE_EXTERNALURL)) {
			bean.setArticlePagePath(resourceResolver
					.map(LinkUtil.getHref(page.getProperties().get(PROP_ARTICLE_EXTERNALURL).toString())));
			bean.setIsExternalURL(true);
			if (null != page.getProperties().get(PROP_ARTICLE_EXTERNALURL_LABEL)) {
				bean.setExternalURLLabel(page.getProperties().get(PROP_ARTICLE_EXTERNALURL_LABEL).toString());
			}
		} else {
			bean.setIsExternalURL(false);
			bean.setArticlePagePath(resourceResolver.map(slingRequest, LinkUtil.getHref(page.getPath())));
		}
	}

	/**
	 * @param index
	 *            the index
	 * @return incrementedIndex
	 */
	public static String incrementPropertyIndex(String index) {
		return String.valueOf(Integer.parseInt(index) + 1);
	}

	/**
	 * @param articlePages
	 *            articlePages to set
	 * @param resourceResolver
	 *            the resource resolver from request
	 * @param noOfArticles
	 *            the no of articles
	 * @param componentName
	 *            the name of component
	 * @param slingRequest
	 *            the slingRequest
	 * @param baseService
	 *            the baseService
	 * @param requestPagePath
	 *            the requestPagePath
	 * @param displayTime
	 *            the flag displayTime
	 * @return list of article bean
	 */
	public static List<ArticleBean> setPathToPage(List<String> articlePages, ResourceResolver resourceResolver,
			String noOfArticles, String componentName, SlingHttpServletRequest slingRequest,
			BaseConfigurationService baseService, String requestPagePath, boolean displayTime) {
		ArticleBean bean = null;
		List<ArticleBean> articleBeanList = new ArrayList<>();
		for (String article : articlePages) {
			Resource resource = resourceResolver.getResource(article);
			if (null != resource) {
				Page page = resource.adaptTo(Page.class);
				if (null != page) {
					bean = new ArticleBean();
					getArticlePageData(page, bean, resourceResolver, slingRequest, componentName, baseService,
							requestPagePath, displayTime);
					articleBeanList.add(bean);
				}
			}
			if (articleBeanList.size() == Integer.parseInt(noOfArticles)) {
				break;
			}
		}
		return articleBeanList;

	}

	/**
	 * @param articleType
	 *            the article type property
	 * @return the List
	 * @throws RepositoryException
	 *             the repository exception
	 * 
	 */
	public static List<String> getArticleTypePropertyList(Property articleType) throws RepositoryException {

		return getPropertyValueList(articleType);
	}

	/**
	 * @param prop
	 *            the property
	 * @return propList the value List
	 * @throws RepositoryException
	 *             the repository exception
	 * 
	 */
	public static List<String> getPropertyValueList(Property prop) throws RepositoryException {
		List<String> propList = new ArrayList<>();
		if (null != prop) {
			Value[] values = null;
			if (prop.isMultiple()) {
				values = prop.getValues();
				for (final Value val : values) {
					propList.add(val.getString());
				}
			} else {
				propList.add(prop.getValue().getString());
			}
		}
		return propList;
	}

	/**
	 * 
	 * @param pagePath
	 *            the pagePath
	 * @param resolver
	 *            the resolver
	 * @param baseService
	 *            the baseService
	 * @return folderPath
	 * 
	 */
	public static String getDefaultArticleFolderPath(String pagePath, ResourceResolver resolver,
			BaseConfigurationService baseService) {
		String folderPath = "";
		try {
			Map<String, String> regionCountrtLangMap = CommonUtils.getRegionCountryLanguage(pagePath, resolver);
			String language = regionCountrtLangMap.get(CortevaConstant.LANGUAGE);
			String country = StringUtils.lowerCase(regionCountrtLangMap.get(CortevaConstant.COUNTRY));
			String region = StringUtils.lowerCase(regionCountrtLangMap.get(CortevaConstant.REGION));
			String defaultCorportaeSitePath = baseService
					.getPropValueFromConfiguration(CortevaConstant.COMPONENT_CONFIG_NAME, DEFAULT_CORPORATE_SITE_PATH);
			String defaultCommericalSitePath = baseService
					.getPropValueFromConfiguration(CortevaConstant.COMPONENT_CONFIG_NAME, DEFAULT_COMMERCIAL_SITE_PATH);

			Resource pageResource = resolver.resolve(pagePath);
			Resource contentResource = CommonUtils.getPageFromResource(resolver, pageResource).getContentResource();
			InheritanceValueMap iValueMap = new HierarchyNodeInheritanceValueMap(contentResource);
			LOGGER.debug("IvalueMap :: {}", iValueMap);
			String corporateTag = iValueMap.getInherited(CortevaConstant.CQ_TAGS, "");
			if (StringUtils.equalsIgnoreCase(corporateTag, CortevaConstant.CORPORATE_TAG)) {
				folderPath = defaultCorportaeSitePath;
			} else {
				defaultCommericalSitePath = defaultCommericalSitePath.replace("$1", region).replace("$2", country)
						.replace("$3", language);
				folderPath = defaultCommericalSitePath;
			}
		} catch (IOException e) {
			LOGGER.error("IO Exception occured while fetching component config properties to get default folder path ",
					e);
		}
		return folderPath;
	}

	/**
	 * Gets the List of all Aticles
	 *
	 * @param resourceResolver
	 *            the resourceResolver
	 * @param contentPath
	 *            the contentPath
	 * @param articleType
	 *            the articleType
	 * @param articleTopics
	 *            the articleTopics
	 * @param intraFacetLogicalOperation
	 *            the logical operation within taxonomy facets
	 * @param interFacetLogicalOperation
	 *            the logical operation across taxonomy facets
	 * @param offset
	 *            the offset
	 * @param limit
	 *            the limit
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @return Iterator
	 * @throws RepositoryException
	 *             the repository exception
	 */
	public static Iterator<Resource> findArticleFeedFilterData(ResourceResolver resourceResolver, String contentPath,
			List<String> articleType, List<ArticleTopicsModel> articleTopics, String intraFacetLogicalOperation,
			String interFacetLogicalOperation, String limit, String offset, String year, String month)
			throws RepositoryException {
		Iterator<Resource> resultIterator = null;
		Map<String, String> params = new HashMap<>();
		params.put(PathPredicateEvaluator.PATH, contentPath);
		params.put("type", CortevaConstant.PROPERTY_PAGE_TYPE);
		String index = "1";
		if (articleType.size() > 0) {
			createParamMapForArticleTypeTags(articleType, params, index);
		}
		if (articleTopics.size() > 0) {
			createParamMapForArticleTopicTags(params, articleTopics, intraFacetLogicalOperation,
					interFacetLogicalOperation);
		}
		if (limit.equalsIgnoreCase(CortevaConstant.UNLIMITED_RESULT)) {
			params.put(CortevaConstant.PROPERTY_LIMIT, limit);
		} else {
			params.put(CortevaConstant.PROPERTY_LIMIT, String.valueOf(Integer.parseInt(limit) + 1));
		}
		params.put(PROPERTY_OFFSET, offset);
		index = incrementPropertyIndex(index);
		if (!year.equalsIgnoreCase(CortevaConstant.ALL)) {
			getDateRangeParamForQuery(params, year, month, index);
			index = incrementPropertyIndex(index);
		}
		// order by based on custom date and title
		params.put(index + PROPERTY_ORDERBY_SORT, "desc");
		params.put(index + PROPERTY_ORDER_BY, PROPERTY_OPERATOR_AMPERSAND + CortevaConstant.JCR_CONTENT
				+ CortevaConstant.FORWARD_SLASH + PROP_ARTICLE_DISPLAY_DATE);
		index = incrementPropertyIndex(index);
		params.put(index + PROPERTY_ORDER_BY, PROPERTY_OPERATOR_AMPERSAND + CortevaConstant.JCR_CONTENT
				+ CortevaConstant.FORWARD_SLASH + PROP_ARTICLE_PAGE_TITLE);
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		if (queryBuilder != null) {
			Query query = queryBuilder.createQuery(PredicateGroup.create(params),
					resourceResolver.adaptTo(Session.class));
			resultIterator = query.getResult().getResources();
		}
		return resultIterator;
	}

	/**
	 * @param articleType
	 *            the article type list
	 * @param params
	 *            the predicate map
	 * @param index
	 *            the index for predicates
	 */
	public static void createParamMapForArticleTypeTags(List<String> articleType, Map<String, String> params,
			String index) {
		params.put(index + CortevaConstant.PROPERTY_CONSTANT,
				CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + PAGE_PROPERTY_CQ_ARTICLE_TYPE);
		for (int i = 1; i < articleType.size() + 1; i++) {
			params.put(
					index + CortevaConstant.PROPERTY_CONSTANT + CortevaConstant.DOT + i
							+ CortevaConstant.VALUE_CONSTANT,
					PROPERTY_LIKE_OPERATOR + articleType.get(i - 1) + PROPERTY_LIKE_OPERATOR);
		}
		params.put(index + CortevaConstant.PROPERTY_OPERATION, PROPERTY_LIKE);
	}

	/**
	 * @param params
	 *            the predicate map
	 * @param articleTopics
	 *            the article topics list
	 * @param intraFacetLogicalOperation
	 *            the logical operation within taxonomy facets
	 * @param interFacetLogicalOperation
	 *            the logical operation across taxonomy facets
	 * @throws RepositoryException
	 *             the repository exception
	 */
	public static void createParamMapForArticleTopicTags(Map<String, String> params,
			List<ArticleTopicsModel> articleTopics, String intraFacetLogicalOperation,
			String interFacetLogicalOperation) throws RepositoryException {
		for (int i = 1; i < articleTopics.size() + 1; i++) {
			ArticleTopicsModel articleTopicsModel = articleTopics.get(i - 1);
			String facet = articleTopicsModel.getArticleFacet();
			String facetRootTag = CortevaConstant.CORTEVA + CortevaConstant.COLON + facet;
			Property facetTags = articleTopicsModel.getFacetTags();
			List<String> facetTagList = getPropertyValueList(facetTags);
			if (facetTagList.size() > 0) {
				params.put(PROPERTY_GROUP_BY + CortevaConstant.DOT + i + CortevaConstant.PROPERTY_CONSTANT,
						CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.CQ_TAGS);
				for (int j = 1; j < facetTagList.size() + 1; j++) {
					String facetTag = facetTagList.get(j - 1);
					if (StringUtils.startsWith(facetTag, facetRootTag)) {
						params.put(
								PROPERTY_GROUP_BY + CortevaConstant.DOT + i + CortevaConstant.PROPERTY_CONSTANT
										+ CortevaConstant.DOT + j + CortevaConstant.VALUE_CONSTANT,
								PROPERTY_LIKE_OPERATOR + facetTag + PROPERTY_LIKE_OPERATOR);
					}
				}
				params.put(PROPERTY_GROUP_BY + CortevaConstant.DOT + i + CortevaConstant.PROPERTY_OPERATION,
						PROPERTY_LIKE);
				params.put(PROPERTY_GROUP_BY + CortevaConstant.DOT + i + CortevaConstant.PROPERTY_CONSTANT
						+ CortevaConstant.DOT + intraFacetLogicalOperation, CortevaConstant.TRUE);
			}
		}
		params.put(CortevaConstant.PROPERTY_GROUP_OPERATION + CortevaConstant.DOT + interFacetLogicalOperation,
				CortevaConstant.TRUE);
	}

	/**
	 * @param params
	 *            the params
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param index
	 *            the index
	 */
	public static void getDateRangeParamForQuery(Map<String, String> params, String year, String month, String index) {
		String dateRangeLowerBound = "";
		String dateRangeUpperBound = "";
		int dateTemp = 0;
		if (month.equalsIgnoreCase(CortevaConstant.ALL)) {
			dateRangeLowerBound = year + CortevaConstant.HYPHEN + "01-01";
			dateTemp = Integer.parseInt(year) + 1;
			dateRangeUpperBound = dateTemp + CortevaConstant.HYPHEN + "01-01";
		} else {
			dateTemp = Integer.parseInt(month) + 1;
			dateRangeLowerBound = year + CortevaConstant.HYPHEN + month + CortevaConstant.HYPHEN + "01";
			dateRangeUpperBound = year + CortevaConstant.HYPHEN + dateTemp + CortevaConstant.HYPHEN + "01";
		}
		params.put(index + "_daterange.property",
				CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + PROP_ARTICLE_DISPLAY_DATE);
		params.put(index + "_daterange.lowerBound", dateRangeLowerBound);
		params.put(index + "_daterange.upperBound", dateRangeUpperBound);
		params.put(index + "_daterange.lowerOperation", ">=");
		params.put(index + "_daterange.upperOperation", "<");
	}

	/**
	 * @param dateMap
	 *            the dateMap
	 * @param cal
	 *            the calendar
	 * @param i
	 *            the index
	 * @throws RepositoryException
	 */
	public static void createDateMapFilter(Map<String, Map<String, String>> dateMap, Calendar cal, int i) {
		String year;
		Map<String, String> monthMap;
		year = new SimpleDateFormat(YYYY).format(cal.getTime());
		if (i == 0) {
			monthMap = new LinkedHashMap<>();
			monthMap.put(CortevaConstant.ALL, ArticleUtils.DEFAULT_SELECT_VALUE_ALL_MONTHS);
			dateMap.put(new SimpleDateFormat(YYYY).format(cal.getTime()), monthMap);
		} else {
			if (!dateMap.containsKey(year)) {
				monthMap = new LinkedHashMap<>();
				monthMap.put(CortevaConstant.ALL, ArticleUtils.DEFAULT_SELECT_VALUE_ALL_MONTHS);
				dateMap.put(new SimpleDateFormat(YYYY).format(cal.getTime()), monthMap);
			}
		}
		dateMap.get(year).put(new SimpleDateFormat(NUMBERIC_MONTH).format(cal.getTime()),
				new SimpleDateFormat(MONTH_NAME).format(cal.getTime()));
	}

	/**
	 * @param dateMap
	 *            the date Map
	 * @return the JSON Array
	 */
	public static JsonArray formatDateMapResponse(Map<String, Map<String, String>> dateMap) {
		JsonObject dateResponseJSON = null;
		JsonObject monthResponseJSON = null;
		JsonArray monthResponseJSONArray = null;
		JsonArray array = new JsonArray();
		for (Map.Entry<String, Map<String, String>> pair : dateMap.entrySet()) {
			dateResponseJSON = new JsonObject();
			monthResponseJSONArray = new JsonArray();
			dateResponseJSON.addProperty("year", pair.getKey());
			Map<String, String> monthMap = pair.getValue();
			for (Map.Entry<String, String> month : monthMap.entrySet()) {
				monthResponseJSON = new JsonObject();
				monthResponseJSON.addProperty(month.getKey(), month.getValue());
				monthResponseJSONArray.add(monthResponseJSON);
			}
			dateResponseJSON.add("month", monthResponseJSONArray);
			array.add(dateResponseJSON);
		}
		return array;
	}

}
