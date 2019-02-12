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
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * This is the sling model for common component elements like title, image etc.
 * This sling model will be used by all the components where we need common
 * element in dialog which will be used by to configure the component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class CommonElementsModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonElementsModel.class);

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The component image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("fileReference")
	private String imagePath;

	/** The component image title. */
	@Inject
	@Optional
	@Via("resource")
	private String title;

	/** The component button text. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonText;

	/** The component button link. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonLink;

	/** The button action. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonAction;

	/** The button size. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonSize;

	/** The button color. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonColor;

	/** The button style. */
	@Inject
	@Optional
	@Via("resource")
	private String buttonStyle;

	/** The link label. */
	@Inject
	@Optional
	@Via("resource")
	private String linkLabel;

	/** The link url. */
	@Inject
	@Optional
	@Via("resource")
	private String linkUrl;

	/** The link action. */
	@Inject
	@Optional
	@Via("resource")
	private String linkAction;

	/** The link style. */
	@Inject
	@Optional
	@Via("resource")
	private String linkStyle;

	/** The link color. */
	@Inject
	@Optional
	@Via("resource")
	private String linkColor;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The component image body Text. */
	@Inject
	@Optional
	@Via("resource")
	private String bodyText;

	/** The component image altText. */
	@Inject
	@Optional
	@Via("resource")
	private String altText;

	/** The component drop down . */
	@Inject
	@Optional
	@Via("resource")
	private String dropdown;

	/** The component radio Button . */
	@Inject
	@Optional
	@Via("resource")
	private String radioButton;

	/** The component variation Button . */
	@Inject
	@Optional
	@Via("resource")
	private String variation;

	/** The component view Type . */
	@Inject
	@Optional
	@Via("resource")
	private String viewType;

	/** The brand logo alt text. */
	@Inject
	@Optional
	@Via("resource")
	private String logoAltText;

	/** The brand logo destination Url. */
	@Inject
	@Optional
	@Via("resource")
	private String logoUrl;

	/** The icon title. */
	@Inject
	@Optional
	@Via("resource")
	private String iconTitle;

	/** The icon alt text. */
	@Inject
	@Optional
	@Via("resource")
	private String iconAltText;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The feature to retrieve feature flag toggle state for. */
	@Inject
	@Optional
	@Named("feature")
	private String feature;

	/** The optional body text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("optionalBodyText")
	private String optionalBodyText;

	/**
	 * This method gets the view type of component .
	 * 
	 * @return the component view type
	 */
	public String getViewType() {
		return viewType;
	}

	/**
	 * This method gets the variation value.
	 * 
	 * @return the component variation value
	 */
	public String getVariation() {
		return variation;
	}

	/**
	 * This method gets the radio value.
	 * 
	 * @return the drop down value
	 */
	public String getRadioButton() {
		return radioButton;
	}

	/**
	 * This method gets the drop down value.
	 * 
	 * @return the drop down value
	 */
	public String getDropdown() {
		return dropdown;
	}

	/**
	 * This method gets the image alt text.
	 * 
	 * @return the hero image altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * This method gets the body text.
	 * 
	 * @return the component body text
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * This method gets the button text.
	 * 
	 * @return the component button text
	 */
	public String getButtonText() {
		return buttonText;
	}

	/**
	 * This method gets the button link.
	 * 
	 * @return the component button link
	 */
	public String getButtonLink() {
		return LinkUtil.getHref(buttonLink);
	}

	/**
	 * This method gets the image title.
	 * 
	 * @return the image title
	 */

	public String getTitle() {

		return title;
	}

	/**
	 * Instantiates a new common element model.
	 *
	 * @param request
	 *            the request
	 */
	public CommonElementsModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * This method gets the bean for component.
	 *
	 * @return the bean object
	 */
	public ImageRenditionBean getImageRenditionList() {
		LOGGER.debug("Inside getImageRenditionList() method");
		if (StringUtils.isNotBlank(viewType)) {
			return getImageRenditionList(imagePath, viewType, slingRequest);
		} else {
			return getImageRenditionList(imagePath, getComponentName(), slingRequest);
		}
	}

	/**
	 * This method gets the image rendition json for hero as a string.
	 *
	 * @return the hero image json string
	 */
	public ImageRenditionBean getImagePath() {
		return getImageRenditionList();

	}

	/**
	 * Gets the link label.
	 *
	 * @return the linkLabel
	 */
	public String getLinkLabel() {
		return linkLabel;
	}

	/**
	 * Gets the link url.
	 *
	 * @return the linkUrl
	 */
	public String getLinkUrl() {
		return LinkUtil.getHref(linkUrl);
	}

	/**
	 * Gets the link action.
	 *
	 * @return the linkAction
	 */
	public String getLinkAction() {
		return linkAction;
	}

	/**
	 * Gets the link style.
	 *
	 * @return the linkStyle
	 */
	public String getLinkStyle() {
		return linkStyle;
	}

	/**
	 * Gets the link color.
	 *
	 * @return the linkColor
	 */
	public String getLinkColor() {
		return linkColor;
	}

	/**
	 * Gets the logo alt text.
	 *
	 * @return the logo alt text
	 */
	public String getLogoAltText() {
		return logoAltText;
	}

	/**
	 * Gets the logo url.
	 *
	 * @return the logo url
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * Gets the icon title.
	 *
	 * @return the icon title
	 */
	public String getIconTitle() {
		return iconTitle;
	}

	/**
	 * Gets the icon alt text.
	 *
	 * @return the icon alt text
	 */
	public String getIconAltText() {
		return iconAltText;
	}

	/**
	 * Gets the sling request.
	 *
	 * @return the slingRequest
	 */
	public SlingHttpServletRequest getSlingRequest() {
		return slingRequest;
	}

	/**
	 * Gets the button action.
	 *
	 * @return the buttonAction
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * Gets the button size.
	 *
	 * @return the buttonSize
	 */
	public String getButtonSize() {
		return buttonSize;
	}

	/**
	 * Gets the button color.
	 *
	 * @return the buttonColor
	 */
	public String getButtonColor() {
		return buttonColor;
	}

	/**
	 * Gets the button style.
	 *
	 * @return the buttonStyle
	 */
	public String getButtonStyle() {
		return buttonStyle;
	}

	/**
	 * Gets the feature flag state.
	 *
	 * @return the featureFlagState
	 */
	public String getFeatureFlagState() {
		if (StringUtils.isNotBlank(feature)) {
			return getFeatureFlagState(feature);
		}
		return getFeatureFlagState(resourceType);
	}

	/**
	 * Gets the component name.
	 *
	 * @return the componentName
	 */
	public String getComponentName() {
		if (StringUtils.isNotBlank(feature)) {
			return getComponentName(feature);
		}
		return getComponentName(resourceType);
	}

	/**
	 * Gets the optional body text.
	 *
	 * @return the optional body text
	 */
	public String getOptionalBodyText() {
		return optionalBodyText;
	}
	
	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
	}
}