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
import com.corteva.model.component.bean.FeatureLinkListBean;
import com.corteva.model.component.models.FeatureLinkListModel;

/**
 * This is the test class to test the Feature Link List Model.
 * 
 * @author Sapient
 */
public class FeatureLinkListModelTest extends BaseAbstractTest {

	/** The feature model. */
	@Mock
	private FeatureLinkListModel featureModel;

	/** The feature list. */
	private List<FeatureLinkListBean> featureList;

	/** The property. */
	private Property property;

	/** The feature link list. */
	private List<FeatureLinkListBean> featureLinkList;

	/**
	 * Method to set up the test objects.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		featureModel = new FeatureLinkListModel();
		featureList = new ArrayList<>();
		FeatureLinkListBean featureBean = new FeatureLinkListBean();
		featureBean.setListItemPath("/content/corporate/featureLinkList.html");
		featureBean.setListItemTitle("Test Feature Link List");
		featureList.add(featureBean);
		property = Mockito.mock(Property.class);
		featureLinkList = Mockito.mock(List.class);
	}

	/**
	 * Test case to test for pass condition.
	 */
	@Test
	public void testGetListItemsSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"listItemPath\":\"/content/corporate/featureLinkList\",\"listItemTitle\":\"Test Feature Link List\"}"));
			featureLinkList = featureModel.getListItems(property);
			for (FeatureLinkListBean bean : featureList) {
				for (FeatureLinkListBean beanTest : featureLinkList) {
					Assert.assertEquals(bean.getListItemPath(), beanTest.getListItemPath());
					Assert.assertEquals(bean.getListItemTitle(), beanTest.getListItemTitle());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
		}
	}

	/**
	 * Test case to test for fail condition.
	 */
	@Test
	public void testGetListItemsFail() {
		try {
			Mockito.when(property.getValue()).thenReturn(
					new StringValue("{\"listItemPath\":\"/content/corporate/test\",\"listItemTitle\":\"Test\"}"));
			featureLinkList = featureModel.getListItems(property);
			for (FeatureLinkListBean bean : featureList) {
				for (FeatureLinkListBean beanTest : featureLinkList) {
					Assert.assertNotEquals(bean.getListItemPath(), beanTest.getListItemPath());
					Assert.assertNotEquals(bean.getListItemTitle(), beanTest.getListItemTitle());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
		}
	}
	
	/**
	 * Test section title.
	 */
	@Test
	public void testSectionTitle() {
		Assert.assertNull(featureModel.getSectionTitle());
	}
	
	/**
	 * Test spotlight path.
	 */
	@Test
	public void testSpotlightPath() {
		Assert.assertNotNull(featureModel.getSpotlightPath());
	}
	
	/**
	 * Test spotlight title.
	 */
	@Test
	public void testSpotlightTitle() {
		Assert.assertNull(featureModel.getSpotlightTitle());
	}
	
	/**
	 * Test list title.
	 */
	@Test
	public void testListTitle() {
		Assert.assertNull(featureModel.getListTitle());
	}
}