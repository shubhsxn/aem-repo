/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
*/
package com.corteva.model.component.test;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.CreatePdfModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * This is the test class for Create Pdf Model.
 *
 * @author Sapient
 */
public class CreatePdfModelTest extends BaseAbstractTest {

	/** The pdf model. */
	@Mock
	private CreatePdfModel pdfModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The pdf url. */
	private String pdfUrl;

	/** The current res path. */
	private String currentResPath;

	/** The mock session. */
	private Session mockSession;

	@Mock
	private Page page;

	/**
	 * This method sets up the resource for test cases.
	 */
	@Before
	public void setUp() {
		try {
			baseService = new BaseConfigurationService();
			context.getService(ConfigurationAdmin.class);
			getContext().registerInjectActivateService(baseService);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			currentResPath = "/content/corteva/na/us/en/home/our-merger/jcr:content";
			context.create().resource(currentResPath);
			page = context.pageManager().create("/content/corteva/na/us/en/home", "our-merger",
					"/apps/sample/templates/homepage", "our-merger");
			context.currentResource(currentResPath);
			mockSession = getResourceResolver().adaptTo(Session.class);
			Node mockNode = mockSession.getRootNode().addNode("/content/corteva/na/us/en/home/our-merger");
			mockNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			Node mockPageContentNode = mockSession.getRootNode()
					.addNode("/content/corteva/na/us/en/home/our-merger/jcr:content");
			mockPageContentNode.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:PageContent");
			mockPageContentNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE,
					CortevaConstant.BASE_PAGE_RESOURCE_TYPE);
			pdfModel = getRequest().adaptTo(CreatePdfModel.class);
			context.registerAdapter(Resource.class, Page.class, page);
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(pageManager.getContainingPage(context.currentResource(currentResPath))).thenReturn(page);
		} catch (RepositoryException | WCMException e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
		

	}

	/**
	 * Test method for getPagePdfUrl Success.
	 */
	@Test
	public void testGetPagePdfUrlSuccess() {
		pdfUrl = "/content/corteva/na/us/en/home/our-merger0.pdf";
		Assert.assertEquals(pdfUrl, pdfModel.getPagePdfUrl(context.currentResource(currentResPath)));
	}

	/**
	 * Test method for getPagePdfUrl Failure.
	 */
	@Test
	public void testGetPagePdfUrlFailure() {
		pdfUrl = "/content/corteva/na/us/en/home/our-mission.pdf";
		Assert.assertNotEquals(pdfUrl, pdfModel.getPagePdfUrl(context.currentResource(currentResPath)));
	}
}