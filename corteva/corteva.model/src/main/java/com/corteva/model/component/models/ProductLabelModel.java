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
import java.util.List;

import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;

/**
 * The is the sling model for the Product Label - US and Non US.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductLabelModel extends ProductSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLabelModel.class);

	/** The product document config. */
	@ChildResource
	private Resource productDocumentConfig;

	/** The safety document config. */
	@ChildResource
	private Resource safetyDocumentConfig;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The product display date. */
	@Inject
	private String productDisplayDate;

	/** The safety display date. */
	@Inject
	private String safetyDisplayDate;

	/** The request. */
	@Inject
	private SlingHttpServletRequest request;
	
	/** The hide Label Finder url. */
	@Inject
	@Via("resource")
	private boolean hideLabelFinderUrl;

	/**
	 * Gets the product doc item list.
	 *
	 * @return the productSpecsItemList
	 */
	public List<ProductDocumentModel> getProductDocItemList() {
		List<ProductDocumentModel> productDocList = new ArrayList<>();
		if (null != productDocumentConfig) {
			productDocList = AEMUtils.getMultifieldItemList(productDocumentConfig, ProductDocumentModel.class);
			Collections.sort(productDocList);
		}
		return productDocList;
	}

	/**
	 * Gets the safety doc item list.
	 *
	 * @return the safety doc item list
	 */
	public List<SafetyDocumentModel> getSafetyDocItemList() {
		List<SafetyDocumentModel> safetyDocList = new ArrayList<>();
		if (null != safetyDocumentConfig) {
			safetyDocList = AEMUtils.getMultifieldItemList(safetyDocumentConfig, SafetyDocumentModel.class);
			Collections.sort(safetyDocList);
		}
		return safetyDocList;
	}

	/**
	 * Gets the product document base path.
	 *
	 * @return the product document base path
	 */
	public String getProductDocumentBasePath() {
		LOGGER.debug("Inside getProductDocumentBasePath() method");
		return getBaseConfigurationService().getPropConfigValue(request, CortevaConstant.PDF_DOCUMENT_ROOT_PATH,
				CortevaConstant.PRODUCT_CONFIG_NAME);
	}

	/**
	 * Gets the label finder path.
	 *
	 * @return the label finder path
	 */
	public String getLabelFinderPath() {
		return getLabelFinderPath(request, currentResource);
	}

	/**
	 * Gets the pdp source.
	 *
	 * @return the pdp source
	 */
	public boolean getPdpSource() {
		boolean pdpFlag = true;
		String pdpSource = getPdpSource(currentResource);
		if (StringUtils.equalsIgnoreCase(CortevaConstant.AGRIAN, pdpSource)) {
			pdpFlag = true;
		} else {
			pdpFlag = false;
		}
		return pdpFlag;
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
		return getLocaleForInternationalization(request);
	}

	/**
	 * Gets the product display date.
	 *
	 * @return the product display date
	 */
	public String getProductDisplayDate() {
		return productDisplayDate;
	}

	/**
	 * Gets the safety display date.
	 *
	 * @return the safety display date
	 */
	public String getSafetyDisplayDate() {
		return safetyDisplayDate;
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