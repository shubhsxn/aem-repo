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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;

/**
 * The sling model product detail page.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductSlingModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSlingModel.class);

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;
	
	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The base service. */
	@Inject
	private BaseConfigurationService baseService;

	/** The Constant to hold value crop list resource type component. */
	private static final String CROP_LIST_SLING_RESOURCE_TYPE = "corteva/components/content/cropList/v1/cropList";

	/** The Constant crop type property. */
	private static final String CROP_TYPE_PROPERTY = "cq:cropType";

	/** Constant to hold value of product type of Product Page. */
	private static final String PAGE_PROPERTY_PRODUCT_TYPE = "cq:productType";

	/**
	 * Gets the pdp source.
	 *
	 * @param currentResource
	 *            the current resource
	 * @return the pdp source
	 */
	public String getPdpSource(Resource currentResource) {
		return getProductAttribute(currentResource, CortevaConstant.PDP_SOURCE);
	}

	/**
	 * Gets the product id.
	 *
	 * @param currentResource
	 *            the current resource
	 * @param pdpSrc
	 *            the pdp src
	 * @return the product id
	 */
	public String getProductId(Resource currentResource, String pdpSrc) {
		LOGGER.debug("Inside getProductId() method");
		String productId;
		if (StringUtils.equalsIgnoreCase(CortevaConstant.NON_AGRIAN, pdpSrc)) {
			Page currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
			productId = currentPage.getName();
		} else {
			productId = getProductAttribute(currentResource, CortevaConstant.PRODUCT_ID);
		}
		LOGGER.debug("Product ID :: {}", productId);
		return productId;
	}

	/**
	 * Gets the product name.
	 *
	 * @param currentResource
	 *            the current resource
	 * @return the product name
	 */
	public String getProductName(Resource currentResource) {
		return getProductAttribute(currentResource, CortevaConstant.PRODUCT_NAME);
	}

	/**
	 * Gets the product attribute.
	 *
	 * @param currentResource
	 *            the current resource
	 * @param productAttribute
	 *            the product attribute
	 * @return the product attribute
	 */
	private String getProductAttribute(Resource currentResource, String productAttribute) {
		String productAttr = StringUtils.EMPTY;
		Page currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
		if (currentPage.getProperties().containsKey(productAttribute)) {
			productAttr = currentPage.getProperties().get(productAttribute).toString();
		}
		return productAttr;
	}

	/**
	 * Gets the label finder path.
	 *
	 * @param request
	 *            the request
	 * @param currentResource
	 *            the current resource
	 * @return the label finder path
	 */
	public String getLabelFinderPath(SlingHttpServletRequest request, Resource currentResource) {
		LOGGER.debug("Inside getLabelFinderPath() method");
		String labelFinderPath;
		StringBuilder sb = new StringBuilder();
		String pdpSource = getPdpSource(currentResource);
		String productId = getProductId(currentResource, pdpSource);
		sb.append(getBaseConfigurationService().getPropConfigValue(request, "labelFinderPath",
				CortevaConstant.PRODUCT_CONFIG_NAME)).append(CortevaConstant.DOT)
				.append(CortevaConstant.HTML_EXTENSION);
		if (StringUtils.isNotBlank(pdpSource) && StringUtils.isNotBlank(productId)) {
			sb.append(CortevaConstant.QUESTION_MARK).append("pid").append(CortevaConstant.EQUAL)
					.append(productId).append(CortevaConstant.AMPERSAND).append(CortevaConstant.PDP_SOURCE)
					.append(CortevaConstant.EQUAL).append(pdpSource);
		}
		labelFinderPath = sb.toString();
		LOGGER.debug("Agrain Label Finder Path :: {}", labelFinderPath);
		return getResourceResolver().map(request, labelFinderPath);
	}

	/**
	 * Gets the state Map.
	 *
	 * @param regionCountryLanguageMap
	 *            the region country language map
	 * @param request
	 *            the request
	 * @return the state map
	 */
	public Map<String, String> getStateMap(Map<String, String> regionCountryLanguageMap,
			SlingHttpServletRequest request) {
		LOGGER.debug("Inside getStateMap() method");
		Map<String, String> stateMap = null;
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		boolean containsKey = regionCountryLanguageMap.containsKey(CortevaConstant.REGION)
				&& regionCountryLanguageMap.containsKey(CortevaConstant.COUNTRY)
				&& regionCountryLanguageMap.containsKey(CortevaConstant.LANGUAGE);
		if (containsKey) {
			StringBuilder sb = new StringBuilder();
			String region = regionCountryLanguageMap.get(CortevaConstant.REGION);
			String country = regionCountryLanguageMap.get(CortevaConstant.COUNTRY);
			String rootTagPath = sb
					.append(baseService.getPropertyValueFromConfiguration(regionCountryLanguageMap,
							CortevaConstant.STATE_BASE_TAG_PATH, CortevaConstant.PRODUCT_CONFIG_NAME))
					.append(region).append(CortevaConstant.FORWARD_SLASH).append(country).toString();
			List<Tag> tags = TagUtil.listChildren(tagManager, rootTagPath);
			if (null != tags && !tags.isEmpty()) {
				stateMap = new TreeMap<>();
				for (Tag tag : tags) {
					String tagKey = tag.getName();
					String tagValue = CommonUtils.getTagLocalizedTitle(slingRequest, tag);

					stateMap.put(tagKey, tagValue);
				}
			}
		}
		LOGGER.debug("State Map from tags :: {}", stateMap);
		return stateMap;
	}

	/**
	 * Gets the crop type tags.
	 *
	 * @param currentResource
	 *            the current resource
	 * @return the crop type tags
	 */
	public List<String> getCropTypeTags(Resource currentResource) {
		LOGGER.debug("Inside getCropTypeTags() method");
		Page currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
		Node componentNode = getComponentNodeFromPage(currentPage.getPath());
		List<String> productTypeTagList = new ArrayList<>();
		if (null != componentNode) {
			try {
				TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
				Property property = componentNode.getProperty(CROP_TYPE_PROPERTY);
				if (null != property && null != tagManager) {
					populateProductTypeTagList(productTypeTagList, tagManager, property);
				}
			} catch (IllegalStateException | RepositoryException e) {
				LOGGER.error("Exception occurred in getCropTypeTags()", e);
			}
		}
		return productTypeTagList;
	}

	/**
	 * Populate product type tag list.
	 *
	 * @param productTypeTagList
	 *            the product type tag list
	 * @param tagManager
	 *            the tag manager
	 * @param property
	 *            the property
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void populateProductTypeTagList(List<String> productTypeTagList, TagManager tagManager, Property property)
			throws RepositoryException {
		if (property.isMultiple()) {
			for (Value value : property.getValues()) {
				populateTagList(productTypeTagList, tagManager, value.getString());
			}
		} else {
			populateTagList(productTypeTagList, tagManager, property.getString());
		}
	}

	/**
	 * Internal method for Populate product type tag list
	 * 
	 * @param productTypeTagList
	 *            the product type tag list
	 * @param tagManager
	 *            the tag manager
	 * @param tagId
	 *            the tag id
	 */
	private void populateTagList(List<String> productTypeTagList, TagManager tagManager, String tagId) {
		Tag tagValue = tagManager.resolve(tagId);
		String resolvedTag = getLevelOneCropTag(tagValue);
		if (StringUtils.isNotBlank(resolvedTag) && !productTypeTagList.contains(resolvedTag)) {
			productTypeTagList.add(resolvedTag);
		}
	}

	/**
	 * Gets the component node from page.
	 *
	 * @param pagePath
	 *            the page path
	 * @return the node
	 */
	private Node getComponentNodeFromPage(String pagePath) {
		Node componentNode = null;
		LOGGER.debug("Fetching first matching Crop List component from Current Page {}", pagePath);
		Iterator<Resource> resultIterator = null;
		Map<String, String> params = new HashMap<>();
		params.put(PathPredicateEvaluator.PATH, pagePath);
		params.put("1_property", CortevaConstant.SLING_RESOURCE_TYPE);
		params.put("1_property.value", CROP_LIST_SLING_RESOURCE_TYPE);
		params.put("p_limit", "-1");
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		if (queryBuilder != null) {
			Query query = queryBuilder.createQuery(PredicateGroup.create(params),
					resourceResolver.adaptTo(Session.class));
			resultIterator = query.getResult().getResources();
			Resource res = resultIterator.next();
			if (res != null) {
				componentNode = res.adaptTo(Node.class);
			}
		}
		return componentNode;
	}

	/**
	 * Gets the level one crop tag.
	 *
	 * @param tagValue
	 *            the tagValue
	 * @return the resolved tag
	 */
	private String getLevelOneCropTag(Tag tagValue) {
		if (null != tagValue) {
			Tag parentTag = tagValue.getParent();
			if (!parentTag.getName().equalsIgnoreCase("cropTypes")) {
				return parentTag.getTitle();
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Gets the product type.
	 * 
	 * @param currentPage
	 *            the current page
	 * @return the product type
	 */
	public List<String> getProductTypeTags(Page currentPage) {
		LOGGER.debug("Inside getProductTypeTags() method");
		ValueMap valueMap = currentPage.getContentResource().getValueMap();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		List<String> productTypeTagList = new ArrayList<>();
		Property property = valueMap.get(PAGE_PROPERTY_PRODUCT_TYPE, Property.class);
		if (null != property && null != tagManager) {
			String resolvedTag = "";
			try {
				if (property.isMultiple()) {
					for (Value value : property.getValues()) {
						resolvedTag = tagManager.resolve(value.getString()).getTitle();
						productTypeTagList.add(resolvedTag);
					}
				} else {
					resolvedTag = tagManager.resolve(property.getString()).getTitle();
					productTypeTagList.add(resolvedTag);
				}
			} catch (IllegalStateException | RepositoryException e) {
				LOGGER.error("Exception occurred in getProductType()", e);
			}
		}
		return productTypeTagList;
	}

	/**
	 * Gets the locale for internationalization.
	 *
	 * @param request
	 *            the request
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization(SlingHttpServletRequest request) {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(request), getResourceResolver());
	}
}