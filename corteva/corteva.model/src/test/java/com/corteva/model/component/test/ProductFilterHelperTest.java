package com.corteva.model.component.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.utils.SearchInput;
import com.corteva.model.component.utils.ProductFilterHelper;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Product Filter Helper.
 * 
 * @author Sapient
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductFilterHelperTest extends BaseAbstractTest {
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The search input. */
	@Mock
	private SearchInput searchInput;
	
	/** The mocked request object. */
	@Mock
	private SlingHttpServletRequest mockRequest;
	
	/** The mocked request path info. */
	@Mock
	private RequestPathInfo requestPathInfo;
	
	@Mock
	private ProductFilterHelper productFilterHelper;
	
	/** The mocked resource resolver object. */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	@Mock
	private Resource mockResource;
	
	@Mock
	private Resource mockPageResource;
	
	/** The mocked tagmanager object. */
	@Mock
	private TagManager tagManager;
	
	/** The valueMap. */
	@Mock
	private ValueMap valueMap;
	
	/** The PageManager. */
	@Mock
	private PageManager pageManager;
	
	/** The Session. */
	@Mock
	private Session session;
	
	/** The Workspace. */
	@Mock
	private Workspace workspace;
	
	/** The QueryManager. */
	@Mock
	private QueryManager queryManager;

	/** The Query. */
	@Mock
	private Query query;
	
	/** The QueryResult. */
	@Mock
	private QueryResult result;
	
	/** The NodeIterator. */
	@Mock
	private NodeIterator nodeItr;
	
	/** The Node. */
	@Mock
	private Node node;
	
	/** The Property. */
	@Mock
	private Property property;
	
	/** The Page. */
	@Mock
	private Page page;
	
	/** The Tag Iterator. */
	@Mock
	private Iterator<Tag> mockItr;
	
	/** The Resource Iterator. */
	@Mock
	private Iterator<Resource> resourceItr;
	
	/**
	 * This method instantiates a new instance of Product Filter Helper.
	 * @throws RepositoryException 
	 * @throws PathNotFoundException 
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		baseService = new BaseConfigurationService();
		productFilterHelper = new ProductFilterHelper();
		valueMap = createValueMap();
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/test");
		Mockito.when(mockResource.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
		Tag fakeTag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.any())).thenReturn(fakeTag);
		Mockito.when(fakeTag.getTagID()).thenReturn("tagId");
		Mockito.when(fakeTag.listChildren()).thenReturn(mockItr);
		Mockito.when(mockItr.hasNext()).thenReturn(true, false);
		Mockito.when(mockItr.next()).thenReturn(fakeTag);
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(mockResourceResolver.adaptTo(Session.class)).thenReturn(session);
		Mockito.when(session.getWorkspace()).thenReturn(workspace);
		Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
		Mockito.when(requestPathInfo.getExtension()).thenReturn("html");
		Mockito.when(requestPathInfo.getSelectors()).thenReturn(new String[] {"2"});
		try {
			Mockito.when(workspace.getQueryManager()).thenReturn(queryManager);
			Mockito.when(queryManager.createQuery(Mockito.anyString(), Mockito.anyString())).thenReturn(query);
			Mockito.when(query.execute()).thenReturn(result);
			Mockito.when(result.getNodes()).thenReturn(nodeItr);
			Mockito.when(nodeItr.hasNext()).thenReturn(false);
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}
	
	/* This method creates Page Properties Value Map. */
	private ValueMap createValueMap() {
		Map<String, Object> map = new HashMap<>();
		String[] productListTags = { "Seeds", "Land Management" };
		map.put("productListTags", productListTags);
		map.put("pdpSrc", "agrian");
		map.put("productId", "123");
		return new ValueMapDecorator(map);
	}
	
	/**
	 * Test method to get product filter list.
	 */
	@Test
	public void testGetProductFilterList() {
		Assert.assertNotNull(ProductFilterHelper.getProductFilterList(mockRequest, mockResource, baseService));
	}
}
