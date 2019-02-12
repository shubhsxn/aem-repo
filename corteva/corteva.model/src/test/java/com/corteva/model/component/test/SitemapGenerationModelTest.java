package com.corteva.model.component.test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.bean.SitemapBean;
import com.corteva.model.component.models.SitemapGenerationModel;
import com.day.cq.wcm.api.Page;

import junitx.util.PrivateAccessor;

public class SitemapGenerationModelTest extends BaseAbstractTest{
	
	/** The mock resource resolver. */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/** The mock resource. */
	@Mock
	private Resource mockResource;
	
	/** The mock page. */
	@Mock
	private Page mockPage;
	
	/** The mock mockSiteMapList. */
	@Mock
	private List<SitemapBean> mockSiteMapList;
	
	/** The mock listRootChildren. */
	@Mock
	private Iterator<Page> listRootChildren;
	
	/** The mock valueMap. */
	@Mock
	private ValueMap mockValueMap;
	
	/** Inject sitemapGenerationModel. */
	@InjectMocks
	private SitemapGenerationModel sitemapGenerationModel;

	
	/** Reflection fields */
	private String rootPath;	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		getContext().addModelsForPackage("com.corteva.model.component.models");

		PrivateAccessor.setField(sitemapGenerationModel, "title",
				"sitemap");
		
		/*Field titleTypeField = sitemapGenerationModel.getClass().getDeclaredField("title");
		titleTypeField.setAccessible(true);
		titleTypeField.set(sitemapGenerationModel, titleTypeField);*/
		
		rootPath = "/content/corteva/na/US/en/homepage";
		Field rootPathTypeField = sitemapGenerationModel.getClass().getDeclaredField("rootPath");
		rootPathTypeField.setAccessible(true);
		rootPathTypeField.set(sitemapGenerationModel, rootPath);
		
		Mockito.when(mockResourceResolver.getResource(rootPath)).thenReturn(mockResource);
		
		Mockito.when(mockResource.adaptTo(Page.class)).thenReturn(mockPage);
		Mockito.when(mockPage.listChildren()).thenReturn(listRootChildren);
		Mockito.when(listRootChildren.next()).thenReturn(mockPage);
		Mockito.when(mockPage.isValid()).thenReturn(true);
		Mockito.when(mockPage.getProperties()).thenReturn(mockValueMap);
		Mockito.when(mockValueMap.get("hideInHtmlSitemap", Boolean.class)).thenReturn(false).thenReturn(null);
		Mockito.when(mockPage.isHideInNav()).thenReturn(true).thenReturn(false);
		Mockito.when(mockPage.getProperties().get("sling:resourceType", String.class)).thenReturn("corteva/components/structure/redirect-page").thenReturn("falseValue");
		Mockito.when(mockPage.getPageTitle()).thenReturn("Products & Solutions");
		Mockito.when(mockPage.getPath()).thenReturn("/content/corteva/na/ca/en/homepage/products-and-solutions");
		Mockito.when(listRootChildren.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.SitemapGenerationModel#getTitle()}.
	 */
	@Test
	public void testGetTitle() {
		Assert.assertNotNull(sitemapGenerationModel.getTitle());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.SitemapGenerationModel#getTitle()}.
	 */
	@Test
	public void testSetTitle() {
		sitemapGenerationModel.setTitle("Site map");
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.SitemapGenerationModel#getSitemap()}.
	 */
	@Test
	public void testGetSitemap() {
		Assert.assertNotNull(sitemapGenerationModel.getSitemap());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.SitemapGenerationModel#getRootPath()}.
	 */
	@Test
	public void testGetRootPath() {
		Assert.assertNotNull(sitemapGenerationModel.getRootPath());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.SitemapGenerationModel#setRootPath()}.
	 */
	@Test
	public void testSetRootPath() {
		sitemapGenerationModel.setRootPath("/content/corteva/na/ca/en/homepage");
	}
}
