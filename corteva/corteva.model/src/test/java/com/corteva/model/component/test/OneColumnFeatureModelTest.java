package com.corteva.model.component.test;

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
import com.corteva.model.component.models.OneColumnFeatureModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

/**
 * The Class OneColumnFeatureModelTest.
 */
public class OneColumnFeatureModelTest extends BaseAbstractTest {

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	@Mock
	private Resource mockResource;
	
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The mockPageMgr. */
	@Mock
	private PageManager mockPageMgr;
	
	/** The valueMap. */
	@Mock
	private ValueMap valueMap;
	
	/** The mocked page object. */
	@Mock
    private Page page;
	
	/** The experience fragment model. */
	@InjectMocks
	private OneColumnFeatureModel oneColumnFeatureModel =new OneColumnFeatureModel(mockRequest);

	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		PrivateAccessor.setField(oneColumnFeatureModel, "buttonLink",
				"/content/corteva/corporate/homepage");
		PrivateAccessor.setField(oneColumnFeatureModel, "imagePath",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(oneColumnFeatureModel, "bleedImagePath",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(oneColumnFeatureModel, "resourceType",
				"/apps/corteva/components/content/oneColumnFeature/v1/oneColumnFeature");
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/corporate/homepage");		
	}

	/**
	 * Test alt text.
	 */
	@Test
	public void testAltText() {
		Assert.assertNull(oneColumnFeatureModel.getAltText());
	}

	/**
	 * Test bleed alt text.
	 */
	@Test
	public void testBleedAltText() {
		Assert.assertNull(oneColumnFeatureModel.getBleedAltText());
	}

	/**
	 * Test body text.
	 */
	@Test
	public void testBodyText() {
		Assert.assertNull(oneColumnFeatureModel.getBodyText());
	}

	/**
	 * Test button action.
	 */
	@Test
	public void testButtonAction() {
		Assert.assertNull(oneColumnFeatureModel.getButtonAction());
	}

	/**
	 * Test button link.
	 */
	@Test
	public void testButtonLink() {
		Assert.assertNotNull(oneColumnFeatureModel.getButtonLink());
	}

	/**
	 * Test button text.
	 */
	@Test
	public void testButtonText() {
		Assert.assertNull(oneColumnFeatureModel.getButtonText());
	}

	/**
	 * Test full bleed.
	 */
	@Test
	public void testFullBleed() {
		Assert.assertFalse(oneColumnFeatureModel.getFullBleed());
	}

	/**
	 * Test resource type.
	 */
	@Test
	public void testResourceType() {
		Assert.assertNotNull(oneColumnFeatureModel.getResourceType());
	}
	
	/**
	 * Test title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(oneColumnFeatureModel.getTitle());
	}
	
	/**
	 * Test view type.
	 */
	@Test
	public void testViewType() {
		Assert.assertNull(oneColumnFeatureModel.getViewType());
	}
	
	/**
	 * Test image path.
	 */
	@Test
	public void testImagePath() {
		Assert.assertNotNull(oneColumnFeatureModel.getImagePath());
	}
	
	/**
	 * Test Bleed Image Path.
	 */
	@Test
	public void testBleedImagePath() {
		Assert.assertNotNull(oneColumnFeatureModel.getBleedImagePath());
	}

}
