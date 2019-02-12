package com.corteva.model.component.test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ArticleFilterModel;
import com.corteva.model.component.models.BioDetailModel;
import com.corteva.model.component.models.FindSalesRepRetailerModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * This is a test class for Find a sales rep retailer model
 *
 * @author Sapient
 */
public class FindSalesRepRetailerModelTest extends BaseAbstractTest {


    /**
     * The base service.
     */
    @Mock
    private BaseConfigurationService baseService;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource mockResource;

    @Mock
    private MockSlingHttpServletRequest mockRequest;

     /**
     * The mocked page object.
     */
    @Mock
    private Page page;

    @Mock
    private Session mockSession;

    /** The experience fragment model. */
    @InjectMocks
    private FindSalesRepRetailerModel findSalesRepRetailerModel =new FindSalesRepRetailerModel(mockRequest);
    /**
     * This method instantiates a new instance of Find a sales rep retailer model
     */
    @Before
    public void setUp() {
        try {
            MockitoAnnotations.initMocks(this);
            getContext().addModelsForPackage("com.corteva.model.component.models");
            Field baseServiceField;
            baseServiceField = findSalesRepRetailerModel.getClass().getDeclaredField("baseConfig");
            baseServiceField.setAccessible(true);
            baseServiceField.set(findSalesRepRetailerModel, baseService);
            Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
            Mockito.when(mockResource.getPath()).thenReturn("");
            Mockito.when(resourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);


        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail("IllegalAccessException  occurred in testInit(): " + e.getMessage());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("NoSuchFieldException  occurred in testInit(): " + e.getMessage());
        }
    }

    /**
     * Test case to get Locale .
     */
    @Test
    public void testGetApiUrl() {
        Assert.assertNotNull(findSalesRepRetailerModel.getApiUrl());
    }


}
