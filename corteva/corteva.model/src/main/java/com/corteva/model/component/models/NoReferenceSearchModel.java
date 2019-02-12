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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;

/**
 * This the sling Model for the No Reference Search Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NoReferenceSearchModel extends ProductSlingModel {

	/** Inject the feature */
	@Inject
	@Via("resource")
	private String feature;

	/** Inject the basePath */
	@Inject
	@Via("resource")
	private String basePath;
	
	/** The resource resolver */
	private ResourceResolver resolver;

	/** The feature URL list */
	private List<String> featureUrlList;

	/** The resource iterator */
	private Iterator<Resource> resIterator;

	/** The Constant to hold feature type articles */
	private static final String FEATURE_TYPE_ARTICLES = "articles";

	/** The Constant to hold feature type products */
	private static final String FEATURE_TYPE_PRODUCTS = "products";

	/** The Constant ARTICLE_RESOURCE_TYPE. */
	private static final String ARTICLE_RESOURCE_TYPE = "corteva/components/structure/article-page";

	/**
	 * Init method post all injections.
	 */
	@PostConstruct
	public void init() {
		featureUrlList = new ArrayList<String>();
		if (StringUtils.isNotBlank(basePath)) {
			resolver = getResourceResolver();
			String resourceType = StringUtils.EMPTY;
			if (feature.equals(FEATURE_TYPE_PRODUCTS)) {
				resourceType = CortevaConstant.PDP_RESOURCE_TYPE;
			} else if (feature.equals(FEATURE_TYPE_ARTICLES)) {
				resourceType = ARTICLE_RESOURCE_TYPE;
			}
			if (StringUtils.isNotBlank(resourceType)) {
				resIterator = CommonUtils.findUnhiddenChildResources(resolver, resourceType, basePath);
				if (null != resIterator) {
					while (resIterator.hasNext()) {
						Resource featureRes = (Resource) resIterator.next();
						featureUrlList.add(LinkUtil.getHref(featureRes.getPath()));
					}
				}
			}
		}
	}

	/**
	 * @return the featureUrlList
	 */
	public List<String> getFeatureUrlList() {
		return featureUrlList;
	}

	/**
	 * @return the feature
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

}