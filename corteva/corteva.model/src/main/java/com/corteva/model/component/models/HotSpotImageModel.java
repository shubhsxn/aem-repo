package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.HotSpotBean;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.google.gson.JsonObject;

/**
 * The Class HotSpotImageModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class HotSpotImageModel extends AbstractSlingModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HotSpotImageModel.class);

	/** The Constant HOT_SPOT_TITLE. */
	private static final String HOT_SPOT_TITLE = "hotSpotTitle";

	/** The Constant HOT_SPOT_TEXT. */
	private static final String HOT_SPOT_TEXT = "hotSpotText";

	/** The Constant MODAL_ID. */
	private static final String MODAL_ID = "modalId";

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The hot spot image. */
	@Inject
	@Optional
	@Via("resource")
	@Named("hotSpotImage")
	private String hotSpotImage;

	/** The title. */
	@Inject
	@Via("resource")
	@Named("title")
	private String title;

	/** The intro text. */
	@Inject
	@Optional
	@Via("resource")
	@Named("introtxt")
	private String introText;

	/** The caption. */
	@Inject
	@Optional
	@Via("resource")
	@Named("caption")
	private String caption;

	/** The hotspotitems. */
	@Inject
	@Optional
	@Via("resource")
	@Named("hotspotitems")
	private Property hotSpotItems;

	/** The resource type. */
	@Inject
	@Optional
	@Via("resource")
	@Named("sling:resourceType")
	private String resourceType;

	/** The resource resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/**
	 * Instantiates a new hot spot image model.
	 *
	 * @param request
	 *            the request
	 */
	public HotSpotImageModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Gets the hot spot image.
	 *
	 * @return the hot spot image
	 */
	public ImageRenditionBean getHotSpotImage() {
		return getImageRenditionList(hotSpotImage, "hotSpotImage", slingRequest);
	}

	/**
	 * Gets the hot spot items.
	 *
	 * @return the hot spot items
	 */
	public List<HotSpotBean> getHotSpotItems() {
		return getHotSpotItems(hotSpotItems);
	}

	/**
	 * Gets the hot spot items.
	 *
	 * @param hotSpotItems
	 *            the hot spot items
	 * @return the hot spot items
	 */
	public List<HotSpotBean> getHotSpotItems(Property hotSpotItems) {
		LOGGER.info("entering getHotSpotItems() method of HotSpotImageModel");
		List<HotSpotBean> hotSpotItemsList = new ArrayList<>();
		LOGGER.debug("hotSpotItems List is :{}", hotSpotItems);
		List<JsonObject> hotSpotJsonList = AEMUtils.getJSONListfromProperty(hotSpotItems);
		LOGGER.debug("hotSpotJsonList is :{}", hotSpotJsonList);
		HotSpotBean hotSpotDto;
		for (JsonObject hotSpotItem : hotSpotJsonList) {
			hotSpotDto = new HotSpotBean();
			if (hotSpotItem.has(HotSpotImageModel.HOT_SPOT_TITLE)) {
				hotSpotDto.setHotSpotTitle(hotSpotItem.get(HotSpotImageModel.HOT_SPOT_TITLE).getAsString());
			}
			if (hotSpotItem.has(HotSpotImageModel.HOT_SPOT_TEXT)) {
				hotSpotDto.setHotSpotText(hotSpotItem.get(HotSpotImageModel.HOT_SPOT_TEXT).getAsString());
			}

			if (hotSpotItem.has(HotSpotImageModel.MODAL_ID)) {
				hotSpotDto.setModalId(hotSpotItem.get(HotSpotImageModel.MODAL_ID).getAsString());
			}

			hotSpotItemsList.add(hotSpotDto);
		}
		LOGGER.info("exiting getHotSpotItems() method of HotSpotImageModel");
		return hotSpotItemsList;
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
	 * Gets the caption.
	 *
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Gets the image cordinates.
	 *
	 * @return the image cordinates
	 */
	public List<HotSpotBean> getImageCordinates() {
		LOGGER.info("entering getImageCordinates() method of HotSpotImageModel");
		List<HotSpotBean> imageCoordinates = new ArrayList<>();
		Resource contentResource = resourceResolver.resolve(hotSpotImage);
		LOGGER.debug("contentResource of hotSpotImage is {}", contentResource);
		Resource jcrResource = null;
		String imageMap;
		if (!ResourceUtil.isNonExistingResource(contentResource)) {
			LOGGER.debug("content resource of hotSpotImage is presnet in repository");
			jcrResource = contentResource
					.getChild(CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.METADATA);
			if (null != jcrResource) {
				ValueMap vMap = jcrResource.getValueMap();
				if (!vMap.isEmpty()) {
					imageMap = (String) vMap.get("imageMap");
					LOGGER.debug("going to fetch the co-ordinates of image");
					imageCoordinates = getImageMapAxis(imageMap);
					LOGGER.debug("imageCoordinates of hotSpotImage are: {}", imageCoordinates);

				}

			}

		}
		LOGGER.info("exiting getImageCordinates() method of HotSpotImageModel");
		return imageCoordinates;
	}

	/**
	 * Gets the image map axis.
	 *
	 * @param imageMap
	 *            the image map
	 * @return the image map axis
	 */
	public List<HotSpotBean> getImageMapAxis(String imageMap) {
		LOGGER.info("entering getImageMapAxis() method of HotSpotImageModel");
		String[] totalHotSpots = imageMap.split("]");
		LOGGER.debug("totalHotSpots are : {}", totalHotSpots);
		List<HotSpotBean> imageCoordinates = new ArrayList<>();
		HotSpotBean bean;
		for (int hotSpotIndex = 0; hotSpotIndex <= totalHotSpots.length - 1; hotSpotIndex++) {
			String hotSpot = totalHotSpots[hotSpotIndex];
			bean = new HotSpotBean();
			String[] hotSpotArray = StringUtils.split(hotSpot, "\\(");
			String[] hotSpotItem = StringUtils.split(hotSpotArray[1], "\\)");
			LOGGER.debug("going to set the co-ordinate for hotspot");
			bean.setFirstCoOrdinate(StringUtils.split(hotSpotItem[0], CortevaConstant.COMMA)[0]);
			bean.setSecondCoOrdinate(StringUtils.split(hotSpotItem[0], CortevaConstant.COMMA)[1]);
			bean.setThirdCoOrdinate(StringUtils.split(hotSpotItem[0], CortevaConstant.COMMA)[2]);
			bean.setFourthCoOrdinate(StringUtils.split(hotSpotItem[0], CortevaConstant.COMMA)[3]);
			bean.setHrefId(getIdHotSpot(hotSpotItem[1]));
			imageCoordinates.add(bean);
		}
		LOGGER.info("exiting getImageMapAxis() method of HotSpotImageModel");
		return imageCoordinates;
	}

	/**
	 * Gets the id hot spot.
	 *
	 * @param extraTokens
	 *            the extra tokens
	 * @return the id hot spot
	 */
	private String getIdHotSpot(String extraTokens) {
		String hrefId = "";
		String[] remainingTokens = StringUtils.split(extraTokens, "|");
		if (remainingTokens.length > 0) {
			hrefId = StringUtils.remove(remainingTokens[0], "\"");
		}
		return hrefId;
	}
	
	/**
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
	}

}
