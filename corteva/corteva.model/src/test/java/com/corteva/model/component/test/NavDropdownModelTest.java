package com.corteva.model.component.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.NavDropdownModel;

/**
 * This is a test class for Nav Dropdown Model.
 * 
 * @author Sapient
 * 
 */
public class NavDropdownModelTest extends BaseAbstractTest {
	
	/** The mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The mock nav dropdown model */
	@Mock
	private NavDropdownModel navModel;

	/**
	 * This method instantiates a new instance of Nav Dropdown Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		navModel = getRequest().adaptTo(NavDropdownModel.class);
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.NavDropdownModel#getPromoUrl()}.
	 */
	@Test
	public void testGetPromoUrl() {
		Assert.assertNull(navModel.getPromoUrl());
	}
}