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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.day.cq.wcm.api.Page;

/**
 * This servlet is triggered when the author fetches the Json stored for sales
 * rep and retailer
 *
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/getsalesrepjson",
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class GetSalesRepRetailerJsonServlet extends AbstractServlet {
	/**
	 * The Constant to hold value Error message string.
	 */
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger Instantiation.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GetSalesRepRetailerJsonServlet.class);
	/**
	 * The Constant SALESREP_SELECTOR.
	 */
	public static final String SALESREP_SELECTOR = "salesrep";
	/**
	 * The Constant RETAILER_SELECTOR.
	 */
	public static final String RETAILER_SELECTOR = "retailer";
	/**
	 * The Constant Contractor_Selector.
	 */
	public static final String CONTRACTOR_SELECTOR = "contractor";
	/**
	 * The Constant NODE_SALES_REP.
	 */
	public static final String NODE_SALES_REP = "json_store_salesrep";
	/**
	 * The Constant NODE_RETAILER.
	 */
	public static final String NODE_RETAILER = "json_store_retailer";
	/**
	 * The Constant NODE_CONTRACTOR.
	 */
	public static final String NODE_CONTRACTOR = "json_store_contractor";
	/**
	 * The Constant JSON_STORE_PROPERTY.
	 */
	public static final String JSON_STORE_PROPERTY = "json_store";

	/**
	 * Resource resolver
	 **/
	private transient ResourceResolver resourceResolver;
	/**
	 * The base service.
	 */
	private transient BaseConfigurationService configurationService;

	/**
	 * Bind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.configurationService = baseServiceConfig;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.configurationService = baseServiceConfig;
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
			throws ServletException, IOException {
		// Sample Request type : /bin/corteva/getSalesRepJson.salesrep.json OR
		// bin/corteva/getSalesRepJson.retailer.json
		boolean featureFlag = true;

		String[] selector = request.getRequestPathInfo().getSelectors();
		String regionPath = String.join(CortevaConstant.FORWARD_SLASH, selector).replaceFirst("/[^/]*?/?$", "");
		if (configurationService != null) {
			featureFlag = configurationService.getToggleInfo("salesRepRetailerJsonServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			LOGGER.debug("Feature Flag Value is {} ", featureFlag);
		}
		if (featureFlag) {
			String jsonType;
			if (selector.length > CortevaConstant.ZERO
					&& ((StringUtils.equalsIgnoreCase(SALESREP_SELECTOR, selector[3]))
							|| (StringUtils.equalsIgnoreCase(RETAILER_SELECTOR, selector[3])) || (StringUtils.equalsIgnoreCase(CONTRACTOR_SELECTOR, selector[3])))) {
				jsonType = selector[3];
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write(INTERNAL_SERVER_ERROR);
				LOGGER.error("Incorrect selector");
				return;
			}
			resourceResolver = request.getResourceResolver();
			Page currentPage = getPagePathFromRequest(request, regionPath);
			if (null == currentPage) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write(INTERNAL_SERVER_ERROR);
				LOGGER.error("Current Page resource cannot be retrieved");
				return;
			}
			String strJson = StringUtils.EMPTY;
			Page countryPage = currentPage.getAbsoluteParent(3);
			Page languagePage = currentPage.getAbsoluteParent(4);
			if (null != countryPage) {
				strJson = getJson(jsonType, countryPage, languagePage);
			}
			response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
			response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
			response.getWriter().write(strJson);
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}

	}

	/**
	 * This method returns the JSON in string format
	 * 
	 * @param jsonType
	 *            the json type
	 * @param countryPage
	 *            the country page
	 * @param languagePage
	 *            the language page
	 * @return json
	 */
	private String getJson(String jsonType, Page countryPage, Page languagePage) {
		String strJson = StringUtils.EMPTY;
		Resource jsonResource;
		LOGGER.debug("Region Page : {}", countryPage.getPath());
		String jsonTypeNode = StringUtils.EMPTY;
		if (StringUtils.equalsIgnoreCase(jsonType, SALESREP_SELECTOR)) {
			jsonTypeNode = NODE_SALES_REP;
		} else if (StringUtils.equalsIgnoreCase(jsonType, RETAILER_SELECTOR)) {
			jsonTypeNode = NODE_RETAILER;
		} else if (StringUtils.equalsIgnoreCase(jsonType, CONTRACTOR_SELECTOR)) {
			jsonTypeNode = NODE_CONTRACTOR;
		}
		jsonResource = resourceResolver
				.getResource(countryPage.getPath() + CortevaConstant.FORWARD_SLASH + jsonTypeNode);
		if (null == jsonResource && null != languagePage) {
			jsonResource = resourceResolver
					.getResource(languagePage.getPath() + CortevaConstant.FORWARD_SLASH + jsonTypeNode);
		}
		if (null != jsonResource
				&& StringUtils.isNotBlank(jsonResource.getValueMap().get(JSON_STORE_PROPERTY).toString())) {
			strJson = jsonResource.getValueMap().get(JSON_STORE_PROPERTY).toString();
			LOGGER.debug("Json Exists :");
		} else {
			LOGGER.debug("Json does not exist");
		}
		return strJson;
	}

	/**
	 * This method gets the current page from request
	 *
	 * @param request
	 *            the request
	 * @param regionPath
	 *            the regionPath
	 * @return the path of the page
	 */
	private Page getPagePathFromRequest(SlingHttpServletRequest request, String regionPath) {
		Resource res = null;
		Page page = null;
		String resourcePath = new StringBuilder().append(CortevaConstant.CONTENT_ROOT_PATH_BREVANT)
				.append(CortevaConstant.FORWARD_SLASH).append(regionPath).toString();
		res = resourceResolver.resolve(request, resourcePath);
		page = res.adaptTo(Page.class);
		LOGGER.debug("Path of the referred page is {} ", resourcePath);
		return page;
	}
}
