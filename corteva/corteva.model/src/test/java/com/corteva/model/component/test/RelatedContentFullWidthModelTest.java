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
import com.corteva.model.component.models.RelatedContentFullWidthModel;

/**
 * This is the test class to test the Related Content Full Width Model.
 */
public class RelatedContentFullWidthModelTest extends BaseAbstractTest {

	/** The related content card model. */
	@Mock
	private RelatedContentFullWidthModel cardModel;

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
		cardModel = getRequest().adaptTo(RelatedContentFullWidthModel.class);
	}

	/**
	 * Test Content Type.
	 */
	@Test
	public void testContentType() {
		Assert.assertNull(cardModel.getContentType());
	}

	/**
	 * Test Page Path.
	 */
	@Test
	public void testGetPagePath() {
		Assert.assertNotNull(cardModel.getPagePath());
	}

	/**
	 * Test Title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(cardModel.getTitle());
	}

	/**
	 * Test Short Description.
	 */
	@Test
	public void testShortDescription() {
		Assert.assertNull(cardModel.getShortDescription());
	}

	/**
	 * Test Video Type.
	 */
	@Test
	public void testVideoType() {
		Assert.assertNull(cardModel.getVideoType());
	}

	/**
	 * Test Youtube Video Id.
	 */
	@Test
	public void testVideoId() {
		Assert.assertNull(cardModel.getVideoId());
	}

	/**
	 * Test Youtube Video title.
	 */
	@Test
	public void testVideoTitle() {
		Assert.assertNull(cardModel.getVideoTitle());
	}

	/**
	 * Test Youtube Video description.
	 */
	@Test
	public void testVideoDescription() {
		Assert.assertNull(cardModel.getVideoDescription());
	}

	/**
	 * Test Youtube Video Alt Text.
	 */
	@Test
	public void testVideoAltText() {
		Assert.assertNull(cardModel.getVideoAltText());
	}

	/**
	 * Test Youtube Video Thumbnail.
	 */
	@Test
	public void testVideoThumbnail() {
		Assert.assertNull(cardModel.getVideoThumbnail());
	}

	/**
	 * Test Scene 7 Video Path.
	 */
	//@Test
	public void testVideoPath() {
		Assert.assertNotNull(cardModel.getVideoPath());
	}

	/**
	 * Test Scene 7 Video Title.
	 */
	@Test
	public void testVideoTitleS7() {
		Assert.assertNull(cardModel.getVideoTitleS7());
	}

	/**
	 * Test Scene 7 Video Description.
	 */
	@Test
	public void testVideoDescriptionS7() {
		Assert.assertNull(cardModel.getVideoDescriptionS7());
	}

	/**
	 * Test Scene 7 Video Alt Text.
	 */
	@Test
	public void testVideoAltTextS7() {
		Assert.assertNull(cardModel.getVideoAltTextS7());
	}

	/**
	 * Test Scene 7 Video Thumbnail.
	 */
	@Test
	public void testVideoThumbnailS7() {
		Assert.assertNull(cardModel.getVideoThumbnailS7());
	}

	/**
	 * Test Cta Label.
	 */
	@Test
	public void testCtaLabel() {
		Assert.assertNull(cardModel.getCtaLabel());
	}
}