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

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;

/**
 * The is the sling model for the Product Label component multifield.
 * 
 * @author Sapient
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDocumentModel extends ProductSlingModel implements Comparable<ProductDocumentModel> {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDocumentModel.class);

	/** The product document label. */
	@Inject
	private String productDocumentLabel;

	/** The product document link. */
	@Inject
	private String productDocumentLink;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/**
	 * Gets the product document label.
	 *
	 * @return the product document label
	 */
	public String getProductDocumentLabel() {
		LOGGER.debug("Inside getProductDocumentLabel() method");
		String prdDocLabel;
		StringBuilder sb = new StringBuilder();
		String productName = getProductName(currentResource);
		if (StringUtils.isNotBlank(productName)) {
			prdDocLabel = sb.append(StringUtils.trim(productName)).append(CortevaConstant.SPACE)
					.append(StringUtils.trim(productDocumentLabel)).toString();
		} else {
			prdDocLabel = productDocumentLabel;
		}
		LOGGER.debug("Product Document Label :: {}", prdDocLabel);
		return prdDocLabel;
	}

	/**
	 * Gets the product document link.
	 *
	 * @return the product document link
	 */
	public String getProductDocumentLink() {
		return productDocumentLink;
	}

	/**
	 * Sets the product document label.
	 *
	 * @param productDocumentLabel
	 *            the new product document label
	 */
	public void setProductDocumentLabel(String productDocumentLabel) {
		this.productDocumentLabel = productDocumentLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProductDocumentModel o) {
		return this.productDocumentLabel.compareTo(o.productDocumentLabel);
	}

	/**
	 * override java.lang.Object#equals(java.lang.Object)
	 *
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	/**
	 * override java.lang.Object#hashCode()
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}