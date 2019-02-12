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

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.CreatePdfServlet;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * The is the test class to test the Create Pdf Servlet. /*
 * 
 * @author Sapient
 *
 */
public class CreatePdfServletTest extends BaseAbstractTest {

	/** The create pdf servlet. */
	private CreatePdfServlet createPdfServlet;

	/** The mock request. */
	private MockSlingHttpServletRequest mockRequest;

	/** The mock response. */
	private MockSlingHttpServletResponse mockResponse;

	/** The base service. */
	private BaseConfigurationService baseService;

	/** The dictionary. */
	@Mock
	private Dictionary<String, Object> dict;

	/** The config. */
	@Mock
	private Configuration config;

	@Mock
	private Page page;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 *
	 * @throws RepositoryException
	 *             that an Repository exception has occurred.
	 */
	@Before
	public void setUp() {
		createPdfServlet = new CreatePdfServlet();
		mockRequest = getRequest();
		mockResponse = getResponse();
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
		try {
			myServiceConfig = configAdmin.getConfiguration(CortevaConstant.PHANTOM_JS_CONFIG);
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.PHANTOM_EXECUTABLE_LOCATION,
					"/opt/aem/phantom/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
			props.put(CortevaConstant.PHANTOM_JS_LOCATION,
					"/opt/aem/phantom/phantomjs-2.1.1-linux-x86_64/examples/rasterize.js");
			props.put(CortevaConstant.DESTINATION_PDF_LOCATION, "/opt/aem/pdfDestination/");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
			String resourcePath = "/content/corteva/na/us/en/home/our-merger";
			context.create().resource(resourcePath);
			page = context.pageManager().create("/content/corteva/na/us/en/home", "our-merger",
					"/apps/sample/templates/homepage", "our-merger");
			context.currentResource(resourcePath);
			context.registerAdapter(Resource.class, Page.class, page);
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(
					pageManager.getContainingPage(context.currentResource(resourcePath)))
					.thenReturn(page);
		} catch (IOException | WCMException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
		mockRequest.setServerPort(4502);
		mockRequest.setServletPath("/content/corteva/na/us/en/home/our-merger.pdf");
	}

	/**
	 * Test create pdf servlet for success.
	 */
	@Test
	public void testCreatePdfServletSuccess() {
		try {
			createPdfServlet.bindBaseConfigurationService(baseService);
			createPdfServlet.doGet(mockRequest, mockResponse);
			Assert.assertEquals(404, mockResponse.getStatus());
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testCreatePdfServletSuccess()" + e.getMessage());
		}
	}
}