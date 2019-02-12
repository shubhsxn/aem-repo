package com.corteva.model.component.test;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

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

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.GalleryVideoPlayerModel;
import com.day.cq.wcm.api.Page;
import junitx.util.PrivateAccessor;

/**
 * This is a test class for Gallery Video Player Model.
 * 
 * @author Sapient
 * 
 */
public class GalleryVideoPlayerModelTest extends BaseAbstractTest {
	
	
	/** The experience fragment model. */
	@Mock
	private GalleryVideoPlayerModel galleryVideoPlayerModel;
	
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
	
	/** the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
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
		galleryVideoPlayerModel = getRequest().adaptTo(GalleryVideoPlayerModel.class);
		
		PrivateAccessor.setField(galleryVideoPlayerModel, "videoDetailsS7", property);
		PrivateAccessor.setField(galleryVideoPlayerModel, "videoDetails", property);	
	}
	
	/**
	 * Test method for YoutubeVideoDetails.
	 */
	@Test
	public void testGetYoutubeVideoDetails() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"videoId\":\"1234\",\"videoTitle\":\"Video Title\",\"videoDescription\":\"Video Description\",\"videoAltText\":\"Video Alt text\",\"videoThumbnail\":\"/content/dam/corteva/Product_catalog.jpg\"}"));
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(galleryVideoPlayerModel.getYoutubeVideoDetails());
	}
	
	/**
	 * Test method for SceneSevenVideoDetails.
	 */
	@Test
	public void testGetSceneSevenVideoDetails() {
		try {
			Mockito.when(property.isMultiple()).thenReturn(false);
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"videoPath\":\"/content/dam/corteva/Product_catalog.jpg\",\"videoTitleS7\":\"Video Title\",\"videoDescriptionS7\":\"Video Description\",\"videoAltTextS7\":\"Video Alt text\",\"videoThumbnailS7\":\"/content/dam/corteva/Product_catalog.jpg\"}"));
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(galleryVideoPlayerModel.getSceneSevenVideoDetails());
	}
	
	/**
	 * Test method for Youtube Api.
	 */
	@Test
	public void testGetYoutubeApi() {
		Assert.assertNotNull(galleryVideoPlayerModel.getYoutubeApi());
	}
	
	/**
	 * Test method for Youtube Key.
	 */
	@Test
	public void testGetYoutubeKey() {
		Assert.assertNotNull(galleryVideoPlayerModel.getYoutubeKey());
	}

}
