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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.models.ProductSlingModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * The test class for Product Sling Model.
 */
public class ProductSlingModelTest extends BaseAbstractTest {

	/** The product model. */
	@InjectMocks
	private ProductSlingModel productModel;

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

	/** The page. */
	@Mock
	private Page page;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The current resource. */
	@Mock
	private Resource currentResource;

	/** The properties. */
	@Mock
	private ValueMap properties;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

	/** The page mgr. */
	@Mock
	private PageManager pageMgr;

	/** The tag manager. */
	@Mock
	private TagManager tagManager;
	
	@Mock
	private Iterator<Resource> queryIterator;
	
	@Mock
	private Iterable<Resource> iterableRes;
	
	@Mock
	private QueryBuilder queryBuilder;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query query;
	
	@Mock
	private SearchResult searchResult;
	
	@Mock
	private Property mockProperty;
	
	@Mock
	private Node mockNode;
	
	@Mock
	private Value mockValue;
	
	@Mock
	private ValueMap valueMap;
	
	/** The mock session. */
	private Session mockSession;
	
	/**
	 * The mocked resource object.
	 */
	@Mock
	private Resource mockResource;

	/**
	 * This method sets the method parameters and registers the service.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockSession = getResourceResolver().adaptTo(Session.class);
		try {
			Node mockPage = mockSession.getRootNode().addNode("/content/corteva/na/us");
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource("/content/corteva/na/us");
			page = context.pageManager().create("/content/corteva/na/us", "en", "/apps/sample/templates/homepage",
					"en");
			Field baseServiceField;
			baseServiceField = productModel.getClass().getDeclaredField("baseService");
			baseServiceField.setAccessible(true);
			baseServiceField.set(productModel, baseService);
			Field resolverField;
			resolverField = productModel.getClass().getSuperclass().getDeclaredField("resourceResolver");
			resolverField.setAccessible(true);
			resolverField.set(productModel, resolver);
			Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
			Mockito.when(resolver.adaptTo(TagManager.class)).thenReturn(tagManager);
			Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
			Mockito.when(properties.get(Mockito.anyString())).thenReturn("test");
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| WCMException | RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}

	}

	/**
	 * Test case when Get Label Finder Path for Agrian Source.
	 */
	@Test
	public void testGetLabelFinderPathForAgrian() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("pdpSrc", "agrian");
			pageNode.setProperty("productId", "1234");
			Mockito.when(resolver.map(mockRequest, "null.html?pid=1234&pdpSrc=agrian"))
					.thenReturn("null.html?pid=1234&pdpSrc=agrian");
			Assert.assertNotNull(productModel.getLabelFinderPath(mockRequest, currentResource));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test case when Get Label Finder Path for Non Agrian Source.
	 */
	@Test
	public void testGetLabelFinderPathForNonAgrian() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("pdpSrc", "nonagrian");
			Mockito.when(resolver.map(mockRequest, "null.html?pid=en&pdpSrc=nonagrian"))
					.thenReturn("null.html?pid=en&pdpSrc=nonagrian");
			Assert.assertNotNull(productModel.getLabelFinderPath(mockRequest, currentResource));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test case when Get Product Name.
	 */
	@Test
	public void testGetProductName() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("productName", "Test Product Name");
			Assert.assertEquals("Test Product Name", productModel.getProductName(currentResource));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test get state map.
	 */
	@Test
	public void testGetStateMap() {
		Map<String, String> regCountLangMap = new HashMap<>();
		regCountLangMap.put(CortevaConstant.REGION, "NA");
		regCountLangMap.put(CortevaConstant.COUNTRY, "US");
		regCountLangMap.put(CortevaConstant.LANGUAGE, "en");
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.anyString())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn("CA");
		Mockito.when(fakeTag.getTagID()).thenReturn("corteva:regionCountryState/US/CA");
		List<Tag> childTags = new ArrayList<>();
		childTags.add(fakeTag);
		Mockito.when(baseService.getPropertyValueFromConfiguration(regCountLangMap, CortevaConstant.STATE_BASE_TAG_PATH,
				CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/etc/tags/corteva/regionCountryState/");
		Mockito.when(fakeTag.listChildren()).thenReturn(childTags.iterator());
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockRequest.getResource().getPath()).thenReturn("/content/corteva/na/us/en/homepage");
		Mockito.when(resolver.getResource("/content/corteva/na")).thenReturn(mockResource);
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(resolver);
		Mockito.when((null != fakeTag.getLocalizedTitle(CommonUtils.getLocale(mockRequest))) 
				? fakeTag.getLocalizedTitle(CommonUtils.getLocale(mockRequest))
				: CommonUtils.getTagTitle(mockRequest, fakeTag))
				.thenReturn("California");
		Assert.assertNotNull(productModel.getStateMap(regCountLangMap, mockRequest));
	}
	
	/**
	 * Test case when Get Crop Type Tags.
	 * 
	 */
	@Test
	public void testGetCropTypeTags() {
		try {
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.anyString())).thenReturn(fakeTag);
		Mockito.when(mockProperty.getString()).thenReturn("cropTypes");
		Mockito.when(fakeTag.getParent()).thenReturn(fakeTag);
		Mockito.when(fakeTag.getName()).thenReturn("cropTypes");
		Mockito.when(mockNode.getProperty(Mockito.anyString())).thenReturn(mockProperty);
				
		RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);			
		Mockito.when(resolver.adaptTo(Session.class)).thenReturn(mockSession);
		String[] selector = new String[5];
		selector[0] = "all";
		selector[1] = "all";
		selector[2] = "all";
		selector[3] = "";
		selector[4] = "article1";
		Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
		Mockito.when(resolver.getResource(Mockito.anyString())).thenReturn(currentResource);
		Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
		Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
		Mockito.when(currentResource.getPath()).thenReturn("/content/corporate/resources/article/");
		Mockito.when(currentResource.getName()).thenReturn("article1");
		Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
				.thenReturn(query);
		Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(baseService.getPropValueFromConfiguration(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("5");
		final SearchResult searchResult = Mockito.mock(SearchResult.class);
		Mockito.when(query.getResult()).thenReturn(searchResult);
		Mockito.when(searchResult.getResources()).thenReturn(queryIterator);
		Mockito.when(queryIterator.next()).thenReturn(currentResource);
		Mockito.when(currentResource.adaptTo(Node.class)).thenReturn(mockNode);
		
		Assert.assertNotNull(productModel.getCropTypeTags(currentResource));
		} catch (IOException | RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		} 
	}
	
	/**
	 * Test case for Get Product Type Tags.
	 */
	@Test
	public void testGetProductTypeTags() {
		try {
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.anyString())).thenReturn(fakeTag);
		Mockito.when(valueMap.get(Mockito.anyString(),Mockito.any(Property.class))).thenReturn(mockProperty);
		Mockito.when(mockProperty.getString()).thenReturn("cropTypes");
		Mockito.when(fakeTag.getTitle()).thenReturn("cropTypes");
		Assert.assertNotNull(productModel.getProductTypeTags(page));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		} 
	}

	/**
	 * Test get locale for internationalization.
	 */
	@Test
	public void testGetLocaleForInternationalization() {
		SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
		Mockito.when(resolver.resolve("/content/corteva/na/us/en/products")).thenReturn(currentResource);
		Mockito.when(request.getResource()).thenReturn(currentResource);
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/products");
		Assert.assertNotNull(productModel.getLocaleForInternationalization(request));
	}
}