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
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.service.servlets.ProductFilterServlet;

/**
 * The Class ProductDetailsServletTest.
 */
public class ProductFilterServletTest extends BaseAbstractTest {
	
	/** The Article feed servlet. */
	private ProductFilterServlet productFilterServlet;
	
	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The mock response. */
	@Mock
	private MockSlingHttpServletResponse mockResponse;
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	/** The mock resource. */
	@Mock
	private Resource mockResource;
	
	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {
		try {
			
			MockitoAnnotations.initMocks(this);
			productFilterServlet = new ProductFilterServlet();
			context.registerInjectActivateService(new BaseConfigurationService());
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
			Mockito.when(resourceResolver.resolve(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString()))
					.thenReturn(mockResource);
			
			productFilterServlet.bindBaseConfigurationService(baseService);
			StringWriter responseWriter = new StringWriter();
			Mockito.when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
			
		} catch (Exception e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}
	
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testdoGetFilterDetail() {
		try {
			Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
			productFilterServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
		} catch (IOException e) {
			Assert.fail("Exception occurred in testdoGetAllArticleType()" + e.getMessage());
		}
	}
	
}