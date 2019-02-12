package com.corteva.model.component.models;


import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import com.corteva.core.configurations.BaseConfigurationService;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The is the sling model for FindSalesRep Retailer Model class
 *
 * @author Sapient
 */

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class FindSalesRepRetailerModel extends AbstractSlingModel {

    /**
     * Logger Instantiation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FindSalesRepRetailerModel.class);


    /**
     * The Constant BING_MAP config.
     */
    private static final String BING_MAP = "com.corteva.core.configurations.BingMapService";
    /**
     * The Constant BING_MAP API
     */
    public static final String BING_MAP_API = "https://www.pioneer.com/bingMapProxy/corteva/locations?domain=https://brevant.com";

    /**
     * The base config.
     */
    @Inject
    private BaseConfigurationService baseConfig;

    /**
     * The sling request.
     */
    @Inject
    private SlingHttpServletRequest slingRequest;

    /**
     * Instantiates Repsearch Model.
     *
     * @param request the request
     */
    public FindSalesRepRetailerModel(SlingHttpServletRequest request) {
        slingRequest = request;
    }


    /**
     * Gets the zip validation API url.
     *
     * @return the zip validation API url
     */
    public String getApiUrl() {
        StringBuilder sb = new StringBuilder();
        String zipApiUrl = baseConfig.getPropConfigValue(slingRequest, "apiurl",
                BING_MAP);
        String siteDomain = baseConfig.getPropConfigValue(slingRequest, "siteDomain",
                BING_MAP);
        String apiUrl = sb.append(zipApiUrl).append(siteDomain).toString();
        LOGGER.debug("apUrl before if condition = \"{}\"",apiUrl);
        if (StringUtils.isBlank(apiUrl)) {
            apiUrl = BING_MAP_API;
            LOGGER.debug("apUrl in if condition = \"{}\"",apiUrl);
        }
        LOGGER.debug("Bing Map Url is : {}", apiUrl);
        return apiUrl;
    }

}
