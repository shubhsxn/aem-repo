/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================*/

package com.corteva.model.component.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
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
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.bean.ProductBean;
import com.corteva.model.component.models.AbstractSlingModel;
import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * The Class AbstractSlingModelTest. 
 */
public class AbstractSlingModelTest extends BaseAbstractTest {

	/** The abstract model. */
	@Mock
	private AbstractSlingModel abstractModel;

	/** The config admin. */
	@Mock
	private ConfigurationAdmin configAdmi;

	/** The dictionary. */
	@Mock
	private Dictionary<String, Object> dict;

	/** The config. */
	@Mock
	private Configuration config;

	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The image path. */
	private String imagePath;

	/** The video path. */
	private String videoPath;

	/** The component path. */
	private String componentPath;

	/** The image json. */
	private String imageJson;

	/** The resource type. */
	/* The component resource type */
	private String resourceType;

	/** The component name. */
	/* The component name */
	private String componentName;

	/** The page. */
	@Mock
	private Page page;

	/** The mock session. */
	private Session mockSession;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The product list. */
	private List<ProductBean> productList;

	/** The product. */
	private ProductBean product;

	/** The Constant TAG_ATTRIBUTE. */
	private static final String TAG_ATTRIBUTE = "tagAttribute";

	/** The properties. */
	@Mock
	private ValueMap properties;

	/**
	 * This method sets the method parameters and registers the service.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
		try {
			myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
			String rootNodePath = "/content/corteva/na/US";
			Dictionary<String, Object> props = new Hashtable<>();
			props.put("hero", "hero_desktop, hero_tablet, hero_mobile");
			props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
			myServiceConfig.update(props);

			getContext().addModelsForPackage("com.corteva.model.component.models");
			mockRequest = getRequest();
			mockSession = getResourceResolver().adaptTo(Session.class);
			mockRequest.setServletPath("/content/corteva/na/US/en");
			abstractModel = mockRequest.adaptTo(AbstractSlingModel.class);
			imagePath = "/content/dam/corteva/Product_catalog.jpg";
			videoPath = "/content/dam/corteva/Sample_video.mp4";
			context.create().resource(imagePath);
			context.create().resource(videoPath);
			componentPath = "hero";
			imageJson = "{\"desktopImagePath\":\"/content/dam/corteva/Product_catalog.jpg\",\"tabletImagePath\":\"/content/dam/corteva/Product_catalog.jpg\",\"mobileImagePath\":\"/content/dam/corteva/Product_catalog.jpg\"}";
			resourceType = "corteva/components/content/tilesContainer/v1/tilesContainer";
			componentName = "tilesContainer";
			Node mockPage = mockSession.getRootNode().addNode(rootNodePath);
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource(rootNodePath);
			page = context.pageManager().create(rootNodePath, "en", "/apps/sample/templates/homepage", "en");
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(pageManager.getContainingPage(context.currentResource("/content/corteva/na/US/en")))
					.thenReturn(page);
			AbstractSlingModel.setRequest(mockRequest);
			context.currentResource("/content/corteva/na/US/en");
		} catch (IOException | WCMException | RepositoryException | SecurityException | IllegalArgumentException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}

	}

	/**
	 * Test case when Image Json Success.
	 */
	@Test
	public void testImageJsonSuccess() {
		Assert.assertEquals(imageJson, abstractModel.getImageRenditionJson(imagePath, componentPath, mockRequest));
	}

	/**
	 * Test case when Image path is null.
	 */
	@Test
	public void testImagePathIsNull() {
		Assert.assertNotNull(abstractModel.getImageRenditionJson(null, componentPath, mockRequest));
	}

	/**
	 * Test case to retrieve component name from resource type.
	 */
	@Test
	public void testComponentNameSuccess() {
		Assert.assertEquals(componentName, abstractModel.getComponentName(resourceType));
	}

	/**
	 * Test case to retrieve feature flag state for the component.
	 */
	@Test
	public void testFeatureFlagStateSuccess() {
		Assert.assertEquals(CortevaConstant.FLAG_ON, abstractModel.getFeatureFlagState(resourceType));
	}

