package com.corteva.model.component.test;

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
import com.corteva.model.component.models.HeroModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

public class HeroModelTest extends BaseAbstractTest {
	
	/**
	 * The Injected heroModel.
	 */
	@InjectMocks
	private HeroModel heroModel;
	
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
	
	@Mock
	private Resource mockResource; 
	
	@Mock
	private Property property;
	
	/** The value. */
	@Mock
    private Value value;
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** the mock page manager. */
	@Mock
	private	PageManager mockPageManager;
	
	/** the mock page manager. */
	@Mock
	private	Page mockPage;
	
	/** the mock value map. */
	@Mock
	private	ValueMap mockValueMap;
	
	/**
	 * This method instantiates a new instance of Hero Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		PrivateAccessor.setField(heroModel, "mobileImagePath", "/content/corteva/homepage/test/path");
		PrivateAccessor.setField(heroModel, "heroTitle", "testHeroTitle");
		PrivateAccessor.setField(heroModel, "title", "testTitle");
		PrivateAccessor.setField(heroModel, "heroImagePath", "/content/corteva/homepage/test/path");
		PrivateAccessor.setField(heroModel, "viewType", "testViewType");
		PrivateAccessor.setField(heroModel, "resourceType", "/corteva/page-base");
		
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/homepage/test/path");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(mockPageManager);
		Mockito.when(mockPageManager.getContainingPage(mockResource)).thenReturn(mockPage);
		Mockito.when(mockPage.getContentResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getValueMap()).thenReturn(mockValueMap);
	}
	
	/**
	 * Test method for title.
	 */
	@Test
	public void testGetTitle() {
		Assert.assertNotNull(heroModel.getTitle());
	}
	
	/**
	 * Test method for hero title.
	 */
	@Test
	public void testGetHeroTitle() {
		Assert.assertNotNull(heroModel.getHeroTitle());
	}
	
	/**
	 * Test method for Mobile Image Path.
	 */
	@Test
	public void testGetMobileImagePath() {
		Assert.assertNotNull(heroModel.getMobileImagePath());
		
	}
	
	/**
	 * Test method for Hero Image Json.
	 */
	@Test
	public void testGetHeroImageJson() {
		Assert.assertNotNull(heroModel.getHeroImageJson());
	}
	
}
