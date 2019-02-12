package com.corteva.model.component.test;

import java.util.Iterator;
import java.util.List;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.value.StringValue;
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
import com.corteva.model.component.bean.HeroCarousalBean;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.models.HeroCarousalModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junitx.util.PrivateAccessor;

public class HeroCarouselModelTest extends BaseAbstractTest {
	
	/**
	 * The Injected herocarouselModel.
	 */
	@InjectMocks
	private HeroCarousalModel herocarouselModel;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private BaseConfigurationService mockedBaseService;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/**
	 * The mocked mockResource object.
	 */
	@Mock
	private Resource mockResource;
	
	/**
	 * The mocked mockResourceResolver object.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;

	/**
	 * The mocked heroCarousalBean object.
	 */
	@Mock
	private HeroCarousalBean heroCarousalBean;
	
	/**
	 * The mocked slideList object.
	 */
	@Mock
	private List<HeroCarousalBean> slideList;
	
	/**
	 * The mocked slideJsonList object.
	 */
	@Mock
	private List<JsonObject> slideJsonList;
	
	/**
	 * The mocked property object.
	 */
	@Mock
	private Property property;
	
	/**
	 * The mocked value object.
	 */
	@Mock
	private Value value;
	
	/**
	 * The mocked valueMap object.
	 */
	@Mock
	private ValueMap valueMap ;
	
	/**
	 * The mockIterator.
	 */
	@Mock
	private Iterator<JsonObject> mockIterator;
	
	/**
	 * The jsonElement.
	 */
	@Mock
	private JsonElement jsonElement;
	
	/**
	 * The imageRenditionBean.
	 */
	@Mock
	private ImageRenditionBean imageRenditionBean;
		
	/**
	 * This method instantiates a new instance of Hero Carousel Slide Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");		
		property = Mockito.mock(Property.class);
		PrivateAccessor.setField(herocarouselModel, "heroSlides", property);
		PrivateAccessor.setField(herocarouselModel, "resourceType", "/page-base");
		PrivateAccessor.setField(herocarouselModel, "request", mockRequest);
		PrivateAccessor.setField(herocarouselModel, "request", mockRequest);
	}

	/**
	 * Test method for carousel slides.
	 * @throws RepositoryException 
	 * @throws ValueFormatException 
	 */
	@Test
	public void testGetCarousalSlides() throws ValueFormatException, RepositoryException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("panelTitle", "panelTitle");
		jsonObject.addProperty("eyebrowLabel", "eyebrowLabel");
		jsonObject.addProperty("altText", "altText");
		jsonObject.addProperty("imageSrc", "imageSrc");
		jsonObject.addProperty("mobImage", "mobImage");
		jsonObject.addProperty("buttonText", "buttonText");
		jsonObject.addProperty("buttonLink", "buttonLink");
		jsonObject.addProperty("buttonAction", "buttonAction");
		
		Value[] valueArr = new Value[1];
		valueArr[0] = new StringValue(jsonObject.toString());
		
		Mockito.when(property.getValue()).thenReturn(valueArr[0]);
		Mockito.when(value.getString()).thenReturn("the Value");
		
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/test/path");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		
		
		Assert.assertNotNull(herocarouselModel.getCarousalSlides());	
	}
}
