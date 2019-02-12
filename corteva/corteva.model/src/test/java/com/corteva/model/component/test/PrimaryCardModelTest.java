package com.corteva.model.component.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.models.PrimaryCardModel;

/**
 * This is a test class for Primary card Model.
 * 
 * @author Sapient
 * 
 */
public class PrimaryCardModelTest extends BaseAbstractTest {
	
	/* the internal link url */
	private String internalLinkUrl;
	
	/* the external link url */
	private String externalLinkUrl;
	
	/* the mock base service */
	@Mock
	private BaseConfigurationService baseService;
	
	/* the mock experience fragment model */
	@Mock
	private PrimaryCardModel primaryModel;

	/**
	 * This method instantiates a new instance of Primary Card Sling Model.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		primaryModel = getRequest().adaptTo(PrimaryCardModel.class);
		internalLinkUrl = "/content/corteva/na/US/en/home";
		externalLinkUrl = "www.somewebsite.com";
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getSimpleCardTitle()}.
	 */
	@Test
	public void testGetSimpleCardTitle() {
		Assert.assertNull(primaryModel.getSimpleCardTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getFeatureCardTitle()}.
	 */
	@Test
	public void testGetFeatureCardTitle() {
		Assert.assertNull(primaryModel.getFeatureCardTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getCtaCardTitle()}.
	 */
	@Test
	public void testGetCtaCardTitle() {
		Assert.assertNull(primaryModel.getCtaCardTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getCtaArrowCardTitle()}.
	 */
	@Test
	public void testGetCtaArrowCardTitle() {
		Assert.assertNull(primaryModel.getCtaArrowCardTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getResponsiveCardTitle()}.
	 */
	@Test
	public void testGetResponsiveCardTitle() {
		Assert.assertNull(primaryModel.getResponsiveCardTitle());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getOptLinkLabel()}.
	 */
	@Test
	public void testGetOptLinkLabel() {
		Assert.assertNull(primaryModel.getOptLinkLabel());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getOptLinkAction()}.
	 */
	@Test
	public void testGetOptLinkAction() {
		Assert.assertNull(primaryModel.getOptLinkAction());
	}

	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getOptLinkStyle()}.
	 */
	@Test
	public void testGetOptLinkStyle() {
		Assert.assertNull(primaryModel.getOptLinkStyle());
	}
	
	/**
	 * Test method for {@link com.corteva.model.component.models.PrimaryCardModel#getOptLinkUrl()}.
	 */
	@Test
	public void testGetOptLinkUrl() {
		Assert.assertNotNull(primaryModel.getOptLinkUrl());
	}
	
	/**
     * Test Optional Link Url Extension Success
     */
	@Test
	public void testGetOptLinkUrlExtensionSuccess() {
		Assert.assertEquals(internalLinkUrl + CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, LinkUtil.getHref(internalLinkUrl));
	}
	
	/**
     * Test Optional Link Url Protocol Success
     */
	@Test
	public void testGetOptLinkUrlProtocolSuccess() {
		Assert.assertEquals(CortevaConstant.HTTP + externalLinkUrl, LinkUtil.getHref(externalLinkUrl));
	}

}
