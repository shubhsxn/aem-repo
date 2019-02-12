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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;

/**
 * This servlet is triggered when the author selects any content. This servlet
 * reads the content metadata of the selected content and returns all the
 * required properties. The author can override the fields if he wants.
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/contentmetadata",
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.POST,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class ContentMetadataServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentMetadataServlet.class);

	/** The Constant PAGE_PATH. */
	private static final String PAGE_PATH = "pagePath";

	/** The Constant JCR_CREATED. */
	private static final String JCR_CREATED = "jcr:created";

	/** The Constant ASSET_PATH. */
	private static final String ASSET_PATH = "assetPath";

	/** The Constant PAGE_PROPERTIES_KEYS. */
	private static final String PAGE_PROPERTIES_KEYS = "pagePropertiesKeys";

	/** The Constant IMAGE_KEY. */
	private static final String IMAGE_KEY = "imageKey";

	/** The Constant DATE. */
	private static final String DATE = "Date";

	/** The Constant ASSET_PROPERTIES_KEYS. */
	private static final String ASSET_PROPERTIES_KEYS = "assetPropertiesKeys";

	/** The base service. */
	private transient BaseConfigurationService baseServiceConfig;

	/**
	 * Bind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.baseServiceConfig = baseServiceConfig;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.baseServiceConfig = baseServiceConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.sling
	 * .api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		boolean featureFlag = true;
		if (baseServiceConfig != null) {
			featureFlag = baseServiceConfig.getToggleInfo("contentMetaDataServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			String contentPath;
			String[] metadataProperties = null;
			/**
			 * Condition if page metadata needs to be fetched.
			 */
			if (StringUtils.isNotBlank(request.getParameter(PAGE_PATH))
					&& null != request.getParameterValues(PAGE_PROPERTIES_KEYS)
					&& StringUtils.isNotBlank(request.getParameter(IMAGE_KEY))) {
				getPageMetaData(request, response);
			}
			/**
			 * Condition if asset metadata needs to be fetched.
			 */
			if (StringUtils.isNotBlank(request.getParameter(ASSET_PATH))
					&& null != request.getParameterValues(ASSET_PROPERTIES_KEYS)) {
				contentPath = request.getParameter(ASSET_PATH);
				metadataProperties = request.getParameterValues(ASSET_PROPERTIES_KEYS);
				/**
				 * Setting the asset metadata response.
				 */
				if (StringUtils.isNotBlank(contentPath) && null != metadataProperties) {
					response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
					response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
					response.getWriter()
							.write(sendResponse(getAssetMetadataProperties(request, contentPath, metadataProperties)));
				}
			}

		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * This method fetches page meta data and sets in response
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             the IO Exception
	 */
	private void getPageMetaData(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		String contentPath;
		String[] metadataProperties;
		contentPath = request.getParameter(PAGE_PATH);
		metadataProperties = request.getParameterValues(PAGE_PROPERTIES_KEYS);
		String imageProperty = request.getParameter(IMAGE_KEY);
		/**
		 * Setting the page metadata response.
		 */
		if (StringUtils.isNotBlank(contentPath) && null != metadataProperties
				&& StringUtils.isNotBlank(imageProperty)) {
			response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
			response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
			response.getWriter().write(
					sendResponse(getMetadataProperties(request, contentPath, metadataProperties, imageProperty)));
		}
	}

	/**
	 * This method reads the content metadata of the given content and returns all
	 * the properties of the metadata.
	 *
	 * @param request
	 *            the request
	 * @param contentPath
	 *            the content path
	 * @param pageKeys
	 *            the page keys
	 * @param imageKey
	 *            the image key
	 * @return the metadata properties
	 */
	private Map<String, Map<String, String>> getMetadataProperties(SlingHttpServletRequest request, String contentPath,
			String[] pageKeys, String imageKey) {
		LOGGER.debug("Inside getMetadataProperties() method");
		List<String> pageKeyList = Arrays.asList(pageKeys);
		Map<String, Map<String, String>> valueMap = null;

		Resource contentResource = request.getResourceResolver().resolve(contentPath);
		Resource jcrResource = null;
		if (!ResourceUtil.isNonExistingResource(contentResource)) {
			jcrResource = contentResource.getChild(CortevaConstant.JCR_CONTENT);
			if (null != jcrResource) {
				ValueMap vMap = jcrResource.getValueMap();
				if (!vMap.isEmpty()) {
					valueMap = populateMetadataPropertiesMap(pageKeyList, imageKey, vMap);
				}
			}
		}
		LOGGER.debug("Page Value Map :: {}", valueMap);
		return valueMap;
	}

	/**
	 * Populate metadata properties map.
	 *
	 * @param pageKeyList
	 *            the page key list
	 * @param imageKey
	 *            the image key
	 * @param vMap
	 *            the v map
	 * @return the map
	 */
	private Map<String, Map<String, String>> populateMetadataPropertiesMap(List<String> pageKeyList, String imageKey,
			ValueMap vMap) {
		LOGGER.debug("Inside populateMetadataPropertiesMap() method");

		Map<String, Map<String, String>> valueMap = new HashMap<>();
		Map<String, String> pagePropMap = new HashMap<>();
		Map<String, String> imagePropMap = new HashMap<>();

		if (vMap.containsKey(imageKey)) {
			imagePropMap.put(imageKey, (String) vMap.get(imageKey));
		}
		LOGGER.debug("Image Path Map :: {}", imagePropMap);
		for (String pageKey : pageKeyList) {
			if (vMap.containsKey(pageKey)) {
				Object vMapValue = vMap.get(pageKey);
				if (StringUtils.startsWithIgnoreCase(pageKey, DATE)
						|| StringUtils.endsWithIgnoreCase(pageKey, DATE)
						|| StringUtils.equalsIgnoreCase(pageKey, JCR_CREATED)
						|| StringUtils.equalsIgnoreCase(pageKey, CortevaConstant.CQ_LAST_MODIFIED)) {
					pagePropMap.put(pageKey, CommonUtils.formatDate(CommonUtils.getDateFromValueMap(vMapValue),
							CortevaConstant.MM_DD_YY));
				} else {
					pagePropMap.put(pageKey, (String) vMapValue);
				}
			}
		}
		valueMap.put("pageJson", pagePropMap);
		valueMap.put("imageJson", imagePropMap);
		return valueMap;
	}

	/**
	 * Gets the asset metadata properties.
	 *
	 * @param request
	 *            the request
	 * @param contentPath
	 *            the content path
	 * @param assetKeys
	 *            the asset keys
	 * @return the asset metadata properties
	 */
	private Map<String, String> getAssetMetadataProperties(SlingHttpServletRequest request, String contentPath,
			String[] assetKeys) {
		LOGGER.debug("Inside getAssetMetadataProperties() method");
		List<String> assetKeyList = Arrays.asList(assetKeys);
		Map<String, String> assetPropMap = null;
		Resource contentResource = request.getResourceResolver().resolve(contentPath);
		Resource jcrResource = null;
		if (!ResourceUtil.isNonExistingResource(contentResource)) {
			jcrResource = contentResource
					.getChild(CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.METADATA);
			if (null != jcrResource) {
				ValueMap vMap = jcrResource.getValueMap();
				if (!vMap.isEmpty()) {
					assetPropMap = populateAssetMetadataPropertiesMap(assetKeyList, vMap);
				}
			}
		}
		LOGGER.debug("Asset Value Map :: {}", assetPropMap);
		return assetPropMap;
	}

	/**
	 * Populate asset metadata properties map.
	 *
	 * @param assetKeyList
	 *            the asset key list
	 * @param vMap
	 *            the v map
	 * @return the map
	 */
	private Map<String, String> populateAssetMetadataPropertiesMap(List<String> assetKeyList, ValueMap vMap) {
		LOGGER.debug("Inside populateAssetMetadataPropertiesMap() method");
		Map<String, String> assetPropMap = new HashMap<>();
		for (String assetKey : assetKeyList) {
			if (vMap.containsKey(assetKey)) {
				Object vMapValue = vMap.get(assetKey);
				if (StringUtils.startsWithIgnoreCase(assetKey, DATE)
						|| StringUtils.endsWithIgnoreCase(assetKey, DATE)
						|| StringUtils.equalsIgnoreCase(assetKey, JCR_CREATED)
						|| StringUtils.equalsIgnoreCase(assetKey, CortevaConstant.CQ_LAST_MODIFIED)) {
					assetPropMap.put(assetKey, CommonUtils.formatDate(CommonUtils.getDateFromValueMap(vMapValue),
							CortevaConstant.MM_DD_YY));
				} else {
					assetPropMap.put(assetKey, (String) vMapValue);
				}
			}
		}
		return assetPropMap;
	}
}