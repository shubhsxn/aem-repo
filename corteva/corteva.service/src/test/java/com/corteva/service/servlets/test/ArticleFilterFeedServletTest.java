/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
*/
package com.corteva.service.servlets.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.ArticleFilterFeedServlet;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


/**
 * The is the test class to test the Create Pdf Servlet. /*
 * 
 * @author Sapient
 *
 */
public class ArticleFilterFeedServletTest extends BaseAbstractTest {

	/** The Article feed servlet. */
	private ArticleFilterFeedServlet articleFilterFeedServlet;

	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/** The mock response. */
	@Mock
	private MockSlingHttpServletResponse mockResponse;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	@Mock
	private ResourceResolver resourceResolver;

	@Mock
	private Resource mockResource;

	@Mock
	private Node mockNode;

	@Mock
	private Property property;
	@Mock
	private Value value;

	@Mock
	private Iterator<Resource> iterator;

	@Mock
	private Iterator<Resource> queryIterator;
	
	@Mock
	private Iterable<Resource> iterableRes;
	@Mock
	private QueryBuilder queryBuilder;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query query;
	/** The mock session. */
	@Mock
	private Session mockSession;
	
	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;
	
	/** The page manger. */
	@Mock
	private PageManager pageManager;
	
	/** The tag manger. */
	@Mock
	private TagManager tagManager;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 *
	 * @throws RepositoryException
	 *             that an Repository exception has occurred.
	 */
	@Before
	public void setUp() throws Exception {
		try {
			MockitoAnnotations.initMocks(this);
			articleFilterFeedServlet = new ArticleFilterFeedServlet();
			context.registerInjectActivateService(new BaseConfigurationService());
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
			Mockito.when(mockRequest.getHeader(Mockito.anyString()))
					.thenReturn("http://localhost:4502/content/corporate/resources/article1.html");
			Mockito.when(resourceResolver.map(Mockito.anyString())).thenReturn("content/corporate/resources/article1.html");
			Mockito.when(resourceResolver.resolve(Mockito.any(SlingHttpServletRequest.class),Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(resourceResolver.resolve(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(mockResource.getChildren()).thenReturn(iterableRes);
			Mockito.when(iterableRes.iterator()).thenReturn(iterator);
			Mockito.when(iterator.hasNext()).thenReturn(true);
			Mockito.when(mockResource.adaptTo(Node.class)).thenReturn(mockNode);
			
			pageManager = Mockito.mock(PageManager.class);
			Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
			Mockito.when(pageManager.getContainingPage(mockResource)).thenReturn(page);
			Mockito.when(page.getContentResource()).thenReturn(mockResource);
			//Mockito.when(new HierarchyNodeInheritanceValueMap(mockResource)).thenReturn(inheritanceValueMap);
			//Mockito.when(inheritanceValueMap.getInherited(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn("corporate");
			
			Mockito.when(mockNode.hasProperty(Mockito.anyString())).thenReturn(true);
			Mockito.when(mockNode.getProperty(Mockito.anyString())).thenReturn(property);
			Mockito.when(property.getValue()).thenReturn(value);
			Mockito.when(value.getString()).thenReturn("managementGuide");
			Mockito.when(property.getString()).thenReturn("corteva/components/content/articleFilter/v1/articleFilter");
			articleFilterFeedServlet.bindBaseConfigurationService(baseService);
			StringWriter responseWriter = new StringWriter();
			Mockito.when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
			mockRequest.setResource(resourceResolver
					.getResource("/content/corporate/resources/article1/jcr:content/root/responsivegrid"));
			mockRequest.setServerPort(4502);
			mockRequest.setServletPath("/bin/corteva/articleFilter.managementGuide.2016.05.0.json");
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setup()" + e.getMessage());
		}
	}

		
	/**
	 * Test create get article feed. when page is Media center
	 */
	@Test
	public void testdoGetAllArticleTypeOnMediaCenterPage() {
		try {
			RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			String[] selector = new String[5];
			selector[0] = "blog";
			selector[1] = "all";
			selector[2] = "all";
			selector[3] = "";
			selector[4] = "article1";
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
			Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
			Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(null);
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
			Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
			Mockito.when(queryIterator.next()).thenReturn(mockResource);
			Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
			Mockito.when(baseService.getPropValueFromConfiguration(Mockito.anyString(), Mockito.anyString())).thenReturn("5");
			articleFilterFeedServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
			Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
			articleFilterFeedServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());			
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testdoGetAllArticleTypeOnMediaCenterPage()" + e.getMessage());
		}
	}
	/**
	 * Test create get article feed for incorrect request
	 */
	@Test
	public void testdoGetIncorrectURLFail() {
		try {
			RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			String[] selector = new String[2];
			selector[0] = "agronomy";
			selector[1] = "";
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
			Mockito.when(mockResource.getPath()).thenReturn("/content/corporate/resources/article2/");
			
			Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
			Mockito.when(baseService.getPropValueFromConfiguration(Mockito.anyString(), Mockito.anyString()))
					.thenReturn("5");
			articleFilterFeedServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testdoGetIncorrectURLFail()" + e.getMessage());
		}
	}

	/**
	 * Test create get article feed.
	 */
	@Test
	public void testdoGet() {
		try {
			RequestPathInfo requestPathInfo = Mockito.mock(RequestPathInfo.class);
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			String[] selector = new String[5];
			selector[0] = "all";
			selector[1] = "all";
			selector[2] = "blog";
			selector[3] = "";
			selector[4] = "article1";
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
			Mockito.when(baseService.getToggleInfo(CortevaConstant.ARTICLE_FILTER_FEED_SERVLET,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME)).thenReturn(true);
			Mockito.when(baseService.getPropValueFromConfiguration(CortevaConstant.COMPONENT_CONFIG_NAME,
					"noOfArticlesToDisplay")).thenReturn("MockNoOfArticles");
			Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/corporate/article1");
			Mockito.when(mockResource.getName()).thenReturn(selector[4], selector[3]);
			Mockito.when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			
			Map<String, String> params = new HashMap<>();
			params.put(PathPredicateEvaluator.PATH, "/content/corteva/corporate/article1");
			params.put("1_property", CortevaConstant.SLING_RESOURCE_TYPE);
			params.put("1_property.value", "corteva/components/content/articleFilter/v1/articleFilter");
			params.put("p_limit", "-1");
			
			Mockito.when(queryBuilder.createQuery(PredicateGroup.create(params),
					resourceResolver.adaptTo(Session.class))).thenReturn(query);
			final SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(searchResult.getResources()).thenReturn(queryIterator);
			Mockito.when(queryIterator.next()).thenReturn(null);
			articleFilterFeedServlet.doGet(mockRequest, mockResponse);
			Assert.assertNotEquals(500, mockResponse.getStatus());
		} catch (ServletException | IOException e) {
			Assert.fail("Exception occurred in testdoGet()" + e.getMessage());
		}
	}
	
	/**
	 * Test unbind Base Service
	 */
	@Test
	public void unbindBaseService() {
		articleFilterFeedServlet.unbindBaseConfigurationService(baseService);
	}
}