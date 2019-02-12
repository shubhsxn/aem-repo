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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * The is the sling model for the Product Header Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ProductHeaderModel extends ProductSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductHeaderModel.class);

	/** Constant to hold value of product type of Product Page. */
	private static final String PAGE_PROPERTY_PRODUCT_TYPE = "cq:productType";

	/** The mobile image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("mobfileReference")
	private String mobileImagePath;

	/** The mobile alt text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("mobAltText")
	private String mobileAltText;

	/** The logo image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("logofileReference")
	private String logoImagePath;

	/** The logo alt text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("altTextLogo")
	private String logoAltText;

	/** The display product type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("displayProductType")
	private String displayProductType;

	/** The current page. */
	private Page currentPage;

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The init method. */
	@PostConstruct
	public void init() {
		LOGGER.debug("Inside init method of articleHeaderModel");
		currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
	}

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public ProductHeaderModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the logo image path.
	 *
	 * @return the logo image path
	 */
	public String getLogoImagePath() {
		return getSceneSevenImagePath(logoImagePath, slingRequest);
	}

	/**
	 * Gets the logo alt text.
	 *
	 * @return the logo alt text
	 */
	public String getLogoAltText() {
		return logoAltText;
	}

	/**
	 * Gets the mobile image path.
	 *
	 * @return the mobile image path
	 */
	public String getMobileImagePath() {
		ImageRenditionBean imgRenBean = getImageRenditionList(mobileImagePath,
				CommonUtils.getComponentName(resourceType), slingRequest);
		return imgRenBean.getMobileImagePath();
	}

	/**
	 * Gets the mobile alt text.
	 *
	 * @return the mobile alt text
	 */
	public String getMobileAltText() {
		return mobileAltText;
	}

	/**
	 * Gets the display product type.
	 *
	 * @return the display product type
	 */
	public String getDisplayProductType() {
		return displayProductType;
	}

	/**
	 * Gets the product type.
	 *
	 * @return the product type
	 */
	public List<String> getProductType() {
		ValueMap valueMap = currentPage.getContentResource().getValueMap();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		List<String> productTypeTagList = new ArrayList<>();
		Property property = valueMap.get(PAGE_PROPERTY_PRODUCT_TYPE, Property.class);
		if (null != property) {
			String resolvedTag = "";
			try {
				if (property.isMultiple()) {
					for (Value value : property.getValues()) {
						resolvedTag = CommonUtils.getTagLocalizedTitle(slingRequest, TagUtil.getTag(tagManager, value.toString()));
						productTypeTagList.add(resolvedTag);
						}
				} else {
					resolvedTag = CommonUtils.getTagLocalizedTitle(slingRequest, TagUtil.getTag(tagManager, property.toString()));
					productTypeTagList.add(resolvedTag);
					}
			} catch (IllegalStateException | RepositoryException e) {
				LOGGER.error("Exception occurred in getProductType()", e);
			} 
		}
		return productTypeTagList;
	}

	/**
	 * Gets the product name.
	 *
	 * @return the product name
	 */
	public String getProductName() {
		return getProductName(currentResource);
	}
}