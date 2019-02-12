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
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.AccordionListItemBean;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * This sling model will be used by all the components where we need to view the
 * content in form of accordion *
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class AccordionListModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccordionListModel.class);
	/** Icon Instantiation. */
	private static final String ICON = "icon";
	/** Image Instantiation. */
	private static final String IMAGE = "image";

	/** Accordion Image Property ConstantInstantiation. */
	private static final String ACCORDION_IMAGE = "accordionImage";

	/** Accordion Image Property ConstantInstantiation. */
	private static final String IMAGE_ACCORDION_SCENE7_VIEW_TYPE = "accordion";

	/** Accordion Image Property ConstantInstantiation. */
	private static final String IMAGE_STATIC_LIST_SCENE7_VIEW_TYPE = "staticList";

	/** Accordion Image Property ConstantInstantiation. */
	private static final String ITEM_DETAIL_PROPERTY = "itemDetail";
	/** Accordion Image Property ConstantInstantiation. */
	private static final String ITEM_IMAGE_ICON_PROPERTY = "iconImage";
	/** Accordion Image Property ConstantInstantiation. */
	private static final String ITEM_ALT_TEXT_PROPERTY = "altText";
	/** Accordion Image Property ConstantInstantiation. */
	private static final String ITEM_HEADER_PROPERTY = "itemHeader";

	/** The Icon List Items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("iconList")
	private Property iconList;

	/** The Image List items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("imageList")
	private Property imageList;

	/** The No Asset List Items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("noAssetList")
	private Property noAssetList;

	/** The Asset type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("accordionDisplay")
	private String accordionDisplay;

	/** The Asset type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("itemHeaderAssetType")
	private String itemHeaderAssetType;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public AccordionListModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets theaccordion List items
	 *
	 * @return the accordion List items
	 */
	public List<AccordionListItemBean> getAccordionListItem() {
		if (getItemHeaderAssetType().equalsIgnoreCase(IMAGE)) {
			return getAccordionListItem(imageList);
		} else if (getItemHeaderAssetType().equalsIgnoreCase(ICON)) {
			return getAccordionListItem(iconList);
		} else {
			return getAccordionListItem(noAssetList);
		}

	}

	/**
	 * Gets theaccordion List items
	 *
	 * @param itemList
	 *            the itemList of accordion
	 * @return the accordion List items
	 */
	public List<AccordionListItemBean> getAccordionListItem(Property itemList) {
		LOGGER.debug("Inside getAccordionListItem() method of AccordionListModel");
		List<AccordionListItemBean> accordionItemList = new ArrayList<>();
		String viewType = getAccordionDisplay().equalsIgnoreCase(CortevaConstant.TRUE)
				? IMAGE_ACCORDION_SCENE7_VIEW_TYPE
				: IMAGE_STATIC_LIST_SCENE7_VIEW_TYPE;
		try {
			for (JsonObject accordionItem : AEMUtils.getJSONListfromProperty(itemList)) {
				if (accordionItem != null) {
					accordionItemList.add(populateAccordionListItemBean(accordionItem, viewType));
				}
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("Inside getAccordionListItem() method of AccordionListModel", e);
		}
		return accordionItemList;
	}

	/**
	 * Populate the Accordion With JSON data
	 * 
	 * @param accordionItem
	 *            the itemList of accordion
	 * @param viewType
	 *            the viewtype of container
	 * @return the accordion List item Bean
	 * @exception JSONException
	 *                On input error.
	 * 
	 */
	private AccordionListItemBean populateAccordionListItemBean(JsonObject accordionItem, String viewType) {
		AccordionListItemBean accordionListItemObj = new AccordionListItemBean();
		if (getItemHeaderAssetType().equalsIgnoreCase(ICON)) {
			viewType = ICON;
			accordionListItemObj.setImageRenditionBean(accordionItem.has(ITEM_IMAGE_ICON_PROPERTY)
					? getImageBean(accordionItem.get(ITEM_IMAGE_ICON_PROPERTY).getAsString(), viewType)
					: null);
		} else {
			accordionListItemObj.setImageRenditionBean(accordionItem.has(ACCORDION_IMAGE)
					? getImageBean(accordionItem.get(ACCORDION_IMAGE).getAsString(), viewType)
					: null);
		}
		accordionListItemObj.setAltText(
				accordionItem.has(ITEM_ALT_TEXT_PROPERTY) ? accordionItem.get(ITEM_ALT_TEXT_PROPERTY).getAsString()
						: null);
		accordionListItemObj.setItemDetail(
				accordionItem.has(ITEM_DETAIL_PROPERTY) ? accordionItem.get(ITEM_DETAIL_PROPERTY).getAsString() : null);
		accordionListItemObj.setItemHeader(accordionItem.get(ITEM_HEADER_PROPERTY).getAsString());
		return accordionListItemObj;
	}

	/**
	 * Populate the Accordion With JSON data
	 *
	 * @param imagePath
	 *            the imagePath of authored image
	 * @param viewType
	 *            the viewtype of container
	 * @return the ImageRenditionBean
	 * 
	 */
	private ImageRenditionBean getImageBean(String imagePath, String viewType) {
		return getImageRenditionList(imagePath, viewType, slingRequest);
	}

	/**
	 * Gets the ItemHeaderAssetType.
	 *
	 * @return the itemHeaderAssetType
	 */
	public String getItemHeaderAssetType() {
		return itemHeaderAssetType;
	}

	/**
	 * Sets the itemHeaderAssetType.
	 *
	 * @param itemHeaderAssetType
	 *            the new itemHeaderAssetType
	 */
	public void setItemHeaderAssetType(String itemHeaderAssetType) {
		this.itemHeaderAssetType = itemHeaderAssetType;
	}

	/**
	 * @return the accordionDisplay
	 */
	public String getAccordionDisplay() {
		return accordionDisplay;
	}

	/**
	 * @param accordionDisplay
	 *            the accordionDisplay to set
	 */
	public void setAccordionDisplay(String accordionDisplay) {
		this.accordionDisplay = accordionDisplay;
	}

}