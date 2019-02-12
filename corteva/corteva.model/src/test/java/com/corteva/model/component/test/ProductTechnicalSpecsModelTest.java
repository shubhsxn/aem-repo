package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.ProductTechnicalSpecsItemModel;
import com.corteva.model.component.models.ProductTechnicalSpecsModel;

/**
 * This is a test class for ProductTechnicalSpecsModel Model.
 * 
 * @author Sapient
 * 
 */
public class ProductTechnicalSpecsModelTest extends BaseAbstractTest {
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The mock request. */
	@Mock
	private SlingHttpServletRequest request;
	
	/** The mock resource. */
	@Mock
	private Resource resource;
	
	/** The mock iterable for resource. */
	@Mock
	private Iterable<Resource> iterable;
	
	/** The mock iterator for resource. */
	@Mock
	private Iterator<Resource> iterator;
	
	/** the ProductTechnicalSpecsModel model */
	@InjectMocks
	private ProductTechnicalSpecsModel productTechnicalSpecsModel;
	
	/** the ProductTechnicalSpecsItemModel model */
	@Mock
	private ProductTechnicalSpecsItemModel productTechnicalSpecsItemModel;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		
		productTechnicalSpecsModel.setBaseService(baseService);
		
		Field mockResourceTypeField = productTechnicalSpecsModel.getClass().getDeclaredField("productSpecs");
		mockResourceTypeField.setAccessible(true);
		mockResourceTypeField.set(productTechnicalSpecsModel, resource);
		
		Field mockRequestTypeField = productTechnicalSpecsModel.getClass().getDeclaredField("slingRequest");
		mockRequestTypeField.setAccessible(true);
		mockRequestTypeField.set(productTechnicalSpecsModel, request);
		
		Mockito.when(resource.getChildren()).thenReturn(iterable);
		Mockito.when(iterable.iterator()).thenReturn(iterator);
		Mockito.when(iterator.hasNext()).thenReturn(true, false);
		Mockito.when(iterator.next()).thenReturn(resource);
		Mockito.when(resource.adaptTo(ProductTechnicalSpecsItemModel.class)).thenReturn(productTechnicalSpecsItemModel);
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsModel#getProductSpecs()}.
	 */
	@Test
	public void testGetProductSpecs() {
		Assert.assertNotNull(productTechnicalSpecsModel.getProductSpecs());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsModel#getProductSpecsItemList()}.
	 */
	@Test
	public void testGetProductSpecsItemList() {
		Assert.assertNotNull(productTechnicalSpecsModel.getProductSpecsItemList());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsModel#getYoutubeApi()}.
	 */
	@Test
	public void testGetYoutubeApi() {
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("test");
		Assert.assertEquals("test", productTechnicalSpecsModel.getYoutubeApi());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsModel#getYoutubeKey()}.
	 */
	@Test
	public void testGetYoutubeKey() {
		Mockito.when(baseService.getPropConfigValue(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString(),
				Mockito.anyString())).thenReturn("youtubeKey");
		Assert.assertEquals("youtubeKey", productTechnicalSpecsModel.getYoutubeKey());
	}

}
