/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.bean;

import java.util.List;

import com.day.cq.tagging.Tag;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class ProductBean.
 * @author Sapient
 */
public class ProductBean implements Comparable<ProductBean> {
	
	/** The product tag. */
	private Tag productTag;
	
	/** The product tag localized title. */
	private String productTagTitle;
	
	/** The list of sub-product tags localized titles. */
	private List<String> subProductTagTitleList;
	
	/** The imageRenditionBean. */
	private ImageRenditionBean imageRenditionBean;
	
	/** The alt text. */
	private String altText;
	
	/** The isSuppressedTag flag. */
	private boolean suppressed;
	
	/**
	 * @return the productTag
	 */
	public Tag getProductTag() {
		return productTag;
	}

	/**
	 * @param productTag the productTag to set
	 */
	public void setProductTag(Tag productTag) {
		this.productTag = productTag;
	}

	/**
	 * @return the productTagTitle
	 */
	public String getProductTagTitle() {
		return productTagTitle;
	}

	/**
	 * @param productTagTitle the productTagTitle to set
	 */
	public void setProductTagTitle(String productTagTitle) {
		this.productTagTitle = productTagTitle;
	}

	/**
	 * @return the subProductTagTitleList
	 */
	public List<String> getSubProductTagTitleList() {
		return subProductTagTitleList;
	}

	/**
	 * @param subProductTagTitleList the subProductTagTitleList to set
	 */
	public void setSubProductTagTitleList(List<String> subProductTagTitleList) {
		this.subProductTagTitleList = subProductTagTitleList;
	}

	/**
	 * @return the imageRenditionBean
	 */
	public ImageRenditionBean getImageRenditionBean() {
		return imageRenditionBean;
	}

	/**
	 * @param imageRenditionBean the imageRenditionBean to set
	 */
	public void setImageRenditionBean(ImageRenditionBean imageRenditionBean) {
		this.imageRenditionBean = imageRenditionBean;
	}
	
	/**
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * @param altText the altText to set
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}

	/**
	 * @return the suppressed
	 */
	public boolean isSuppressed() {
		return suppressed;
	}

	/**
	 * @param suppressed the suppressed to set
	 */
	public void setSuppressed(boolean suppressed) {
		this.suppressed = suppressed;
	}

	/* 
	 * sorts Products by product tag localized title
	 */
	@Override
	public int compareTo(ProductBean anotherProduct) {
		if (StringUtils.isNotBlank(this.getProductTagTitle()) && StringUtils.isNotBlank(anotherProduct.getProductTagTitle())) {
			return this.getProductTagTitle().compareTo(anotherProduct.getProductTagTitle());
		}
		return 0;
	}

	/** 
	 * override java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	/**
	 * override java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
}
