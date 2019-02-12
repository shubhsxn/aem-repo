package com.corteva.model.component.test;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.jackrabbit.value.StringValue;
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
import com.corteva.model.component.models.GalleryImageModel;
import com.day.cq.wcm.api.Page;
import junitx.util.PrivateAccessor;

/**
 * This is a test class for Gallery Image Model.
 * 
 * @author Sapient
 * 
 */
public class GalleryImageModelTest extends BaseAbstractTest {
	
	
	/** The experience fragment model. */
	@Mock
	private GalleryImageModel galleryImageModel;
	
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
	
	@Mock
	private Property property;
	
	/** The value. */
	@Mock
    private Value value;
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/** The page property map. */
	Map<String, Object> pagePropMap = new HashMap<>();

	/**
	 * This method instantiates a new instance of Gallery Image Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockRequest = getRequest();
		getContext().addModelsForPackage("com.corteva.model.component.models");
		mockRequest.setServletPath("/content/corteva/corporate");
		currentResPath = "/content/corteva/corporate/gallery-page/jcr:content";
		context.create().resource(currentResPath);
		context.currentResource(currentResPath);
		context.registerAdapter(Resource.class, Page.class, page);
		
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		galleryImageModel = getRequest().adaptTo(GalleryImageModel.class);
		
		Configuration myServiceConfig;
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("comparativeImageSlider", "comparativeImageSlider_desktop, comparativeImageSlider_tablet, comparativeImageSlider_mobile");
		props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
		myServiceConfig.update(props);
		PrivateAccessor.setField(galleryImageModel, "gallery", property);
		PrivateAccessor.setField(galleryImageModel, "resourceType",
				"/apps/corteva/components/content/comparativeImageSlider/v1/comparativeImageSlider");	
	}
	
	/**
	 * Test method for Gallery Images.
	 */
	@Test
	public void testGetGalleryImages() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"altText\":\"Alt text\",\"imagePath\":\"/content/dam/corteva/Product_catalog.jpg\",\"imageCaption\":\"Image Caption\"}"));
			Mockito.when(value.getString()).thenReturn("Test");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(galleryImageModel.getGalleryImages());
	}
	
	/**
	 * Test method for title.
	 */
	@Test
	public void testGetTitle() {
		Assert.assertNull(galleryImageModel.getTitle());
	}
	
	/**
	 * Test method for intro text.
	 */
	@Test
	public void testGetIntroText() {
		Assert.assertNull(galleryImageModel.getIntroText());
	}

}
