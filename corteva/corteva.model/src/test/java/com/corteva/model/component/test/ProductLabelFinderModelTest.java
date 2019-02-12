/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================*/

package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ProductLabelFinderModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * The test class for Product Label Finder Model.
 */
public class ProductLabelFinderModelTest extends BaseAbstractTest {

	/** The product model. */
	@InjectMocks
	private ProductLabelFinderModel productModel;

	/** The mock request. */
	@Mock
	private SlingHttpServletRequest request;

	/** The page. */
	@Mock
	private Page page;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The current resource. */
	@Mock
	private Resource currentResource;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

	/** The page mgr. */
	@Mock
	private PageManager pageMgr;

	/** The mock session. */
	private Session mockSession;

	/** The query builder. */
	@Mock
	private QueryBuilder queryBuilder;

	/** The query. */
	@Mock
	private Query query;

	/** The session. */
	@Mock
	private Session session;

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
			Field requestField;
			requestField = productModel.getClass().getDeclaredField("request");
			requestField.setAccessible(true);
			requestField.set(productModel, request);
			Mockito.when(resolver.getResource("/content/corteva/na")).thenReturn(currentResource);
			Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
			Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
			Mockito.when(request.getResource()).thenReturn(currentResource);
			session = Mockito.mock(Session.class);
			queryBuilder = Mockito.mock(QueryBuilder.class);
			query = Mockito.mock(Query.class);
			Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PRODUCT_NODE_PATH_PROPERTY,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn(CortevaConstant.PRODUCT_NODE_PATH);
			SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| WCMException | RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}

	}

	/**
	 * Test get product data agrian for false.
	 */
	@Test
	public void testGetProductDataAgrianForFalse() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en");
			Mockito.when(resolver.resolve("/content/corteva/na/us/en")).thenReturn(currentResource);
			productModel.init();
			Resource res = context.create().resource("/etc/corteva/product");
			Mockito.when(resolver.resolve(CortevaConstant.PRODUCT_NODE_PATH)).thenReturn(res);
			Node parentNode = res.adaptTo(Node.class);
			parentNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			Node childNode = parentNode.addNode("740");
			childNode.setProperty(CortevaConstant.PRODUCT_ID, "740");
			childNode.setProperty(CortevaConstant.PRODUCT_NAME, "Accord Concentrate");
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-agrian-product-1");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "740");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.AGRIAN);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get product data for non agrian true.
	 */
	@Test
	public void testGetProductDataForNonAgrianTrue() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en");
			Mockito.when(resolver.resolve("/content/corteva/na/us/en")).thenReturn(currentResource);
			productModel.init();
			Resource res = context.create().resource("/etc/corteva/product");
			Mockito.when(resolver.resolve(CortevaConstant.PRODUCT_NODE_PATH)).thenReturn(res);
			Node parentNode = res.adaptTo(Node.class);
			parentNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			Node childNode = parentNode.addNode("740");
			childNode.setProperty(CortevaConstant.PRODUCT_ID, "740");
			childNode.setProperty(CortevaConstant.PRODUCT_NAME, "Accord Concentrate");
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-nonagrian-product-1");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "");
			pdpParent.setProperty(CortevaConstant.PRODUCT_NAME, "Test Product Name");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			pdpParent.setProperty(CortevaConstant.HIDE_PRODUCT, CortevaConstant.TRUE);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get product data for agrian true.
	 */
	@Test
	public void testGetProductDataForAgrianTrue() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en");
			Mockito.when(resolver.resolve("/content/corteva/na/us/en")).thenReturn(currentResource);
			productModel.init();
			Resource res = context.create().resource("/etc/corteva/product");
			Mockito.when(resolver.resolve(CortevaConstant.PRODUCT_NODE_PATH)).thenReturn(res);
			Node parentNode = res.adaptTo(Node.class);
			parentNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			Node childNode = parentNode.addNode("740");
			childNode.setProperty(CortevaConstant.PRODUCT_ID, "740");
			childNode.setProperty(CortevaConstant.PRODUCT_NAME, "Accord Concentrate");
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-agrian-product-2");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "7810");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.AGRIAN);
			pdpParent.setProperty(CortevaConstant.HIDE_PRODUCT, CortevaConstant.TRUE);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get product data for non agrian false.
	 */
	@Test
	public void testGetProductDataForNonAgrianFalse() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en");
			Mockito.when(resolver.resolve("/content/corteva/na/us/en")).thenReturn(currentResource);
			productModel.init();
			Resource res = context.create().resource("/etc/corteva/product");
			Mockito.when(resolver.resolve(CortevaConstant.PRODUCT_NODE_PATH)).thenReturn(res);
			Node parentNode = res.adaptTo(Node.class);
			parentNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			Node childNode = parentNode.addNode("740");
			childNode.setProperty(CortevaConstant.PRODUCT_ID, "740");
			childNode.setProperty(CortevaConstant.PRODUCT_NAME, "Accord Concentrate");
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-nonagrian-product-2");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "");
			pdpParent.setProperty(CortevaConstant.PRODUCT_NAME, "Test Non Agrian Product 2");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get product data for non agrian non us false.
	 */
	@Test
	public void testGetProductDataForNonAgrianNonUsFalse() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/ca/en");
			Mockito.when(resolver.resolve("/content/corteva/na/ca/en")).thenReturn(currentResource);
			productModel.init();
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-nonagrian-product-3");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "");
			pdpParent.setProperty(CortevaConstant.PRODUCT_NAME, "Test Non Agrian Product 3");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get product data for non agrian non us true.
	 */
	@Test
	public void testGetProductDataForNonAgrianNonUsTrue() {
		try {
			Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/ca/en");
			Mockito.when(resolver.resolve("/content/corteva/na/ca/en")).thenReturn(currentResource);
			productModel.init();
			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
					CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/content/corteva/na/us/en/homepage/products");
			Resource pdpPageResource = context.create()
					.resource("/content/corteva/na/us/en/homepage/products/us-nonagrian-product-3");
			Node pdpParent = pdpPageResource.adaptTo(Node.class);
			pdpParent.setProperty(CortevaConstant.PRODUCT_ID, "");
			pdpParent.setProperty(CortevaConstant.PRODUCT_NAME, "Test Non Agrian Product 3");
			pdpParent.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			pdpParent.setProperty(CortevaConstant.HIDE_PRODUCT, CortevaConstant.TRUE);
			final List<Resource> results = new ArrayList<>();
			results.add(pdpPageResource);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Assert.assertNotNull(productModel.getProductData());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductData()" + e.getMessage());
		}
	}

	/**
	 * Test get locale for internationalization.
	 */
	@Test
	public void testGetLocaleForInternationalization() {
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/homepage/products");
		Mockito.when(resolver.resolve("/content/corteva/na/us/en/homepage/products")).thenReturn(currentResource);
		Assert.assertNotNull(productModel.getLocaleForInternationalization());
	}

	/**
	 * Test get maximum suggestion.
	 */
	@Test
	public void testGetMaximumSuggestion() {
		SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
		Mockito.when(resolver.resolve("/content/corteva/na/us/en/homepage/products")).thenReturn(currentResource);
		Mockito.when(request.getResource()).thenReturn(currentResource);
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/homepage/products");
		Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.MAX_SUGGESTION_LENGTH,
				CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("5");
		Assert.assertNull(productModel.getMaximumSuggestion());
	}

	/**
	 * Test get minimum character.
	 */
	@Test
	public void testGetMinimumCharacter() {
		SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
		Mockito.when(resolver.resolve("/content/corteva/na/us/en/homepage/products")).thenReturn(currentResource);
		Mockito.when(request.getResource()).thenReturn(currentResource);
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/homepage/products");
		Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.MIN_CHAR_LENGTH,
				CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("3");
		Assert.assertNull(productModel.getMinimumCharacter());
	}

	/**
	 * Test get product document base path.
	 */
	@Test
	public void testGetProductDocumentBasePath() {
		Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.PDF_DOCUMENT_ROOT_PATH,
				CortevaConstant.PRODUCT_CONFIG_NAME)).thenReturn("/testpath");
		Assert.assertNotNull(productModel.getProductDocumentBasePath());
	}

	/**
	 * Test get product details servlet path.
	 */
	@Test
	public void testGetProductDetailsServletPath() {
		Assert.assertNotNull(productModel.getProductDetailsServletPath());
	}
}