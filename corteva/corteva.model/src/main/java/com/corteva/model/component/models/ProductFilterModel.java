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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ProductFilterBean;
import com.corteva.model.component.utils.ProductFilterHelper;
import com.day.cq.wcm.api.Page;

/**
 * This sling model will be used by the Product Filter component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ProductFilterModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFilterModel.class);

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;
	
	/** The current page. */
	private Page currentPage;
	
	/** The init method. */
	@PostConstruct
	public void init() {
		LOGGER.debug("Inside init method of productFilterModel");
		currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
	}

	/**
	 * Gets the page pdf url.
	 * 
	 * @return the page pdf url
	 */
	public String getServletPath() {
		LOGGER.debug("Inside getServletPath() method");
		String servletPath = currentResource.getPath() + CortevaConstant.DOT + CortevaConstant.PRODUCT_FILTER_SELECTOR
				+ CortevaConstant.DOT + CortevaConstant.JSON;
		servletPath = servletPath.replace(CortevaConstant.JCR_CONTENT, CortevaConstant.JCR_UNDERSCORE_CONTENT);
		LOGGER.debug("Servlet URL with json extension :: {}", servletPath);
		return servletPath;
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
	 * Gets the product list.
	 *
	 * @return the product list
	 */
	public ProductFilterBean getProductList() {
		LOGGER.debug("Inside getProductList of ProductFilterModel");
		ProductFilterBean filterBean = ProductFilterHelper.getProductFilterList(slingRequest, currentResource,
				getBaseConfigurationService());
		LOGGER.debug("Exiting getProductList of ProductFilterModel");
		return filterBean;

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