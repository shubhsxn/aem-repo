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
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Property;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.LinkListBean;
import com.google.gson.JsonObject;

/**
 * The is the sling model for Link List component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class LinkListModel extends AbstractSlingModel {

    /** Logger Instantiation. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkListModel.class);

    /** The navigation links. */
    @Inject
    @Optional
    @Via("resource")
    private Property navLinks;
    
    /** The request. */
	@Inject
	private SlingHttpServletRequest request;
	
	/** The Constant NAVIGATION_PROPERTY_LINK_LABEL. */
	private static final String NAVIGATION_PROPERTY_LINK_LABEL = "linkLabel";
	
	/** The Constant NAVIGATION_PROPERTY_LINK_URL. */
	private static final String NAVIGATION_PROPERTY_LINK_URL = "linkUrl";
	
	/** The Constant NAVIGATION_PROPERTY_LINK_ACTION. */
	private static final String NAVIGATION_PROPERTY_LINK_ACTION = "linkAction";
	
	/** The Constant NAVIGATION_PROPERTY_COUNTRY_SELECTION. */
	private static final String NAVIGATION_PROPERTY_COUNTRY_SELECTION = "countrySel";
	
	/** The Constant NAVIGATION_PROPERTY_HAS_DROPDOWN. */
	private static final String NAVIGATION_PROPERTY_HAS_DROPDOWN = "hasDropdown";
	
	/** The Constant NAVIGATION_PROPERTY_LINK_STYLE. */
	private static final String NAVIGATION_PROPERTY_LINK_STYLE = "linkStyle";
	
	/** The Constant NAVIGATION_PROPERTY_LINK_COLOR. */
	private static final String NAVIGATION_PROPERTY_LINK_COLOR = "linkColor";
	
	/**
     * Gets the navigation links.
     *
     * @return the navigation links
     */
    public List<LinkListBean> getNavigationLinks() {
    	return getNavigationLinks(navLinks);	
    	}
    /**
     * Gets the navigation links.
     * @param links the links
     * @return the navigation links
     */
    public List<LinkListBean> getNavigationLinks(Property links) {
        LOGGER.info("Inside getNavigationLinks() method of LinkListModel");
        List<LinkListBean> linksList = new ArrayList<>();
        try {
            List<JsonObject> linksJsonList = AEMUtils.getJSONListfromProperty(links);
            LinkListBean navLinkDto;
            for (JsonObject navLink : linksJsonList) {
                navLinkDto = new LinkListBean();
                createLinkListBean(navLinkDto, navLink);
                linksList.add(navLinkDto);
            }
        } catch (final IllegalStateException e) {
            LOGGER.error("JSONException occurred in getNavigationLinks() of LinkListModel", e);
        }
        return linksList;
    }
    
	/**
	 * Creates the link list bean.
	 *
	 * @param navLinkDto the nav link dto
	 * @param navLink the nav link
	 */
	private void createLinkListBean(LinkListBean navLinkDto, JsonObject navLink) {
		if (navLink.has(NAVIGATION_PROPERTY_LINK_LABEL)) {
		    navLinkDto.setLinkLabel(navLink.get(NAVIGATION_PROPERTY_LINK_LABEL).getAsString());
		}
		if (navLink.has(NAVIGATION_PROPERTY_LINK_URL)) {
		    navLinkDto.setLinkUrl(LinkUtil.getHref(navLink.get(NAVIGATION_PROPERTY_LINK_URL).getAsString()));
		}
		if (navLink.has(NAVIGATION_PROPERTY_LINK_ACTION)) {
		    navLinkDto.setLinkAction(navLink.get(NAVIGATION_PROPERTY_LINK_ACTION).getAsString());
		}
		if (navLink.has(NAVIGATION_PROPERTY_COUNTRY_SELECTION)) {
		    navLinkDto.setCountrySel(navLink.get(NAVIGATION_PROPERTY_COUNTRY_SELECTION).getAsString());
		}
		if (navLink.has(NAVIGATION_PROPERTY_HAS_DROPDOWN)) {
		    navLinkDto.setHasDropdown(navLink.get(NAVIGATION_PROPERTY_HAS_DROPDOWN).getAsString());
		}
		if (navLink.has(NAVIGATION_PROPERTY_LINK_STYLE)) {
		    navLinkDto.setLinkStyle(navLink.get(NAVIGATION_PROPERTY_LINK_STYLE).getAsString());
		}
		if (navLink.has(NAVIGATION_PROPERTY_LINK_COLOR)) {
			navLinkDto.setLinkColor(navLink.get(NAVIGATION_PROPERTY_LINK_COLOR).getAsString());
		}
	}
    
    /**
	 * Gets the country code.
	 *
	 * @return the country code
	 */
	public String getCountryCode() {
		LOGGER.debug("Inside getCountryCode() method");
		String countryCode = StringUtils.EMPTY;
		Map<String, String> regCountLangMap = CommonUtils
				.getRegionCountryLanguage(CommonUtils.getPagePathFromRequest(request), getResourceResolver());
		if (null != regCountLangMap) {
			if (regCountLangMap.containsKey(CortevaConstant.GLOBAL_FLAG)) {
				countryCode = CortevaConstant.GLOBAL_FLAG;
			} else if (regCountLangMap.containsKey(CortevaConstant.COUNTRY)) {
				countryCode = StringUtils.lowerCase(regCountLangMap.get(CortevaConstant.COUNTRY));
			}
		} 
		LOGGER.debug("Country Code :: {}", countryCode);
		return countryCode;
	}

}
