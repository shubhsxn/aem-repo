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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.ContentMetadataServlet;

/**
 * The is the test class to test the Content Metadata Servlet.
 * 
 * @author Sapient
 *
 */
public class ContentMetadataServletTest extends BaseAbstractTest {

	/** The content metadata servlet. */
	private ContentMetadataServlet contentMetadataServlet;

	/** The mock request. */
	private MockSlingHttpServletRequest mockRequest;

	/** The mock response. */
	private MockSlingHttpServletResponse mockResponse;

	/** The base service. */
	private BaseConfigurationService baseService;

	/** The mock session. */
	private Session mockSession;

	/** The metadata properties map. */
	private Map<String, Object> metadataPropertiesMap = new HashMap<>();

	/** The resource util. */
	@Mock
	private ResourceUtil resourceUtil;

	/** The page test json. */
	private String pageTestJson;

	/** The asset test json. */
	private String assetTestJson;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
		contentMetadataServlet = new ContentMetadataServlet();
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		mockResponse = getResponse();
		String[] metadataProperties = new String[] { "jcr:title" };
		String[] assetProperties = new String[] { "imageAltText" };
		String pagePath = "/content/corteva/na/us/en/test-blank";
		metadataPropertiesMap.put("pagePropertiesKeys", metadataProperties);
		metadataPropertiesMap.put("pagePath", pagePath);
		metadataPropertiesMap.put("imageKey", "primaryImage");

		metadataPropertiesMap.put("assetPropertiesKeys", assetProperties);
		metadataPropertiesMap.put("assetPath", "/content/dam/dpagco/corteva/NA/US/english/image/pc.jpg");

		mockRequest.setParameterMap(metadataPropertiesMap);

		String currentResPath = "/apps/corteva/components/structure/page-base/cq:dialog/content/items/tabs/items/basic/items/column/items/title/items/title";
		String pageResPath = "/content/corteva/na/us/en/test-blank/jcr:content";
		pageTestJson = "{\"imageJson\":{\"primaryImage\":\"/content/dam/dpagco/corteva/NA/US/english/image/pc.jpg\"},\"pageJson\":{\"jcr:title\":\"Dialog Population\"}}";

		String assetResPath = "/conf/global/settings/dam/adminui-extension/metadataschema/corteva-image/items/tabs/items/tab1/items/col1/items/1522912186528";
		String assetPath = "/content/dam/dpagco/corteva/NA/US/english/image/pc.jpg/jcr:content/metadata";
		assetTestJson = "{\"imageAltText\":\"Test Alt Text\"}";
		try {
			myServiceConfig = configAdmin.getConfiguration(CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.FEATURE_JAVA_FRAMEWORK, "true");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
			mockSession = getResourceResolver().adaptTo(Session.class);

			/**
			 * Current Resource is set.
			 */
			Node mockNode = mockSession.getRootNode().addNode(currentResPath);
			/**
			 * Setting mock node properties for component dialog.
			 */
			mockNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, "granite/ui/components/coral/foundation/form/textfield");
			mockNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			mockNode.setProperty(CortevaConstant.NAME, "./jcr:title");
			context.create().resource(currentResPath);
			context.currentResource(currentResPath).getValueMap();

			/**
			 * Current Page is set.
			 */
			Node mockPageNode = mockSession.getRootNode().addNode(pageResPath);
			/**
			 * Setting mock node properties for page resource.
			 */
			mockPageNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, "corteva/components/structure/page-base");
			mockPageNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:PageContent");
			mockPageNode.setProperty(CortevaConstant.CQ_TEMPLATE,
					"/conf/corteva/settings/wcm/templates/corteva-blank-page-template");
			mockPageNode.setProperty(CortevaConstant.NAME, "./jcr:title");
			mockPageNode.setProperty("jcr:title", "Dialog Population");
			mockPageNode.setProperty("primaryImage",
					"/content/dam/dpagco/corteva/NA/US/english/image/pc.jpg");
			context.create().resource(pageResPath);
			context.currentResource(pageResPath).getValueMap();

			/**
			 * Page node is being added.
			 */
			Node mockPage = mockSession.getRootNode().addNode(pagePath);
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource(pagePath);

			/**
			 * Asset Node is set.
			 */
			Node mockAssetNode = mockSession.getRootNode().addNode(assetResPath);
			/**
			 * Setting mock node properties for asset component.
			 */
			mockAssetNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE,
					"granite/ui/components/coral/foundation/form/textfield");
			mockAssetNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			mockAssetNode.setProperty(CortevaConstant.NAME, "./jcr:content/metadata/imageAltText");
			context.create().resource(assetResPath);
			context.currentResource(assetResPath).getValueMap();

			/**
			 * Asset Content Node is set.
			 */
			Node mockAssetContentNode = mockSession.getRootNode().addNode(assetPath);
			/**
			 * Setting mock node properties for page resource.
			 */
			mockAssetContentNode.setProperty("imageAltText", "Test Alt Text");
			context.create().resource(assetPath);
			context.currentResource(assetPath).getValueMap();

		} catch (RepositoryException | IOException e) {
			Assert.fail("Exception occurred in setUp(): " + e.getMessage());
		}
	}

	/**
	 * Test content metadata servlet for success for page.
	 */
	@Test
	public void testContentMetadataServletSuccessForPage() {
		try {
			contentMetadataServlet.bindBaseConfigurationService(baseService);
			contentMetadataServlet.doPost(mockRequest, mockResponse);
			Assert.assertTrue(mockResponse.getOutputAsString().contains(pageTestJson));
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testContentMetadataServletSuccessForPage(): " + e.getMessage());
		}
	}

	/**
	 * Test content metadata servlet for success for asset.
	 */
	@Test
	public void testContentMetadataServletSuccessForAsset() {
		try {
			contentMetadataServlet.bindBaseConfigurationService(baseService);
			contentMetadataServlet.doPost(mockRequest, mockResponse);
			Assert.assertTrue(mockResponse.getOutputAsString().contains(assetTestJson));
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testContentMetadataServletSuccessForAsset(): " + e.getMessage());
		}
	}

	/**
	 * Test content metadata servlet for failure.
	 */
	@Test
	public void testContentMetadataServletServletFail() {
		try {
			contentMetadataServlet.bindBaseConfigurationService(baseService);
			contentMetadataServlet.doPost(mockRequest, mockResponse);
			Assert.assertFalse(mockResponse.getOutputAsString().contains("Any Test Text"));
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testContentMetadataServletServletFail(): " + e.getMessage());
		}

	}
}