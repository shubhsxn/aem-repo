package com.corteva.model.component.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.PageMetaModel;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;

/**
 * This is a test class for PageMetaModel.
 *
 * @author Sapient
 *
 */
public class PageMetaModelTest extends BaseAbstractTest {

	/** The page meta model. */
	@InjectMocks
	private PageMetaModel pageMetaModel;
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The page. */
	@Mock
	private Page page;

	/** The current res path. */
	private String currentResPath;

	/** The regional page path. */
	private String regionalPagePath;

	/** The resource. */
	@Mock
	private Resource resource;

	/** The resource. */
	@Mock
	private Template mockTemplate;

	/** The mock session. */
	private Session mockSession;

	/** The jcr:content resource. */
	@Mock
	private Resource jcrContentResource;

	/** The regional page. */
	@Mock
	private Page regionalPage;

	/** The valueMap. */
	@Mock
	private ValueMap valueMap;

	/** The resolver. */
	@Mock
	private ResourceResolver resolver;

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
	private Iterator<Resource> resultIterator;
	
	/** The sling service. */
	@Mock
	private SlingSettingsService slingService;
	
	/** The constant to hold value of default folder path. */
	private static final String TEST_DATA_DEFAULT_FOLDER_PATH = "/content/corteva/corporate/resources";

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			baseService = new BaseConfigurationService();
			context.getService(ConfigurationAdmin.class);
			getContext().registerInjectActivateService(baseService);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			currentResPath = "/content/corteva/na/us/en/home/our-merger/jcr:content";
			regionalPagePath = "/content/corteva/na";
			ValueMap properties = createPagePropertyValueMap();
			context.create().resource(currentResPath, properties);
			context.build().resource("/content/corteva/na/us/en/home/our-merger/jcr:content",
					CortevaConstant.SITE_MAP_INDEX, "true");
			page = context.pageManager().create("/content/corteva/na/us/en/home", "our-merger",
					"/apps/sample/templates/homepage", "our-merger");
			context.currentResource(currentResPath);
			context.registerAdapter(ResourceResolver.class, QueryBuilder.class, queryBuilder);
			Node resNode = page.getContentResource().adaptTo(Node.class);
			resNode.setProperty(CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_HEADER, "testheader");
			resNode.setProperty(CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_FOOTER, "testfooter");
			getRequest().setScheme("https");
			pageMetaModel.setCurrentResource(context.currentResource(currentResPath));
			context.registerAdapter(Resource.class, Page.class, page);
			Mockito.mock(ResourceResolver.class);
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
			Mockito.when(pageManager.getContainingPage(context.currentResource())).thenReturn(page);
			Mockito.when(resolver.resolve("/content/corteva/na/us/en/home/our-merger0"))
					.thenReturn(context.currentResource());
			Mockito.when(resolver.resolve(Mockito.anyString())).thenReturn(context.currentResource());
			regionalPage = Mockito.mock(Page.class);
			Mockito.when(resolver.getResource(regionalPagePath)).thenReturn(resource);
			Mockito.when(pageManager.getContainingPage(resource)).thenReturn(regionalPage);
			Mockito.when(regionalPage.getContentResource()).thenReturn(jcrContentResource);
			Mockito.when(jcrContentResource.getValueMap()).thenReturn(valueMap);
			Mockito.when(valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn("corteva:corporate");

		} catch (Exception e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test get site map content.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetSiteMapContent() throws Exception {
		Assert.assertEquals(CortevaConstant.CRAWL_STRING, pageMetaModel.getSiteMapContent());
	}

	/**
	 * Test get last modified date.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLastModifiedDate() throws Exception {
		Assert.assertNotNull(pageMetaModel.getLastModifiedDate());
	}

	/**
	 * Test get company.
	 */
	@Test
	public void testGetCompany() {
		Assert.assertNull(pageMetaModel.getCompany());
	}

	/**
	 * Test get contentType.
	 */
	@Test
	public void testGetContentType() {
		Assert.assertNotNull(pageMetaModel.getContentType());
	}

	/**
	 * Test Default environment.
	 */
	@Test
	public void testDefaultEnvironment() {
		Set<String> envSetDev = new HashSet<>();
		envSetDev.add("dev");
		Mockito.when(slingService.getRunModes()).thenReturn(envSetDev);
		Assert.assertNotNull(pageMetaModel.getEnvironment());
	}

	/**
	 * Test QA environment.
	 */
	@Test
	public void testQAEnvironment() {
		Set<String> envSetQA = new HashSet<>();
		envSetQA.add("qa");
		Mockito.when(slingService.getRunModes()).thenReturn(envSetQA);
		Assert.assertNotNull(pageMetaModel.getEnvironment());
	}

	/**
	 * Test Stage environment.
	 */
	@Test
	public void testStageEnvironment() {
		Set<String> envSetStage = new HashSet<>();
		envSetStage.add("preprod");
		Mockito.when(slingService.getRunModes()).thenReturn(envSetStage);
		Assert.assertNotNull(pageMetaModel.getEnvironment());
	}

	/**
	 * Test Corteva QA environment.
	 */
	@Test
	public void testCortevaQAEnvironment() {
		Set<String> envSetCortevaQA = new HashSet<>();
		envSetCortevaQA.add("test");
		Mockito.when(slingService.getRunModes()).thenReturn(envSetCortevaQA);
		Assert.assertNotNull(pageMetaModel.getEnvironment());
	}

	/**
	 * Test Prod environment.
	 */
	@Test
	public void testProdEnvironment() {
		Set<String> envSetPROD = new HashSet<>();
		envSetPROD.add("prod");
		Mockito.when(slingService.getRunModes()).thenReturn(envSetPROD);
		Assert.assertNotNull(pageMetaModel.getEnvironment());
	}

	/**
	 * Test country.
	 */
	@Test
	public void testCountry() {
		Assert.assertNotNull(pageMetaModel.getCountry());
	}

	/**
	 * Test language.
	 */
	@Test
	public void testLanguage() {
		Assert.assertNotNull(pageMetaModel.getLanguage());
	}

	/**
	 * Test category.
	 */
	@Test
	public void testCategory() {
		Assert.assertNotNull(pageMetaModel.getCategory());
	}

	/**
	 * Test subcategory.
	 */
	@Test
	public void testSubcategory() {
		Assert.assertNotNull(pageMetaModel.getSubcategory());
	}

	/**
	 * Test product type.
	 */
	@Test
	public void testProductType() {
		try {
			Assert.assertNotNull(pageMetaModel.getProductType());
		} catch (RepositoryException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test header exp fragment path.
	 */
	@Test
	public void testHeaderExpFragmentPath() {
		Assert.assertNotNull(pageMetaModel.getHeaderExpFragmentPath());
	}

	/**
	 * Test footer exp fragment path.
	 */
	@Test
	public void testFooterExpFragmentPath() {
		Assert.assertNotNull(pageMetaModel.getFooterExpFragmentPath());
	}

	/**
	 * Test canonical url.
	 */
	@Test
	public void testCanonicalUrl() {
		Node pageNode = page.getContentResource().adaptTo(Node.class);
		try {
			pageNode.setProperty("canonicalUrl", "/content/corteva/na/us/en/homepage");
			Mockito.when(resolver.map("/content/corteva/na/us/en/homepage"))
					.thenReturn("/content/corteva/na/us/en/homepage");
			Assert.assertNull(pageMetaModel.getCanonicalUrl());
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in testCanonicalUrl()" + e.getMessage());
		}

	}

	/**
	 * Test product src.
	 */
	@Test
	public void testProductSrc() {
		Assert.assertNotNull(pageMetaModel.getProductSrc());
	}

	/**
	 * Test pdp url.
	 */
	@Test
	public void testPdpUrl() {
		Assert.assertNotNull(pageMetaModel.getPdpUrl());
	}

	/**
	 * Test pdp product id.
	 */
	@Test
	public void testPdpProductId() {
		Assert.assertNotNull(pageMetaModel.getPdpProductId());
	}

	/**
	 * Test product type tags.
	 */
	@Test
	public void testProductTypeTags() {
		Assert.assertNotNull(pageMetaModel.getProductTypeTags());
	}

	/**
	 * Test product name.
	 */
	@Test
	public void testProductName() {
		Assert.assertNotNull(pageMetaModel.getProductName());
	}

	/**
	 * Test crop category.
	 */
	@Test
	public void testCropCategory() {
		Assert.assertNull(pageMetaModel.getCropCategory());
	}

	/**
	 * Test alternate href.
	 */
	@Test
	public void testAlternateHref() {
		Assert.assertNotNull(pageMetaModel.getAlternateHREF());
	}

	/**
	 * Test region.
	 */
	@Test
	public void testRegion() {
		Assert.assertNull(pageMetaModel.getRegion());
	}

	/**
	 * Creates the page property value map.
	 *
	 * @return the value map
	 */
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<String, Object>();
		String[] alternateHref = { "/content/corteva/au/au/en", "/content/corteva/au/nz/en" };
		pagePropMap.put("cq:alternateHref", alternateHref);

		return new ValueMapDecorator(pagePropMap);
	}

	/**
	 * Test Template Type Method
	 */
	@Test
	public void getCurrentPageTemplateType() {
		Page mockPage = Mockito.mock(Page.class);
		Mockito.when(mockPage.getTemplate()).thenReturn(mockTemplate);
		Resource mockResource = Mockito.mock(Resource.class);
		ValueMap mockValueMap = Mockito.mock(ValueMap.class);
		Resource mockChildResource = Mockito.mock(Resource.class);
		Mockito.when(mockTemplate.adaptTo(Resource.class)).thenReturn(mockResource);
		Mockito.when(mockResource.getChild(Mockito.anyString())).thenReturn(mockChildResource);
		Mockito.when(mockChildResource.getValueMap()).thenReturn(mockValueMap);
		String str = "/conf/corteva/settings/wcm/template-types/pdp";
		Object mockObj = Mockito.mock(Object.class);
		Mockito.when(mockValueMap.get(Mockito.anyString())).thenReturn(mockObj);
		Mockito.when(mockObj.toString()).thenReturn(str);

		Assert.assertNotNull(pageMetaModel.getCurrentPageTemplateType(mockPage));
	}

	/**
	 * Test Template Type Method When Node Incorrect
	 */
	@Test
	public void getCurrentPageTemplateTypeWhenPropMissing() {
		Page mockPage = Mockito.mock(Page.class);
		Mockito.when(mockPage.getTemplate()).thenReturn(mockTemplate);
		Resource mockResource = Mockito.mock(Resource.class);
		Mockito.when(mockTemplate.adaptTo(Resource.class)).thenReturn(mockResource);
		Mockito.when(mockResource.getChild(Mockito.anyString())).thenReturn(null);

		Assert.assertNotNull(pageMetaModel.getCurrentPageTemplateType(mockPage));
	}

	/**
	 * Test get seo schema definition.
	 */
	@Test
	public void testGetSeoSchema() {
		Assert.assertNotNull(pageMetaModel.getSeoSchema());
	}

	/**
	 * Test get SEO Image object type definition.
	 */
	@Test
	public void testGetSeoImageObject() {
		Assert.assertNotNull(pageMetaModel.getSeoImageObject());
	}

	/**
	 * Test get SEO Image object type definition.
	 */
	@Test
	public void testGetSeoOrganisation() {
		Assert.assertNotNull(pageMetaModel.getSeoOrganisation());
	}

	/**
	 * Test get SEO Article object type definition.
	 */
	@Test
	public void testGetArticle() {
		Assert.assertNotNull(pageMetaModel.getSeoArticle());
	}

	/**
	 * Test Article Header Component Name.
	 */
	@Test
	public void testArticleHeaderImagePath() {
		Assert.assertNotNull(CortevaConstant.ARTICLE_HEADER_COMPONENT_NODE);
	}

	/**
	 * Test Article Header Component Name.
	 */
	@Test
	public void testGlobalHeaderLogoPath() {
		Assert.assertNotNull(pageMetaModel.getHeaderExpFragmentPath());
	}

	/**
	 * Test Scene 7 Image Path.
	 */
	@Test
	public void testImagePathFromComponent() {
		mockSession = getResourceResolver().adaptTo(Session.class);
		Mockito.when(resolver.adaptTo(Session.class)).thenReturn(mockSession);
		Mockito.mock(ResourceResolver.class);
	}

}