	/**
	 * Test case to retrieve feature flag state for the component.
	 */
	@Test
	public void testFeatureFlagStateSuccessOff() {
		abstractModel.setBaseService(baseService);
		Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Assert.assertEquals(CortevaConstant.FLAG_OFF, abstractModel.getFeatureFlagState(resourceType));
	}

	/**
	 * Test case to retrieve feature flag state for the component.
	 */
	@Test
	public void testGetYoutubeApi() {
		abstractModel.setBaseService(baseService);
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("test");
		Assert.assertEquals("test", abstractModel.getYoutubeApi(mockRequest));
	}

	/**
	 * Test case to retrieve you tube key.
	 */
	@Test
	public void testGetYoutubeKey() {
		abstractModel.setBaseService(baseService);
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("youtubeKey");
		Assert.assertEquals("youtubeKey", abstractModel.getYoutubeKey(mockRequest));

	}

	/**
	 * Test case to retrieve you tube key.
	 */
	@Test
	public void testGetBaseConfiguration() {
		abstractModel.setBaseService(baseService);
		Assert.assertNotNull(abstractModel.getBaseConfigurationService());

	}

	/**
	 * Test case when Get Image Rendition List.
	 */
	@Test
	public void testGetImageRenditionList() {
		ImageRenditionBean bean = abstractModel.getImageRenditionList(imagePath, componentPath, mockRequest);
		Assert.assertEquals("/content/dam/corteva/Product_catalog.jpg", bean.getDesktopImagePath());
	}

	/**
	 * Test case when Get Image Rendition List.
	 */
	@Test
	public void testGetImageRenditionListII() {
		ImageRenditionBean bean = abstractModel.getImageRenditionList(imagePath);
		Assert.assertEquals("/content/dam/corteva/Product_catalog.jpg", bean.getDesktopImagePath());
	}

	/**
	 * Test case when Get scene 7 image path.
	 */
	@Test
	public void testGetSceneSevenImagePath() {
		Assert.assertNotNull(abstractModel.getSceneSevenVideoPath(imagePath, mockRequest));

	}

	/**
	 * Test case when Get Locale.
	 */
	@Test
	public void testGetLocale() {
		Assert.assertNotNull(abstractModel.getLocale(mockRequest));

	}

	/**
	 * Test case when Populate Tagged Assets.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testPopulateTaggedAssets() {
		TagManager mockTagManager = Mockito.mock(TagManager.class);
		Tag fakeTag = Mockito.mock(Tag.class);
		RangeIterator<Resource> rangeIterator = Mockito.mock(RangeIterator.class);
		Resource res = context.create().resource(imagePath);
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn(TAG_ATTRIBUTE);
		Mockito.when(mockTagManager.find(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(rangeIterator);
		Mockito.when(rangeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(rangeIterator.next()).thenReturn(res, null);
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
		product = new ProductBean();
		product.setProductTag(fakeTag);
		productList = new ArrayList<ProductBean>();
		productList.add(product);
		// if branch scenario
		abstractModel.populateTaggedAssets(productList, resourceType, mockTagManager, mockRequest, true);
		// else branch scenario
		abstractModel.populateTaggedAssets(productList, resourceType, mockTagManager, mockRequest, false);
	}

	/**
	 * Test get scene seven image path II.
	 */
	@Test
	public void testGetSceneSevenImagePathII() {
		Assert.assertNotNull(abstractModel.getSceneSevenImagePath(imagePath, mockRequest));

	}

	/**
	 * Test get scene seven image path III.
	 */
	@Test
	public void testGetSceneSevenImagePathIII() {
		Assert.assertNotNull(abstractModel.getSceneSevenImagePath(imagePath));

	}

	@Test
	public void testGetYoutubeKeyII() {
		abstractModel.setBaseService(baseService);
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("youtubeKey");
		Assert.assertNotNull(abstractModel.getYoutubeKey());

	}

	@Test
	public void testGetYoutubeApiII() {
		abstractModel.setBaseService(baseService);
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("test");
		Assert.assertNotNull(abstractModel.getYoutubeApi());

	}

	@Test
	public void testGetScene7VideoPath() {
		Assert.assertNotNull(abstractModel.getSceneSevenVideoPath(videoPath));

	}
	
}