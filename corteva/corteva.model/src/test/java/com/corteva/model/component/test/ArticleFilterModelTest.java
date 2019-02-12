package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.value.StringValue;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ArticleFilterModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * This is a test class for Accordion Model.
 * 
 * @author Sapient
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ArticleFilterModelTest extends BaseAbstractTest {



	private static final String TEST_DATA_ARTICLE_TYPE = "agronomy";

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

    /** The config admin. */
    @Mock
    private ConfigurationAdmin configAdmi;

    /** The dictionary. */
    @Mock
    private Dictionary<String, Object> dict;

    /** The config. */
    @Mock
    private Configuration config;
	@Mock
	private Page mockPage;
	/**
	 * The mocked query builder.
	 */
	@Mock
	private QueryBuilder mockQueryBuilder;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query mockQuery;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Hit hit;
	/**
	 * The mocked request object.
	 */
	@Mock
	private SlingHttpServletRequest mockRequest;
	
	/** The mocked request path info. */
	@Mock
	private RequestPathInfo requestPathInfo;
	
	/** The mocked current resource. */
	@Mock
	private Resource currentResource;

	/** The experience fragment model. */
	@InjectMocks
	private ArticleFilterModel articalFilterModel =new ArticleFilterModel(mockRequest);
	@Mock
	private ResourceResolver mockResourceResolver;
	@Mock
	private Resource mockResource;	
	@Mock
	private Iterator<Resource> mockIterator;
	
	@Mock
	private Property articleType;
		
	@Mock
	private Value value;
	
	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;
	/**
	 * The mocked tag object.
	 */
	@Mock
	private Tag tag;
	
	/** The mock session. */
	@Mock
	private Session mockSession;
	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 * @throws Exception 
	 */
	@Before
	public void setUp() throws Exception {
		try {
			MockitoAnnotations.initMocks(this);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			String[] articleTypeArr = new String[2];
			articleTypeArr[0] = TEST_DATA_ARTICLE_TYPE;					
			final TagManager mockTagManager = Mockito.mock(TagManager.class);	
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			context.registerAdapter(ResourceResolver.class, QueryBuilder.class, mockQueryBuilder);
			
			Field viewTypeField;
			viewTypeField = articalFilterModel.getClass().getDeclaredField("articleType");
			viewTypeField.setAccessible(true);
			viewTypeField.set(articalFilterModel,articleType);
			Field folderPath;
			folderPath = articalFilterModel.getClass().getDeclaredField("folderPath");
			folderPath.setAccessible(true);
			folderPath.set(articalFilterModel,"/content/corteba/corporate/resources");
			Field displayFilterSection;
			displayFilterSection = articalFilterModel.getClass().getDeclaredField("displayFilterSection");
			displayFilterSection.setAccessible(true);
			displayFilterSection.set(articalFilterModel,true);
			Field displayDateSection;
			displayDateSection = articalFilterModel.getClass().getDeclaredField("displayDateFilter");
			displayDateSection.setAccessible(true);
			displayDateSection.set(articalFilterModel,true);
			Field displayArticleTypeFilter;
			displayArticleTypeFilter = articalFilterModel.getClass().getDeclaredField("displayArticleTypeFilter");
			displayArticleTypeFilter.setAccessible(true);
			displayArticleTypeFilter.set(articalFilterModel,true);
			Mockito.when(articleType.getValue()).thenReturn(new StringValue(TEST_DATA_ARTICLE_TYPE));
			Mockito.when(baseService.getPropValueFromConfiguration(Mockito.anyString(), Mockito.anyString())).thenReturn("5");
			Field baseServiceField;
			baseServiceField = articalFilterModel.getClass().getDeclaredField("baseService");
			baseServiceField.setAccessible(true);
			baseServiceField.set(articalFilterModel,baseService);
			
			Mockito.when(mockResourceResolver.adaptTo(QueryBuilder.class)).thenReturn(mockQueryBuilder);
			Mockito.when(mockResourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
			Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
			Mockito.when(tagManager.resolve("/etc/tags/corteva/contentTypes/article/agronomy")).thenReturn(tag);
			Mockito.when(tag.getTitle()).thenReturn(TEST_DATA_ARTICLE_TYPE);
			Mockito.when(mockQueryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(mockQuery);
			final SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(mockQuery.getResult()).thenReturn(searchResult);
			Mockito.when(mockQuery.getResult().getResources()).thenReturn(mockIterator);
			Mockito.when(mockIterator.hasNext()).thenReturn(true,true,true,true,true,true,false);
			Mockito.when(mockIterator.next()).thenReturn(mockResource);
			Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
			Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
			Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
			Mockito.when(requestPathInfo.getSelectors()).thenReturn(new String[] {"2"});
			Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/resources/i");
			ValueMap properties = createPagePropertyValueMap();
			Mockito.when(mockPage.getProperties()).thenReturn(properties);
			articleType = Mockito.mock(Property.class);	
			
			context.registerInjectActivateService(new BaseConfigurationService());
	 
			 value = Mockito.mock(Value.class);
		} catch (ValueFormatException e) {
			Assert.fail("ValueFormatException occurred in setUp(): " + e.getMessage());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in setUp(): " + e.getMessage());
		} 
	}
	
	@Test
	public void testInit() {

			List<String> articletypeList= new ArrayList<>();
			articletypeList.add(TEST_DATA_ARTICLE_TYPE);
			
			articalFilterModel.init();
			Assert.assertEquals(true, articalFilterModel.getShowLoadMoreButton());
			Assert.assertNotNull(articalFilterModel.getArticleBeanList());
			Assert.assertNotNull(articalFilterModel.getArticleBeanListJSON());
			Assert.assertNotNull(articalFilterModel.getDateFilterJSON());
	}

	
	@Test
	public void testGetShowLoadMoreButton() {
		Assert.assertNotNull(articalFilterModel.getShowLoadMoreButton());
	}
	/* This method creates Temp Page Properties Value Map.*/ 
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());		
		pagePropMap.put(CortevaConstant.CQ_LAST_MODIFIED, cal);
		pagePropMap.put("displayDate", cal);
		pagePropMap.put("cq:articleType", (Object) "corteva:content/Blog");
		return new ValueMapDecorator(pagePropMap);
	}
	
	@Test
	public void testPaginationUrl() throws Exception {
		Field currentResourceField;
		currentResourceField = articalFilterModel.getClass().getDeclaredField("currentResource");
		currentResourceField.setAccessible(true);
		currentResourceField.set(articalFilterModel, currentResource);
		Assert.assertNull(articalFilterModel.getPaginationUrl());
	}
	
	@Test
	public void testGetArticleTypeFilterJSON() throws Exception {
		List<String> articleTypeArr = new ArrayList<>();
		articleTypeArr.add(TEST_DATA_ARTICLE_TYPE);
		Field articleTypeArrField;
		articleTypeArrField = articalFilterModel.getClass().getDeclaredField("articleTypeArr");
		articleTypeArrField.setAccessible(true);
		articleTypeArrField.set(articalFilterModel, articleTypeArr);
		Assert.assertNotNull(articalFilterModel.getArticleTypeFilterJSON());
	}
	
	@Test
	public void testFolderPath() {
		Assert.assertEquals("/content/corteba/corporate/resources", articalFilterModel.getFolderPath());
	}

	@Test
	public void testDisplayDateFilter() {
		Assert.assertTrue(articalFilterModel.getDisplayDateFilter());
	}
	
	@Test
	public void testDisplayFilterSection() {
		Assert.assertTrue(articalFilterModel.getDisplayFilterSection());
	}
	
	@Test
	public void testDisplayArticleTypeFilter() {
		Assert.assertTrue(articalFilterModel.getDisplayArticleTypeFilter());
	}
	
	@Test
	public void testNoOfArticlesToDisplay() {
		Assert.assertNull(articalFilterModel.getNoOfArticlesToDisplay());
	}

}
