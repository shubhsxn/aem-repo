package com.corteva.model.component.test;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.TilesModel;
import com.day.cq.wcm.api.Page;

/**
 * This is a test class for Tiles Model.
 * 
 * @author Sapient
 * 
 */
public class TilesModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private TilesModel tilesModel;	

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The resource. */
	@Mock
	private Resource resource;
	
	@Mock
	private Page page;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/**
	 * Method to setup the test objects.
	 */
	@Before
	public void setUp() {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		context.create().resource("/content/corteva/na");
		context.currentResource("/content/corteva/na");
		tilesModel = getRequest().adaptTo(TilesModel.class);
	}
	
	/**
	 * Test ImpactTitle.
	 */
	@Test
	public void testImpactTitle() {
		Assert.assertNull(tilesModel.getImpactTitle());
	}
	
	/**
	 * Test ImpactExpandedTitle.
	 */
	@Test
	public void testImpactExpandedTitle() {
		Assert.assertNull(tilesModel.getImpactExpandedTitle());
	}
	
	/**
	 * Test BgColor.
	 */
	@Test
	public void tesBgColor() {
		Assert.assertNull(tilesModel.getBgColor());
	}
	
	/**
	 * Test LinkUrlCropIcon.
	 */
	@Test
	public void testLinkUrlCropIcon() {
		Assert.assertNotNull(tilesModel.getLinkUrlCropIcon());
	}
		
	/**
	 * Test LinkActionCropIcon.
	 */
	@Test
	public void testLinkActionCropIcon() {
		Assert.assertNull(tilesModel.getLinkActionCropIcon());
	}
	
	/**
	 * Test LinkStyleCropIcon.
	 */
	@Test
	public void testLinkStyleCropIcon() {
		Assert.assertNull(tilesModel.getLinkStyleCropIcon());
	}
	
	/**
	 * Test ImpactBgColor.
	 */
	@Test
	public void testImpactBgColor() {
		Assert.assertNull(tilesModel.getImpactBgColor());
	}
	
	/**
	 * Test TextPlacement.
	 */
	@Test
	public void testTextPlacement() {
		Assert.assertNull(tilesModel.getTextPlacement());
	}
	
	/**
	 * Test EyebrowLabel.
	 */
	@Test
	public void testEyebrowLabel() {
		Assert.assertNull(tilesModel.getEyebrowLabel());
	}
	
	/**
	 * Test DisplayPublishDate.
	 */
	@Test
	public void testIsDisplayPublishDate() {
		Assert.assertFalse(tilesModel.isDisplayPublishDate());
	} 
}