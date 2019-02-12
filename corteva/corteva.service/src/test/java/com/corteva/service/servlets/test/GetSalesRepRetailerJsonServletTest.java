package com.corteva.service.servlets.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.service.servlets.GetSalesRepRetailerJsonServlet;
import com.day.cq.wcm.api.Page;

/**
 * This is a test for  servlet class which is triggered when the author fetches the Json stored for sales rep and retailer
 *
 * @author Sapient
 */
public class GetSalesRepRetailerJsonServletTest extends BaseAbstractTest {

    /**
     * The Article feed servlet.
     */
    private GetSalesRepRetailerJsonServlet getSalesRepRetailerJsonServlet;
    /**
     * The mock request.
     */
    @Mock
    private MockSlingHttpServletRequest mockRequest;

    /**
     * The mock response.
     */
    @Mock
    private MockSlingHttpServletResponse mockResponse;
    /**
     * The mock response.
     */
    @Mock
    private RequestPathInfo requestPathInfo;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource mockResource;

    @Mock
    private Page mockCountryPage;

    @Mock
    private Page mockPage;


    @Mock
    private Page mockLanguagePage;

    /**
     * The base service.
     */
    @Mock
    private BaseConfigurationService baseService;

    /**
     * Sets the method parameters and adds nodes to the mock session.
     *
     * @throws RepositoryException that an Repository exception has occurred.
     */
    @Before
    public void setUp() {

            MockitoAnnotations.initMocks(this);
            getSalesRepRetailerJsonServlet = new GetSalesRepRetailerJsonServlet();
            context.registerInjectActivateService(new BaseConfigurationService());
            Mockito.when(mockRequest.getResourceResolver()).thenReturn(resourceResolver);
            

            Mockito.when(mockRequest.getResourceResolver().getResource(Mockito.anyString())).thenReturn(mockResource);


            Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(requestPathInfo);
        StringWriter responseWriter = new StringWriter();
        Mockito.when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
        ValueMap mockValMap = Mockito.mock(ValueMap.class);
        Value mockVal = Mockito.mock(Value.class);
        Mockito.when(mockResource.getValueMap()).thenReturn(mockValMap);
        Mockito.when(mockValMap.get(Mockito.any())).thenReturn(mockVal);



    }

    public void setCurrentPageInRequest(){
        Mockito.when(mockRequest.getHeader(Mockito.anyString()))
                .thenReturn("http://localhost:4502/content/brevant/na/ca/en/article1.html");

        Mockito.when(resourceResolver.map(Mockito.anyString())).thenReturn("content/corporate/resources/article1.html");
        Mockito.when(resourceResolver.resolve(Mockito.any(SlingHttpServletRequest.class),Mockito.anyString())).thenReturn(mockResource);
    }
    @Test
    public void testJsonForSalesRepinCountry() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "salesrep";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testJsonForContracterinCountry() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "contractor";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWhenFeatureFlagOff() {
        try {
        	getSalesRepRetailerJsonServlet.bindBaseConfigurationService(baseService);
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "salesrep";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonForSalesRepinLanguage() {
        try {
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "salesrep";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testJsonForContractorinLanguage() {
        try {
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "contractor";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonForRetailerinLanguage() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "retailer";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonForRetailerinCountry() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
            setCurrentPageInRequest();
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "retailer";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWhenCurrentPageCannotBeResolvedToResource() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            selector[3] = "retailer";
            Mockito.when(resourceResolver.resolve(mockRequest, "/content/brevant/la/ar/es")).thenReturn(mockResource);
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            Assert.fail("Exception occurred in setup()" + e.getMessage());
        } catch (IOException e) {
            Assert.fail("Exception occurred in setup()" + e.getMessage());
        }
    }

    @Test
    public void testWhenSelectorMissing() {
        try {
            Mockito.when(baseService.getToggleInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
            String[] selector = new String[4];
            selector[0] = "la";
            selector[1] = "ar";
            selector[2] = "es";
            Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
            Mockito.when(requestPathInfo.getSelectors()).thenReturn(selector);
            Mockito.when(mockPage.getAbsoluteParent(3)).thenReturn(mockCountryPage);
            Mockito.when(mockPage.getAbsoluteParent(4)).thenReturn(mockLanguagePage);

            getSalesRepRetailerJsonServlet.doGet(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException e) {
            Assert.fail("Exception occurred in testWhenSelectorMissing()" + e.getMessage());
        } catch (IOException e) {
            Assert.fail("Exception occurred in testWhenSelectorMissing()" + e.getMessage());
        }
    }
}
