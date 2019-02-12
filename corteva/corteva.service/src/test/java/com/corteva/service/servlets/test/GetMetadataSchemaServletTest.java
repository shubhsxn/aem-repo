/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.service.servlets.test;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.GetMetadataSchemaServlet;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;


/**
 * The is the test class to test the Content Metadata Servlet.
 * 
 * @author Sapient
 *
 */
public class GetMetadataSchemaServletTest extends BaseAbstractTest {

	/** The content metadata servlet. */
	private GetMetadataSchemaServlet getMetadataSchemaServlet;

	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The mock response. */
	private MockSlingHttpServletResponse mockResponse;

	/** The base service. */
	private BaseConfigurationService baseService;

	/** The mock resource. */
	@Mock
	private Resource mockResource;
	
	/** The mock resourceResolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/** The resource util. */
	@Mock
	private ResourceUtil resourceUtil;

	/** The page test json. */
	private String metadataSchema;
	
	@Mock
	private HierarchyNodeInheritanceValueMap valueMap;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {		
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
		getMetadataSchemaServlet = new GetMetadataSchemaServlet();
		MockitoAnnotations.initMocks(this);
		mockResponse = getResponse();
		
		Mockito.when(mockRequest.getParameter(Mockito.anyString())).thenReturn("/content/dam/test");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
		Mockito.when(resourceResolver.resolve(Mockito.anyString())).thenReturn(mockResource);
		metadataSchema = "{\"metadataSchema\":\"corteva-icon\"}";
		
		try {	
			myServiceConfig = configAdmin.getConfiguration(CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.FEATURE_JAVA_FRAMEWORK, "true");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
			}
			catch(IOException e) {
			Assert.fail("Exception occurred in setup()" + e.getMessage());
		}
		
	}

	/**
	 * Test content metadata servlet.
	 */
	@Test
	public void testGetMetadataSchemaServlet() {
		try {
			getMetadataSchemaServlet.bindBaseConfigurationService(baseService);
			getMetadataSchemaServlet.doPost(mockRequest, mockResponse);
			Assert.assertFalse(mockResponse.getOutputAsString().contains(metadataSchema));
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testGetMetadataSchemaServletFail(): " + e.getMessage());
		}

	}


}