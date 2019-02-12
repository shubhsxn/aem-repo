package com.corteva.model.component.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.ArticleBean;
import com.corteva.model.component.models.ArticleTopicsModel;
import com.corteva.model.component.utils.ArticleUtils;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

/**
 * This is a test class for Accordion Model.
 * 
 * @author Sapient
 * 
 */

public class ArticleUtilsTest extends BaseAbstractTest {

	/** The constant to hold value of default folder path. */
	private static final String TEST_DATA_DEFAULT_FOLDER_PATH = "/content/corteva/corporate/resources";

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The article utils. */
	private ArticleUtils articleUtil;
	/** The mock session. */
	@Mock
	private Session mockSession;
	/**
	 * The mocked query builder.
	 */
	@Mock
	private QueryBuilder queryBuilder;
	/**
	 * The mocked tag object.
	 */
	@Mock
	private Tag tag;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query query;
	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;

	/** The page manger. */
	@Mock
	private PageManager pageManager;

	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver resourceResolver;

	/** The resource. */
	@Mock
	private Resource resource;

	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;

	/** The ValueMap. */
	@Mock
	private ValueMap valueMap;

	/** The property. */
	private Property property;

	private List<ArticleTopicsModel> articletopicList = new ArrayList<>();

	private ArticleTopicsModel articleTopicModel = new ArticleTopicsModel();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		articleUtil = new ArticleUtils();
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		context.registerAdapter(ResourceResolver.class, QueryBuilder.class, queryBuilder);
		context.registerAdapter(Resource.class, Page.class, page);
		String currentResPath = "/content/corteva/na/us/en/home/our-merger/jcr:content";
		ValueMap properties = createPagePropertyValueMap();
		context.create().resource(currentResPath, properties);
		Mockito.when(resource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);
		property = Mockito.mock(Property.class);
		mockRequest.setServletPath("/content/corteva/na/US/en");
		PrivateAccessor.setField(articleTopicModel, "articleFacet", "Blog");
		articletopicList.add(articleTopicModel);
		Mockito.when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
		Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
		Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
				.thenReturn(query);
		final SearchResult searchResult = Mockito.mock(SearchResult.class);
		Mockito.when(query.getResult()).thenReturn(searchResult);
	}

	@Test
	public void testGetDateRangeParamForQueryForAllMonth() {
		String year = "2016";
		String month = "All";
		String index = "1";
		Map<String, String> params = new HashMap<>();
		ArticleUtils.getDateRangeParamForQuery(params, year, month, index);
		Assert.assertEquals("2016-01-01", params.get("1_daterange.lowerBound"));
	}

	@Test
	public void testGetDateRangeParamForQueryForSpecificMonth() {
		Map<String, String> params = new HashMap<>();
		ArticleUtils.getDateRangeParamForQuery(params, "2016", "05", "1");
		Assert.assertEquals("2016-05-01", params.get("1_daterange.lowerBound"));
	}

	@Test
	public void testFindArticleFeedFilterDataMultipleArticleType() {
		String contentPath = TEST_DATA_DEFAULT_FOLDER_PATH;
		List<String> articletype = new ArrayList<>();
		articletype.add("managementGuide");
		articletype.add("blog");
		Resource pageResource = context.create().resource(contentPath);
		final List<Resource> results = new ArrayList<>();
		results.add(pageResource);
		Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
		Iterator<Resource> resultIterator;
		try {
			resultIterator = articleUtil.findArticleFeedFilterData(resourceResolver, contentPath, articletype,
					articletopicList, "OR", "OR", "5", "0", "All", "All");
			Assert.assertEquals(true, resultIterator.hasNext());
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in testFindArticleFeedFilterDataMultipleArticleType(): "
					+ e.getMessage());
		}
	}

	@Test
	public void testFindArticleFeedFilterData() {
		String contentPath = TEST_DATA_DEFAULT_FOLDER_PATH;
		List<String> articletype = new ArrayList<>();
		List<ArticleTopicsModel> articletopic = new ArrayList<>();
		articletype.add("agronomy");
		Resource pageResource = context.create().resource(contentPath);
		final List<Resource> results = new ArrayList<>();
		results.add(pageResource);
		Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
		Iterator<Resource> resultIterator;
		try {
			resultIterator = articleUtil.findArticleFeedFilterData(resourceResolver, contentPath, articletype,
					articletopic, "OR", "OR", "5", "0", "All", "All");
			Assert.assertEquals(true, resultIterator.hasNext());
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in testFindArticleFeedFilterData(): " + e.getMessage());
		}

	}

	@Test
	public void testGetArticleTypePropertyList() {
		try {
			String[] articleType = new String[2];
			articleType[0] = "blog";
			Mockito.when(property.getValue()).thenReturn(new StringValue(articleType[0]));
			List<String> arrList = articleUtil.getArticleTypePropertyList(property);
			Assert.assertEquals("blog", arrList.get(0));
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in testGetArticleTypePropertyList(): " + e.getMessage());
		}
	}

	@Test
	public void testGetArticlePageData() {
		ArticleBean bean = new ArticleBean();
		Mockito.when(page.getProperties()).thenReturn(createPagePropertyValueMap());
		resource = Mockito.mock(Resource.class);
		Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);
		pageManager = Mockito.mock(PageManager.class);
		Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(resource);
		Mockito.when(resource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY))
				.thenReturn(CortevaConstant.CORPORATE_TAG);
		Mockito.when(mockRequest.getResource()).thenReturn(resource);
		Mockito.when(resource.getPath()).thenReturn("/content/corteva/na/us/en/homepage");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
		Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);
		articleUtil.getArticlePageData(page, bean, resourceResolver, mockRequest, "articleFilter", baseService,
				"/content/corteva/corporate/resources/articlePage", false);
		Assert.assertEquals("james", bean.getArticleTitle());
	}

	/**
	 * Test setPathToPage() method.
	 */
	@Test
	public void setPathToPage() {
		List<String> list = Mockito.mock(List.class);
		Iterator<String> iterator = Mockito.mock(Iterator.class);
		Mockito.when(list.iterator()).thenReturn(iterator);
		Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
		Mockito.when(iterator.next()).thenReturn("mockArticle");
		Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);
		Page page = Mockito.mock(Page.class);
		Mockito.when(resource.adaptTo(Page.class)).thenReturn(page);
		Mockito.when(page.getProperties()).thenReturn(valueMap);
		Assert.assertNotNull(ArticleUtils.setPathToPage(list, resourceResolver, "2", "mockComponent", mockRequest, baseService, "/content/corteva/na/us/en/homepage", true));
	}
	
	/* This method creates Temp Page PRoperties Value Map. */
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<>();
		pagePropMap.put("pageTitle", "james");
		pagePropMap.put("shortDescription", "short description");
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		pagePropMap.put(CortevaConstant.CQ_LAST_MODIFIED, cal);
		pagePropMap.put("displayDate", cal);
		pagePropMap.put("cq:articleType", (Object) "corteva:content/Blog");
		pagePropMap.put("externalurl", "https://facebook.com");
		pagePropMap.put("externalurllabel", "Facebook");
		return new ValueMapDecorator(pagePropMap);
	}
}
