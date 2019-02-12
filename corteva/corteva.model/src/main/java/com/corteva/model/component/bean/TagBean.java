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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The Class TagBean.
 * @author Sapient
 */
@JsonInclude(Include.NON_EMPTY)
public class TagBean {

    /** The title. */
    private String title;
    
    /** The localized title. */
    private String localizedTitle;

    /** The value. */
    private String value;

    /** The description. */
    private String description;

    /** The child tags. */
    private List<TagBean> childTags;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Gets the localizedTitle.
     *
     * @return the localizedTitle
     */
    public String getLocalizedTitle() {
        return this.localizedTitle;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Sets the localized title.
     *
     * @param localizedTitle the localizedTitle to set
     */
    public void setLocalizedTitle(String localizedTitle) {
        this.localizedTitle = localizedTitle;
    }

    /**
     * Sets the value.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the child tags.
     *
     * @return the child tags
     */
    public List<TagBean> getChildTags() {
        return childTags;
    }

    /**
     * Sets the child tags.
     *
     * @param childTags the new child tags
     */
    public void setChildTags(List<TagBean> childTags) {
        this.childTags = childTags;
    }
}