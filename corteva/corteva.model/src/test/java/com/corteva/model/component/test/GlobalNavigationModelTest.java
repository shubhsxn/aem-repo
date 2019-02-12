package com.corteva.model.component.test;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.GlobalNavigationModel;

/**
 * This is a test class for Nav Dropdown Model.
 * 
 * @author Sapient
 * 
 */
public class GlobalNavigationModelTest extends BaseAbstractTest {
	
	/** The mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The mock global nav model */
	@Mock
	private GlobalNavigationModel gblNavModel;
	
	/** The logo url. */
	private String logoUrl;

	/**
	 * This method instantiates a new instance of Nav Dropdown Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		gblNavModel = getRequest().adaptTo(GlobalNavigationModel.class);
		logoUrl = "/content/dam/corteva/Product_catalog.jpg";
		Field viewTypeField;
		viewTypeField = gblNavModel.getClass().getDeclaredField("logoUrl");
		viewTypeField.setAccessible(true);
		viewTypeField.set(gblNavModel, logoUrl);
		
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.GlobalNavigationModel#getLogoUrl()}.
	 */
	@Test
	public void testGetLogoUrl() {
		Assert.assertNotNull(gblNavModel.getLogoUrl());
	}
}