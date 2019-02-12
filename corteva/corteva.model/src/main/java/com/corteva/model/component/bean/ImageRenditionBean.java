/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.bean;

/**
 * The is the bean class that contains the image rendition path for desktop, tablet and mobile.
 * 
 * @author Sapient
 */
public class ImageRenditionBean {

    /** The desktop image path. */
    private String desktopImagePath;

    /** The tablet image path. */
    private String tabletImagePath;

    /** The mobile image path. */
    private String mobileImagePath;

    /**
     * Gets the desktop image path.
     *
     * @return the desktopImagePath
     */
    public String getDesktopImagePath() {
        return desktopImagePath;
    }

    /**
     * Sets the desktop image path.
     *
     * @param desktopImagePath the desktopImagePath to set
     */
    public void setDesktopImagePath(String desktopImagePath) {
        this.desktopImagePath = desktopImagePath;
    }

    /**
     * Gets the tablet image path.
     *
     * @return the tabletImagePath
     */
    public String getTabletImagePath() {
        return tabletImagePath;
    }

    /**
     * Sets the tablet image path.
     *
     * @param tabletImagePath the tabletImagePath to set
     */
    public void setTabletImagePath(String tabletImagePath) {
        this.tabletImagePath = tabletImagePath;
    }

    /**
     * Gets the mobile image path.
     *
     * @return the mobileImagePath
     */
    public String getMobileImagePath() {
        return mobileImagePath;
    }

    /**
     * Sets the mobile image path.
     *
     * @param mobileImagePath the mobileImagePath to set
     */
    public void setMobileImagePath(String mobileImagePath) {
        this.mobileImagePath = mobileImagePath;
    }

}