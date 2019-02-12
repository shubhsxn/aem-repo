package com.corteva.model.component.models.vendor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * @author Sapient This class returns a map generated from ACS GenericList
 *         utility.
 *
 */
public class GetAcsGenericItemMap extends WCMUsePojo {
	/**
	 * MAp for the dropdown options for form
	 */
	private Map<String, String> acsGenericMap = null;
	/**
	 * logger instantiation
	 */
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CortevaPrivacyForm.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.adobe.cq.sightly.WCMUsePojo#activate()
	 */
	@Override
	public void activate() throws Exception {
		String path = get("acsListPath", String.class);
		acsGenericMap = new LinkedHashMap<>();
		try {
			PageManager pageManager = getPageManager();
			Page listPage = pageManager.getPage(path);
			GenericList list = listPage.adaptTo(GenericList.class);
			if (list != null) {
				for (GenericList.Item gl : list.getItems()) {
					acsGenericMap.put(gl.getValue(), gl.getTitle());

				}
			}
		} catch (Exception e) {
			LOGGER.error("Some exception occured while fetching list value", e);
		}
	}

	/**
	 * @return Map of list
	 */
	public Map<String, String> getAcsGenericMap() {
		return acsGenericMap;
	}
}