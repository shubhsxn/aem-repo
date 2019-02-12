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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ProductBean;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * The is the sling model for the TextOnImage Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class CropListModel extends AbstractSlingModel {

	/**
	 * The Constant Crop Types.
	 */
	private static final String CROP_TYPES = "cropTypes";

	/** The crop type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("cq:cropType")
	private String[] cropType;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/**
	 * Instantiates a new crop list model.
	 *
	 * @param request
	 *            the request
	 */
	public CropListModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the crop type map.
	 *
	 * @param tagManager
	 *            the tagManager
	 * @return the crop type map
	 */
	private Map<Tag, List<String>> getCropTypeMap(TagManager tagManager) {
		Tag parentTag = null;
		Map<Tag, List<String>> cropTypeMap = new HashMap<>();
		if (null != tagManager) {
			for (String cropTypeId : cropType) {
				Tag childTag = tagManager.resolve(cropTypeId);
				String localizedTitle = CommonUtils.getTagLocalizedTitle(slingRequest, childTag);
				if (StringUtils.isNotBlank(localizedTitle)) {
					parentTag = childTag.getParent();
					createCropTypeMap(parentTag, cropTypeMap, childTag, localizedTitle);
				}
			}
		}
		return cropTypeMap;
	}

	/**
	 * Creates the crop type map.
	 *
	 * @param parentTag
	 *            the parent tag
	 * @param cropTypeMap
	 *            the crop type map
	 * @param childTag
	 *            the child tag
	 * @param localizedTitle
	 *            the localized title
	 */
	private void createCropTypeMap(Tag parentTag, Map<Tag, List<String>> cropTypeMap, Tag childTag,
			String localizedTitle) {
		if (!cropTypeMap.containsKey(childTag)) {
			if (cropTypeMap.containsKey(parentTag)) {
				cropTypeMap.get(parentTag).add(localizedTitle);
			} else {
				List<String> cropTypeLevelTwo = new ArrayList<>();
				parentTag = createLevelTwoTag(parentTag, childTag, localizedTitle, cropTypeLevelTwo);
				cropTypeMap.put(parentTag, cropTypeLevelTwo);
			}
		}
	}

	/**
	 * Creates the level two tag.
	 *
	 * @param parentTag
	 *            the parent tag
	 * @param childTag
	 *            the child tag
	 * @param localizedTitle
	 *            the localized title
	 * @param cropTypeLevelTwo
	 *            the crop type level two
	 * @return the tag
	 */
	private Tag createLevelTwoTag(Tag parentTag, Tag childTag, String localizedTitle, List<String> cropTypeLevelTwo) {
		if (parentTag.getName().equalsIgnoreCase(CROP_TYPES)) {
			parentTag = childTag;
		} else {
			cropTypeLevelTwo.add(localizedTitle);
		}
		return parentTag;
	}

	/**
	 * sets the product List items.
	 *
	 * @return the product List items
	 */
	public List<ProductBean> getProductList() {
		List<ProductBean> productList = new ArrayList<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		for (Map.Entry<Tag, List<String>> entry : getCropTypeMap(tagManager).entrySet()) {
			ProductBean product = new ProductBean();
			product.setProductTag(entry.getKey());
			String parentTagLocalizedTitle = (null != entry.getKey().getLocalizedTitle(CommonUtils.getLocale(slingRequest)))
					? entry.getKey().getLocalizedTitle(CommonUtils.getLocale(slingRequest))
					: CommonUtils.getTagTitle(slingRequest, entry.getKey());
			product.setProductTagTitle(parentTagLocalizedTitle);
			Collections.sort(entry.getValue());
			product.setSubProductTagTitleList(entry.getValue());
			product.setImageRenditionBean(null);
			product.setSuppressed(false);
			productList.add(product);
		}
		populateTaggedAssets(productList, resourceType, tagManager, slingRequest, false);
		Collections.sort(productList);
		return productList;
	}

}