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
 * The Class CountryBean.
 */
public class CountryBean {
	
	/** The counrty title. */
	private String counrtyTitle;
	
	/** The countrty code. */
	private String countryCode;
	
	/** The country name. */
	private String countryName;
	
	/** The language bean list. */
	private List<LanguageBean> languageBeanList;
	
	/** The country link. */
	private String countryLink;
	
	/** The language count. */
	private int languageCount;
	/**
	 * Gets the counrty title.
	 *
	 * @return the counrty title
	 */
	public String getCounrtyTitle() {
		return counrtyTitle;
	}

	/**
	 * Sets the counrty title.
	 *
	 * @param counrtyTitle the new counrty title
	 */
	public void setCounrtyTitle(String counrtyTitle) {
		this.counrtyTitle = counrtyTitle;
	}



	/**
	 * Gets the country code.
	 *
	 * @return the country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Sets the country code.
	 *
	 * @param countryCode the new country code
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Gets the country name.
	 *
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Sets the country name.
	 *
	 * @param countryName the new country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Gets the language bean list.
	 *
	 * @return the language bean list
	 */
	public List<LanguageBean> getLanguageBeanList() {
		return languageBeanList;
	}

	/**
	 * Sets the language bean list.
	 *
	 * @param languageBeanList the new language bean list
	 */
	public void setLanguageBeanList(List<LanguageBean> languageBeanList) {
		this.languageBeanList = languageBeanList;
	}

	/**
	 * Gets the country link.
	 *
	 * @return the country link
	 */
	public String getCountryLink() {
		return countryLink;
	}

	/**
	 * Sets the country link.
	 *
	 * @param countryLink the new country link
	 */
	public void setCountryLink(String countryLink) {
		this.countryLink = countryLink;
	}

	/**
	 * Gets the language count.
	 *
	 * @return the language count
	 */
	public int getLanguageCount() {
		return languageCount;
	}

	/**
	 * Sets the language count.
	 *
	 * @param languageCount the new language count
	 */
	public void setLanguageCount(int languageCount) {
		this.languageCount = languageCount;
	}
	
	 

}
