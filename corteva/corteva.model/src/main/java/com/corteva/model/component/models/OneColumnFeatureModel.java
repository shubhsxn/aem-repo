package com.corteva.model.component.models;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * The is the sling model for the one column feature component.
 * @author Sapient
 */

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class OneColumnFeatureModel extends AbstractSlingModel {
	
	/** The viewType. */
	@Inject
	@Optional
	@Via("resource")
	private String viewType;
	
	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	private String title;
	
	/** The sling request. */
    private SlingHttpServletRequest oneColumnSlingRequest;
	
	/** The bodyText. */
	@Inject
	@Optional
	@Named("oneColumnBodyText")
	@Via("resource")
	private String bodyText;
	
	/** The altText. */
	@Inject
	@Optional
	@Named("OneColumnAltText")
	@Via("resource")
	private String altText;
	
	
	/** The resource type. */
	@Inject
	@Optional
	@Named("sling:resourceType")
	@Via("resource")
	private String resourceType;
	
	/** The image path fileReference. */
	@Inject
	@Optional
	@Via("resource")
	@Named("fileReference")
	private String imagePath;
	
	/** The second image path fileReference. */
	@Inject
	@Optional
	@Via("resource")
	@Named("bleedfileReference")
	private String bleedImagePath;
	
	/** The second image bleedAltText. */
	@Inject
	@Optional
	@Via("resource")
	private String bleedAltText;
	
	/** The fullBleed checkbox. */
	@Inject
	@Optional
	@Via("resource")
	private boolean fullBleed;
	
	/** The buttonText. */
	@Inject
	@Optional
	@Named("oneColumnButtonText")
	@Via("resource")
	private String buttonText;
	
	/** The buttonLink. */
	@Inject
	@Optional
	@Named("oneColumnButtonLink")
	@Via("resource")
	private String buttonLink;
	
	/** The buttonAction. */
	@Inject
	@Optional
	@Named("oneColumnButtonAction")
	@Via("resource")
	private String buttonAction;
	
	/**
     * Instantiates a new common element model.
     *
     * @param request the request
     */
    public OneColumnFeatureModel(SlingHttpServletRequest request) {
        oneColumnSlingRequest = request;
    }

	/**
	 * Gets the view type.
	 *
	 * @return the view type
	 */
	public String getViewType() {
		return viewType;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the body text.
	 *
	 * @return the body text
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * Gets the alt text.
	 *
	 * @return the alt text
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * Gets the resource type.
	 *
	 * @return the resource type
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * Gets the image path.
	 *
	 * @return the image path
	 */
	public ImageRenditionBean getImagePath() {
		return getImageRenditionList(imagePath, CommonUtils.getComponentName(resourceType), oneColumnSlingRequest);
	}

	/**
	 * Gets the bleed image path.
	 *
	 * @return the bleed image path
	 */
	public ImageRenditionBean getBleedImagePath() {
		return getImageRenditionList(bleedImagePath, CommonUtils.getComponentName(resourceType), oneColumnSlingRequest);
	}

	/**
	 * Gets the bleed alt text.
	 *
	 * @return the bleed alt text
	 */
	public String getBleedAltText() {
		return bleedAltText;
	}

	/**
	 * Gets the full bleed.
	 *
	 * @return the full bleed
	 */
	public boolean getFullBleed() {
		return fullBleed;
	}

	/**
	 * Gets the button text.
	 *
	 * @return the button text
	 */
	public String getButtonText() {
		return buttonText;
	}

	/**
	 * Gets the button link.
	 *
	 * @return the button link
	 */
	public String getButtonLink() {
		return LinkUtil.getHref(buttonLink);
	}

	/**
	 * Gets the button action.
	 *
	 * @return the button action
	 */
	public String getButtonAction() {
		return buttonAction;
	}
		
}
