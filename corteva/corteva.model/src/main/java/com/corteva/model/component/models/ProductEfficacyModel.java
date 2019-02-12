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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.ProductBean;
import com.day.cq.tagging.TagManager;

/**
 * This sling model will be used by the components to display product efficacy
 * list
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductEfficacyModel extends ProductSlingModel {

	/** The Constant to hold the tag root for product efficacy tags */
	private static final String EFFICACY_TAG_ROOT = "corteva:efficacy";

	/** The Component title. */
	@Inject
	@Via("resource")
	private String controlTitle;

	/** The Component intro text. */
	@Inject
	@Via("resource")
	@Named("bodyText")
	private String introText;

	/** The Use. */
	@Inject
	@Via("resource")
	private String controlUse;

	/** The Controlled Tag List. */
	@Inject
	@Via("resource")
	@Named("cq:controltag")
	private String[] controlTag;

	/** The Suppressed Tag List. */
	@Inject
	@Via("resource")
	@Named("cq:suppresstag")
	private String[] suppressTag;

	/** The component resource type. */
	@Inject
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The Resource Resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The Tag Manager */
	private TagManager tagManager;

	/** The list of product beans. */
	private List<ProductBean> productList;

	/** The flag to show suppressed list item description. */
	private boolean showSuppressItemDesc = false;

	/** The hide Label Finder url. */
	@Inject
	@Via("resource")
	private boolean hideLabelFinderUrl;
	/**
	 * method to populate list of product beans for controlled and suppressed tags
	 */
	private void populateProductBeanList() {

		tagManager = resourceResolver.adaptTo(TagManager.class);
		productList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(EFFICACY_TAG_ROOT);
		final String efficacyControlUseRoot = sb.append(CortevaConstant.FORWARD_SLASH).append(controlUse).toString();

		if (null != controlTag && controlTag.length > 0) {
			List<String> controlTagList = new ArrayList<>(Arrays.asList(controlTag));
			populateList(controlTagList, efficacyControlUseRoot, false);

			if (null != suppressTag && suppressTag.length > 0) {
				List<String> suppressTagList = new ArrayList<>(Arrays.asList(suppressTag));
				suppressTagList.removeAll(controlTagList);
				if (!suppressTagList.isEmpty()) {
					showSuppressItemDesc = true;
					populateList(suppressTagList, efficacyControlUseRoot, true);
				}
			}
		}

		if (!productList.isEmpty()) {
			populateTaggedAssets(productList, resourceType, tagManager, slingRequest, true);
			Collections.sort(productList);
		}
	}

	/**
	 * helper method to populate list of product beans
	 * 
	 * @param tagList
	 *            the list of control/suppress tags
	 * 
	 * @param efficacyControlUseRoot
	 *            the tag root path
	 * 
	 * @param isSuppressedFlag
	 *            whether it is a suppressed tag
	 */
	private void populateList(List<String> tagList, String efficacyControlUseRoot, boolean isSuppressedFlag) {
		for (String tagId : tagList) {
			if (tagId.equals(efficacyControlUseRoot) || !tagId.startsWith(efficacyControlUseRoot)) {
				continue;
			}
			ProductBean product = new ProductBean();
			product.setProductTag(TagUtil.getTag(tagManager, tagId));
			product.setProductTagTitle(CommonUtils.getTagLocalizedTitle(slingRequest, TagUtil.getTag(tagManager, tagId)));
			product.setSuppressed(isSuppressedFlag);
			product.setSubProductTagTitleList(null);
			productList.add(product);
		}
	}

	/**
	 * @return the controlTitle
	 */
	public String getControlTitle() {
		return controlTitle;
	}

	/**
	 * @return the introText
	 */
	public String getIntroText() {
		return introText;
	}

	/**
	 * @return the controlUse
	 */
	public String getControlUse() {
		return controlUse;
	}

	/**
	 * @return the productList the list of product beans
	 */
	public List<ProductBean> getProductList() {
		populateProductBeanList();
		return productList;
	}

	/**
	 * @return the labelFinderLink the link for label finder
	 */
	public String getLabelFinderLink() {
		return getLabelFinderPath(slingRequest, currentResource);
	}

	/**
	 * @return flag to show suppressed list item description
	 */
	public boolean isSuppressTag() {
		return showSuppressItemDesc;
	}

	/**
	 * Gets the product title.
	 *
	 * @return the product title
	 */
	public String getProductTitle() {
		return getProductName(currentResource);
	}

	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return getLocaleForInternationalization(slingRequest);
	}

	/**
	 * @return flag to hide label finder url
	 */
	public boolean isHideLabelFinderUrl() {
		return hideLabelFinderUrl;
	}
	
	/**
	 * @param hideLabelFinderUrl the hideLabelFinderUrl to set
	 */
	public void setHideLabelFinderUrl(boolean hideLabelFinderUrl) {
		this.hideLabelFinderUrl = hideLabelFinderUrl;
	}
}