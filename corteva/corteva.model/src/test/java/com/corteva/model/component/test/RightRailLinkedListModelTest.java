package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.SlingHttpServletRequest;
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

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.RightRailLinkedListModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;


/**
 * This is a test class for Right Rail Linked LIst Model.
 * 
 * @author Sapient
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class RightRailLinkedListModelTest extends BaseAbstractTest {

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	@Mock
	private Page page;
	/**
	 * The mocked query builder.
	 */
	@Mock
	private QueryBuilder queryBuilder;
	/**
	 * The mocked query object.
	 */
	@Mock
	private Query query;
	
	@Mock
	private ResourceResolver resourceResolver;
	
	/**
	 * The mocked query object.
	 */
	@Mock
	private Hit hit;
	/** The  mocked Request. */
	@Mock
	private SlingHttpServletRequest mockReq;
	/** The experience fragment model. */
	@InjectMocks
	private RightRailLinkedListModel rightRailLinkedListModel = new RightRailLinkedListModel(mockReq);

	
	@Mock
	private Property articleType;

	@Mock
	private Property articlePagePaths;

	@Mock
	private Value value;
	
	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;
	/** The mock session. */
	@Mock
	private Session mockSession;
	
	@Mock
	private Resource mockResource;

	@Mock
	private Iterator<Resource> iterator;
	
	/** The page manger. */
	@Mock
	private PageManager pageManager;
	
	/** The ValueMap. */
	@Mock
	private ValueMap valueMap;

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			String[] articleTypeArr = new String[2];
			articleTypeArr[0] = "blog";
			articleType = Mockito.mock(Property.class);
			Mockito.when(articleType.getValue()).thenReturn(new StringValue(articleTypeArr[0]));
			final TagManager mockTagManager = Mockito.mock(TagManager.class);
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			context.registerAdapter(ResourceResolver.class, QueryBuilder.class, queryBuilder);
			ValueMap properties = createPagePropertyValueMap();
			Mockito.when(page.getProperties()).thenReturn(properties);
			Mockito.when(page.getPath()).thenReturn("/content/corteva/corporate/resources/article1");
			Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			final SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(query.getResult().getResources()).thenReturn(iterator);
			Mockito.when(iterator.hasNext()).thenReturn(true, true, true, true, true, true, false);
			Mockito.when(iterator.next()).thenReturn(mockResource);
			Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(page);
			Mockito.when(mockReq.getResource()).thenReturn(mockResource);
			Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/resources/i");
			pageManager = Mockito.mock(PageManager.class);
			Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
			Mockito.when(pageManager.getContainingPage(mockResource)).thenReturn(page);
			Mockito.when(page.getContentResource()).thenReturn(mockResource);
			Mockito.when(mockResource.getValueMap()).thenReturn(valueMap);
			Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(CortevaConstant.CORPORATE_TAG);
			
			context.registerInjectActivateService(new BaseConfigurationService());

			value = Mockito.mock(Value.class);
		} catch (ValueFormatException e) {
			Assert.fail("ValueFormatException occurred in testInit(): " + e.getMessage());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testInit(): " + e.getMessage());
		}
	}

	@Test
	public void testInitForFeed() {
		try {
			List<String> articletypeList = new ArrayList<>();
			articletypeList.add("blog");
			Field viewTypeField;
			viewTypeField = rightRailLinkedListModel.getClass().getDeclaredField("articleType");
			viewTypeField.setAccessible(true);
			viewTypeField.set(rightRailLinkedListModel, articleType);
			Field folderPath;
			folderPath = rightRailLinkedListModel.getClass().getDeclaredField("folderPath");
			folderPath.setAccessible(true);
			folderPath.set(rightRailLinkedListModel, "/content/corteba/corporate/resources");
			Field displayFilterSection;
			displayFilterSection = rightRailLinkedListModel.getClass().getDeclaredField("linkListType");
			displayFilterSection.setAccessible(true);
			displayFilterSection.set(rightRailLinkedListModel, "feed");
			Field baseServiceField;
			baseServiceField = rightRailLinkedListModel.getClass().getDeclaredField("baseService");
			baseServiceField.setAccessible(true);
			baseServiceField.set(rightRailLinkedListModel, baseService);
			Field noofArticlesDisplayedField;
			noofArticlesDisplayedField = rightRailLinkedListModel.getClass().getDeclaredField("noofArticlesDisplayed");
			noofArticlesDisplayedField.setAccessible(true);
			noofArticlesDisplayedField.set(rightRailLinkedListModel, "3");
			rightRailLinkedListModel.init();
			Assert.assertEquals(3, rightRailLinkedListModel.getArticleBeanList().size());
	
		} catch (SecurityException e) {
			Assert.fail("SecurityException occurred in testInit(): " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Assert.fail("IllegalArgumentException occurred in testInit(): " + e.getMessage());
		} catch (NoSuchFieldException e) {
			Assert.fail("NoSuchFieldException occurred in testInit(): " + e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.fail("IllegalAccessException occurred in testInit(): " + e.getMessage());
		}
	}

	@Test
	public void testInitForFeatured() {
		try {
			Value[] valueArr= new Value[2];
			valueArr[0] =new StringValue("{\"articlePagePath\":\"/content/corteva/corporate/resources/article-page/article3\"}");
			valueArr[1] =new StringValue("{\"articlePagePath\":\"/content/corteva/corporate/resources/article-page/article4\"}");
			PrivateAccessor.setField(rightRailLinkedListModel, "articlePagePaths", articlePagePaths);
			Mockito.when(articlePagePaths.isMultiple()).thenReturn(true);
			Mockito.when(articlePagePaths.getValues()).thenReturn(valueArr);
			PrivateAccessor.setField(rightRailLinkedListModel, "linkListType", "featured");
			PrivateAccessor.setField(rightRailLinkedListModel, "baseService", baseService);
			rightRailLinkedListModel.init();
			Assert.assertEquals(2, rightRailLinkedListModel.getArticleBeanList().size());
		} catch (SecurityException e) {
			Assert.fail("SecurityException occurred in testInit(): " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Assert.fail("IllegalArgumentException occurred in testInit(): " + e.getMessage());
		} catch (NoSuchFieldException e) {
			Assert.fail("NoSuchFieldException occurred in testInit(): " + e.getMessage());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testInit(): " + e.getMessage());
		}
	}

	/* This method creates Temp Page PRoperties Value Map.*/ 
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		pagePropMap.put(CortevaConstant.CQ_LAST_MODIFIED, cal);
		pagePropMap.put("displayDate", cal);
		pagePropMap.put("cq:articleType", (Object) "corteva:content/Blog");
		return new ValueMapDecorator(pagePropMap);
	}

}
