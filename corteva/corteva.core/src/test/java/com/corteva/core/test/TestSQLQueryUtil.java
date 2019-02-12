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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.utils.SQLQueryUtil;
import com.corteva.core.utils.SearchInput;

/**
 * This is test class to test the AEM utils.
 */
public class TestSQLQueryUtil extends BaseAbstractTest {

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	/** The resource. */
	@Mock
	private Resource resource;
	
	/** The session. */
	@Mock
	private Session session;
	
	/** The workspace. */
	@Mock
	private Workspace workspace;
	
	/** The queryManager. */
	@Mock
	private QueryManager queryManager;
	
	/** The query. */
	@Mock
	private Query query;
	
	/** The result. */
	@Mock
	private QueryResult result;
	
	/** The node iterator. */
	@Mock
	private NodeIterator nodeItr;
	
	/** The node. */
	@Mock
	private Node node;

	/**
	 * Method to set up the test objects.
	 * @throws Exception 
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		String nodePath = "/content/corteva/na/us/en/productNode";
		Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		Mockito.when(session.getWorkspace()).thenReturn(workspace);
		Mockito.when(workspace.getQueryManager()).thenReturn(queryManager);
		Mockito.when(queryManager.createQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(query);
		Mockito.when(query.execute()).thenReturn(result);
		Mockito.when(result.getNodes()).thenReturn(nodeItr);
		Mockito.when(nodeItr.getSize()).thenReturn(Long.parseLong("1"));
		Mockito.when(nodeItr.hasNext()).thenReturn(true, false);
		Mockito.when(nodeItr.nextNode()).thenReturn(node);
		Mockito.when(node.getPath()).thenReturn(nodePath);
		Mockito.when(resourceResolver.getResource(nodePath)).thenReturn(resource);
	}

	/**
	 * Test get JSON List From Property.
	 */
	@Test
	public void testGetJSONListFromProperty() {
		SearchInput searchInput = new SearchInput();
		searchInput.setRootPath(
				"/content/corteva/na/us/en/homepage/products-and-solutions");
		searchInput.setType("corteva/components/structure/pdp");
		List<String> tagList = new ArrayList<>();
		tagList.add("corteva:productTypes/cropProtection");
		tagList.add("corteva:productTypes/seeds");
		searchInput.setTagList(tagList);
		searchInput.setOffset(10);
		Iterator<Resource> documents = SQLQueryUtil
				.getDocuments(resourceResolver, searchInput);

		Assert.assertNotNull(documents);
	}

}