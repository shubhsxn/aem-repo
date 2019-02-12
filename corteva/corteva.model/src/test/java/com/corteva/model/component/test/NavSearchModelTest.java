package com.corteva.model.component.test;

import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.sling.api.resource.Resource;
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
import com.corteva.model.component.models.NavSearchModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * This is the test class to test the NavSearchModel.
 */
public class NavSearchModelTest extends BaseAbstractTest {

    /** The navSearch model. */
    @Mock
    private NavSearchModel navSearchModel;

    /** The base service. */
    @Mock
    private BaseConfigurationService baseService;

    /** The mock Request */
    @Mock
    private MockSlingHttpServletRequest mockRequest;

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
    /**
     * Method to set up the test objects.
     */
    
    /** The Constant CONTENT_PATH. */
	private static final String CONTENT_PATH = "/content/corteva/na/us/en/test1";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
        Configuration myServiceConfig;
        context.registerInjectActivateService(new BaseConfigurationService());
        myServiceConfig = configAdmin.getConfiguration(CortevaConstant.SEARCH_CONFIG_NAME);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("minCharacterLength","3");
        props.put("maxSuggestionLength",5);
        props.put("autoCompleteUrl","www.google.com");
        baseService = context.getService(BaseConfigurationService.class);
        myServiceConfig.update(props);
        getContext().addModelsForPackage("com.corteva.model.component.models");
        mockRequest = getRequest();
        context.create().resource(CONTENT_PATH);
        mockRequest.setServletPath(CONTENT_PATH);
        mockRequest.setResource(context.currentResource(CONTENT_PATH));
        context.registerAdapter(Resource.class, Page.class, mockPageNavSearch());
        PageManager pageManager = Mockito.mock(PageManager.class);
		Mockito.when(pageManager.getContainingPage(context.currentResource(CONTENT_PATH))).thenReturn(page);
        navSearchModel=mockRequest.adaptTo(NavSearchModel.class);
        navSearchModel.setSearchBarText("Search");
        navSearchModel.setSearchFormAction("/content/corteva/searchtest");
    }

    /**
     * Test SearchBar Text
     */
   @Test
    public void testGetSearchBarText() throws Exception {
        Assert.assertNotNull(navSearchModel.getSearchBarText());
    }

    /**
     * Test Search Form Action
     */

   @Test
    public void testGetSearchFormAction() throws Exception {
       Assert.assertTrue(navSearchModel.getSearchFormAction().endsWith(CortevaConstant.HTML_EXTENSION));
    }

    /**
     * Test Autocomplete Url
     */

   @Test
    public void testGetAutoCompleteUrl() throws Exception {

        Assert.assertEquals("www.google.com",navSearchModel.getAutoCompleteUrl());
    }

    /**
     * Test Maximum suggestion
     */

   @Test
    public void testGetMaximumSuggestion() throws Exception {
        Assert.assertEquals("5",navSearchModel.getMaximumSuggestion());
    }

    /**
     * Test Minimum Character
     */

   @Test
    public void testGetMinimumCharacter() throws Exception {
        Assert.assertEquals("3",navSearchModel.getMinimumCharacter());
    }
    /**
     * Method that mock page and content Resource
     */
    private Page mockPageNavSearch() {
        try {
			page = context.pageManager().create("/content/corteva/na/us/en", "test1",
			        "/apps/sample/templates/homepage", "title1");
        } catch (WCMException e) {
			Assert.fail("WCMException occurred in: " + e.getMessage());
		}
        return page;

    }
}