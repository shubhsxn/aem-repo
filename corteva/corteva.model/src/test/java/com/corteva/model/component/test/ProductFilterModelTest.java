package com.corteva.model.component.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
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
import com.corteva.model.component.bean.ProductFilterBean;
import com.corteva.model.component.models.ProductFilterModel;
import com.day.cq.wcm.api.Page;

/**
 * This is a test class for Accordion Model.
 * 
 * @author Sapient
 */
public class ProductFilterModelTest extends BaseAbstractTest {

	/** The Constant CONTENT_PATH. */
	private static final String CONTENT_PATH = "/content/corteva/na/us/en/resources/testpage/jcr:content";
	/** The experience fragment model. */
	@InjectMocks
	private ProductFilterModel productFilterModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The mock resource. */
	@Mock
	private Resource mockResource;

	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The mock request. */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The mock request. */
	@Mock
	private RequestPathInfo requestPathInfo;
	
	@Mock
	private Page currentPage;

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest = getRequest();
		ValueMap properties = createPagePropertyValueMap();
		context.create().resource(CONTENT_PATH, properties);
		mockRequest.setResource(context.currentResource(CONTENT_PATH));
		mockRequest.setServletPath(CONTENT_PATH);
		mockResource = getContext().currentResource();
		productFilterModel = getRequest().adaptTo(ProductFilterModel.class);
		Mockito.mock(Property.class);
	}

	/**
	 * Creates the page property value map.
	 * 
	 * @return the value map
	 */
	/* This method creates Temp Page PRoperties Value Map. */
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<String, Object>();
		String[] str = { "", "" };
		pagePropMap.put("productListTags", str);
		pagePropMap.put("variationType", "showFilter");
		pagePropMap.put("cropFilter", "true");
		pagePropMap.put("stateFilter", "true");
		pagePropMap.put("pestsFilter", "true");
		pagePropMap.put("weedsFilter", "true");
		pagePropMap.put("diseaseFilter", "true");
		pagePropMap.put("useSiteFilter", "true");
		pagePropMap.put("marketFilter", "true");
		pagePropMap.put("productNameFilter", "true");
		pagePropMap.put("productTypeFilter", "true");
		pagePropMap.put("productTypeSequentialFilter", "true");
		pagePropMap.put("productTypeSubFilter", "true");
		pagePropMap.put("productTypeSubSequentialFilter", "true");

		return new ValueMapDecorator(pagePropMap);
	}

	/**
	 * Test get servlet path.
	 */
	@Test
	public void testGetServletPath() {
		String servletPath = productFilterModel.getServletPath();
		Assert.assertEquals((CONTENT_PATH + ".product.JSON").replace(CortevaConstant.JCR_CONTENT, "_jcr_content"),
				servletPath);
	}
	
	/**
	 * Test get pagination url.
	 * @throws Exception
	 */
	@Test
	public void testGetPaginationUrl() throws Exception {
		
		mockRequest = Mockito.mock(MockSlingHttpServletRequest.class);
		requestPathInfo = Mockito.mock(RequestPathInfo.class);
		Field requestField;
		requestField = productFilterModel.getClass().getDeclaredField("slingRequest");
		requestField.setAccessible(true);
		requestField.set(productFilterModel, mockRequest);
		Field resolverField;
		resolverField = productFilterModel.getClass().getDeclaredField("resourceResolver");
		resolverField.setAccessible(true);
		resolverField.set(productFilterModel, mockResourceResolver);
		Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(requestPathInfo.getSelectors()).thenReturn(new String[] {"1"});
		currentPage = Mockito.mock(Page.class);
		Field pageField;
		pageField = productFilterModel.getClass().getDeclaredField("currentPage");
		pageField.setAccessible(true);
		pageField.set(productFilterModel, currentPage);
		Mockito.when(currentPage.getPath()).thenReturn("/content/corteva/na/us/en/homepage/product-finder");
		
		String paginationUrl = productFilterModel.getPaginationUrl();
		Assert.assertNull(paginationUrl);
	}

	/**
	 * Test get accordion list item for icon fail.
	 */
	@Test
	public void testGetAccordionListItemForIconFail() {
		ProductFilterBean productList;
			productList = productFilterModel.getProductList();
			Assert.assertNotNull(productList);
	}
}