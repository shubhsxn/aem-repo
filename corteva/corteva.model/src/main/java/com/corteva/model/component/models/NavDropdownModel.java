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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ImageRenditionBean;

/**
 * The is the sling model for Navigation Dropdown component.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class NavDropdownModel extends AbstractSlingModel {

    /** The promo url. */
    @Inject
    @Optional
    @Via("resource")
    private String promoUrl;

    /** The promo image. */
    @Inject
    @Optional
    @Via("resource")
    @Named("fileReference")
    private String promoImg;
    
    /** The component resource type. */
    @Inject
    @Optional
    @Via("resource")
    @Named("sling:resourceType")
    private String resourceType;
    
    /** The sling request. */
    @Inject
    private SlingHttpServletRequest slingRequest;
    
    /**
     * Gets the promo url.
     *
     * @return the promo url
     */
    public String getPromoUrl() {
        return promoUrl;
    }

    /**
     * Gets the promo img.
     *
     * @return the promo img
     */
    public ImageRenditionBean getPromoImg() {
        return getImageRenditionList(promoImg, CommonUtils.getComponentName(resourceType), slingRequest);
    }

}
