package com.corteva.model.component.test;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Property;

import org.apache.jackrabbit.value.StringValue;
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
import com.corteva.model.component.bean.SocialLinkBean;
import com.corteva.model.component.models.FooterElementsModel;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;

import junitx.util.PrivateAccessor;

public class FooterElementsModelTest extends BaseAbstractTest{
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
    
    /** The sling request. */
    @Mock
    private MockSlingHttpServletRequest mockRequest;
    
	@Mock
	private Resource mockResource;

    /** The Footer bean list. */
    private List<SocialLinkBean> socialLinkBeanList;
    
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	@Mock
	private Property property;
	
	/** The experience fragment model. */
	@InjectMocks
	private FooterElementsModel footerModel =new FooterElementsModel(mockRequest);

    /**
     * Method to set up the test objects.
     */
    @Before
    public void setUp() throws Exception{
    	MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");
        socialLinkBeanList = new ArrayList<>();
        SocialLinkBean socialBean = new SocialLinkBean();
        socialBean.setIconLabel("Twitter");
        socialBean.setIconImage("/content/dam/dpagco/corteva/NA/US/english/hero_l1.jpg");
        socialBean.setIconLink("/content/corteva/NA/us");
        socialBean.setIconAlt("iconAlt");
        socialLinkBeanList.add(socialBean);
        property = Mockito.mock(Property.class);
                   
        PrivateAccessor.setField(footerModel, "logoImage",
				"/content/dam/corteva/Product_catalog.jpg");
		PrivateAccessor.setField(footerModel, "resourceType",
				"/apps/corteva/components/content/footerModel/v1/footerModel");
		PrivateAccessor.setField(footerModel, "socialLinks", property);
		Mockito.when(property.isMultiple()).thenReturn(false);
		Mockito.when(property.getValue()).thenReturn(new StringValue(
				"{\"iconAlt\":\"Alt text\",\"iconLink\":\"/content/corteva/corporate/homepage\",\"iconLabel\":\"Icon Label\",\"iconImage\":\"/content/dam/dpagco/corteva/NA/US/english/hero_l1.jpg\"}"));
		Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
		Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
		Mockito.when(mockResourceResolver.getResource(Mockito.anyString())).thenReturn(mockResource);
		Mockito.when(mockResource.getPath()).thenReturn("/content/corteva/corporate/homepage");
     }        
    
    /**
	 * Test Logo url.
	 */
	@Test
	public void testLogoUrl() {
		Assert.assertNull(footerModel.getLogoUrl());
	}
	
	/**
	 * Test Social Selection.
	 */
	@Test
	public void testSocialSel() {
		Assert.assertNull(footerModel.getSocialSel());
	}
	
	/**
	 * Test Social Text.
	 */
	@Test
	public void testSocialText() {
		Assert.assertNull(footerModel.getSocialText());
	}
	
	/**
	 * Test logo image.
	 */
	@Test
	public void testLogoImage() {
		Assert.assertNotNull(footerModel.getLogoImage());
	}
	
	/**
	 * Test logo image alt text.
	 */
	@Test
	public void testLogoImgAltText() {
		Assert.assertNull(footerModel.getLogoImgAltText());
	}
	
	/**
	 * Test AnchorLinksContainer.
	 */
	@Test
	public void testAnchorLinksContainer() {
		Assert.assertNotNull(footerModel.getAnchorLinksContainer());
	}

}
