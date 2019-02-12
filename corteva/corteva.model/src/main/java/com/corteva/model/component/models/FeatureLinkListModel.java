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
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.FeatureLinkListBean;
import com.google.gson.JsonObject;

/**
 * The is the sling model for the Feature Link List.
 *
 * @author Sapient
 */
@Model(adaptables = { Resource.class })
public class FeatureLinkListModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureLinkListModel.class);

	/** The Constant LIST_ITEM_PATH. */
	private static final String LIST_ITEM_PATH = "listItemPath";

	/** The Constant LIST_ITEM_TITLE. */
	private static final String LIST_ITEM_TITLE = "listItemTitle";

	/** The section title. */
	@Inject
	@Optional
	@Named("sectionTitle")
	private String sectionTitle;

	/** The spotlight path. */
	@Inject
	@Optional
	@Named("spotlightPath")
	private String spotlightPath;

	/** The spotlight title. */
	@Inject
	@Optional
	@Named("spotlightTitle")
	private String spotlightTitle;

	/** The list title. */
	@Inject
	@Optional
	@Named("listTitle")
	private String listTitle;

	/** The list items. */
	@Inject
	@Optional
	private Property listItems;

	/**
	 * Gets the list items.
	 *
	 * @return the list items
	 */
	public List<FeatureLinkListBean> getListItems() {
		return getListItems(listItems);
	}

	/**
	 * Gets the list items.
	 *
	 * @param listItems
	 *            the list items
	 * @return the list items
	 */
	public List<FeatureLinkListBean> getListItems(Property listItems) {
		LOGGER.debug("Inside getListItems() method of FeatureLinkListModel");
		List<FeatureLinkListBean> featureList = new ArrayList<>();
		FeatureLinkListBean featureBean;
		try {
			List<JsonObject> featureObjectList = AEMUtils.getJSONListfromProperty(listItems);
			for (JsonObject featureObj : featureObjectList) {
				featureBean = new FeatureLinkListBean();
				if (featureObj.has(LIST_ITEM_PATH)) {
					featureBean.setListItemPath(LinkUtil.getHref(featureObj.get(LIST_ITEM_PATH).getAsString()));
				}
				if (featureObj.has(LIST_ITEM_TITLE)) {
					featureBean.setListItemTitle(featureObj.get(LIST_ITEM_TITLE).getAsString());
				}
				featureList.add(featureBean);
			}
		} catch (IllegalStateException e) {
			LOGGER.error("Exception occurred in getListItems() of FeatureLinkListModel", e);
		}
		return featureList;
	}

	/**
	 * Gets the section title.
	 *
	 * @return the sectionTitle
	 */
	public String getSectionTitle() {
		return sectionTitle;
	}

	/**
	 * Gets the spotlight path.
	 *
	 * @return the spotlightPath
	 */
	public String getSpotlightPath() {
		return LinkUtil.getHref(spotlightPath);
	}

	/**
	 * Gets the spotlight title.
	 *
	 * @return the spotlightTitle
	 */
	public String getSpotlightTitle() {
		return spotlightTitle;
	}

	/**
	 * Gets the list title.
	 *
	 * @return the listTitle
	 */
	public String getListTitle() {
		return listTitle;
	}
}