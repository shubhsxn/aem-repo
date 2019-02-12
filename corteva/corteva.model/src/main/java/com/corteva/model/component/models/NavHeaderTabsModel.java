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
import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.NavHeaderTabsBean;
import com.google.gson.JsonObject;

/**
 * The Class GalleryImageModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class NavHeaderTabsModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(NavHeaderTabsModel.class);

	/** The title. */
	@Inject
	@Optional
	@Via("resource")
	private String title;

	/** The sub title. */
	@Inject
	@Optional
	@Via("resource")
	private String subTitle;

	/** The Tab List Items. */
	@Inject
	@Optional
	@Via("resource")
	private Property navHeaderTabs;
	
	/** The Constant PROPERTY_TAB_LABEL. */
	private static final String PROPERTY_TAB_LABEL = "tabLabel";

	/**
	 * Gets the gallery images.
	 *
	 * @return the gallery images
	 */
	public List<NavHeaderTabsBean> getNavHeaderTabs() {
		return getNavHeaderTabs(navHeaderTabs);
	}

	/**
	 * Gets the gallery images.
	 *
	 * @param tabs
	 *            the tabs
	 * @return the nav tabs
	 */
	public List<NavHeaderTabsBean> getNavHeaderTabs(Property tabs) {
		LOGGER.debug("Inside getNavHeaderTabs() method of NavHeaderTabsModel");
		List<NavHeaderTabsBean> navTabsList = new ArrayList<>();

		try {
			List<JsonObject> tabsJsonList = AEMUtils.getJSONListfromProperty(tabs);
			NavHeaderTabsBean tabsDto;
			for (JsonObject headerTabs : tabsJsonList) {
				tabsDto = new NavHeaderTabsBean();
				if (headerTabs.has(PROPERTY_TAB_LABEL)) {
					tabsDto.setTabLabel(headerTabs.get(PROPERTY_TAB_LABEL).getAsString());
				}
				navTabsList.add(tabsDto);
			}

		} catch (final IllegalStateException e) {
			LOGGER.error("JSONException occurred in getNavHeaderTabs() of NavHeaderTabsModel", e);
		}
		return navTabsList;

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
	 * Gets the sub title.
	 *
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

}
