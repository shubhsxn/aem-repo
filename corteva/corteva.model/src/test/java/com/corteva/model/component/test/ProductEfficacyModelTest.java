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
import com.corteva.model.component.models.ProductEfficacyModel;
import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for ProductEfficacy Model.
 * 
 * @author Sapient
 * 
 */
public class ProductEfficacyModelTest extends BaseAbstractTest{
	
	/** The mock resource resolver. */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The mock request. */
	@Mock
	private SlingHttpServletRequest request;
	
	/** The mock resource. */
	@Mock
	private Resource resource;
	
	/** The mock tag manager. */
	@Mock
	private TagManager tagManager;
	
	/** The mock page manager. */
	@Mock
	private PageManager pageManager;
	
	/** The mock tag. */
	@Mock
	private Tag tag;
	
	/** The mock page. */
	@Mock
	private Page page;
	
	/** The mock rangeIterator. */
	@Mock
	private RangeIterator<Resource> rangeIterator;
	
	/** the ProductEfficacy model */
	@InjectMocks
	private ProductEfficacyModel productEfficacyModel;
	
	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;
	
	/** Reflection fields */
	private String controlTitle;
	private String introText;	
	private String controlUse;
	private String[] controlTag;	
	private String[] suppressTag;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");

		Field mockRequestTypeField = productEfficacyModel.getClass().getDeclaredField("slingRequest");
		mockRequestTypeField.setAccessible(true);
		mockRequestTypeField.set(productEfficacyModel, request);
		
		controlTitle = "testControlTitle";
		Field controlTitleTypeField = productEfficacyModel.getClass().getDeclaredField("controlTitle");
		controlTitleTypeField.setAccessible(true);
		controlTitleTypeField.set(productEfficacyModel, controlTitle);
		
		introText = "testIntoText";
		Field intoTextTypeField = productEfficacyModel.getClass().getDeclaredField("introText");
		intoTextTypeField.setAccessible(true);
		intoTextTypeField.set(productEfficacyModel, introText);
		
		controlUse = "weeds";
		Field controlUseTypeField = productEfficacyModel.getClass().getDeclaredField("controlUse");
		controlUseTypeField.setAccessible(true);
		controlUseTypeField.set(productEfficacyModel, controlUse);
		
		controlTag = new String[1];
		controlTag[0] = "corteva:efficacy/weeds/ageratum";
		Field controlTagTypeField = productEfficacyModel.getClass().getDeclaredField("controlTag");
		controlTagTypeField.setAccessible(true);
		controlTagTypeField.set(productEfficacyModel, controlTag);
		
		suppressTag = new String[1];
		suppressTag[0] = "corteva:efficacy/weeds/alder";
		Field suppressTagTypeField = productEfficacyModel.getClass().getDeclaredField("suppressTag");
		suppressTagTypeField.setAccessible(true);
		suppressTagTypeField.set(productEfficacyModel, suppressTag);
		
		String pagePath = "/content/corteva/na/US/en";
		
		Mockito.when(request.getResource()).thenReturn(resource);
		Mockito.when(resource.getPath()).thenReturn(pagePath);
		Mockito.when(request.getResourceResolver()).thenReturn(mockResourceResolver);
		
		Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
		Mockito.when(tagManager.resolve(controlTag[0])).thenReturn(tag);
		Mockito.when(tagManager.resolve(suppressTag[0])).thenReturn(tag);
		Mockito.when(tag.getTagID()).thenReturn(controlTag[0]);
		Mockito.when(tag.getTitle(Mockito.any())).thenReturn(controlTag[0]);
		Mockito.when(tagManager.find(null, controlTag)).thenReturn(rangeIterator);
		Mockito.when(rangeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(rangeIterator.next()).thenReturn(resource);
		
		Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		Mockito.when(mockResourceResolver.resolve(pagePath)).thenReturn(resource);
		Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductEfficacyModel#getControlTitle()}.
	 */
	@Test
	public void testGetControlTitle() {
		Assert.assertNotNull(productEfficacyModel.getControlTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductEfficacyModel#getIntroText()}.
	 */
	@Test
	public void testGetIntroText() {
		Assert.assertNotNull(productEfficacyModel.getIntroText());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.ProductEfficacyModel#getControlUse()}.
	 */
	@Test
	public void testGetControlUse() {
		Assert.assertNotNull(productEfficacyModel.getControlUse());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.ProductEfficacyModel#getProductList()}.
	 */
	@Test
	public void testGetProductList() {
		Assert.assertNotNull(productEfficacyModel.getProductList());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.ProductEfficacyModel#isSuppressTag()}.
	 */
	@Test
	public void testIsSuppressTag() {
		Assert.assertFalse(productEfficacyModel.isSuppressTag());
	}
	/**
	 * Test get Hide Label Finder Url.
	 */
	@Test
	public void testGetHideLabelFinderUrl() {
		Assert.assertFalse(productEfficacyModel.isHideLabelFinderUrl());
	}
}
