package com.corteva.model.component.test;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.models.NoReferenceSearchModel;

/**
 * This is a test class for NoReferenceSearchModel.
 * 
 * @author Sapient
 * 
 */
public class NoReferenceSearchModelTest extends BaseAbstractTest {
	
	/** the NoReferenceSearchModel model */
	@InjectMocks
	private NoReferenceSearchModel noReferenceSearchModel;
	
	/** The resolver. */
	@Mock
	private ResourceResolver resolver;
	
	/** Reflection fields */
	private String feature;
	private String basePath;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		
		Field resolverField;
		resolverField = noReferenceSearchModel.getClass().getSuperclass().getDeclaredField("resourceResolver");
		resolverField.setAccessible(true);
		resolverField.set(noReferenceSearchModel, resolver);
		
		feature = "products";
		Field featureTypeField = noReferenceSearchModel.getClass().getDeclaredField("feature");
		featureTypeField.setAccessible(true);
		featureTypeField.set(noReferenceSearchModel, feature);
		
		basePath = "/content/corteva/na/us/en/homepage/products-and-solutions/crop-protection";
		Field basePathTypeField = noReferenceSearchModel.getClass().getDeclaredField("basePath");
		basePathTypeField.setAccessible(true);
		basePathTypeField.set(noReferenceSearchModel, basePath);
	}

	/**
	 * Test method for getFeatureUrlList() for products.
	 */
	@Test
	public void testGetProductsFeatureUrlList() {
		noReferenceSearchModel.init();
		Assert.assertNotNull(noReferenceSearchModel.getFeatureUrlList());
	}
	
	/**
	 * Test method for getFeatureUrlList() for articles.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testGetArticlesFeatureUrlList() throws Exception {
		feature = "articles";
		Field featureTypeField = noReferenceSearchModel.getClass().getDeclaredField("feature");
		featureTypeField.setAccessible(true);
		featureTypeField.set(noReferenceSearchModel, feature);
		
		basePath = "/content/corteva/corporate/our-homepage/resources/media-center";
		Field basePathTypeField = noReferenceSearchModel.getClass().getDeclaredField("basePath");
		basePathTypeField.setAccessible(true);
		basePathTypeField.set(noReferenceSearchModel, basePath);
		
		noReferenceSearchModel.init();
		Assert.assertNotNull(noReferenceSearchModel.getFeatureUrlList());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.NoReferenceSearchModel#getFeature()}.
	 */
	@Test
	public void testGetFeature() {
		Assert.assertNotNull(noReferenceSearchModel.getFeature());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.NoReferenceSearchModel#getBasePath()}.
	 */
	@Test
	public void testGetBasePath() {
		Assert.assertNotNull(noReferenceSearchModel.getBasePath());
	}

}
