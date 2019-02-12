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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.value.StringValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.bean.AnchorLinkBean;
import com.corteva.model.component.models.AnchorLinksNavigationModel;

/**
 * This is the test class to test the Anchor Navigation Model.
 * 
 * @author Sapient
 */
public class AnchorLinksNavigationModelTest extends BaseAbstractTest {

	/** The anchor nav model. */
	@Mock
	private AnchorLinksNavigationModel anchorNavModel;

	/** The anchor bean list. */
	private List<AnchorLinkBean> anchorBeanList;

	/** The property. */
	private Property property;

	/** The anchor list. */
	private List<AnchorLinkBean> anchorList;

	/**
	 * Method to set up the test objects.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		anchorNavModel = new AnchorLinksNavigationModel();
		anchorBeanList = new ArrayList<>();
		AnchorLinkBean anchorBean = new AnchorLinkBean();
		anchorBean.setAnchorLinkLabel("Test");
		anchorBean.setAnchorNodeName("Test_1");// Sample Node Name.
		anchorBeanList.add(anchorBean);
		property = Mockito.mock(Property.class);
		anchorList = Mockito.mock(List.class);
	}

	/**
	 * Test case to test for pass condition.
	 */
	@Test
	public void testGetAnchorLinksContainerSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(
					new StringValue("{\"anchorLinkLabel\":\"Test\",\"anchorLinkLabelHidden\":\"Test_1\"}"));
			anchorList = anchorNavModel.getAnchorLinksContainer(property);
			for (AnchorLinkBean bean : anchorBeanList) {
				for (AnchorLinkBean beanTest : anchorList) {
					Assert.assertEquals(bean.getAnchorLinkLabel(), beanTest.getAnchorLinkLabel());
					Assert.assertEquals(bean.getAnchorNodeName(), beanTest.getAnchorNodeName());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAnchorLinksContainerSuccess(): " + e.getMessage());
		}
	}

	/**
	 * Test case to test for fail condition.
	 */
	@Test
	public void testGetAnchorLinksContainerFail() {
		try {
			Mockito.when(property.getValue()).thenReturn(
					new StringValue("{\"anchorLinkLabel\":\"Test 1\",\"anchorLinkLabelHidden\":\"Test1_1\"}"));
			anchorList = anchorNavModel.getAnchorLinksContainer(property);
			for (AnchorLinkBean bean : anchorBeanList) {
				for (AnchorLinkBean beanTest : anchorList) {
					Assert.assertNotEquals(bean.getAnchorLinkLabel(), beanTest.getAnchorLinkLabel());
					Assert.assertNotEquals(bean.getAnchorNodeName(), beanTest.getAnchorNodeName());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAnchorLinksContainerFail(): " + e.getMessage());
		}
	}

	/**
	 * Test case to test Anchor Link Container when View Type is InPageNavigation
	 */
	@Test
	public void testGetAnchorLinksContainerInPageNavSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(
					new StringValue("{\"anchorLinkLabel\":\"Test\",\"anchorLinkLabelHidden\":\"Test_1\"}"));
			Field viewTypeField = anchorNavModel.getClass().getDeclaredField("viewType");
			viewTypeField.setAccessible(true);
			viewTypeField.set(anchorNavModel, "inPageNavigation");
			Field sectionsField = anchorNavModel.getClass().getDeclaredField("sections");
			sectionsField.setAccessible(true);
			sectionsField.set(anchorNavModel, property);
			anchorList = anchorNavModel.getAnchorLinksContainer();
			for (AnchorLinkBean bean : anchorBeanList) {
				for (AnchorLinkBean beanTest : anchorList) {
					Assert.assertEquals(bean.getAnchorLinkLabel(), beanTest.getAnchorLinkLabel());
					Assert.assertEquals(bean.getAnchorNodeName(), beanTest.getAnchorNodeName());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAnchorLinksContainerInPageNavSuccess(): " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Assert.fail("IllegalArgumentException occurred in testGetAnchorLinksContainerInPageNavSuccess(): "
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.fail("IllegalAccessException occurred in testGetAnchorLinksContainerInPageNavSuccess(): "
					+ e.getMessage());
		} catch (NoSuchFieldException e) {
			Assert.fail(
					"NoSuchFieldException occurred in testGetAnchorLinksContainerInPageNavSuccess(): " + e.getMessage());
		} catch (SecurityException e) {
			Assert.fail(
					"SecurityException occurred in testGetAnchorLinksContainerInPageNavSuccess(): " + e.getMessage());
		}
	}
}