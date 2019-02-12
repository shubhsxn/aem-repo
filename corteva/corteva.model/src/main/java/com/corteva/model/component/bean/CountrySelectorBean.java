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



/**
 * The Class CountrySelectorBean.
 */
public class CountrySelectorBean {
	
	/** The region id. */
	private String regionId ;
	
	/** The region title. */
	private String regionTitle ;
	
	/** The region link. */
	private String regionLink;
	
	/** The country bean list. */
	private List<CountryBean> countryBeanList;
		
	/**
	 * Gets the region id.
	 *
	 * @return the region id
	 */
	public String getRegionId() {
		return regionId;
	}
	
	/**
	 * Sets the region id.
	 *
	 * @param regionId the new region id
	 */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	/**
	 * Gets the region title.
	 *
	 * @return the region title
	 */
	public String getRegionTitle() {
		return regionTitle;
	}
	
	/**
	 * Sets the region title.
	 *
	 * @param regionTitle the new region title
	 */
	public void setRegionTitle(String regionTitle) {
		this.regionTitle = regionTitle;
	}

	/**
	 * Gets the country bean list.
	 *
	 * @return the country bean list
	 */
	public List<CountryBean> getCountryBeanList() {
		return countryBeanList;
	}

	/**
	 * Sets the country bean list.
	 *
	 * @param countryBeanList the new country bean list
	 */
	public void setCountryBeanList(List<CountryBean> countryBeanList) {
		this.countryBeanList = countryBeanList;
	}

	/**
	 * Gets the region link.
	 *
	 * @return the region link
	 */
	public String getRegionLink() {
		return regionLink;
	}

	/**
	 * Sets the region link.
	 *
	 * @param regionLink the new region link
	 */
	public void setRegionLink(String regionLink) {
		this.regionLink = regionLink;
	}
	
}
