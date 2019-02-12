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
public class SafetyDocumentModel extends ProductSlingModel implements Comparable<SafetyDocumentModel> {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SafetyDocumentModel.class);

	/** The product document label. */
	@Inject
	private String safetyDocumentLabel;

	/** The product document link. */
	@Inject
	private String safetyDocumentLink;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/**
	 * Gets the safety document label.
	 *
	 * @return the safety document label
	 */
	public String getSafetyDocumentLabel() {
		LOGGER.debug("Inside getSafetyDocumentLabel() method");
		String safeDocLabel;
		StringBuilder sb = new StringBuilder();
		String productName = getProductName(currentResource);
		if (StringUtils.isNotBlank(productName)) {
			safeDocLabel = sb.append(StringUtils.trim(productName)).append(CortevaConstant.SPACE)
					.append(StringUtils.trim(safetyDocumentLabel)).toString();
		} else {
			safeDocLabel = safetyDocumentLabel;
		}
		LOGGER.debug("Safety Document Label :: {}", safeDocLabel);
		return safeDocLabel;
	}

	/**
	 * Sets the safety document label.
	 *
	 * @param safetyDocumentLabel
	 *            the new safety document label
	 */
	public void setSafetyDocumentLabel(String safetyDocumentLabel) {
		this.safetyDocumentLabel = safetyDocumentLabel;
	}

	/**
	 * Gets the safety document link.
	 *
	 * @return the safety document link
	 */
	public String getSafetyDocumentLink() {
		return safetyDocumentLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SafetyDocumentModel o) {
		return this.safetyDocumentLabel.compareTo(o.safetyDocumentLabel);
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