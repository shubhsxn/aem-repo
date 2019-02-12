package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Property;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
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
import com.corteva.model.component.models.ProductHeaderModel;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

/**
 * This is a test class for Primary card Model.
 * 
 * @author Sapient
 * 
 */
public class ProductHeaderModelTest extends BaseAbstractTest {
	
	
	/** The experience fragment model. */
	@Mock
	private ProductHeaderModel productHeaderModel;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The current res path. */
	private String currentResPath;
	
	/** The mocked page object. */
	@Mock
    private Page page;
	
	@Mock
	private Resource mockResource; 
	
	@Mock
	private ValueMap valueMap;
	
	@Mock
	private Property property;
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The page property map. */
	Map<String, Object> pagePropMap = new HashMap<>();

	/**
	 * This method instantiates a new instance of Product Header Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest.setServletPath("/content/corteva/corporate");
		currentResPath = "/content/corteva/corporate/product-detail-page/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		context.registerAdapter(Resource.class, Page.class, page);
		
		PageManager pageManager = Mockito.mock(PageManager.class);
		Mockito.when(pageManager.getContainingPage(context.currentResource(currentResPath))).thenReturn(page);
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		productHeaderModel = getRequest().adaptTo(ProductHeaderModel.class);
		
		Configuration myServiceConfig;
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("productHeader", "productHeader_desktop, productHeader_tablet, productHeader_mobile");
		props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
		myServiceConfig.update(props);
		PrivateAccessor.setField(productHeaderModel, "logoImagePath",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(productHeaderModel, "mobileImagePath",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(productHeaderModel, "resourceType",
				"/apps/corteva/components/content/productHeader/v1/productHeader");
		property.setValue(true);
		property.setValue("test");
		valueMap.put("cq:productType", property);
		
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Mockito.when(mockResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(Mockito.anyString(), Mockito.any(Property.class))).thenReturn(property);
		Mockito.when(property.isMultiple()).thenReturn(true);
		Mockito.when(page.getProperties()).thenReturn(createPagePropertyValueMap());
		
		Tag fakeTag = Mockito.mock(Tag.class);			
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle()).thenReturn("Nutrient Management");
		context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
	}
	
	/**
	 * Test method for mobile alt text.
	 */
	@Test
	public void testGetMobileAltText() {
		Assert.assertNull(productHeaderModel.getMobileAltText());
	}
	
	/**
	 * Test get mobile image path.
	 */
	@Test
	public void testGetMobileImagePath() {
		Assert.assertNotNull(productHeaderModel.getMobileImagePath());
	}
	
	/**
	 * Test method for logo alt text.
	 */
	@Test
	public void testGetLogoAltText() {
		Assert.assertNull(productHeaderModel.getLogoAltText());
	}
	
	/**
	 * Test method for logo image path.
	 */
	@Test
	public void testGetLogoImagePath() {
		Assert.assertNotNull(productHeaderModel.getLogoImagePath());
	}

	/**
	 * Test method for product name.
	 */
	@Test
	public void testGetProductName() {
		Assert.assertNotNull(productHeaderModel.getProductName());
	}

	/**
	 * Test method for display product type.
	 */
	@Test
	public void testGetDisplayProductType() {
		Assert.assertNull(productHeaderModel.getDisplayProductType());
	}

	/**
	 * Test method for product type.
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetProductType() throws Exception {
		Field mockResolverTypeField = productHeaderModel.getClass().getDeclaredField("resourceResolver");
		mockResolverTypeField.setAccessible(true);
		mockResolverTypeField.set(productHeaderModel, mockResourceResolver);
		
		Field mockPageTypeField = productHeaderModel.getClass().getDeclaredField("currentPage");
		mockPageTypeField.setAccessible(true);
		mockPageTypeField.set(productHeaderModel, page);
		
		final TagManager mockTagManager = Mockito.mock(TagManager.class);
		Mockito.when(page.getContentResource()).thenReturn(mockResource);
		Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(mockTagManager);
		
		Mockito.when(mockResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(Mockito.anyString(), Mockito.any(Property.class))).thenReturn(property);
		Mockito.when(property.isMultiple()).thenReturn(true);
		
		Tag fakeTag = Mockito.mock(Tag.class);			
		Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTitle()).thenReturn("Nutrient Management");
		
		Assert.assertNotNull(productHeaderModel.getProductType());
	}
	
	/* This method creates temp Page Properties Value Map. */
	private ValueMap createPagePropertyValueMap() {
		pagePropMap.put("cq:productType", "corteva:productTypes/nutrientManagement");
		return new ValueMapDecorator(pagePropMap);
	}

}
