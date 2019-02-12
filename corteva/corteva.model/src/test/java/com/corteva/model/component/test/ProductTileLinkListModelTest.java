/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.test;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
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
import com.corteva.model.component.bean.ProductTileLinkListBean;
import com.corteva.model.component.models.ProductTileLinkListModel;
import com.day.cq.wcm.api.Page;

import junitx.util.PrivateAccessor;

/**
 * This is the test class to test the Product Tile Link List Model.
 * 
 * @author Sapient
 */
public class ProductTileLinkListModelTest extends BaseAbstractTest {

	/** The product tile link list model. */
	@Mock
	private ProductTileLinkListModel productTileLinkListModel;

	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The product tile image block bean list. */
	private List<ProductTileLinkListBean> productTileBeanList;

	/** The product tile image block list. */
	private List<ProductTileLinkListBean> productTileList;

	/** The property. */
	@Mock
	private Property property;

	/** The value. */
	@Mock
	private Value value;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;

	/** The current res path. */
	private String currentResPath;

	/**
	 * This method instantiates a new instance of Product Tile Link List Sling
	 * Model.
	 */

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest.setServletPath("/content/brevant/corporate");
		currentResPath = "/content/brevant/corporate/product-details/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		context.registerAdapter(Resource.class, Page.class, page);

		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		productTileLinkListModel = getRequest().adaptTo(ProductTileLinkListModel.class);
		property = Mockito.mock(Property.class);

		Configuration myServiceConfig;
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("productTileLinkList",
				"productTileLinkList_desktop, productTileLinkList_tablet, productTileLinkList_mobile");
		props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
		myServiceConfig.update(props);
		PrivateAccessor.setField(productTileLinkListModel, "imageBlocks", property);
		PrivateAccessor.setField(productTileLinkListModel, "resourceType",
				"/apps/corteva/components/content/productTileLinkList/v1/productTileLinkList");

	}

	/**
	 * Test method for Image Block details.
	 */
	@Test
	public void testGetImageBlockDetails() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"imageLogo\":\"/content/dam/brevant/Product_catalog.jpg\",\"logoAltText\":\"Logo Alt Text\",\"imageLogoUrl\":\"/content/dam/dpagco/corteva/na/ca/fr/products/logos/LG_GoldSky_US_TextOnly.png\"}"));
			productTileList = productTileLinkListModel.getImageBlockDetails(property);
			Mockito.when(value.getString()).thenReturn("Test");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(productTileLinkListModel.getImageBlockDetails());
	}
	
	/**
	 * Test method for Mandatory resource section.
	 */
	@Test
	public void testGetResourceSection() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);			
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"resourceLinkType\":\"external\",\"resourceLinkLabel\":\"Brevant Seeds\",\"resourceLinkDestinationUrl\":\"https://brevant.ca\"}"));
			productTileList = productTileLinkListModel.getResourceSection(property);
			Mockito.when(value.getString()).thenReturn("Test");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(productTileLinkListModel.getResourceSection());
	}

	/**
	 * Test method for Image Block details.
	 */
	@Test
	public void testGetResources() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"sectionLabel\":\"Yield data\",\"sectionDescription\":\"Optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor\",\"resourceLinks\":[{\"resourceType\":\"internal\",\"resourceType@Delete\":\"internal\",\"resourceLabel\":\"View Yield data\",\"destinationUrl\":\"/content/dam/dpagco/brevant/na/ca/fr/products/files/DF_Instinct_II_FactSheet.pdf\"}],\"imageLogo\":\"/content/dam/dpagco/corteva/na/ca/fr/products/logos/LG_GoldSky_US_TextOnly.png\",\"logoAltText\":\"Goldsky® herbicide\",\"imageLogoUrl\":\"/content/brevant/na/ca/en/homepage/products-and-solutions/seed-treatments\"}"));
			productTileList = productTileLinkListModel.getResources(property);
			Mockito.when(value.getString()).thenReturn("Test");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(productTileLinkListModel.getImageBlockDetails());
	}

	/**
	 * Test image block title.
	 */
	@Test
	public void testGetImageBlockTitle() {
		Assert.assertNull(productTileLinkListModel.getImageBlockTitle());
	}
}
