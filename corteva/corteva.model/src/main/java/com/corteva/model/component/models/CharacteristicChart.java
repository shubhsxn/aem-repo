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

import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.bean.CharacteristicChartBean;
import com.google.gson.JsonObject;

/**
 * The is the sling model for the Product Tile and Linked List.
 *
 * @author Sapient
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class CharacteristicChart extends AbstractSlingModel {

    /**
     * The theme
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("theme")
    private String theme;

    /**
     * The title
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("title")
    private String title;

    /**
     * The chartData
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("chartData")
    private String chartData;

    /**
     * The displaySidebar
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("displaySidebar")
    private String displaySidebar;

    /**
     * The sidebar
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("recordSidebar")
    private Property sidebar;

    /**
     * The disclaimer
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("disclaimer")
    private String disclaimer;
    
    /**
     * The title
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("emptyCsvFields")
    private String emptyCsvFields;

    /**
     * Gets the Product Registered States US.
     *
     * @return theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Process.
     *
     * @param theme the state list
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Process.
     *
     * @param title the state list
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return chartData
     */
    public String getChartData() {
        return chartData;
    }

    /**
     * Process.
     *
     * @param chartData the state list
     */
    public void setChartData(String chartData) {
        this.chartData = chartData;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return displaySidebar
     */
    public String getDisplaySidebar() {
        return displaySidebar;
    }

    /**
     * Process.
     *
     * @param displaySidebar the state list
     */
    public void setDisplaySidebar(String displaySidebar) {
        this.displaySidebar = displaySidebar;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return sidebar
     */
    public Property getSidebar() {
        return sidebar;
    }

    /**
     * Process.
     *
     * @param sidebar the state list
     */
    public void setSidebar(Property sidebar) {
        this.sidebar = sidebar;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Process.
     *
     * @param disclaimer the state list
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * Gets the Product Registered States US.
     *
     * @return sidebar list
     */
    public List<CharacteristicChartBean> getSidebarList() {
        return getSidebarList(sidebar);
    }

    /**
     * getSidebarList.
     *
     * @param sidebar the state list
     * @return the List
     */
    public List<CharacteristicChartBean> getSidebarList(Property sidebar) {
        List<CharacteristicChartBean> sidebarList = new ArrayList<>();

        List<JsonObject> sidebarJsonList = AEMUtils.getJSONListfromProperty(sidebar);

        CharacteristicChartBean sidebarDto;
        for (JsonObject sidebars : sidebarJsonList) {
            sidebarDto = new CharacteristicChartBean();
            sidebarDto.setLabelSidebar(
                    sidebars.has("labelSidebar") ? sidebars.get("labelSidebar").getAsString() : null);
            sidebarDto.setValueSidebar(
                    sidebars.has("valueSidebar") ? sidebars.get("valueSidebar").getAsString() : null);
            sidebarList.add(sidebarDto);
        }

        return sidebarList;
    }

    /**
     * getEmptyCsvFields.
     *
     * @return the List
     */
	public String getEmptyCsvFields() {
		return emptyCsvFields;
	}

	/**
     * setEmptyCsvFields.
     *
     * @param emptyCsvFields the checkbox value
     */
	public void setEmptyCsvFields(String emptyCsvFields) {
		this.emptyCsvFields = emptyCsvFields;
	}

}
