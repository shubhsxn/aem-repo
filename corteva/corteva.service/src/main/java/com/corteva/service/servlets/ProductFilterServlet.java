/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.service.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.ProductFilterBean;
import com.corteva.model.component.utils.ProductFilterHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The is the ProductFilterServlet.
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_RESOURCE_TYPES + CortevaConstant.PRODUCT_FILTER_COMPONENT,
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.GET,
		CortevaConstant.SLING_SERVLET_SELECTORS + CortevaConstant.PRODUCT_FILTER_SELECTOR,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class ProductFilterServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7815015307250280937L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFilterServlet.class);
	
	/** The Constant to hold response status value Failed. */
	private static final String RESPONSE_STATUS_FAILED = "Failed";

	/** The base service. */
	private transient BaseConfigurationService configurationService;

	/**
	 * Bind base configuration service.
	 * 
	 * @param configurationService
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Unbind base configuration service.
	 * 
	 * @param configurationService
	 *            the configurationService
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.sling
	 * .api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		LOGGER.debug("Inside doGet of ProductFilterServlet");
		boolean featureFlag = true;
		if (configurationService != null) {
			featureFlag = configurationService.getToggleInfo("productFilterServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		String productList = null;
		if (featureFlag) {
				productList = getProductList(request);
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
		LOGGER.debug("Exiting doGet of ProductFilterServlet");
		response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
		response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
		if (null != productList) {
			response.getWriter().write(productList);
		} else {
			response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(RESPONSE_STATUS_FAILED);
		}
	}

	/**
	 * Gets the product list.
	 *
	 * @param request
	 *            the request
	 * @return the product list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getProductList(final SlingHttpServletRequest request) throws IOException {
		LOGGER.debug("Inside getProductList of ProductFilterServlet");
		ProductFilterBean filterBean = ProductFilterHelper.getProductFilterList(request, request.getResource(),
				configurationService);
		LOGGER.debug("Exiting getProductList of ProductFilterServlet");
		return new ObjectMapper().writeValueAsString(filterBean);
	}
}