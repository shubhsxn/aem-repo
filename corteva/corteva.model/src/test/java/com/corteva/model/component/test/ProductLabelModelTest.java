package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
import com.corteva.model.component.models.ProductLabelModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Product Label Model.
 * 
 * @author Sapient
 * 
 */
public class ProductLabelModelTest extends BaseAbstractTest {

	/** The mock base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The mock product label model. */
	@InjectMocks
	private ProductLabelModel productModel;

	/** The current resource. */
	@Mock
	private Resource currentResource;

	/** The page mgr. */
	@Mock
	private PageManager pageMgr;

	/** The page. */
	@Mock
	private Page page;

	/** The properties. */
	@Mock
	private ValueMap properties;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

	/** The request. */
	@Mock
	private SlingHttpServletRequest request;

	/** The mock session. */
	private Session mockSession;

	/**
	 * This method instantiates a new instance of Product Label Model.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		Configuration myServiceConfig;
		myServiceConfig = configAdmin.getConfiguration("com.corteva.core.configurations.ProductConfigurationService");
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(CortevaConstant.PDF_DOCUMENT_ROOT_PATH, "/testpath");
		myServiceConfig.update(props);
		mockSession = getResourceResolver().adaptTo(Session.class);
		baseService = context.getService(BaseConfigurationService.class);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		Node mockPage = mockSession.getRootNode().addNode("/content/corteva/na/us");
		mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
		context.create().resource("/content/corteva/na/us");
		page = context.pageManager().create("/content/corteva/na/us", "en", "/apps/sample/templates/homepage", "en");
		baseService = context.getService(BaseConfigurationService.class);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		Field requestField;
		requestField = productModel.getClass().getDeclaredField("request");
		requestField.setAccessible(true);
		requestField.set(productModel, request);
		Field prodDocField;
		prodDocField = productModel.getClass().getDeclaredField("productDocumentConfig");
		prodDocField.setAccessible(true);
		prodDocField.set(productModel, currentResource);
		Field safetyDocField;
		safetyDocField = productModel.getClass().getDeclaredField("safetyDocumentConfig");
		safetyDocField.setAccessible(true);
		safetyDocField.set(productModel, currentResource);
		Field resolverField;
		resolverField = productModel.getClass().getSuperclass().getDeclaredField("resourceResolver");
		resolverField.setAccessible(true);
		resolverField.set(productModel, resolver);
		Field currentResourceField;
		currentResourceField = productModel.getClass().getDeclaredField("currentResource");
		currentResourceField.setAccessible(true);
		currentResourceField.set(productModel, currentResource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
		Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
		// Mockito.when(page.getProperties()).thenReturn(properties);
		// Mockito.when(properties.get(Mockito.anyString())).thenReturn(CortevaConstant.AGRIAN);
		Mockito.when(request.getResource()).thenReturn(currentResource);
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/test");
		Mockito.when(request.getResourceResolver()).thenReturn(resolver);
		Mockito.when(resolver.resolve(Mockito.anyString())).thenReturn(currentResource);
		// Mockito.when(page.getContentResource()).thenReturn(currentResource);

	}

	/**
	 * Test get product doc item list.
	 */
	@Test
	public void testGetProductDocItemList() {
		Assert.assertNotNull(productModel.getProductDocItemList());
	}

	/**
	 * Test get safety doc item list.
	 */
	@Test
	public void testGetSafetyDocItemList() {
		Assert.assertNotNull(productModel.getSafetyDocItemList());
	}

	/**
	 * Test get product document base path.
	 */
	@Test
	public void testGetProductDocumentBasePath() {
		Assert.assertNull(productModel.getProductDocumentBasePath());
	}

	/**
	 * Test get label finder path.
	 */
	@Test
	public void testGetLabelFinderPath() {
		Mockito.when(resolver.map(request, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);
		Assert.assertNull(productModel.getLabelFinderPath());
	}

	/**
	 * Test get pdp source for false.
	 */
	@Test
	public void testGetPdpSourceFalse() {
		Assert.assertFalse(productModel.getPdpSource());
	}

	/**
	 * Test get pdp source for true.
	 */
	@Test
	public void testGetPdpSourceTrue() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("pdpSrc", "agrian");
			Assert.assertTrue(productModel.getPdpSource());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetPdpSourceTrue()" + e.getMessage());
		}
	}

	/**
	 * Test get product title.
	 */
	@Test
	public void testGetProductTitle() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("productName", "Test Product Name");
			Assert.assertEquals("Test Product Name", productModel.getProductTitle());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetProductDocumentLabel()" + e.getMessage());
		}
	}

	/**
	 * Test get product display date.
	 */
	@Test
	public void testGetProductDisplayDate() {
		Assert.assertNull(productModel.getProductDisplayDate());
	}

	/**
	 * Test get safety display date.
	 */
	@Test
	public void testGetSafetyDisplayDate() {
		Assert.assertNull(productModel.getSafetyDisplayDate());
	}

	/**
	 * Test get locale for internationalization.
	 */
	@Test
	public void testGetLocaleForInternationalization() {
		Mockito.when(currentResource.getPath()).thenReturn("/content/corteva/na/us/en/products");
		Mockito.when(resolver.resolve("/content/corteva/na/us/en/products")).thenReturn(currentResource);
		Assert.assertNotNull(productModel.getLocaleForInternationalization());
	}
	

	/**
	 * Test get Hide Label Finder Url.
	 */
	@Test
	public void testGetHideLabelFinderUrl() {
		Assert.assertFalse(productModel.isHideLabelFinderUrl());
	}
}