package com.corteva.model.component.test;

import java.util.ArrayList;
import java.util.List;


import javax.jcr.Property;
import javax.jcr.RepositoryException;


import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.bean.HotSpotBean;
import com.corteva.model.component.models.HotSpotImageModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junitx.util.PrivateAccessor;

public class HotSpotImageModelTest extends BaseAbstractTest {
	
	/**
	 * The Injected hotSpotImageModel.
	 */
	@InjectMocks
	private HotSpotImageModel hotSpotImageModel;
	
	/**
	 * The baseConfigurationService.
	 */
	@Mock
	private BaseConfigurationService baseConfigurationService;
	
	/**
	 * The mockRequest.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/**
	 * The mockResourceResolver.
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
	private PageManager mockPageManager;
	
	/** The valueMap. */
	@Mock
	private ValueMap mockValueMap;
	
	/** The hotSpotBeanList. */
	private List<HotSpotBean> hotSpotBeanList;
	
	/** The property. */
	private Property property;
	
	/** The hotSpotList. */
	private List<HotSpotBean> hotSpotList;
	
	/**
	 * This method instantiates a new instance of Hot Spot Image Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		PrivateAccessor.setField(hotSpotImageModel, "hotSpotImage", "/content/dam/corteva/test/image.png");
		PrivateAccessor.setField(hotSpotImageModel, "title", "testTitle");
		PrivateAccessor.setField(hotSpotImageModel, "introText", "testIntroText");
		PrivateAccessor.setField(hotSpotImageModel, "caption", "testCaption");
		
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/homepage/test/path");
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(mockPageManager);
		Mockito.when(mockPageManager.getContainingPage(mockResource)).thenReturn(mockPage);
		Mockito.when(mockPage.getContentResource()).thenReturn(mockResource);
		Mockito.when(mockResource.getValueMap()).thenReturn(mockValueMap);
		
		hotSpotBeanList = new ArrayList<>();
		property = Mockito.mock(Property.class);
		HotSpotBean hotSpot = new HotSpotBean();
		hotSpot.setHotSpotTitle("Vegetative Stage");
		hotSpot.setHotSpotText("Beginning at about V6 increasing stalk");
		hotSpot.setModalId("phenology-1");
		hotSpot.setFirstCoOrdinate("59");
		hotSpot.setSecondCoOrdinate("269");
		hotSpot.setThirdCoOrdinate("99");
		hotSpot.setFourthCoOrdinate("309");
		hotSpot.setHrefId("#phenology-1");
		hotSpotBeanList.add(hotSpot);
	}
	
	/**
	 * Test method for getHotSpotItems()
	 */
	@Test
	public void testGetHotSpotItemsSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"hotSpotTitle\":\"Vegetative Stage\",\"hotSpotText\":\"Beginning at about V6 increasing stalk\", \"modalId\":\"phenology-1\"}"));
		    hotSpotList = hotSpotImageModel.getHotSpotItems(property);
		    for (HotSpotBean bean : hotSpotBeanList) {
		    	 for (HotSpotBean beanTest : hotSpotList) {
		    		 Assert.assertEquals(bean.getHotSpotTitle(),beanTest.getHotSpotTitle());
		    		 Assert.assertEquals(bean.getHotSpotText(),beanTest.getHotSpotText());
		    		 Assert.assertEquals(bean.getModalId(),beanTest.getModalId());
		    	 }
		    	
		    }
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetHotSpotItemsSuccess(): "+e.getMessage());
		}
	}

	/**
	 * Test method for getImageCordinates()
	 */
	@Test
	public void testGetImageCordinates() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"hotSpotImage\":\"/content/dam/dpagco/corteva/NA/US/english/corn-growth.jpg\"}"));
			String imageMap = "[rect(59,269,99,309)\"#phenology-1\"|\"\"|\"\"]";
			Mockito.when(mockResourceResolver.resolve("/content/dam/corteva/test/image.png")).thenReturn(mockResource);
			Mockito.when(mockResource.getChild(Mockito.anyString())).thenReturn(mockResource);
			Mockito.when(mockValueMap.get("imageMap")).thenReturn(imageMap);

			Assert.assertNotNull(hotSpotImageModel.getImageCordinates());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetImageCordinates(): "+e.getMessage());
		}
	}
	
	/**
	 * Test method for getTitle()
	 */
	@Test
	public void testGetTitle() {
		Assert.assertEquals("testTitle", hotSpotImageModel.getTitle());
	}

	/**
	 * Test method for getIntroText()
	 */
	@Test
	public void testGetIntroText() {
		Assert.assertEquals("testIntroText", hotSpotImageModel.getIntroText());
	}

	/**
	 * Test method for getCaption()
	 */
	@Test
	public void testGetCaption() {
		Assert.assertEquals("testCaption", hotSpotImageModel.getCaption());
	}
	
	/**
	 * Test method for getHotSpotImage()
	 */
	@Test
	public void testGetHotSpotImage() {
		Assert.assertNotNull(hotSpotImageModel.getHotSpotImage());
	}
	
	/**
	 * Test method for GetLocaleForInternationalization()
	 */
	@Test
	public void testGetLocaleForInternationalization() {
		Assert.assertNotNull(hotSpotImageModel.getLocaleForInternationalization());
	}
}
