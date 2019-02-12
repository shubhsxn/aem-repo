/**
 * 
 */
package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.bean.SocialLinkBean;
import com.google.gson.JsonObject;

/**
 * This is the sling model for common component elements like title, image etc.
 * This sling model will be used by all the components where we need common
 * element in dialog which will be used by to configure the component.
 * 
 * @author Sapient
 */


/**
 * The Class FooterElementsModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class FooterElementsModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FooterElementsModel.class);
	
	 /** The component resource type. */
    @Inject
    @Optional
    @Via("resource")
    @Named("sling:resourceType")
    private String resourceType;
	
	/** The hero image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("logoUrl")
	
	/** The logo url. */
	private String logoUrl;

	/** The hero2 image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("logoImage")
	private String logoImage;
	
	/** The logo image alt text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("logoImgAltText")
	private String logoImgAltText;

	/** The hero1 image path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("socialText")
	private String socialText;

	/** The social navigation links. */
	@Inject
	@Optional
	@Via("resource")
	@Named("socialLinks")
	private Property socialLinks;
	
	/** The social selection. */
	@Inject
	@Optional
	@Via("resource")
	private String socialSel;
	
	/** The Constant ANCHOR_PROPERTY_ICON_IMAGE. */
	private static final String ANCHOR_PROPERTY_ICON_IMAGE = "iconImage";
	
	/** The Constant ANCHOR_PROPERTY_ICON_LABEL. */
	private static final String ANCHOR_PROPERTY_ICON_LABEL = "iconLabel";
	
	/** The Constant ANCHOR_PROPERTY_ICON_ALT. */
	private static final String ANCHOR_PROPERTY_ICON_ALT = "iconAlt";
	
	/** The Constant ANCHOR_PROPERTY_ICON_LINK. */
	private static final String ANCHOR_PROPERTY_ICON_LINK = "iconLink";

	/** The Constant ICON. */
	private static final String ICON = "icon";

	/**
	 * Gets the social sel.
	 *
	 * @return the social sel
	 */
	public String getSocialSel() {
		return socialSel;
	}

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;
	
	/**
	 * Instantiates a new footer model.
	 *
	 * @param request
	 *            the request
	 */
	public FooterElementsModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * This method gets the bean for component.
	 *
	 * @return the bean object
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * This method gets the bean for component.
	 *
	 * @return the bean object
	 */
	public ImageRenditionBean getLogoImage() {
		return getImageRenditionList(logoImage, FooterElementsModel.ICON, slingRequest);
	}
	
	/**
	 * This method gets the bean for component.
	 *
	 * @return the bean object
	 */
	public String getLogoImgAltText() {
		return logoImgAltText;
	}

	/**
	 * This method gets the bean for component.
	 *
	 * @return the social text
	 */
	public String getSocialText() {
		return socialText;
	}

	/**
	 * Gets the anchor links container.
	 *
	 * @return the anchor links container
	 */
	public List<SocialLinkBean> getAnchorLinksContainer() {
		return getAnchorLinksContainer(socialLinks);
	}
	/**
	 * This method creates the parsys for each anchor link title provided by the
	 * author.
	 * @param footerSocialLinks the footer social links
	 * @return the anchor links container
	 */
	public List<SocialLinkBean> getAnchorLinksContainer(Property footerSocialLinks) {
		LOGGER.info("Inside getAnchorLinksContainer() method of AnchorLinksNavigationModel");
		List<SocialLinkBean> socialLinksList = new ArrayList<>();
		try {
			List<JsonObject> socialJsonList = AEMUtils.getJSONListfromProperty(footerSocialLinks);
			SocialLinkBean socialLinkDto;
			for (JsonObject anchorLink : socialJsonList) {
				socialLinkDto = new SocialLinkBean();
                if (anchorLink.has(ANCHOR_PROPERTY_ICON_IMAGE)) {
                	ImageRenditionBean imgRenBean = getImageRenditionList(anchorLink.get(ANCHOR_PROPERTY_ICON_IMAGE).getAsString(), FooterElementsModel.ICON, slingRequest);
                    socialLinkDto.setIconImage(imgRenBean.getDesktopImagePath());
                }
                if (anchorLink.has(ANCHOR_PROPERTY_ICON_LABEL)) {
                    socialLinkDto.setIconLabel(anchorLink.get(ANCHOR_PROPERTY_ICON_LABEL).getAsString());
                }
                if (anchorLink.has(ANCHOR_PROPERTY_ICON_LINK)) {
                    socialLinkDto.setIconLink(anchorLink.get(ANCHOR_PROPERTY_ICON_LINK).getAsString());
                }
                if (anchorLink.has(ANCHOR_PROPERTY_ICON_ALT)) {
                    socialLinkDto.setIconAlt(anchorLink.get(ANCHOR_PROPERTY_ICON_ALT).getAsString());
                }
                socialLinksList.add(socialLinkDto);
			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getAnchorLinksContainer() of FooterElements", e);
		}
		return socialLinksList;
	}
	
}