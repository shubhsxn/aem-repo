/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.RelatedContentCardModel;

/**
 * This is the test class to test the Related Content Card Model.
 */
public class RelatedContentCardModelTest extends BaseAbstractTest {

    /** The related content card model. */
    @Mock
    private RelatedContentCardModel cardModel;
    
    /** The base service. */
	@Mock
	private BaseConfigurationService baseService;
    
    /**
     * Method to set up the test objects.
     */
    @Before
    public void setUp() {
    	baseService = new BaseConfigurationService();
    	context.getService(ConfigurationAdmin.class);
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		cardModel = getRequest().adaptTo(RelatedContentCardModel.class);
    }

    /**
	 * Test Page Path.
	 */
	@Test
	public void testGetPagePath() {
		Assert.assertNotNull(cardModel.getPagePath());
	}
	
	/**
	 * Test title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(cardModel.getTitle());
	}
	
	/**
	 * Test Alt Text.
	 */
	@Test
	public void testAltText() {
		Assert.assertNull(cardModel.getAltText());
	}
}