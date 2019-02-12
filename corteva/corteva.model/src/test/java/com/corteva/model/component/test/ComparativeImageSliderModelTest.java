package com.corteva.model.component.test;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ComparativeImageSliderModel;
import com.day.cq.wcm.api.Page;
import junitx.util.PrivateAccessor;

/**
 * This is a test class for Comparative Image Slider Model.
 * 
 * @author Sapient
 * 
 */
public class ComparativeImageSliderModelTest extends BaseAbstractTest {
	
	
	/** The experience fragment model. */
	@Mock
	private ComparativeImageSliderModel comparativeImageSliderModel;
	
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
	
	/** The current res path. */
	private String currentResPath;
	
	/** The mocked page object. */
	@Mock
    private Page page;
	
	@Mock
	private Resource mockResource; 
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The page property map. */
	Map<String, Object> pagePropMap = new HashMap<>();

	/**
	 * This method instantiates a new instance of Comparative Image Slider Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest.setServletPath("/content/corteva/corporate");
		currentResPath = "/content/corteva/corporate/image-slider-page/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		context.registerAdapter(Resource.class, Page.class, page);
		
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		comparativeImageSliderModel = getRequest().adaptTo(ComparativeImageSliderModel.class);
		
		Configuration myServiceConfig;
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("comparativeImageSlider", "comparativeImageSlider_desktop, comparativeImageSlider_tablet, comparativeImageSlider_mobile");
		props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
		myServiceConfig.update(props);
		PrivateAccessor.setField(comparativeImageSliderModel, "leftImage",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(comparativeImageSliderModel, "rightImage",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(comparativeImageSliderModel, "resourceType",
				"/apps/corteva/components/content/comparativeImageSlider/v1/comparativeImageSlider");	
	}
	
	/**
	 * Test method for locale.
	 */
	@Test
	public void testGetLocale() {
		Assert.assertNotNull(comparativeImageSliderModel.getLocale());
	}
	
	/**
	 * Test method for title.
	 */
	@Test
	public void testGetTitle() {
		Assert.assertNull(comparativeImageSliderModel.getTitle());
	}
	
	/**
	 * Test method for intro text.
	 */
	@Test
	public void testGetIntroText() {
		Assert.assertNull(comparativeImageSliderModel.getIntroText());
	}
	
	/**
	 * Test method for caption.
	 */
	@Test
	public void testGetCaption() {
		Assert.assertNull(comparativeImageSliderModel.getCaption());
	}

	/**
	 * Test method for left imgae.
	 */
	@Test
	public void testGetLeftImage() {
		Assert.assertNotNull(comparativeImageSliderModel.getLeftImage());
	}

	/**
	 * Test method for right image.
	 */
	@Test
	public void testGetRightImage() {
		Assert.assertNotNull(comparativeImageSliderModel.getRightImage());
	}


}
