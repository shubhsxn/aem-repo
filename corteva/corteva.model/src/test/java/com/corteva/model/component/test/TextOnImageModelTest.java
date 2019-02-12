package com.corteva.model.component.test;

import java.util.Map;

import javax.jcr.Property;
import javax.jcr.Value;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.models.TextOnImageModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

public class TextOnImageModelTest extends BaseAbstractTest {
	
	@InjectMocks
	private TextOnImageModel textOnImageModel;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The mocked page object. */
	@Mock
    private Page page;
	
	/** The mockResource. */
	@Mock
	private Resource mockResource; 
	
	/** The mockPage. */
	@Mock
	private Page mockPage;
	
	/** The mockPageMgr. */
	@Mock
	private PageManager mockPageMgr;
	
	/** The valueMap. */
	@Mock
	private ValueMap valueMap;
	
	/** The property. */
	@Mock
	private Property property;
	
	/** The value. */
	@Mock
    private Value value;
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;

	/** the mock imageRenditionBean */
	@Mock
	private ImageRenditionBean imageRenditionBean;
	
	/** the mock regCountryLangMap */
	@Mock
	private Map<String, String> regCountryLangMap;
	
	/**
	 * This method instantiates a new instance of TextOnImageModel.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");	
		PrivateAccessor.setField(textOnImageModel, "slingRequest", mockRequest);
	}

	/**
	 * Test method for getMobileImagePath.
	 */
	@Test
	public void testGetMobileImagePath() {
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/homepage/test/path");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(mockPageMgr);
		Mockito.when(mockPageMgr.getContainingPage(mockResource)).thenReturn(mockPage);
		Mockito.when(mockPage.getContentResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getValueMap()).thenReturn(valueMap);
		Assert.assertNull(textOnImageModel.getMobileImagePath());
	}
	
}
