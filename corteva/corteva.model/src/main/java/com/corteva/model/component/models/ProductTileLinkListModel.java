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
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.ProductTileLinkListBean;
import com.corteva.model.component.bean.ResourceLinkBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The is the sling model for the Product Tile and Linked List.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class ProductTileLinkListModel extends AbstractSlingModel {
	
	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductTileLinkListModel.class);
	
	/** The Image Block title */
	@Inject
	@Optional
	@Via("resource")
	@Named("imageBlockTitle")
	private String imageBlockTitle;
	
	/** The Image Blocks items */
	@Inject
	@Optional
	@Via("resource")
	@Named("imageBlocks")
	private Property imageBlocks;

	/** The Resource Section Items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("resourceSections")
	private Property resourceSectionDetails;
	
	/** The Required Resource Section Items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("resourceSectionLinks")
	private Property requiredResources;
	
	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;
	
	/** The Constant RESOURCE_SECTION_LABEL. */
	private static final String RESOURCE_SECTION_LABEL = "sectionLabel";
	
	/** The Constant RESOURCE_SECTION_DESCRIPTION. */
	private static final String RESOURCE_SECTION_DESCRIPTION = "sectionDescription";
	
	/** The Constant PROPERTY_IMAGE_LOGO_SOURCE. */
	private static final String IMAGE_LOGO_SOURCE = "imageLogo";
	
	/** The Constant LOGO_ALT_TEXT. */
	private static final String LOGO_ALT_TEXT = "logoAltText";
	
	/** The Constant LOGO_DESTINATION_URL. */
	private static final String IMAGE_LOGO_DESTINATION_URL = "imageLogoUrl";
	
	/** The Constant RESOURCE_LINKS. */
	private static final String RESOURCE_LINKS = "resourceLinks";
	
	/** The Constant RESOURCE_TYPE. */
	private static final String RESOURCE_TYPE = "resourceType";
	
	/** The Constant RESOURCE_LABEL. */
	private static final String RESOURCE_LABEL = "resourceLabel";
	
	/** The Constant RESOURCE_DESTINATION_URL. */
	private static final String RESOURCE_DESTINATION_URL = "destinationUrl";
	
	/** The Constant RESOURCE_TYPE_EXTERNAL. */
	private static final String RESOURCE_TYPE_EXTERNAL = "external";
	
	/** The Constant LINK_ACTION_SELF. */
	private static final String LINK_ACTION_SELF = "_self";
	
	/** The Constant LINK_ACTION_BLANK. */
	private static final String LINK_ACTION_BLANK = "_blank";
	
	/** The Constant RESOURCE_LINK_TYPE. */
	private static final String RESOURCE_LINK_TYPE = "resourceLinkType";
	
	/** The Constant RESOURCE_LINK_LABEL. */
	private static final String RESOURCE_LINK_LABEL = "resourceLinkLabel";
	
	/** The Constant RESOURCE_LINK_DESTINATION_URL. */
	private static final String RESOURCE_LINK_DESTINATION_URL = "resourceLinkDestinationUrl";
	
	/**
	 * @return the imageBlockTitle
	 */
	public String getImageBlockTitle() {
		return imageBlockTitle;
	}

	/**
	 * @param imageBlockTitle
	 *            the imageBlockTitle to set
	 */
	public void setImageBlockTitle(String imageBlockTitle) {
		this.imageBlockTitle = imageBlockTitle;
	}

	/**
	 * Instantiates a new product tile link list model.
	 *
	 * @param request
	 *            the request
	 */
	public ProductTileLinkListModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}
	
	/**
	 * This method returns the authored image block data.
	 *
	 * @return the image block list
	 */
	public List<ProductTileLinkListBean> getImageBlockDetails() {
		return getImageBlockDetails(imageBlocks);
	}

	/**
	 * Gets the image block details.
	 * 
	 * @param imageBlocks
	 *            the image blocks
	 * @return the image block details
	 */
	public List<ProductTileLinkListBean> getImageBlockDetails(Property imageBlocks) {

		LOGGER.debug("Inside getImageBlockDetails() method of ProductTileLinkListModel");
		List<ProductTileLinkListBean> imageList = new ArrayList<>();
		try {
			List<JsonObject> imageLogoList = AEMUtils.getJSONListfromProperty(imageBlocks);
			ProductTileLinkListBean imageDto;
			for (JsonObject imageBlockJson : imageLogoList) {
				imageDto = new ProductTileLinkListBean();			
				imageDto.setImageLogoPath(imageBlockJson.has(IMAGE_LOGO_SOURCE)
						? getSceneSevenImagePath(imageBlockJson.get(IMAGE_LOGO_SOURCE).getAsString(), slingRequest)
						: null);				
				imageDto.setLogoAltText(imageBlockJson.has(LOGO_ALT_TEXT)
						? imageBlockJson.get(LOGO_ALT_TEXT).getAsString()
						: null);
				imageDto.setImageLogoUrl(imageBlockJson.has(IMAGE_LOGO_DESTINATION_URL)
						? LinkUtil.getHref(imageBlockJson.get(IMAGE_LOGO_DESTINATION_URL).getAsString()) 
						: null);
				if (null != imageDto.getImageLogoUrl()) {
					imageDto.setLogoCallToAction(
							StringUtils.startsWith(imageBlockJson.get(IMAGE_LOGO_DESTINATION_URL).getAsString(),
									CortevaConstant.CONTENT_ROOT) ? LINK_ACTION_SELF : LINK_ACTION_BLANK);
				}
				imageList.add(imageDto);
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getImageBlockDetails() of ProductTileLinkListModel", e);
		}
		return imageList;
	}
	
	/**
	 * This method returns the mandatory resource section data.
	 *
	 * @return the resource section list
	 */
	public List<ProductTileLinkListBean> getResourceSection() {
		return getResourceSection(requiredResources);
	}

	/**
	 * Gets the required resource section data.
	 * 
	 * @param requiredResources
	 *            the required resources
	 * @return the required resource section details
	 */
	public List<ProductTileLinkListBean> getResourceSection(Property requiredResources) {

		LOGGER.debug("Inside getResourceSection() method of ProductTileLinkListModel");
		List<ProductTileLinkListBean> resSecList = new ArrayList<>();
		try {
			List<JsonObject> resourceSecList = AEMUtils.getJSONListfromProperty(requiredResources);
			ProductTileLinkListBean resSecDto;
			for (JsonObject resSectionJson : resourceSecList) {
				resSecDto = new ProductTileLinkListBean();						
				resSecDto.setResourceLinkType(resSectionJson.has(RESOURCE_LINK_TYPE)
						? resSectionJson.get(RESOURCE_LINK_TYPE).getAsString()
						: null);
				resSecDto.setResourceLinkStyle(
						resSectionJson.get(RESOURCE_LINK_TYPE).getAsString().equalsIgnoreCase(RESOURCE_TYPE_EXTERNAL)
								? LINK_ACTION_BLANK
								: LINK_ACTION_SELF);
				resSecDto.setResourceLinkLabel(resSectionJson.has(RESOURCE_LINK_LABEL)
						? resSectionJson.get(RESOURCE_LINK_LABEL).getAsString()
								: null);
				resSecDto.setResourceLinkDestinationUrl(resSectionJson.has(RESOURCE_LINK_DESTINATION_URL)
						? LinkUtil.getHref(resSectionJson.get(RESOURCE_LINK_DESTINATION_URL).getAsString())
						: null);
				resSecList.add(resSecDto);
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getResourceSection() of ProductTileLinkListModel", e);
		}
		return resSecList;
	}

	/**
     * Gets the resource section details.
     *
     * @return the resource section details.
     */
    public List<ProductTileLinkListBean> getResources() {
    	return getResources(resourceSectionDetails);	
    }
    
    /**
     * Gets the resource section details.
     * @param resourceSectionDetails the resourceSectionDetails
     * @return the resourceLinksList
     */
    public List<ProductTileLinkListBean> getResources(Property resourceSectionDetails) {
        LOGGER.info("Inside getResources() method of ProductTileLinkListModel");
        List<ProductTileLinkListBean> resourceLinksList = new ArrayList<>();
        try {
            List<JsonObject> resourceSectionJsonList = AEMUtils.getJSONListfromProperty(resourceSectionDetails);                     
            
            ProductTileLinkListBean resourceSectionDto;
            for (JsonObject resources : resourceSectionJsonList) {
            	resourceSectionDto = new ProductTileLinkListBean();
                createResourceSections(resourceSectionDto, resources);
                resourceLinksList.add(resourceSectionDto);
            }
        } catch (final IllegalStateException e) {
            LOGGER.error("JSONException occurred in getResources() of ProductTileLinkListModel", e);
        }
        return resourceLinksList;
    }
    
	/**
	 * Creates the Product Tile Link List bean.
	 *
	 * @param resourceSectionDto the resource section dto
	 * @param resources the resource section details
	 */
	private void createResourceSections(ProductTileLinkListBean resourceSectionDto, JsonObject resources) {
		resourceSectionDto.setSectionLabel(
				resources.has(RESOURCE_SECTION_LABEL) ? resources.get(RESOURCE_SECTION_LABEL).getAsString()
						: null);
		resourceSectionDto.setSectionDescription(
				resources.has(RESOURCE_SECTION_DESCRIPTION) ? resources.get(RESOURCE_SECTION_DESCRIPTION).getAsString()
						: null);		
		if (resources.has(RESOURCE_LINKS)) {
			resourceSectionDto.setResourceLinkList(getResourceLinkDetails(resources));
		}		
	}
	
	/**
	 * Gets the resource link details.
     * @param resources the resources
	 * @return the resourceList
	 */
	public List<ResourceLinkBean> getResourceLinkDetails(JsonObject resources) {
		LOGGER.info("Inside getResourceLinkDetails() method of ProductTileLinkListModel");
		List<ResourceLinkBean> resourceList = new ArrayList<>();
		try {
			JsonArray nestedArr =  resources.get(RESOURCE_LINKS).getAsJsonArray();
			ResourceLinkBean resourceLinkDto;
			
			for (JsonElement res : nestedArr) {
				JsonObject nestedJson = res.getAsJsonObject();
				resourceLinkDto = new ResourceLinkBean();
				resourceLinkDto.setResourceType(
						nestedJson.has(RESOURCE_TYPE) ? nestedJson.get(RESOURCE_TYPE).getAsString() : null);
				resourceLinkDto.setResourceLinkStyle(
						nestedJson.get(RESOURCE_TYPE).getAsString().equalsIgnoreCase(RESOURCE_TYPE_EXTERNAL)
								? LINK_ACTION_BLANK
								: LINK_ACTION_SELF);
				resourceLinkDto.setResourceLabel(
						nestedJson.has(RESOURCE_LABEL) ? nestedJson.get(RESOURCE_LABEL).getAsString() : null);
				resourceLinkDto.setDestinationUrl(nestedJson.has(RESOURCE_DESTINATION_URL)
						? LinkUtil.getHref(nestedJson.get(RESOURCE_DESTINATION_URL).getAsString())
						: null);
				if (!(nestedJson.get(RESOURCE_LABEL).getAsString().equalsIgnoreCase("")) && !(nestedJson.get(RESOURCE_DESTINATION_URL).getAsString().equalsIgnoreCase(""))) {
					resourceList.add(resourceLinkDto);
				}				
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getResources() of getResourceLinkDetails", e);
		}
		return resourceList;
	}
}