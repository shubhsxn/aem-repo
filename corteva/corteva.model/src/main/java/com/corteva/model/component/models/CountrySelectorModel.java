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
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.CountryBean;
import com.corteva.model.component.bean.CountrySelectorBean;
import com.corteva.model.component.bean.LanguageBean;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


/**
 * The Class CountrySelector.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class CountrySelectorModel extends AbstractSlingModel {

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The base config. */
	@Inject
	private BaseConfigurationService baseConfig;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The page path. */
	@Inject
	private String pagePath;

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/**
	 * Find region related data.
	 *
	 * @return the iterator
	 */
	public List<CountrySelectorBean> getRegionRelatedData() {
		Iterator<Page> pageIterator = null;
		String specificTag;
		String[] pageTag;
		CountrySelectorBean bean;
		Page page;
		List<CountrySelectorBean> countrySelectorList = new ArrayList<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

		String rootPagePath = getRootPath();
		if (null != pageManager && StringUtils.isNotBlank(rootPagePath)) {
			Page rootPage = pageManager.getPage(rootPagePath);
			if (null != rootPage) {
				pageIterator = rootPage.listChildren();
			}
		}

		while (null != pageIterator && pageIterator.hasNext()) {
			page = pageIterator.next();
			if (page != null && hasCQTagsProperty(page)) {
				bean = new CountrySelectorBean();
				bean.setRegionLink(LinkUtil.getHref(page.getPath()));
				pageTag = getCQTagsProperty(page);
				specificTag = getSpecificTag(pageTag);
				// get region details from tag and set it in a bean
				if (StringUtils.isNotBlank(specificTag)) {
					getRegionSpecifcDetails(specificTag, tagManager, bean);
					// going to get countries of a region and set it in bean
					getCountrySpecificDetails(page, tagManager, bean);
					countrySelectorList.add(bean);
				}
			}
		}
		return countrySelectorList;
	}

	/**
	 * Gets the specific tag.
	 *
	 * @param specificTagArray
	 *            the specific tag array
	 * @return the specific tag
	 */
	private String getSpecificTag(String[] specificTagArray) {
		List<String> specificTagList = Arrays.asList(specificTagArray);
		Iterator<String> iterator = specificTagList.listIterator();
		while (iterator.hasNext()) {
			String specificTag = iterator.next();
			if (specificTag.contains(CortevaConstant.REGION)) {
				return specificTag;
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Gets the region specifc details.
	 *
	 * @param regionTag            the region tag
	 * @param tagManager            the tag manager
	 * @param countrySelector            the country selector
	 * 
	 */
	private void getRegionSpecifcDetails(String regionTag, TagManager tagManager, CountrySelectorBean countrySelector) {
		Tag regionSpecificTag = TagUtil.getTag(tagManager, regionTag);
		if (null != regionSpecificTag) {
			regionSpecificTag.getPath();
			countrySelector.setRegionTitle(CommonUtils.getTagLocalizedTitle(slingRequest,regionSpecificTag));
			countrySelector.setRegionId(regionSpecificTag.getDescription());
		}
	}

	/**
	 * Gets the country specific details.
	 *
	 * @param page            the page
	 * @param tagManager            the tag manager
	 * @param countrySelector            the country selector
	 * 
	 */
	private void getCountrySpecificDetails(Page page, TagManager tagManager, CountrySelectorBean countrySelector) {
		CountryBean countryBean;
		// get the country pages for each region
		Iterator<Page> countryPageIterator = page.listChildren();
		List<CountryBean> countryBeanList = new ArrayList<>();
		while (null != countryPageIterator && countryPageIterator.hasNext()) {
			Page countryPage = countryPageIterator.next();
			if (countryPage != null && hasCQTagsProperty(countryPage)) {
				countryBean = new CountryBean();
				countryBean.setCountryLink(LinkUtil.getHref(countryPage.getPath()));
				String[] countryTag = getCQTagsProperty(countryPage);
				String countrySpecificTag = getSpecificTag(countryTag);
				// get country details from tag and set it in a bean
				getCountrySpecifcDetails(countrySpecificTag, tagManager, countryBean);
				// get language pages for each country of each region and set in a bean
				getLanguageSpecificDetails(countryPage, tagManager, countryBean);
				countryBeanList.add(countryBean);
			}
		}
		countrySelector.setCountryBeanList(countryBeanList);
	}

	/**
	 * Gets the country specifc details.
	 *
	 * @param countryTag            the country tag
	 * @param tagManager            the tag manager
	 * @param countryBean            the country bean
	 * 
	 */
	private void getCountrySpecifcDetails(String countryTag, TagManager tagManager, CountryBean countryBean) {
		Tag countrySpecTag = TagUtil.getTag(tagManager, countryTag);
		if (null != countrySpecTag) {
			countryBean.setCounrtyTitle(CommonUtils.getTagLocalizedTitle(slingRequest, countrySpecTag));
			countryBean.setCountryName(countrySpecTag.getName());
			countryBean.setCountryCode(countrySpecTag.getDescription());
		}
	}

	/**
	 * Gets the language specific details.
	 *
	 * @param page            the page
	 * @param tagManager            the tag manager
	 * @param countrybean            the countrybean
	 *
	 */
	private void getLanguageSpecificDetails(Page page, TagManager tagManager, CountryBean countrybean) {
		LanguageBean languageBean;
		// get the language pages for each country of each region
		Iterator<Page> languagePageIterator = page.listChildren();
		List<LanguageBean> languageBeanList = new ArrayList<>();
		while (null != languagePageIterator && languagePageIterator.hasNext()) {
			Page languagePage = languagePageIterator.next();
			if (languagePage != null && hasCQTagsProperty(languagePage)) {
				languageBean = new LanguageBean();
				languageBean.setLanguageLink(LinkUtil.getHref(languagePage.getPath()));
				String[] languageTag = getCQTagsProperty(languagePage);
				String languageSpecificTag = getSpecificTag(languageTag);
				// get language details from tag and set it in a bean
				getLanguageSpecifcDetails(languageSpecificTag, tagManager, languageBean);
				languageBeanList.add(languageBean);
			}
		}
		countrybean.setLanguageCount(languageBeanList.size());
		countrybean.setLanguageBeanList(languageBeanList);
	}

	/**
	 * Gets the language specifc details.
	 *
	 * @param countryTag            the country tag
	 * @param tagManager            the tag manager
	 * @param languageBean            the language bean
	 * 
	 */
	private void getLanguageSpecifcDetails(String countryTag, TagManager tagManager, LanguageBean languageBean) {
		Tag languageSpecTag = TagUtil.getTag(tagManager, countryTag);
		if (null != languageSpecTag) {
			languageBean.setLanguageTitle(CommonUtils.getTagLocalizedTitle(slingRequest, languageSpecTag));
			languageBean.setLanguageName(languageSpecTag.getName());
			languageBean.setLanguageCode(languageSpecTag.getDescription());
		}
	}

	/**
	 * Checks for CQ tags property.
	 *
	 * @param page
	 *            the page
	 * @return true, if successful
	 */
	private boolean hasCQTagsProperty(Page page) {
		boolean hasTagPresent = false;

		if (null != page.getProperties().get(CortevaConstant.CQ_TAGS)) {
			hasTagPresent = true;
		}
		return hasTagPresent;
	}

	/**
	 * Gets the CQ tags property.
	 *
	 * @param page
	 *            the page
	 * @return the CQ tags property
	 */
	private String[] getCQTagsProperty(Page page) {
		return (String[]) page.getProperties().get(CortevaConstant.CQ_TAGS);
	}

	/**
	 * Gets the root path.
	 *
	 * @return the root path
	 */
	private String getRootPath() {
		return baseConfig.getPropConfigValue(slingRequest, CortevaConstant.COUNTRY_SELECTOR_ROOT_PATH,
				CortevaConstant.GLOBAL_CONFIG_NAME);
	}

	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(pagePath, getResourceResolver());
	}

}
