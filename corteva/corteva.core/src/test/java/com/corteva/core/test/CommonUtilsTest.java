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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.day.cq.i18n.I18n;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * This is test class to test the common utils.
 * 
 * @author Sapient
 */
public class CommonUtilsTest extends BaseAbstractTest {

	/** The test data for resource path. */
	private static final String TEST_DATA_RESOURCE_PATH = "/content/corteva/na/us/en/full-bleed-tiles-container0";

	private static final String CONTEXTUAL_PATH_BROWSER = "corteva/components/content/contextualpathbrowser";

	/** The page path. */
	private String pagePath;
	
	/** The root page path. */
	private String rootPagePath;
	
	/** The error page path. */
	private String errorPagePath;

	/** The cmmn utils. */
	private CommonUtils cmmnUtils;

	/** The mock session. */
	private Session mockSession;
	
	/** The mock base service*/
	@Mock
	private BaseConfigurationService baseService;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;
	
	/** The mock request. */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The resource. */
	@Mock
	private Resource resource;
	
	/** The jcr:content resource. */
	@Mock
	private Resource jcrContentResource;
	
	/** The valueMap. */
	@Mock
	private ValueMap valueMap;

	/** The query builder. */
	@Mock
	private QueryBuilder queryBuilder;

	/** The session. */
	@Mock
	private Session session;

	/** The query. */
	@Mock
	private Query query;

	/** The page. */
	@Mock
	private Page page;
	
	/** The page manger. */
	@Mock
	private PageManager pageManager;

	/** The resource type. */
	/* The component resource type */
	private String resourceType;

