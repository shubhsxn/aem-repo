package com.corteva.model.component.test;

import com.adobe.granite.ui.components.ds.DataSource;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.DynamicFacetTypeListModel;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.resourceresolver.MockResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * This is a test class for DynamicFacetTypeListModel.
 *
 * @author Sapient
 *
 */
public class DynamicFacetTypeListModelTest extends BaseAbstractTest {

    /** The navSearch model. */
    @Mock
    private DynamicFacetTypeListModel dynamicFacetTypeListModel;

    /** The mock Request */
    @Mock
    private MockSlingHttpServletRequest mockRequest;
    
    /** The mock Request */
    @Mock
    private MockResourceResolver mockResourceResolver;
    
    /** The mock Request */
    @Mock
    private Tag mockTag;
    
    /** The mock Request */
    @Mock
    private TagManager mockTagManager;
    
  

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.registerInjectActivateService(new BaseConfigurationService());
        getContext().addModelsForPackage("com.corteva.model.component.models");
        dynamicFacetTypeListModel=new DynamicFacetTypeListModel();
        Mockito.when(mockRequest.adaptTo(ResourceResolver.class)).thenReturn(mockResourceResolver);
        Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(mockTagManager);
        Mockito.when(mockTagManager.resolve(Mockito.anyString())).thenReturn(mockTag);
        List<Tag> list = new ArrayList<>();
        list.add(mockTag);
        list.add(mockTag);
        Field viewTypeField;
		viewTypeField = dynamicFacetTypeListModel.getClass().getDeclaredField("resourceResolver");
		viewTypeField.setAccessible(true);
		viewTypeField.set(dynamicFacetTypeListModel,mockResourceResolver);
		 Field slingReqField;
		 slingReqField = dynamicFacetTypeListModel.getClass().getDeclaredField("slingRequest");
		 slingReqField.setAccessible(true);
		 slingReqField.set(dynamicFacetTypeListModel,mockRequest);
        Mockito.when(mockTag.listChildren()).thenReturn(list.iterator());
      
    }

    @Test
    public void testInit() throws Exception {
    	dynamicFacetTypeListModel.init();
    	Assert.assertNull(mockRequest.getAttribute(DataSource.class.getName()));
    }
}