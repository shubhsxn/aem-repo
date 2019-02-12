package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.CountryBean;
import com.corteva.model.component.bean.CountrySelectorBean;
import com.corteva.model.component.bean.LanguageBean;
import com.corteva.model.component.models.CountrySelectorModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The Class CountrySelectorTest.
 */

public class CountrySelectorTest extends BaseAbstractTest {

	/** The mock resource resolver. */
	@Mock
	private ResourceResolver mockResourceResolver;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The tag manager. */
	@Mock
	private TagManager tagManager;

	/** The mock query builder. */
	@Mock
	private QueryBuilder mockQueryBuilder;

	/** The mock session. */
	@Mock
	private Session mockSession;

	/** The mock query. */
	@Mock
	private Query mockQuery;

	/** The mock iterator. */
	@Mock
	private Iterator<Resource> mockIterator;

	/** The mock region iterator. */
	@Mock
	private Iterator<Page> mockRegionIterator;

	/** The mock country iterator. */
	@Mock
	private Iterator<Page> mockCountryIterator;

	/** The mock language iterator. */
	@Mock
	private Iterator<Page> mockLanguageIterator;

	/** The country selector model. */
	@InjectMocks
	private CountrySelectorModel countrySelectorModel;

	/** The mock resource. */
	@Mock
	private Resource mockResource;
	
	/** The mock jcr:content resource. */
	@Mock
	private Resource mockJcrContentResource;
	
	
	/** The mock valueMap */
	@Mock
	private ValueMap mockValueMap;

	/** The mock page. */
	@Mock
	private Page mockPage;

	/** The mock region page. */
	@Mock
	private Page mockRegionPage;

	/** The mock country page. */
	@Mock
	private Page mockCountryPage;

	/** The mock language page. */
	@Mock
	private Page mockLanguagePage;

	/** The tag. */
	@Mock
	private Tag tag;

	/** The page tag. */
	private String pageTag = "corteva:consumerRegionCountry/northAmerica";

	/** The country selector bean. */
	private List<CountrySelectorBean> countrySelectorBean;

	/** The country bean. */
	private List<CountryBean> countryBean;

	/** The language bean. */
	private List<LanguageBean> languageBean;
	
	/** The regional page path. */
	private String regionalPagePath = "/content/corteva/na";

	/** The page path. */
	private String pagePath = "/content/corteva/na.html";

	/** The country path. */
	private String countryPath = "/content/corteva/na/us.html";

	/** The language path. */
	private String languagePath = "/content/corteva/na/us/en.html";

	/** The region title. */
	private String regionTitle = "North America";

	/** The region name. */
	private String regionName = "na";

	/** The mock page manager. */
	@Mock
	private PageManager mockPageManager;