	/**
	 * Sets the method parameters and registers the service.
	 */
	@Before
	public void setUp() {
		pagePath = "/content/corteva/na/us/en/full-bleed-tiles-container";
		mockSession = getResourceResolver().adaptTo(Session.class);
		cmmnUtils = new CommonUtils();
		baseService = Mockito.mock(BaseConfigurationService.class);
		String currentResPath = "/apps/corteva/components/content/fullBleedTilesContainer/v1/fullBleedTilesContainer/cq:dialog/content/items/tab1/items/columns/items/setCtaCardWithArrow/items/ctaCardWithArrowPaths/field/items/column/items/ctaCardWithArrowPath";
		resourceType = "corteva/components/content/tilesContainer/v1/tilesContainer";
		try {
			Node mockNode = mockSession.getRootNode().addNode(currentResPath);
			mockNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CommonUtilsTest.CONTEXTUAL_PATH_BROWSER);
			mockNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, CortevaConstant.NT_UNSTRUCTURED);
			mockNode.setProperty("fieldLabel", "CTA Card With Arrow Path");
			mockNode.setProperty("class", "expfragPath");
			mockNode.setProperty(CortevaConstant.NAME, "./ctaCardWithArrowPath");
			mockNode.setProperty("cardView", "ctaCardWithArrow");
			mockNode.setProperty("required", "{Boolean}true");
			mockNode.setProperty(CortevaConstant.CORTEVA_ROOT_PATH, "");
			context.create().resource(currentResPath);
			context.currentResource(currentResPath).getValueMap();
			/**
			 * Page node is being added.
			 */
			Node mockPage = mockSession.getRootNode().addNode(pagePath);
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource(pagePath);
			page = context.pageManager().create("/content/corteva/na/us/en", "full-bleed-tiles-container",
					"/apps/sample/templates/homepage", "full-bleed-tiles-container");
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(pageManager.getContainingPage(context.currentResource(pagePath))).thenReturn(page);
		} catch (RepositoryException | WCMException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testGetResource() {
		Assert.assertNotNull(
				CommonUtils.getResource(getRequest(),CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION+"experience-fragments", context.currentResource(), getResourceResolver(), "/content/corteva/na/en/us/test.png"));
		Assert.assertNotNull(
				CommonUtils.getResource(getRequest(),TEST_DATA_RESOURCE_PATH, context.currentResource(), getResourceResolver(), ""));
		Assert.assertNotNull(
				CommonUtils.getResource(getRequest(),"/conf/experience-fragments", context.currentResource(), getResourceResolver(), "/content/dam/corteva/na/en/us/test.png"));
	}

	/**
	 * Test get RegionCountryLanguage Map for Commercial Page.
	 */
	@Test
	public void testGetRegionCountryLanguageForCommercial() {
		
		String regionalPagePath = "/content/corteva/na";
		String testPagePath = "/content/corteva/na/us/en/homepage/test";
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		Mockito.when(resolver.getResource(regionalPagePath)).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		
		Assert.assertNotNull(cmmnUtils.getRegionCountryLanguage(testPagePath, resolver));
	}
	
	/**
	 * Test get RegionCountryLanguage Map for Corporate Page.
	 */
	@Test
	public void testGetRegionCountryLanguageForCorporate() {
		
		String regionalPagePath = "/content/corteva/na";
		String testPagePath = "/content/corteva/na/us/en/homepage/test";
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		page = Mockito.mock(Page.class);
		jcrContentResource = Mockito.mock(Resource.class);
		valueMap = Mockito.mock(ValueMap.class);
		Mockito.when(resolver.getResource(regionalPagePath)).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(jcrContentResource);
		Mockito.when(jcrContentResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(CortevaConstant.CORPORATE_TAG);
		Assert.assertNotNull(cmmnUtils.getRegionCountryLanguage(testPagePath, resolver));
	}
	
	/**
	 * Test get RegionCountryLanguage Map for Language Master Page.
	 */
	@Test
	public void testGetRegionCountryLanguageForLanguageMaster() {
		
		String regionalPagePath = "/content/corteva/na";
		String testPagePath = "/content/corteva/na/us/en/homepage/test";
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		jcrContentResource = Mockito.mock(Resource.class);
		valueMap = Mockito.mock(ValueMap.class);
		page = Mockito.mock(Page.class);
		Mockito.when(resolver.getResource(regionalPagePath)).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Mockito.when(page.getContentResource()).thenReturn(jcrContentResource);
		Mockito.when(jcrContentResource.getValueMap()).thenReturn(valueMap);
		Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(CortevaConstant.LANGUAGE_MASTER_TAG);
		
		Assert.assertNotNull(cmmnUtils.getRegionCountryLanguage(testPagePath, resolver));
	}

	/**
	 * Test get I 18 n locale.
	 */
	@Test
	public void TestGetI18nLocale() {
		Assert.assertNotNull(cmmnUtils.getI18nLocale(TEST_DATA_RESOURCE_PATH, getResourceResolver()));
	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testGetPagePath() {
		MockSlingHttpServletRequest request = getRequest();
		request.setServletPath("/content/corteva/na/us/en/home");
		Assert.assertNotNull(cmmnUtils.getPagePath(request));
	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testGetRelativePagePath() {
		Assert.assertNotNull(cmmnUtils.getRelativePagePath("/content/corteva/na/us/en/home.html"));
	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testGetFileNameWithoutExtn() {
		Assert.assertNotNull(cmmnUtils.getFileNameWithoutExtn("abc.jpg"));
	}

	/**
	 * Test get property value from configuration.
	 */
	@Test
	public void testFormatDate() {
		Date date = null;
		GregorianCalendar newGregCal = new GregorianCalendar();
		date = newGregCal.getTime();
		Assert.assertNotNull(cmmnUtils.formatDate(date, CortevaConstant.MM_DD_YY));
	}

	/**
	 * Test get component name from resource type.
	 */
	@Test
	public void testComponentNameIsNotNull() {
		Assert.assertNotNull(cmmnUtils.getComponentName(resourceType));
	}

	/**
	 * Test getInheritedProperty.
	 */
	@Test
	public void testInheritedProperty() {
		Assert.assertNotNull(
				cmmnUtils.getInheritedProperty("stageHeaderScript", context.currentResource(TEST_DATA_RESOURCE_PATH)));
	}

	/**
	 * Test get category subcategory.
	 * @throws RepositoryException 
	 * @throws LockException 
	 * @throws ConstraintViolationException 
	 * @throws VersionException 
	 * @throws PathNotFoundException 
	 * @throws ItemExistsException 
	 */
	@Test
	public void testGetCategorySubcategory() throws RepositoryException {
		String regionalPagePath = "/content/corteva/na";
		String testPagePath = "/content/corteva/na/us/en/homepage/categorypage/subcategorypage";
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		Mockito.when(resolver.getResource(regionalPagePath)).thenReturn(resource);
		Mockito.when(resolver.resolve(regionalPagePath)).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Assert.assertNotNull(cmmnUtils.getCategorySubcategory(testPagePath, resolver));
	}

	/**
	 * Test get pdp root path.
	 */
	@Test
	public void testGetPdpRootPath() {
		Map<String, String> regCountLangMap = new HashMap<>();
		regCountLangMap.put(CortevaConstant.REGION, "NA");
		regCountLangMap.put(CortevaConstant.COUNTRY, "US");
		regCountLangMap.put(CortevaConstant.LANGUAGE, "en");
		String pdpRootPath = StringUtils.EMPTY;
		Assert.assertNotNull(cmmnUtils.getPdpRootPath(regCountLangMap, pdpRootPath));
	}

	/**
	 * Test get resource of type.
	 */
	@Test
	public void testGetResourceOfType() {
		try {
			Resource rootResource = context.create()
					.resource("/content/corteva/na/us/en/products/us-non-agrian-product-1/jcr:content/root");
			Node rootNode = rootResource.adaptTo(Node.class);
			rootNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, "wcm/foundation/components/responsivegrid");
			Node productLabelNode = rootNode.addNode("productlabel");
			productLabelNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.PRODUCT_LABEL_RES_TYPE);
			Assert.assertNotNull(cmmnUtils.getResourceOfType(rootResource, CortevaConstant.PRODUCT_LABEL_RES_TYPE));
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testGetResourceOfType" + e.getMessage());
		}
	}

	/**
	 * Test find all pdp resources.
	 */
	@Test
	public void testFindAllPdpResources() {
		try {
			String contentPath = "/content/corteva/na/us/en/products";
			Resource res = context.create().resource(contentPath);
			Node contentNode = res.adaptTo(Node.class);
			Node childNode = contentNode.addNode("us-non-agrian-product-1/jcr:content");
			childNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.PDP_RESOURCE_TYPE);
			childNode.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			resolver = Mockito.mock(ResourceResolver.class);
			session = Mockito.mock(Session.class);
			queryBuilder = Mockito.mock(QueryBuilder.class);
			query = Mockito.mock(Query.class);
			Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			final List<Resource> results = new ArrayList<>();
			results.add(res);
			SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Iterator<Resource> resultIterator = cmmnUtils.findAllPdpResources(resolver, contentPath);
			Assert.assertEquals(true, resultIterator.hasNext());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testFindAllNonAgrianPdpResources" + e.getMessage());
		}
	}
	
	/**
	 * Test find all unhidden child resources of resource type.
	 */
	@Test
	public void testFindUnhiddenChildResources() {
		try {
			String contentPath = "/content/corteva/na/us/en/homepage/products-and-solutions/crop-protection";
			Resource res = context.create().resource(contentPath);
			Node contentNode = res.adaptTo(Node.class);
			Node childNode = contentNode.addNode("blackhawk/jcr:content");
			childNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.PDP_RESOURCE_TYPE);
			resolver = Mockito.mock(ResourceResolver.class);
			session = Mockito.mock(Session.class);
			queryBuilder = Mockito.mock(QueryBuilder.class);
			query = Mockito.mock(Query.class);
			Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			final List<Resource> results = new ArrayList<>();
			results.add(res);
			SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Iterator<Resource> resultIterator = cmmnUtils.findUnhiddenChildResources(resolver, CortevaConstant.PDP_RESOURCE_TYPE, contentPath);
			Assert.assertEquals(true, resultIterator.hasNext());
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testFindUnhiddenChildResources" + e.getMessage());
		}
	}

	@Test
	public void testFindNonPdpResource() {
		try {
			String contentPath = "/content/corteva/na/us/en/products";
			Resource res = context.create().resource(contentPath);
			Node contentNode = res.adaptTo(Node.class);
			Node childNode = contentNode.addNode("us-non-agrian-product-1/jcr:content");
			childNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.PDP_RESOURCE_TYPE);
			childNode.setProperty(CortevaConstant.PDP_SOURCE, CortevaConstant.NON_AGRIAN);
			resolver = Mockito.mock(ResourceResolver.class);
			session = Mockito.mock(Session.class);
			queryBuilder = Mockito.mock(QueryBuilder.class);
			query = Mockito.mock(Query.class);
			Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
			Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(query);
			final List<Resource> results = new ArrayList<>();
			results.add(res);
			SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(query.getResult()).thenReturn(searchResult);
			Mockito.when(query.getResult().getResources()).thenReturn(results.iterator());
			Resource resultRes = cmmnUtils.findNonAgrianPdpResource(resolver, contentPath, "us-non-agrian-product-1");
			Assert.assertNotNull(resultRes);
		} catch (RepositoryException e) {
			Assert.fail("Exception occurred in testFindAllNonAgrianPdpResources" + e.getMessage());
		}
	}
	/**
	 * Test to get I18n Value.
	 */
	@Test
	public void testGetI18nValue() {
		try {
			String pagePath = "/content/corteva/na/us/en/products";
			Node mockPage = mockSession.getRootNode().addNode(pagePath);
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			Resource currentResource = context.create().resource(pagePath);
			page = context.pageManager().create(pagePath, "test", "/apps/sample/templates/homepage", "test");
			PageManager pageMgr = Mockito.mock(PageManager.class);
			SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
			resolver = Mockito.mock(ResourceResolver.class);
			ResourceBundle resourceBundle = Mockito.mock(ResourceBundle.class);
			Mockito.when(resolver.resolve(pagePath)).thenReturn(currentResource);
			Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
			Mockito.when(pageMgr.getContainingPage(currentResource)).thenReturn(page);
			Mockito.when(request.getResource()).thenReturn(currentResource);
			Mockito.when(request.getResourceBundle(Mockito.any())).thenReturn(resourceBundle);
			I18n i18n = Mockito.mock(I18n.class);
			Mockito.when(i18n.get(Mockito.anyString())).thenReturn("All States");
			CommonUtils.getI18nValue(request, resolver, "pdp.productLabelFinder.stateKey");
		} catch (RepositoryException | WCMException e) {
			Assert.fail("Exception occurred in testGetI18nValue()" + e.getMessage());
		}
	}
	
	/**
	 * Test getInheritedProperty.
	 * @throws RepositoryException 
	 */
	@Test
	public void testGetProductTypeProperty() throws RepositoryException {
		Assert.assertNotNull(
				CommonUtils.getProductTypeProperty("cq:productType", context.currentResource(TEST_DATA_RESOURCE_PATH)));
		Assert.assertNotNull(
				CommonUtils.getProductTypeProperty("cq:productType", context.currentResource(TEST_DATA_RESOURCE_PATH)));
	}
	
	/**
	 * Test get date from value map.
	 *
	 * @throws RepositoryException the repository exception
	 */
	@Test
	public void testGetDateFromValueMap() throws RepositoryException {
		Assert.assertNotNull(
				cmmnUtils.getDateFromValueMap(new GregorianCalendar()));
	}
	
	/**
	 * Test get local date.
	 */
	@Test
	public void testGetLocalDate() {
		Assert.assertNotNull(
				cmmnUtils.getLocalDate("2018-08-01T08:36:11.056Z", CortevaConstant.ABSOLUTE_TIME_FORMAT));
	}
	
	/**
	 * Test get page path from request.
	 */
	@Test
	public void testGetPagePathFromRequest() {
		MockSlingHttpServletRequest request = getRequest();
		rootPagePath = "/content/corteva/na/us/en/homepage";
		errorPagePath = "/content/corteva/na/us/en/homepage/errorpage";
		context.create().resource(rootPagePath);
		request.setServletPath(errorPagePath);
		Assert.assertEquals(rootPagePath, cmmnUtils.getPagePathFromRequest(request));
	}
	
	/**
	 * Test get local object.
	 */
	@Test
	public void getLocaleObject() {
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		Mockito.when(resolver.getResource("/content/corteva/na")).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Assert.assertNotNull(
				cmmnUtils.getLocaleObject("/content/corteva/na/us/en/homepage", resolver));
	}
	
	/**
	 * Test get local object.
	 */
	@Test
	public void getFormattedLocalizedDate() {
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		Mockito.when(resolver.getResource("/content/corteva/na")).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Assert.assertNotNull(
				CommonUtils.getFormattedLocalizedDate(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject("/content/corteva/na/us/en/homepage", resolver), false, mockRequest, baseService));
		Assert.assertNotNull(
				CommonUtils.getFormattedLocalizedDate(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject("/content/corteva/na/us/en/homepage", resolver), true, mockRequest, baseService));
	}
	
	/**
	 * Test getFormattedLocalizedDateForServlet() method
	 */
	@Test
	public void getFormattedLocalizedDateForServlet() {
		resolver = Mockito.mock(ResourceResolver.class);
		pageManager = Mockito.mock(PageManager.class);
		resource = Mockito.mock(Resource.class);
		Mockito.when(resolver.getResource("/content/corteva/na")).thenReturn(resource);
		Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		Assert.assertNotNull(
				CommonUtils.getFormattedLocalizedDateForServlet(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject("/content/corteva/na/us/en/homepage", resolver), false, "d.M.yy" ));
		Assert.assertNotNull(
				CommonUtils.getFormattedLocalizedDateForServlet(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
						CommonUtils.getLocaleObject("/content/corteva/na/us/en/homepage", resolver), true, "d.M.yy"));
	
	}
	
	/**
	 * Test get Tage Localized Title.
	 */
	@Test
	public void getTagLocalizedTitle() {
		MockSlingHttpServletRequest request = getRequest();
		Tag tag = Mockito.mock(Tag.class);
		Mockito.when(tag.getTitle()).thenReturn("MockTitle");
		Assert.assertNotNull(CommonUtils.getTagLocalizedTitle(request, tag));
		
		Mockito.when(tag.getLocalizedTitle(CommonUtils.getLangLocale(request))).thenReturn("MockString");
		Assert.assertNotNull(CommonUtils.getTagLocalizedTitle(request, tag));
		
		Mockito.when(tag.getLocalizedTitle(CommonUtils.getLocale(request))).thenReturn("MockString");
		Assert.assertNotNull(CommonUtils.getTagLocalizedTitle(request, tag));
		
		
	}
	
	/**
	 * Test get Tag Title.
	 */
	@Test
	public void getTagTitle() {
		MockSlingHttpServletRequest request = getRequest();
		Tag tag = Mockito.mock(Tag.class);
		Mockito.when(tag.getLocalizedTitle(CommonUtils.getLangLocale(request))).thenReturn(StringUtils.EMPTY);
		Assert.assertNull(CommonUtils.getTagTitle(request, tag));
		
		Mockito.when(tag.getLocalizedTitle(CommonUtils.getLangLocale(request))).thenReturn("MockString");
		Assert.assertNotNull(CommonUtils.getTagTitle(request, tag));
	}
	
	/**
	 * Test get Lang Locale.
	 */
	@Test
	public void getLangLocale() {
		MockSlingHttpServletRequest request = getRequest();
		Map<String, String> countryLangMap = new HashMap<>();
		String pagePath = "/content/corteva/na/en/us/homepage";
		countryLangMap.put(CortevaConstant.LANGUAGE, CortevaConstant.EN);
		Assert.assertNull(CommonUtils.getLangLocale(request));
	}
	
	/**
	 * Test get Locale from Page Properties.
	 */
	@Test
	public void getLocaleFromPageProperties() {
		MockSlingHttpServletRequest request = getRequest();
		Assert.assertNotNull(CommonUtils.getLocaleFromPageProperties(request));
	}

	/**
	 * Test get Environment
	 */
	@Test
	public void testGetEnvironment() {
		Set<String> runModes = new HashSet<>();
		runModes.add(CortevaConstant.DEV);
		
		runModes.add(CortevaConstant.QA);
		Assert.assertEquals(CortevaConstant.QA, CommonUtils.getEnvironment(runModes));
		runModes.remove(CortevaConstant.QA);
		
		runModes.add(CortevaConstant.STAGE);
		Assert.assertEquals(CortevaConstant.STAGE, CommonUtils.getEnvironment(runModes));
		runModes.remove(CortevaConstant.STAGE);
		
		runModes.add(CortevaConstant.CORTEVA_QA);
		Assert.assertEquals(CortevaConstant.CORTEVA_QA, CommonUtils.getEnvironment(runModes));
		runModes.remove(CortevaConstant.CORTEVA_QA);
		
		runModes.add(CortevaConstant.PROD);
		Assert.assertEquals(CortevaConstant.PROD, CommonUtils.getEnvironment(runModes));
		runModes.remove(CortevaConstant.PROD);
		
		Assert.assertEquals(CortevaConstant.DEV, CommonUtils.getEnvironment(runModes));
		
	}
}