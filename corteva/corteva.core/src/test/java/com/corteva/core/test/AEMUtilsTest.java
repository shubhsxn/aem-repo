/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.test;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.utils.AEMUtils;

/**
 * This is test class to test the AEM utils.
 * @param <E>
 */
public class AEMUtilsTest extends BaseAbstractTest {

	/** The aem utils. */
	private AEMUtils aemUtils;

    /** The property. */
    private Property property;
    
    /** The Node. */
    private Node currentNode;
    
    /** The value. */
    private Value value;
    
    /** The resource. */
    private Resource resource;
    
    /** The class. */
    private Class type;
    
    /** The iterable for resource. */
    private Iterable<Resource> iterable;
    
    /** The iterator for resource. */
    private Iterator<Resource> iterator;

    /**
     * Method to set up the test objects.
     */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		aemUtils = new AEMUtils();
        property = Mockito.mock(Property.class);
        value = Mockito.mock(Value.class);
        resource = Mockito.mock(Resource.class);
        type = Class.class;
        iterable = Mockito.mock(Iterable.class);
        iterator = Mockito.mock(Iterator.class);
	}

	/**
	 * Test get JSON List From Property.
	 */
	@Test
	public void testGetJSONListFromProperty() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"anchorLinkTitle\":\"Test\"}"));
			Mockito.when(value.getString()).thenReturn("Test");
			Assert.assertNotNull(aemUtils.getStringfromProperty(property));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetJSONListFromProperty()" + e.getMessage());
		}
	}
	
	/**
	 * Test get JSON List From Property.
	 */
	@Test
	public void testGetStringfromProperty() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"anchorLinkTitle\":\"Test\"}"));
			Mockito.when(value.getString()).thenReturn("Test");
			Assert.assertNotNull(aemUtils.getJSONListfromProperty(property));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetStringfromProperty()" + e.getMessage());
		}
	}
	
	/**
	 * Test get JSON List From MultiField JSON Property.
	 */
	@Test
	public void testGetListFromMultiFieldJSONProperty() {
		try {
			Value[] valueArr= new Value[2];
			valueArr[0] =new StringValue("{\"articlePagePath\":\"/content/corteva/corporate/resources/article-page/article3\"}");
			valueArr[1] =new StringValue("{\"articlePagePath\":\"/content/corteva/corporate/resources/article-page/article4\"}");
			Mockito.when(property.isMultiple()).thenReturn(true);
			Mockito.when(property.isMultiple()).thenReturn(true);
			Mockito.when(property.getValues()).thenReturn(valueArr);			
			Assert.assertNotNull(aemUtils.getListFromMultiFieldJSONProperty(property, "articlePagePath"));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetListFromMultiFieldJSONProperty()" + e.getMessage());
		}
	}
	
	/**
	 * Test get MultiField Items List.
	 */
	@Test
	public void testGetMultifieldItemList() {
		Mockito.when(resource.getChildren()).thenReturn(iterable);
		Mockito.when(iterable.iterator()).thenReturn(iterator);
		Mockito.when(iterator.hasNext()).thenReturn(true, false);
		Mockito.when(iterator.next()).thenReturn(resource);
		Mockito.when(resource.adaptTo(type)).thenReturn(type);
		Assert.assertNotNull(AEMUtils.getMultifieldItemList(resource, type));
	}
	
	/**
	 * Test get Node Property As String.
	 */

	@Test
	public void testGetNodePropertyAsString() {
		String testPageProperty = "fileReference";
		currentNode = Mockito.mock(Node.class);
		try {
		Mockito.when(currentNode.hasProperty(testPageProperty)).thenReturn(true);
		Mockito.when(currentNode.getProperty(testPageProperty)).thenReturn(property);
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetNodePropertyAsString()" + e.getMessage());
		}
	}
	
}