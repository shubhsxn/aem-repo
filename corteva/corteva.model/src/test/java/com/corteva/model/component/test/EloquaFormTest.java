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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.EloquaFormModel;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


/**
 * This is test class to test the base configuration service.
 */
public class EloquaFormTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private EloquaFormModel eloquaFormModel;
	
	/**
	 * The mocked request object.
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/** The dictionary. */
	@Mock
	private Dictionary<String, Object> dict;
	
	@Mock
	private FrameworkUtil frmUtil;

	/** The config. */
	@Mock
	private Configuration config;
	
	/** The country lang map. */
	private Map<String, String> countryLangMap = new HashMap<>();
	
	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;
	
	/** The current res path. */
	private String currentResPath;
	
	/**
	 * The mocked resource resolver object.
	 */
	@Mock
	private ResourceResolver resourceResolver;
	
	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;
	
	/** The resource. */
	@Mock
	private Resource resource;
	
	/**
	 * The mocked tag object.
	 */
	@Mock
	private Tag tag;


	/**
	 * Sets the method parameters and registers the service.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		context.registerInjectActivateService(new BaseConfigurationService());
		ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerService(FrameworkUtil.class, frmUtil);
		Configuration myServiceConfig;
		mockRequest = getRequest();
		try {
			myServiceConfig = configAdmin
					.getConfiguration("com.corteva.core.configurations.EloquaConfigurationService");
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put("countryTags", "/etc/tags/corteva/eloqua/country");
			props.put("roleTags", "/etc/tags/corteva/eloqua/role");
			props.put("protocol", "https://");
			props.put("submitUrl", ".t.eloqua.com/e/f2");
			props.put("globalSiteId", "1238138546");
			props.put("naSiteId", "777435755");
			props.put("emailAddPattern", "^[a-zA-Z0-9_+-]+(\\.[a-zA-Z0-9_+-]+)*@[a-zA-Z0-9\\\\-]+(\\.[a-zA-Z0-9\\-]+)*(\\.[a-zA-Z]{1,})$");
			props.put("mobileValidationPattern", "[0-9]{10}");
			props.put("captchaSiteKey", "6Lf3kF4UAAAAAMauuguR");
			props.put("captchaSecretKey", "6Lf3kF4UAAAAAF251TgkG9A");
			props.put("siteDomain", "http://www.corteva.com");
			props.put("zipValidationAPIUrl", "http://www.pioneer.com/bingMapProxy/corteva/locations?domain=");
			props.put("confirmationUrl", "t.eloqua.com/e/DefaultFormSubmitConfirmation.aspx");
			myServiceConfig.update(props);

			countryLangMap.put(CortevaConstant.COUNTRY, "US");
			countryLangMap.put(CortevaConstant.LANGUAGE, "en");
			eloquaFormModel = getRequest().adaptTo(EloquaFormModel.class);
			mockRequest.setServletPath("/content/corteva/na/us/en/home");
			currentResPath = "/content/corteva/na/us/en/home/eloqua-form/jcr:content";
			context.registerAdapter(Resource.class, Page.class, page);
			context.create().resource(currentResPath);
			context.currentResource(currentResPath);
			context.registerAdapter(Resource.class, Page.class, page);
			
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
			page = context.pageManager().create("/content/corteva/na/us/en/home", "eloqua-form",
					"/apps/sample/templates/homepage", "eloqua-form");
			Mockito.when(pageManager.getContainingPage(context.currentResource(currentResPath))).thenReturn(page);

			final TagManager mockTagManager = Mockito.mock(TagManager.class);
			Tag fakeTag = Mockito.mock(Tag.class);
			Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			List<Tag> childTags = new ArrayList<>();
			childTags.add(fakeTag);
			Mockito.when(fakeTag.listChildren()).thenReturn(childTags.iterator());
			page = Mockito.mock(Page.class);
			Mockito.when(page.getContentResource()).thenReturn(resource);
			Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);

			PrivateAccessor.setField(eloquaFormModel, "rolesPath", "corteva:eloqua/role/UnitedStates");
			
		} catch (Exception e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		} 
	}

	/**
	 * Test GlobalSubmitUrl is not null.
	 */
	@Test
	public void testGlobalSiteIdNotNull() {
	  Assert.assertNotNull(eloquaFormModel.getGlobalSubmitUrl());
	}

	/**
	 * Test NASubmitUrl is not null.
	 */
	@Test
	public void testNASubmitUrlNotNull() {
		Assert.assertNotNull(eloquaFormModel.getNASubmitUrl());
	}

	/**
	 * Test FormSubmitUrl is not null.
	 */
	@Test
	public void testFormSubmitUrlNotNull() {
		Assert.assertNotNull(eloquaFormModel.getFormSubmitUrl("1238138546"));
		Assert.assertNotNull(eloquaFormModel.getFormSubmitUrl("777435755"));
	}

	/**
	 * Test Protocol is not null.
	 */
	@Test
	public void testProtocolNotNull() {
		Assert.assertNotNull(eloquaFormModel.getProtocol());
	}
	
	/**
	 * Test SiteDomain is not null.
	 */
	@Test
	public void testSiteDomainNotNull() {
		Assert.assertNotNull(eloquaFormModel.getSiteDomain());
	}
	
	/**
	 * Test EmailValidationPattern is not null.
	 */
	@Test
	public void testEmailValidationPatternNotNull() {
		Assert.assertNotNull(eloquaFormModel.getEmailValidationPattern());
	}
	
	/**
	 * Test ZipValidationAPIUrl is not null.
	 */
	@Test
	public void testZipValidationAPIUrlNotNull() {
		Assert.assertNotNull(eloquaFormModel.getZipValidationAPIUrl());
	}
	
	/**
	 * Test MobileValidationPattern is not null.
	 */
	@Test
	public void testMobileValidationPatternNotNull() {
		Assert.assertNotNull(eloquaFormModel.getMobileValidationPattern());
	}
	
	/**
	 * Test CountriesList is not null.
	 */
	@Test
	public void testCountriesListNotNull() {
		Assert.assertNotNull(eloquaFormModel.getCountriesList());
	}
	
	/**
	 * Test RolesList is not null.
	 */
	@Test
	public void testRolesListNotNull() {
		Assert.assertNotNull(eloquaFormModel.getRolesList());
	}

	/**
	 * Test CountryCodes is not null.
	 */
	@Test
	public void testCountryCodesNotNull() {
		Assert.assertNotNull(eloquaFormModel.getCountryCodes());
	}
	
	/**
	 * Test CountryStatesListJson is not null.
	 */
	@Test
	public void testCountryStatesListJsonNotNull() {
		Assert.assertNotNull(eloquaFormModel.getCountryStatesListJson());
	}

	/**
	 * Test TagsTitles is not null.
	 */
	@Test
	public void testgetTagsTitlesNotNull() {
		Assert.assertNotNull(eloquaFormModel.getTagsTitles("/etc/tags/corteva/eloqua/country", false));
	}
	
	@Test
	public void testCaptchaSiteKey() {
		Assert.assertNotNull(eloquaFormModel.getGoogleCaptchaSiteKey());
	}
	
	@Test
	public void testCaptchaSecretKey() {
		Assert.assertNotNull(eloquaFormModel.getGoogleCaptchaSecretKey());
	}

	/**
	 * Test LocaleForInternationalization is not null.
	 */
	@Test
	public void testLocaleForInternationalization() {
		Assert.assertNotNull(eloquaFormModel.getLocaleForInternationalization());
	}

	/**
	 * Test ConfirmationUrl is not null.
	 */
	@Test
	public void testConfirmationUrl() {
		Assert.assertNotNull(eloquaFormModel.getConfirmationUrl());
	}
	
	/**
	 * Test CurrentPagePath is not null.
	 */
	@Test
	public void testCurrentPagePath() {
		Mockito.when(resourceResolver.map(Mockito.anyString()))
				.thenReturn("/content/corteva/na/us/en/homepage");
		Assert.assertNotNull(eloquaFormModel.getCurrentPagePath());
	}

	/**
	 * Test originName is constructed properly.
	 */
	@Test
	public void testOriginName() {
		Assert.assertNotNull(eloquaFormModel.getOriginName());
	}
}
