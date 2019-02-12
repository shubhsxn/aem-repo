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

import java.util.List;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.bean.CharacteristicChartBean;
import com.corteva.model.component.bean.ProductTileLinkListBean;
import com.corteva.model.component.models.CharacteristicChart;
import com.day.cq.wcm.api.Page;

/**
 * This is a test class for CharacteristicChartTest Model.
 * 
 * @author Sapient
 * 
 */
public class CharacteristicChartTest extends BaseAbstractTest {

	/** The CharacteristicChart model. */
	@Mock
	private CharacteristicChart characteristicChart;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The CharacteristicChart list. */
	private List<CharacteristicChartBean> sidebarList;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;

	/**
	 * The mocked request.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The current res path. */
	private String currentResPath;

	/** The property. */
	@Mock
	private Property property;
	
	/** The value. */
	@Mock
	private Value value;

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
		characteristicChart = getRequest().adaptTo(CharacteristicChart.class);
		property = Mockito.mock(Property.class);

	}

	@Test
	public void getSidebarList() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue())
					.thenReturn(new StringValue("{\"labelSidebar\":\"test5\",\"valueSidebar\":\"600\"}"));
			sidebarList = characteristicChart.getSidebarList(property);
			Mockito.when(value.getString()).thenReturn("Test");
		} catch (ValueFormatException e) {

		} catch (RepositoryException e) {

		}

		Assert.assertNotNull(characteristicChart.getSidebarList());
	}

}
