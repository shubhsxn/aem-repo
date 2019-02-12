/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.test;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;

/**
 * This is test class to test the base configuration service.
 */
public class BaseConfigurationTest extends BaseAbstractTest {

	/** The base service. */
	private BaseConfigurationService baseService;

	/** The dictionary. */
	@Mock
	private Dictionary<String, Object> dict;
	
	@Mock
	private FrameworkUtil frmUtil;

	/** The config. */
	@Mock
	private Configuration config;

	/** The country lang map. */
	private Map<String, String> countryLangMap = new HashMap<>();

	/** The property. */
	private String property;

	/** The config pid. */
	private String configPid;
	
	/** The component name. */
	private String componentName;
	
	/** The feature flag config pid. */
	private String featureFlagConfigPid;

	/**
	 * Sets the method parameters and registers the service.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerService(FrameworkUtil.class, frmUtil);
		Configuration myServiceConfig;
		try {
			myServiceConfig = configAdmin
					.getConfiguration("com.corteva.core.configurations.GlobalConfigurationService_en_US");
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.CORTEVA_ROOT_PATH, "test value");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
			countryLangMap.put(CortevaConstant.COUNTRY, "US");
			countryLangMap.put(CortevaConstant.LANGUAGE, "en");
			property = CortevaConstant.CORTEVA_ROOT_PATH;
			configPid = CortevaConstant.GLOBAL_CONFIG_NAME;
			componentName = "tilesContainer";
			featureFlagConfigPid = CortevaConstant.FEATURE_FLAG_CONFIG_NAME;
		} catch (IOException e) {
			Assert.fail("IOException occurred in setUp()" + e.getMessage());
		}

	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testPropertyValueFromConfigNotNull() {
		Assert.assertNotNull(baseService.getPropertyValueFromConfiguration(countryLangMap, property, configPid));
	}

	/**
	 * Test property does not exist.
	 */
	@Test
	public void testPropertyDoesNotExist() {
		Assert.assertNotNull(baseService.getPropertyValueFromConfiguration(countryLangMap, "testProp", configPid));
	}

	/**
	 * Test config pid does not exist.
	 */
	@Test
	public void testConfigPidDoesNotExist() {
		Assert.assertNotNull(baseService.getPropertyValueFromConfiguration(countryLangMap, property, "com.corteva"));
	}

	/**
	 * Test country map is null.
	 */
	@Test
	public void testCountryMapIsNull() {
		Assert.assertNotNull(baseService.getPropertyValueFromConfiguration(null, property, configPid));
	}
	
	/**
	 * Test feature flag toggle information is not null.
	 */
	@Test
	public void testToggleInfoIsNotNull() {
		Assert.assertNotNull(baseService.getToggleInfo(componentName, featureFlagConfigPid));
	}
	
	/**
	 * Test property values.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testPropertyValues() throws IOException {
		Assert.assertNull(baseService.getPropValuesFromConfiguration(property, configPid));
	}
}
