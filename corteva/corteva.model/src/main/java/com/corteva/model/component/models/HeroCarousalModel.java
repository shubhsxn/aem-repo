package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.commons.lang3.StringUtils;
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
import com.corteva.model.component.bean.HeroCarousalBean;
import com.google.gson.JsonObject;

/**
 * This is the sling model for footer component elements like title, image etc.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class HeroCarousalModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroCarousalModel.class);

	/** The hero carousal slides. */
	@Inject
	@Optional
	@Via("resource")
	@Named("heroSlides")
	private Property heroSlides;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;
	
	/** The Constant SLIDE_PROPERTY_PANEL_TITLE. */
	private static final String SLIDE_PROPERTY_PANEL_TITLE = "panelTitle";
	
	/** The Constant SLIDE_PROPERTY_EYEBROW_LABEL. */
	private static final String SLIDE_PROPERTY_EYEBROW_LABEL = "eyebrowLabel";
	
	/** The Constant SLIDE_PROPERTY_IMAGE_SOURCE. */
	private static final String SLIDE_PROPERTY_IMAGE_SOURCE = "imageSrc";
	
	/** The Constant SLIDE_PROPERTY_MOBILE_IMAGE. */
	private static final String SLIDE_PROPERTY_MOBILE_IMAGE = "mobImage";
	
	/** The Constant SLIDE_PROPERTY_BUTTON_TEXT. */
	private static final String SLIDE_PROPERTY_BUTTON_TEXT = "buttonText";
	
	/** The Constant SLIDE_PROPERTY_BUTTON_LINK. */
	private static final String SLIDE_PROPERTY_BUTTON_LINK = "buttonLink";
	
	/** The Constant SLIDE_PROPERTY_BUTTON_ACTION. */
	private static final String SLIDE_PROPERTY_BUTTON_ACTION = "buttonAction";
	
	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public HeroCarousalModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * This method return the hero carousal data author and is used in sightly.
	 *
	 * @return the anchor links container list
	 */
	public List<HeroCarousalBean> getCarousalSlides() {
		return getCarousalSlides(heroSlides);
	}

	/**
	 * Gets the carousal slides.
	 * 
	 * @param heroCarousal
	 *            the hero carousal
	 * @return the carousal slides
	 */
	public List<HeroCarousalBean> getCarousalSlides(Property heroCarousal) {

		LOGGER.debug("Inside getCarousalSlides() method of HeroCarousalModel");
		List<HeroCarousalBean> slideList = new ArrayList<>();
		try {
			List<JsonObject> slideJsonList = AEMUtils.getJSONListfromProperty(heroCarousal);
			HeroCarousalBean slidesDto;
			for (JsonObject slide : slideJsonList) {
				slidesDto = new HeroCarousalBean();
				slidesDto.setPanelTitle(slide.get(SLIDE_PROPERTY_PANEL_TITLE).getAsString());
				slidesDto.setEyebrowLabel(slide.get(SLIDE_PROPERTY_EYEBROW_LABEL).getAsString());
				slidesDto.setAltText(slide.get(CortevaConstant.ALT_TEXT).getAsString());
				slidesDto.setImageSrc(getImageRenditionList(slide.get(SLIDE_PROPERTY_IMAGE_SOURCE).getAsString(),
						CommonUtils.getComponentName(resourceType), slingRequest).getDesktopImagePath());
				slidesDto.setMobImage(getImageRenditionList(slide.get(SLIDE_PROPERTY_MOBILE_IMAGE).getAsString(),
						CommonUtils.getComponentName(resourceType), slingRequest).getMobileImagePath());
				String buttonText = slide.get(SLIDE_PROPERTY_BUTTON_TEXT).getAsString();
				if (!StringUtils.isBlank(buttonText)) {
					slidesDto.setButtonText(buttonText);
					slidesDto.setButtonLink(LinkUtil.getHref(slide.get(SLIDE_PROPERTY_BUTTON_LINK).getAsString()));
					slidesDto.setButtonAction(slide.get(SLIDE_PROPERTY_BUTTON_ACTION).getAsString());
				}
				slideList.add(slidesDto);

			}
		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getCarousalSlides() of HeroCarousalModel", e);
		}
		return slideList;
	}

}
