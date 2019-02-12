package com.corteva.model.component.test;

import java.lang.reflect.Field;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.ProductTechnicalSpecsItemModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for ProductTechnicalSpecsItemModel Model.
 * 
 * @author Sapient
 * 
 */
public class ProductTechnicalSpecsItemModelTest extends BaseAbstractTest {
	
	/** The mock resource resolver. */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The mock request. */
	@Mock
	private SlingHttpServletRequest request;
	
	/** The mock resource. */
	@Mock
	private Resource resource;
	
	/** The mock page manager. */
	@Mock
	private PageManager pageManager;
	
	/** The mock page. */
	@Mock
	private Page page;
	
	/** the ProductEfficacy model */
	@InjectMocks
	private ProductTechnicalSpecsItemModel productTechnicalSpecsItemModel;
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** Reflection fields */
	private String componentName;
	private String sectionLabel;
	private String sectionDetail;
	private String mediaType;
	private String desktopImageUrl;
	private String imgAltText;
	private String videoId;
	private String videoUrl;
	private String vidTitle;
	private String vidDesc;
	private String vidAltText;
	private String thumbnailImage;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		
		componentName = "productTechnicalSpecs";
		String pagePath = "/content/corteva/na/US/en";
		
		sectionLabel = "pdp.label.application.method";
		Field sectionLabelTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("sectionLabel");
		sectionLabelTypeField.setAccessible(true);
		sectionLabelTypeField.set(productTechnicalSpecsItemModel, sectionLabel);
		
		sectionDetail = "testDetail";
		Field sectionDetailTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("sectionDetail");
		sectionDetailTypeField.setAccessible(true);
		sectionDetailTypeField.set(productTechnicalSpecsItemModel, sectionDetail);
		
		mediaType = "image";
		Field mediaTypeTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("mediaType");
		mediaTypeTypeField.setAccessible(true);
		mediaTypeTypeField.set(productTechnicalSpecsItemModel, mediaType);
		
		desktopImageUrl = "/content/dam/corteva/Desktop.jpg";
		Field desktopImageUrlTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("desktopImageUrl");
		desktopImageUrlTypeField.setAccessible(true);
		desktopImageUrlTypeField.set(productTechnicalSpecsItemModel, desktopImageUrl);
		
		imgAltText = "testImgAltText";
		Field imgAltTextTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("imgAltText");
		imgAltTextTypeField.setAccessible(true);
		imgAltTextTypeField.set(productTechnicalSpecsItemModel, imgAltText);
		
		videoId = "testVideoId";
		Field videoIdTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("videoId");
		videoIdTypeField.setAccessible(true);
		videoIdTypeField.set(productTechnicalSpecsItemModel, videoId);
		
		videoUrl = "/content/dam/corteva/Video.mp4";
		Field videoUrlTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("videoUrl");
		videoUrlTypeField.setAccessible(true);
		videoUrlTypeField.set(productTechnicalSpecsItemModel, videoUrl);
		
		vidTitle = "testVidTitle";
		Field vidTitleTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("vidTitle");
		vidTitleTypeField.setAccessible(true);
		vidTitleTypeField.set(productTechnicalSpecsItemModel, vidTitle);
		
		vidDesc = "testVidDesc";
		Field vidDescTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("vidDesc");
		vidDescTypeField.setAccessible(true);
		vidDescTypeField.set(productTechnicalSpecsItemModel, vidDesc);
		
		vidAltText = "testVidAltText";
		Field vidAltTextTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("vidAltText");
		vidAltTextTypeField.setAccessible(true);
		vidAltTextTypeField.set(productTechnicalSpecsItemModel, vidAltText);
		
		thumbnailImage = "/content/dam/corteva/thumbnail.jpg";
		Field thumbnailImageTypeField = productTechnicalSpecsItemModel.getClass().getDeclaredField("thumbnailImage");
		thumbnailImageTypeField.setAccessible(true);
		thumbnailImageTypeField.set(productTechnicalSpecsItemModel, thumbnailImage);
		
		context.create().resource(desktopImageUrl);
		context.create().resource(videoUrl);
		context.create().resource(thumbnailImage);
		
		productTechnicalSpecsItemModel.setRequest(request);
		productTechnicalSpecsItemModel.setComponentName(componentName);
		
		Mockito.when(request.getResource()).thenReturn(resource);
		Mockito.when(request.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(resource.getPath()).thenReturn(pagePath);
		Mockito.when(mockResourceResolver.resolve(pagePath)).thenReturn(resource);
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
		
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getSectionLabel()}.
	 */
	@Test
	public void testGetSectionLabel() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getSectionLabel());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getSectionDetail()}.
	 */
	@Test
	public void testGetSectionDetail() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getSectionDetail());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getMediaType()}.
	 */
	@Test
	public void testGetMediaType() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getMediaType());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getDesktopImageUrl()}.
	 */
	@Test
	public void testGetDesktopImageUrl() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getDesktopImageUrl());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getImgAltText()}.
	 */
	@Test
	public void testGetImgAltText() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getImgAltText());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getVideoId()}.
	 */
	@Test
	public void testGetVideoId() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getVideoId());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getVideoUrl()}.
	 */
	@Test
	public void testGetVideoUrl() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getVideoUrl());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getVidTitle()}.
	 */
	@Test
	public void testGetVidTitle() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getVidTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getVidDesc()}.
	 */
	@Test
	public void testGetVidDesc() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getVidDesc());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getVidAltText()}.
	 */
	@Test
	public void testGetVidAltText() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getVidAltText());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductTechnicalSpecsItemModel#getThumbnailImage()}.
	 */
	@Test
	public void testGetThumbnailImage() {
		Assert.assertNotNull(productTechnicalSpecsItemModel.getThumbnailImage());
	}

}
