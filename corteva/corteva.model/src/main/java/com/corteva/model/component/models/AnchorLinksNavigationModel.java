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

import javax.inject.Inject;
import javax.jcr.Property;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.AnchorLinkBean;
import com.google.gson.JsonObject;

/**
 * The is the sling model for Anchor Link navigation component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class AnchorLinksNavigationModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AnchorLinksNavigationModel.class);

	/** Constant For In page navigation View Type. */
	private static final String IN_PAGE_NAV = "inPageNavigation";
	
	/** The Constant to hold property anchorLinkLabel. */
	private static final String PROPERTY_ANCHOR_LINK_LABEL = "anchorLinkLabel";
	
	/** The Constant to hold property anchorLinkLabelHidden. */
	private static final String PROPERTY_ANCHOR_LINK_LABEL_HIDDEN = "anchorLinkLabelHidden";

	/** The instructional label. */
	@Inject
	@Optional
	@Via("resource")
	private String instructionalLabel;

	/** The view Type. */
	@Inject
	@Optional
	@Via("resource")
	private String viewType;

	/** The intro text. */
	@Inject
	@Optional
	@Via("resource")
	private String introText;

	/** The anchor navigation links. */
	@Inject
	@Optional
	@Via("resource")
	private Property anchorLinks;

	/** The anchor navigation links. */
	@Inject
	@Optional
	@Via("resource")
	private Property sections;
	
	/** The sling request. */
	@Inject
	private SlingHttpServletRequest slingRequest;
	
	/**
	 * This method creates the parsys for each anchor link title provided by the
	 * author and is used in sightly.
	 *
	 * @return the anchor links container list
	 */
	public List<AnchorLinkBean> getAnchorLinksContainer() {
		List<AnchorLinkBean> anchorBeanList = null;
		if (viewType.equalsIgnoreCase(IN_PAGE_NAV)) {
			anchorBeanList = getAnchorLinksContainer(sections);
		} else {
			anchorBeanList = getAnchorLinksContainer(anchorLinks);
		}
		return anchorBeanList;
	}

	/**
	 * This method creates the parsys for each anchor link label provided by the
	 * author.
	 *
	 * @param anchorLinks
	 *            the anchor links
	 * @return the anchor links container list
	 */
	public List<AnchorLinkBean> getAnchorLinksContainer(Property anchorLinks) {
		LOGGER.debug("Inside getAnchorLinksContainer() method of AnchorLinksNavigationModel");
		List<AnchorLinkBean> anchorList = new ArrayList<>();
		AnchorLinkBean anchorBean;
		try {
			List<JsonObject> anchorNavObjectList = AEMUtils.getJSONListfromProperty(anchorLinks);
			for (JsonObject anchorNav : anchorNavObjectList) {
				anchorBean = new AnchorLinkBean();
				anchorBean.setAnchorLinkLabel(anchorNav.get(PROPERTY_ANCHOR_LINK_LABEL).getAsString());
				anchorBean.setAnchorNodeName(anchorNav.get(PROPERTY_ANCHOR_LINK_LABEL_HIDDEN).getAsString());
				anchorList.add(anchorBean);
			}
		} catch (IllegalStateException e) {
			LOGGER.error("Exception occurred in getAnchorLinksContainer() of AnchorLinksNavigationModel", e);
		}
		return anchorList;
	}

	/**
	 * Gets the instructional label.
	 *
	 * @return the instructionalLabel
	 */
	public String getInstructionalLabel() {
		return instructionalLabel;
	}

	/**
	 * Gets the intro text.
	 *
	 * @return the introText
	 */
	public String getIntroText() {
		return introText;
	}
	
	/**
	 * Gets the locale for internationalization.
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
	}
}
