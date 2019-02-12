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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ProductRegistrationJsonBean;
import com.corteva.model.component.bean.ProductRegistrationJsonProp;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * The is the sling model for getting the states list for different country.
 * author.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ProductRegistrationModel extends AbstractSlingModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRegistrationModel.class);

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;
	
	/** The registration country. */
	@Inject
	private String registrationCountry;
	
	/** The current resource. */
	@SlingObject
	private Resource currentResource;
	
	/** The registration states */
	private String[] stateRegistrationList;
	
	/** The init method. */
	@PostConstruct
	public void init() {
		LOGGER.debug("Inside init method of productRegistrationModel");
		Page currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
		ValueMap valueMap = currentPage.getContentResource().getValueMap();
		String[] tags = valueMap.get(CortevaConstant.CQ_TAGS, String[].class);
		if (null != tags) {
			stateRegistrationList = filterStateTags(tags);
		}
		
	}
	
	/**
	 * @param allTags
	 * 			the array of cq:tags
	 * @return the state tag list
	 */
	private String[] filterStateTags(String[] allTags) {
		List<String> tagList = new ArrayList<>(Arrays.asList(allTags));
		tagList.removeIf(tag -> !StringUtils.startsWithIgnoreCase(tag, CortevaConstant.STATE_TAG_ROOT));
		return tagList.toArray(new String[tagList.size()]);
	}
	
	/**
	 * Gets the Product Registered States.
	 *
	 * 
	 * @return the states json
	 */
	public String getRegisteredStates() {
		if (null != stateRegistrationList) {
			return process(stateRegistrationList);
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Gets the Product Registered States for MAP View.
	 *
	 * 
	 * @return the states map json
	 */
	public String getRegisteredStatesMap() {
		if (null != stateRegistrationList) {
			return processMap(stateRegistrationList, registrationCountry);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Process.
	 *
	 * @param stateList
	 *            the state list
	 * @return the string
	 */
	public String process(String[] stateList) {
		String statesJson;
		final Map<String, String> states = getStatesFromList(stateList);
		Gson gsonObj = new Gson();
		statesJson = gsonObj.toJson(states);
		return statesJson;
	}

	/**
	 * Gets the Map of the licensed states.
	 *
	 * @param stateList
	 *            the state list
	 * @return the Map.
	 */

	public Map<String, String> getStatesFromList(String[] stateList) {
		final Map<String, String> prodStates = new HashMap<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (null != tagManager) {
			for (String state : stateList) {
				Tag tag = tagManager.resolve(state);
				prodStates.put(tag.getName(), CommonUtils.getTagLocalizedTitle(slingRequest, tag));
			}
		}
		return prodStates;
	}

	/**
	 * Gets the Map of the licensed states without localized title.
	 *
	 * @param stateList
	 *            the state list
	 * @return the Map.
	 */

	public Map<String, String> getStatesFromListWithTitle(String[] stateList) {
		final Map<String, String> prodStates = new HashMap<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (null != tagManager) {
			for (String state : stateList) {
				Tag tag = tagManager.resolve(state);
				prodStates.put(tag.getName(), tag.getTitle());
			}
		}
		return prodStates;
	}
	
	/**
	 * Gets the JSON with updated presence value for D3 maps
	 * 
	 * @param country
	 *            the country
	 * @param stateList
	 *            the state list
	 * @return the string
	 */

	private String processMap(String[] stateList, String country) {
		String output;
		Map<String, String> stateListInMap = getStatesFromListWithTitle(stateList);
		output = processLicensedStatesMap(stateListInMap, country, resourceResolver);
		return output;

	}

	/**
	 * Process the states in then JSON for D3 map view
	 * 
	 * @param country
	 *            the country
	 * @param stateListMap
	 *            the map of the state list
	 * @param resourceResolver
	 *            the resource
	 * @return the string
	 */

	public String processLicensedStatesMap(Map<String, String> stateListMap, String country,
			ResourceResolver resourceResolver) {
		String finalOutput = StringUtils.EMPTY;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		String jsonData = getJsonData(country, resourceResolver);
		ProductRegistrationJsonBean beanClass;
		try {
			ProductRegistrationJsonProp properties;
			beanClass = mapper.readValue(jsonData, ProductRegistrationJsonBean.class);
			if (null != beanClass && null != beanClass.getFeatures()) {
				for (int i = 0; i < beanClass.getFeatures().size(); i++) {
					properties = beanClass.getFeatures().get(i).getProperties();
					if (null != properties) {
						if (stateListMap.containsValue(properties.getName())) {
							properties.setPresence(CortevaConstant.ONE);
						} else {
							properties.setPresence(0);
						}
					}
				}
			}
			finalOutput = mapper.writeValueAsString(beanClass);
		} catch (JsonParseException e) {
			LOGGER.error("JsonParseException occured during JSON parsing", e);
		} catch (IOException e) {
			LOGGER.error("IOException occurred during i/o of file", e);
		}
		return finalOutput;
	}

	/**
	 * Gets the Json data from the etc hierarchy in AEM
	 *
	 * @param country
	 *            the states of which are to be retrieved.
	 * @param resourceResolver
	 *            the resource
	 * @return the string
	 */
	public String getJsonData(String country, ResourceResolver resourceResolver) {
		String jsonData = StringUtils.EMPTY;
		Resource resource = resourceResolver.getResource(
				CortevaConstant.PATH_TO_JSON_FILE + CortevaConstant.FORWARD_SLASH + country + CortevaConstant.DOT
						+ CortevaConstant.JSON_LOWERCASE + CortevaConstant.FORWARD_SLASH + CortevaConstant.JCR_CONTENT);
		if (null != resource) {
			Node jsonFileNode = resource.adaptTo(Node.class);
			if (null != jsonFileNode) {
				try (InputStream is = jsonFileNode.getProperty("jcr:data").getBinary().getStream();
						BufferedInputStream bis = new BufferedInputStream(is)) {
					jsonData = getEncodedData(bis);
				} catch (IOException e) {
					LOGGER.error("IOException occured during fetching property", e);
				} catch (RepositoryException e) {
					LOGGER.error("RepositoryException occured during resource resolution", e);
				}
			}
		}
		return jsonData;
	}

	/**
	 * Encodes the read data JSON data in UTF-8
	 *
	 * @param bis
	 *            input streams.
	 * @return the string
	 */

	private String getEncodedData(BufferedInputStream bis) {
		String encodedData = StringUtils.EMPTY;
		int result;
		try (ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
			result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			encodedData = buf.toString(CortevaConstant.CHARACTER_ENCODING_UTF_8);
		} catch (IOException e) {
			LOGGER.error("IOException occured during reading/writing of json", e);
		}
		return encodedData;
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