	/** The mock request. */
	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/**
	 * Sets the up.
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest = getRequest();

		try {
			Field currentPagePath = countrySelectorModel.getClass().getDeclaredField("pagePath");
			currentPagePath.setAccessible(true);
			currentPagePath.set(countrySelectorModel, languagePath);

			Field baseServiceField;
			baseServiceField = countrySelectorModel.getClass().getDeclaredField("baseConfig");
			baseServiceField.setAccessible(true);
			baseServiceField.set(countrySelectorModel, baseService);

			Field requestField;
			requestField = countrySelectorModel.getClass().getDeclaredField("slingRequest");
			requestField.setAccessible(true);
			requestField.set(countrySelectorModel, request);

			context.registerAdapter(ResourceResolver.class, TagManager.class, tagManager);
			context.registerAdapter(ResourceResolver.class, QueryBuilder.class, mockQueryBuilder);

			Mockito.when(baseService.getPropConfigValue(request, CortevaConstant.COUNTRY_SELECTOR_ROOT_PATH,
					CortevaConstant.GLOBAL_CONFIG_NAME)).thenReturn(CortevaConstant.CONTENT_ROOT_PATH);
			
			Mockito.when(request.getResource()).thenReturn(mockResource);
			Mockito.when(request.getResourceResolver()).thenReturn(mockResourceResolver);
			Mockito.when(mockResource.getPath()).thenReturn(languagePath);
			
			Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
			Mockito.when(mockResourceResolver.adaptTo(QueryBuilder.class)).thenReturn(mockQueryBuilder);
			Mockito.when(mockResourceResolver.adaptTo(Session.class)).thenReturn(mockSession);
			Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(mockPageManager);
			Mockito.when(mockPageManager.getContainingPage(mockResource)).thenReturn(mockLanguagePage);
			Mockito.when(mockPageManager.getPage(Mockito.any(String.class))).thenReturn(mockLanguagePage);
			Mockito.when(mockLanguagePage.listChildren()).thenReturn(mockRegionIterator);
			Mockito.when(mockRegionIterator.hasNext()).thenReturn(true, false);
			Mockito.when(mockRegionIterator.next()).thenReturn(mockRegionPage);
			ValueMap properties = createPagePropertyValueMap();
			Mockito.when(mockRegionPage.getProperties()).thenReturn(properties);
			Mockito.when(mockRegionPage.getPath()).thenReturn(pagePath);
			Mockito.when(mockRegionPage.listChildren()).thenReturn(mockCountryIterator);
			Mockito.when(mockResourceResolver.resolve(languagePath)).thenReturn(mockResource);
			Mockito.when(mockResourceResolver.getResource(regionalPagePath)).thenReturn(mockResource);
			Mockito.when(mockPageManager.getContainingPage(mockResource)).thenReturn(mockRegionPage);
			Mockito.when(mockRegionPage.getContentResource()).thenReturn(mockJcrContentResource);
			Mockito.when(mockJcrContentResource.getValueMap()).thenReturn(mockValueMap);
			Mockito.when(mockValueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);
			Mockito.when(mockQueryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
					.thenReturn(mockQuery);
			final SearchResult searchResult = Mockito.mock(SearchResult.class);
			Mockito.when(mockQuery.getResult()).thenReturn(searchResult);
			Mockito.when(mockQuery.getResult().getResources()).thenReturn(mockIterator);
			Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
			Mockito.when(mockIterator.next()).thenReturn(mockResource);
			Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
			Mockito.when(mockPage.getProperties()).thenReturn(properties);
			Mockito.when(mockPage.getPath()).thenReturn(pagePath);
			Mockito.when(tagManager.resolve(pageTag)).thenReturn(tag);
			Mockito.when(tag.getTitle(Locale.forLanguageTag("en"))).thenReturn(regionTitle);
			Mockito.when(tag.getTitle()).thenReturn(regionTitle);
			Mockito.when(tag.getDescription()).thenReturn(regionName);
			Mockito.when(tag.getName()).thenReturn(regionName);
			Mockito.when(mockPage.listChildren()).thenReturn(mockCountryIterator);
			Mockito.when(mockCountryIterator.hasNext()).thenReturn(true, false);
			Mockito.when(mockCountryIterator.next()).thenReturn(mockCountryPage);
			Mockito.when(mockCountryPage.getProperties()).thenReturn(properties);
			Mockito.when(mockCountryPage.getPath()).thenReturn(countryPath);
			Mockito.when(mockCountryPage.listChildren()).thenReturn(mockLanguageIterator);
			Mockito.when(mockLanguageIterator.hasNext()).thenReturn(true, false);
			Mockito.when(mockLanguageIterator.next()).thenReturn(mockLanguagePage);
			Mockito.when(mockLanguagePage.getProperties()).thenReturn(properties);
			Mockito.when(mockLanguagePage.getPath()).thenReturn(languagePath);
			countrySelectorBean = Mockito.mock(List.class);
			countryBean = Mockito.mock(List.class);
			languageBean = Mockito.mock(List.class);
		} catch (NoSuchFieldException e) {
			Assert.fail("NoSuchFieldException  occurred in testInit(): " + e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.fail("IllegalAccessException  occurred in testInit(): " + e.getMessage());
		}

	}

	/**
	 * Test region details not null.
	 */
	@Test
	public void testRegionDetailsNotNull() {
		countrySelectorBean = getRegionData();
		for (CountrySelectorBean bean : countrySelectorBean) {
			Assert.assertEquals(bean.getRegionLink(), pagePath);
			Assert.assertEquals(bean.getRegionId(), regionName);
			Assert.assertEquals(bean.getRegionTitle(), regionTitle);
		}
	}

	/**
	 * Test country details not null.
	 */
	@Test
	public void testCountryDetailsNotNull() {
		countrySelectorBean = getRegionData();
		if (!countrySelectorBean.isEmpty()) {
			countryBean = getCountryData();
			for (CountryBean bean : countryBean) {
				Assert.assertEquals(bean.getCounrtyTitle(), regionTitle);
				Assert.assertEquals(bean.getCountryLink(), countryPath);
			}
		}
	}

	/**
	 * Test language details not null.
	 */
	@Test
	public void testLanguageDetailsNotNull() {
		countrySelectorBean = getRegionData();
		if (!countrySelectorBean.isEmpty()) {
			countryBean = getCountryData();
			languageBean = countryBean.get(0).getLanguageBeanList();
			for (LanguageBean bean : languageBean) {
				Assert.assertEquals(bean.getLanguageTitle(), regionTitle);
				Assert.assertEquals(bean.getLanguageLink(), languagePath);
			}
		}
	}

	/**
	 * Test locale not null.
	 */
	@Test
	public void testLocaleForInternationalization() {
		Assert.assertNotNull(countrySelectorModel.getLocaleForInternationalization());
	}

	/**
	 * Creates the page property value map.
	 *
	 * @return the value map
	 */
	private ValueMap createPagePropertyValueMap() {
		Map<String, Object> pagePropMap = new HashMap<>();
		String[] tags = { pageTag };
		pagePropMap.put(CortevaConstant.CQ_TAGS, tags);
		return new ValueMapDecorator(pagePropMap);
	}

	/**
	 * Gets the region data.
	 *
	 * @return the region data
	 */
	private List<CountrySelectorBean> getRegionData() {
		return countrySelectorModel.getRegionRelatedData();
	}

	/**
	 * Gets the country data.
	 *
	 * @return the country data
	 */
	private List<CountryBean> getCountryData() {
		return countrySelectorBean.get(0).getCountryBeanList();
	}
}
