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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;

/**
 * This the sling Model for the Product Technical Specs Component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductTechnicalSpecsModel extends ProductSlingModel {

	/** The component resource type. */
	@Inject
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** Inject the productSpecs node under the current node */
	@Inject
	@Via("resource")
	private Resource productSpecs;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/**
	 * @return the productSpecs resource
	 */
	public Resource getProductSpecs() {
		return productSpecs;
	}

	/**
	 * @return the productSpecsItemList
	 */
	public List<ProductTechnicalSpecsItemModel> getProductSpecsItemList() {
		setRequest(slingRequest);
		setComponentName(CommonUtils.getComponentName(resourceType));
		List<ProductTechnicalSpecsItemModel> productSpecsItemList = AEMUtils.getMultifieldItemList(productSpecs,
				ProductTechnicalSpecsItemModel.class);
		return productSpecsItemList;
	}

	/**
	 * Gets the youtube api url.
	 *
	 * @return the youtube api
	 */
	@Override
	public String getYoutubeApi() {
		return getYoutubeApi(slingRequest);
	}

	/**
	 * Gets the youtube api key.
	 *
	 * @return the youtube key
	 */
	@Override
	public String getYoutubeKey() {
		return getYoutubeKey(slingRequest);
	}
	
	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return getLocaleForInternationalization(slingRequest);
	}
}