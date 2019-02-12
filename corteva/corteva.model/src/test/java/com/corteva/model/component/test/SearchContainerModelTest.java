package com.corteva.model.component.test;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.SearchContainerModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * This is the test class to test the SearchContainerModel
 */
public class SearchContainerModelTest extends BaseAbstractTest {

    private static final String TEST_DATA_SEARCH_URL = "www.google.com";

	/** The searchCotainer model. */
    @Mock
    private SearchContainerModel searchContainerModel;

    /** The base service. */
    @Mock
    private BaseConfigurationService baseService;

    /** The mock request */
    @Mock
    private MockSlingHttpServletRequest mockRequest;

    /** The resource Resolver */
    @Mock
    private ResourceResolver resourceResolver;

    /** The config admin. */
    @Mock
    private ConfigurationAdmin configAdmi;

    /** The dictionary. */
    @Mock
    private Dictionary<String, Object> dict;

    /** The config. */
    @Mock
    private Configuration config;
    /**
     * The mocked page object.
     */
    @Mock
    private Page page;
    
    /** The Constant CONTENT_PATH. */
	private static final String CONTENT_PATH = "/content/corteva/na/us/en/test1";
    
    @Mock
    private Resource mockResource;

    /**
     * Method to set up the test objects.
     */

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.registerInjectActivateService(new BaseConfigurationService());
        ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
        Configuration myServiceConfig;
        myServiceConfig = configAdmin.getConfiguration(CortevaConstant.SEARCH_CONFIG_NAME);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("minCharacterLength","3");
        props.put("maxSuggestionLength",5);
        props.put("searchUrl",TEST_DATA_SEARCH_URL);
        props.put("autoCompleteUrl",TEST_DATA_SEARCH_URL);
        props.put("sortOption","ABC###ABC,DEF###DEF");
        props.put("filterOption","Career###q1=Career");
        baseService = context.getService(BaseConfigurationService.class);
        myServiceConfig.update(props);
        getContext().addModelsForPackage("com.corteva.model.component.models");
        mockRequest = getRequest();
        context.create().resource(CONTENT_PATH);
        mockRequest.setResource(context.currentResource(CONTENT_PATH));
        mockRequest.setServletPath(CONTENT_PATH);
        mockPage();
        String[] tagArr={"corteva:contentTypes/career","corteva:contentTypes/event"};
        String[] customArr={TEST_DATA_SEARCH_URL,"www.corteva.com"};
        PageManager pageManager = Mockito.mock(PageManager.class);
		Mockito.when(pageManager.getContainingPage(Mockito.any(Resource.class)))
				.thenReturn(page);
		context.registerAdapter(Resource.class, Page.class, mockPage());
        searchContainerModel = mockRequest.adaptTo(SearchContainerModel.class);
        searchContainerModel.setResourceResolver(getResourceResolver());
        searchContainerModel.setTagArr(tagArr);
        searchContainerModel.setCustomFilters(customArr);
    }


    /**
     * Test Search url
     */

    @Test
    public void testGetSearchUrl() throws Exception {
        Assert.assertEquals(TEST_DATA_SEARCH_URL,searchContainerModel.getSearchUrl());
    }

    /**
     * Test Search Autocomplete url
     */
    @Test
    public void testGetAutoCompleteUrl() throws Exception {
        Assert.assertEquals(TEST_DATA_SEARCH_URL,searchContainerModel.getAutoCompleteUrl());
    }

    /**
     * Test Maximum Suggestion
     */
    @Test
    public void testGetMaximumSuggestion() throws Exception {
        Assert.assertEquals("5",searchContainerModel.getMaximumSuggestion());

    }

    /**
     * Test Minimum Character
     */
   @Test
    public void testGetMinimumCharacter() throws Exception {
        Assert.assertEquals("3",searchContainerModel.getMinimumCharacter());
    }

    /**
     * Test Sort Options
     */
    @Test
    public void testGetSortOptions() throws Exception {
            List<Map<String,String>> list=searchContainerModel.getSortOptions();
            Assert.assertEquals(2,list.size());
    }

    /**
     * Test SearchBar Text
     */
    @Test
    public void testGetFilterOptions() throws Exception {
        List<Map<String,String>> list=searchContainerModel.getFilterOptions();
        Assert.assertEquals(2,list.size());
    }
    /**
     * Method that mock page and content Resource
     * @throws WCMException 
     */
   private Page mockPage() {
		String currentResPath = "/content/corteva/na/us/en/jcr:content";
		context.create().resource(currentResPath);
		try {
			page = context.pageManager().create("/content/corteva/na/us/en", "test1", "/apps/sample/templates/homepage",
					"title1");
		} catch (WCMException e) {
			Assert.fail("WCMException occurred in: " + e.getMessage());
		}
		return page;

    }
}