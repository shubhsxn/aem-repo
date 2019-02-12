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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.bean.ProductBean;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the Abstract Sling Model for injecting resource resolver, base
 * configuration service etc. This class should be extended by all the Sling
 * models.
 * 
 * @author Sapient
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSlingModel.class);

	/** The Constant VIDEO_CONFIG_NAME */
	private static final String VIDEO_CONFIG_NAME = "com.corteva.core.configurations.VideoConfigurationService";

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The base service. */
	@Inject
	private BaseConfigurationService baseService;

	/** The sling request. */
	private static SlingHttpServletRequest request;

	/** The sling request. */
	private static String componentName;

	/**
	 * @param request
	 *            the request to set
	 */
	public static void setRequest(SlingHttpServletRequest request) {
		AbstractSlingModel.request = request;
	}

	/**
	 * @param componentName
	 *            the componentName to set
	 */
	public static void setComponentName(String componentName) {
		AbstractSlingModel.componentName = componentName;
	}

	/**
	 * This method gets the resource resolver.
	 * 
	 * @return resourceResolver
	 */
	public ResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	/**
	 * This method gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return resourceResolver.adaptTo(Session.class);
	}

	/**
	 * This method gets the base configuration service.
	 *
	 * @return the baseService
	 */
	public BaseConfigurationService getBaseConfigurationService() {
		return baseService;
	}

	/**
	 * This method is used to get the responsive grid node name in sightly.
	 *
	 * @param componentHiddenPath
	 *            the component hidden path
	 * @return the responsive grid node name
	 */
	public String getResponsiveGridNodeName(Property componentHiddenPath) {
		LOGGER.debug("Inside getResponsiveGridNodeName() method");
		String respGridNodeName = StringUtils.EMPTY;
		final Session session = getSession();
		if (null != session) {
			String compHiddenPathStr = AEMUtils.getStringfromProperty(componentHiddenPath);
			if (StringUtils.isNotBlank(compHiddenPathStr)) {
				compHiddenPathStr = StringUtils.replace(compHiddenPathStr, CortevaConstant.JCR_UNDERSCORE_CONTENT,
						CortevaConstant.JCR_CONTENT);
				LOGGER.debug("Component Resource Path :: {}", compHiddenPathStr);
				String[] compResPathSplit = StringUtils.split(compHiddenPathStr, CortevaConstant.FORWARD_SLASH);
				String compnodeName = compResPathSplit[compResPathSplit.length - 1];
				respGridNodeName = prepareRespgridName(session, respGridNodeName, compHiddenPathStr, compnodeName);
			}
		}
		LOGGER.debug("Responsive Grid Node Name :: {}", respGridNodeName);
		return respGridNodeName;
	}

	/**
	 * This method creates the responsive grid name in which the the experience
	 * fragment component nodes will be created.
	 *
	 * @param session
	 *            the session
	 * @param respGridNodeName
	 *            the responsive grid node name
	 * @param compHiddenpath
	 *            the component hidden path
	 * @param compnodeName
	 *            the component node name
	 * @return the responsive grid node name
	 */
	private String prepareRespgridName(final Session session, String respGridNodeName, String compHiddenpath,
			String compnodeName) {
		try {
			if (session.nodeExists(compHiddenpath)) {
				Node expfragNode = session.getNode(compHiddenpath);
				String cmpnodeName = expfragNode.getName();
				LOGGER.debug("Experience Fragment Node Name :: {}", cmpnodeName);
				if (StringUtils.equalsIgnoreCase(compnodeName, cmpnodeName)) {
					StringBuilder sb = new StringBuilder();
					respGridNodeName = sb.append(CortevaConstant.RESPONSIVE_GRID).append(cmpnodeName).toString();
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error("Repository Exception occurred in getResponsiveGridNodeName()", e);
		}
		return respGridNodeName;
	}

	/**
	 * Gets the image rendition json.
	 *
	 * @param imagePath
	 *            the image path
	 * @param componentPath
	 *            the component path
	 * @param request
	 *            the request
	 * @return the image rendition json
	 */
	public String getImageRenditionJson(String imagePath, String componentPath, SlingHttpServletRequest request) {
		LOGGER.debug("Inside getImageRenditionJson() method");
		String imageJson = StringUtils.EMPTY;
		ObjectMapper mapper = new ObjectMapper();
		try {
			imageJson = mapper.writeValueAsString(getImageRenditionList(imagePath, componentPath, request));
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occurred in getHeroImageJson()", e);
		}
		LOGGER.debug("Image Rendition JSON :: {}", imageJson);
		return imageJson;
	}

	/**
	 * This method returns a list of Image rendition bean that contains the desktop,
	 * tablet and mobile image paths.
	 *
	 * @param imagePath
	 *            the image path
	 * @param componentName
	 *            the component name
	 * @param request
	 *            the request
	 * @return the image rendition list
	 */
	public ImageRenditionBean getImageRenditionList(String imagePath, String componentName,
			SlingHttpServletRequest request) {
		Map<String, String> regCountryLangMap = CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePath(request),
				request.getResourceResolver());
		return ImageUtil.getImageRenditionList(imagePath, componentName, request, baseService, regCountryLangMap);
	}

	/**
	 * Overloaded method for
	 * {@link #getImageRenditionList(String, String, SlingHttpServletRequest)}
	 *
	 * @param imagePath
	 *            the image path
	 * 
	 * @return the image rendition list
	 */
	public ImageRenditionBean getImageRenditionList(String imagePath) {
		return getImageRenditionList(imagePath, componentName, request);
	}

	/**
	 * This method gets the region country language map.
	 *
	 * @param request
	 *            the request
	 * @return the region country language map
	 */
	private Map<String, String> getRegionCountryLanguageMap(SlingHttpServletRequest request) {
		return CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePath(request), request.getResourceResolver());
	}

	/**
	 * @param request
	 *            the sling request
	 * @return locale the locale
	 */
	public Locale getLocale(SlingHttpServletRequest request) {
		LOGGER.debug("Inside getLocale() method");
		Map<String, String> countryLangMap = getRegionCountryLanguageMap(request);
		Locale locale = null;
		if (null != countryLangMap && !countryLangMap.isEmpty()) {
			String language = countryLangMap.get(CortevaConstant.LANGUAGE);
			LOGGER.debug("Language from countryLangMap :: {}", language);
			if (StringUtils.isNotBlank(language)) {
				locale = new Locale(language);
			}
		}
		if (null == locale) {
			locale = new Locale(CortevaConstant.EN);
		}
		return locale;
	}

	/**
	 * Gets the scene seven image path.
	 *
	 * @param imagePath
	 *            the image path
	 * @param request
	 *            the request
	 * @return the scene seven image path
	 */
	public String getSceneSevenImagePath(String imagePath, SlingHttpServletRequest request) {
		Map<String, String> regCountryLangMap = CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePath(request),
				request.getResourceResolver());
		return ImageUtil.getSceneSevenImagePath(imagePath, request, baseService, regCountryLangMap);
	}

	/**
	 * Overloaded method for
	 * {@link #getSceneSevenImagePath(String, SlingHttpServletRequest)}
	 *
	 * @param imagePath
	 *            the image path
	 * 
	 * @return the scene seven image path
	 */
	public String getSceneSevenImagePath(String imagePath) {
		return getSceneSevenImagePath(imagePath, request);
	}

	/**
	 * Gets the scene seven video path.
	 *
	 * @param videoPath
	 *            the video path
	 * @param request
	 *            the request
	 * @return the scene seven video path
	 */
	public String getSceneSevenVideoPath(String videoPath, SlingHttpServletRequest request) {
		LOGGER.debug("Inside getSceneSevenVideoPath() method");
		String sceneSevenVideoPath = StringUtils.EMPTY;
		StringBuilder sb = new StringBuilder();
		Map<String, String> regCountryLangMap = getRegionCountryLanguageMap(request);
		boolean isVideoFromS7 = Boolean.parseBoolean(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
				CortevaConstant.IS_ASSET_FROM_S7, CortevaConstant.IMAGE_CONFIG_NAME));
		if (isVideoFromS7) {
			Resource videoResource = resourceResolver.resolve(videoPath);
			try {
				if (!ResourceUtil.isNonExistingResource(videoResource)) {
					Node videoNode = videoResource.adaptTo(Node.class);
					if (null != videoNode && StringUtils.isNotBlank(videoNode.getName())) {
						String sSevenVideoName = CommonUtils.getFileNameWithoutExtn(videoNode.getName());
						String sceneSevenDomain = baseService.getPropertyValueFromConfiguration(regCountryLangMap,
								CortevaConstant.SCENE_7_VIDEO_ROOT_PATH, AbstractSlingModel.VIDEO_CONFIG_NAME);
						sceneSevenVideoPath = sb.append(sceneSevenDomain).append(CortevaConstant.FORWARD_SLASH)
								.append(sSevenVideoName).toString();
					}
				}
			} catch (RepositoryException e) {
				LOGGER.debug("Repository Exception occurred in getSceneSevenVideoPath()", e);
			}
		} else {
			sceneSevenVideoPath = videoPath;
		}
		LOGGER.debug("Scene7 Video Path :: {}", sceneSevenVideoPath);
		return sceneSevenVideoPath;
	}

	/**
	 * Overloaded method for
	 * {@link #getSceneSevenVideoPath(String, SlingHttpServletRequest)}
	 *
	 * @param videoPath
	 *            the video path
	 * @return the scene seven video path
	 */
	public String getSceneSevenVideoPath(String videoPath) {
		return getSceneSevenVideoPath(videoPath, request);
	}

	/**
	 * Gets the feature flag toggle state for the component.
	 *
	 * @param resourceType
	 *            the component resource type
	 * @return the feature flag toggle state as on/off
	 */
	public String getFeatureFlagState(String resourceType) {
		return baseService.getToggleInfo(getComponentName(resourceType), CortevaConstant.FEATURE_FLAG_CONFIG_NAME)
				? CortevaConstant.FLAG_ON
				: CortevaConstant.FLAG_OFF;
	}

	/**
	 * Gets the component name from resource type.
	 *
	 * @param resourceType
	 *            the component resource type
	 * @return the component name
	 */
	public String getComponentName(String resourceType) {
		return CommonUtils.getComponentName(resourceType);
	}

	/**
	 * Sets the base service.
	 *
	 * @param baseService
	 *            the baseService to set
	 */
	public void setBaseService(BaseConfigurationService baseService) {
		this.baseService = baseService;
	}

	/**
	 * Gets the youtube api url.
	 *
	 * @param slingRequest
	 *            the sling request
	 * @return the youtube api
	 */
	public String getYoutubeApi(SlingHttpServletRequest slingRequest) {
		return baseService.getPropConfigValue(slingRequest, "youtubeApi", AbstractSlingModel.VIDEO_CONFIG_NAME);
	}

	/**
	 * Overloaded method for {@link #getYoutubeApi(SlingHttpServletRequest)}
	 * 
	 * @return the youtube api
	 */
	public String getYoutubeApi() {
		return getYoutubeApi(request);
	}

	/**
	 * Gets the youtube api key.
	 *
	 * @param slingRequest
	 *            the sling request
	 * @return the youtube key
	 */
	public String getYoutubeKey(SlingHttpServletRequest slingRequest) {
		return baseService.getPropConfigValue(slingRequest, "youtubeKey", AbstractSlingModel.VIDEO_CONFIG_NAME);
	}

	/**
	 * Overloaded method for {@link #getYoutubeKey(SlingHttpServletRequest)}
	 * 
	 * @return the youtube key
	 */
	public String getYoutubeKey() {
		return getYoutubeKey(request);
	}

	/**
	 * Populates product bean with imageRenditionBean, i.e. bean of tagged asset
	 * 
	 * @param productList
	 *            the list of product bean
	 * @param resourceType
	 *            the resource type of component
	 * @param tagManager
	 *            the tag manager
	 * @param request
	 *            the sling request
	 * @param strictMatch
	 * 			  the flag to whether strictly match asset against tag
	 */
	public void populateTaggedAssets(List<ProductBean> productList, String resourceType, TagManager tagManager,
			SlingHttpServletRequest request, boolean strictMatch) {
		LOGGER.debug("Inside populateTaggedAssets() method");
		String assetBasePathProperty = getComponentName(resourceType) + "TaggedAssetsPath";
		String assetBasePath = getBaseConfigurationService().getPropConfigValue(request, assetBasePathProperty,
				CortevaConstant.PRODUCT_CONFIG_NAME);
		for (ProductBean product : productList) {
			LOGGER.debug("Tag Id :: {}", product.getProductTag().getTagID());
			List<Resource> resList = TagUtil.getResources(tagManager, product.getProductTag(), assetBasePath);
			if (null != resList && !resList.isEmpty()) {
				Asset damAsset = null;
				Resource asset = resList.get(0);
				if (strictMatch) {
					asset = strictMatchTaggedAsset(resList, product.getProductTag().getTagID());
				}
				if (null != asset) {
					String assetAltText = StringUtils.EMPTY;
					assetAltText = DamUtil.getInheritedProperty("imageAltText", asset,
							DamUtil.getInheritedProperty("iconAltText", asset, assetAltText));
					product.setAltText(assetAltText);
					damAsset = DamUtil.resolveToAsset(asset);
				}
				if (null != damAsset) {
					LOGGER.debug("Tagged Dam Asset Path to {} :: {}", product.getProductTag().getTagID(),
							damAsset.getPath());
					ImageRenditionBean imageRenditionBean = getImageRenditionList(damAsset.getPath(),
							CommonUtils.getComponentName(resourceType), request);
					product.setImageRenditionBean(imageRenditionBean);
				}

			} else {
				product.setImageRenditionBean(null);
			}
		}
	}
	
	/**
	 * @param resList
	 * 			the list of assets
	 * @param tagID
	 * 			the tagID to match
	 * @return res
	 * 			the strictly matched asset
	 */
	private Resource strictMatchTaggedAsset(List<Resource> resList, String tagID) {
		for (Resource res : resList) {
			String[] cqTags = DamUtil.getInheritedProperty(CortevaConstant.CQ_TAGS, res, new String[]{});
			if (null != cqTags && cqTags.length > 0) {
				List<String> tagList = Arrays.asList(cqTags);
				if (tagList.contains(tagID)) {
					return res;
				}
			}
		}
		return null;
	}
	
	
	
}