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

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.GalleryImageBean;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.google.gson.JsonObject;

/**
 * The Class GalleryImageModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class GalleryImageModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GalleryImageModel.class);

	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	@Named("title")
	private String title;

	/** The intro text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("introText")
	private String introText;

	/** The Icon List Items. */
	@Inject
	@Optional
	@Via("resource")
	@Named("gallery")
	private Property gallery;

	/** The component resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;
	
	/** The Constant IMAGE_PATH. */
	private static final String IMAGE_PATH = "imagePath";
	
	/** The Constant IMAGE_CAPTION. */
	private static final String IMAGE_CAPTION = "imageCaption";

	/**
	 * Instantiates a new hero model.
	 *
	 * @param request
	 *            the request
	 */
	public GalleryImageModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the gallery images.
	 *
	 * @return the gallery images
	 */
	public List<GalleryImageBean> getGalleryImages() {
		return getGalleryImages(gallery);
	}

	/**
	 * Gets the gallery images.
	 *
	 * @param galleryImages
	 *            the gallery images
	 * @return the gallery images
	 */
	public List<GalleryImageBean> getGalleryImages(Property galleryImages) {
		LOGGER.debug("Inside getGalleryImages() method of GalleryImageModel");
		List<GalleryImageBean> galleryImageList = new ArrayList<>();

		try {
			List<JsonObject> galleryJsonList = AEMUtils.getJSONListfromProperty(galleryImages);
			GalleryImageBean galleryDto;
			for (JsonObject galleryItem : galleryJsonList) {
				galleryDto = new GalleryImageBean();
				galleryDto.setAltText(galleryItem.get(CortevaConstant.ALT_TEXT).getAsString());
				galleryDto
						.setImageRenditionBean(getImagePath(galleryItem.get(IMAGE_PATH).getAsString(), resourceType));
				galleryDto.setImageCaption(galleryItem.get(IMAGE_CAPTION).getAsString());
				galleryImageList.add(galleryDto);
			}

		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getGalleryImages() of GalleryImageModel", e);
		}
		return galleryImageList;

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
	 * Gets the intro text.
	 *
	 * @return the intro text
	 */
	public String getIntroText() {
		return introText;
	}

	/**
	 * This method gets the image rendition json for hero as a string.
	 *
	 * @param imagePath
	 *            the image path
	 * @param resourceType
	 *            the resource type
	 * @return the hero image json string
	 */
	public ImageRenditionBean getImagePath(String imagePath, String resourceType) {
		return getImageRenditionList(imagePath, getComponentName(resourceType), slingRequest);

	}

}
