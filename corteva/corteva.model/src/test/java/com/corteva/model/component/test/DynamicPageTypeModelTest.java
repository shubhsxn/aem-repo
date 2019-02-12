package com.corteva.model.component.test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.DynamicPageTypeModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * This is a test class for PageMetaModel.
 *
 * @author Sapient
 *
 */
public class DynamicPageTypeModelTest extends BaseAbstractTest {

    /** The navSearch model. */
    @Mock
    private DynamicPageTypeModel dynamicPageTypeModel;

    /** The mock Request */
    @Mock
    private MockSlingHttpServletRequest mockRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.registerInjectActivateService(new BaseConfigurationService());
        getContext().addModelsForPackage("com.corteva.model.component.models");
        mockRequest = getRequest();
        mockRequest.setServletPath("/content/corteva/na/us/en/test1");
        dynamicPageTypeModel=mockRequest.adaptTo(DynamicPageTypeModel.class);
        dynamicPageTypeModel.setResourceResolver(getResourceResolver());
    }

    @Test
    public void testInit() throws Exception {
        Assert.assertEquals(0,dynamicPageTypeModel.init().size());
    }
    
    @Test
    public void testGetLocale() throws Exception {
    	mockRequest = Mockito.mock(MockSlingHttpServletRequest.class);
    	ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
    	Resource resource = Mockito.mock(Resource.class);
    	PageManager pageMgr = Mockito.mock(PageManager.class);
    	Page page = Mockito.mock(Page.class);
    	ValueMap valueMap = Mockito.mock(ValueMap.class);
    	Mockito.when(mockRequest.getResourceResolver()).thenReturn(resolver);
    	Mockito.when(resolver.getResource(Mockito.anyString())).thenReturn(resource);
    	Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageMgr);
    	Mockito.when(pageMgr.getContainingPage(resource)).thenReturn(page);
    	Mockito.when(page.getContentResource()).thenReturn(resource);
    	Mockito.when(resource.getValueMap()).thenReturn(valueMap);
    	Mockito.when(mockRequest.getParameter(CortevaConstant.ITEM)).thenReturn("/content/corteva/na/us/en/homepage");
    	Mockito.when(mockRequest.getHeader(CortevaConstant.REFERER)).thenReturn("/mnt/overlay/wcm/core/content/sites/createpagewizard.html/content/corteva/na/us/en/homepage");
    	Assert.assertEquals(dynamicPageTypeModel.getLocale(mockRequest), new Locale(CortevaConstant.EN.concat("_").concat(CortevaConstant.US)));
    }
}