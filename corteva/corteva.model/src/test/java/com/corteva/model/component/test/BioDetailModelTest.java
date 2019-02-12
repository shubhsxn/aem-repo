package com.corteva.model.component.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.BioDetailModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

import junitx.util.PrivateAccessor;

/**
 * This is a test class for Accordion Model.
 * 
 * @author Sapient
 * 
 */
public class BioDetailModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private BioDetailModel bioDetailModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	@Mock
	private ResourceResolver resourceResolver;

	@Mock
	private MockSlingHttpServletRequest mockRequest;

	/* The image path. */
	private String imagePath;

	/**
	 * The mocked page object.
	 */
	@Mock
	private Page page;

	private Session mockSession;

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			baseService = new BaseConfigurationService();
			getContext().registerInjectActivateService(baseService);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			mockRequest = getRequest();
			mockRequest.setServletPath("/content/corteva/na/US/en");
			bioDetailModel = getRequest().adaptTo(BioDetailModel.class);

			String currentResPath = "/content/corteva/na/us/en/jcr:content/root/responsivegrid/bioDetail";
			context.create().resource(currentResPath);
			context.currentResource(currentResPath).getValueMap();
			Configuration myServiceConfig;
			ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
			myServiceConfig = configAdmin.getConfiguration(CortevaConstant.IMAGE_CONFIG_NAME);
			Dictionary<String, Object> props = new Hashtable<>();
			props.put("bioDetail", "bioDetail_desktop, bioDetail_tablet, bioDetail_mobile");
			props.put("sceneSevenImageRoot", "http://s7d4.scene7.com/is/image/dpagco");
			myServiceConfig.update(props);
			mockSession = getResourceResolver().adaptTo(Session.class);
			imagePath = "/content/dam/corteva/Product_catalog.jpg";
			context.create().resource(imagePath);
			Field viewTypeField;
			viewTypeField = bioDetailModel.getClass().getDeclaredField("bioHeadShotImagePath");
			viewTypeField.setAccessible(true);
			viewTypeField.set(bioDetailModel, imagePath);
			PrivateAccessor.setField(bioDetailModel, "resourceType",
					"/apps/corteva/components/content/bioDetail/v1/bioDetail");
			String rootNodePath = "/content/corteva/na/US";
			Node mockPage = mockSession.getRootNode().addNode(rootNodePath);
			mockPage.setProperty(CortevaConstant.JCR_PRIMARY_TYPE, "cq:Page");
			context.create().resource(rootNodePath);
			page = context.pageManager().create(rootNodePath, "en", "/apps/sample/templates/homepage", "en");
			PageManager pageManager = Mockito.mock(PageManager.class);
			Mockito.when(pageManager.getContainingPage(context.currentResource("/content/corteva/na/US/en")))
					.thenReturn(page);
		} catch (IOException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException
				| SecurityException | WCMException | RepositoryException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test case to get Locale .
	 */
	@Test
	public void testGetLocale() {
		Assert.assertNotNull(bioDetailModel.getLocale());
	}

	/**
	 * Test Biography File.
	 */
	@Test
	public void testGetBiographyFile() {
		Assert.assertNull(bioDetailModel.getBiographyFile());
	}

	/**
	 * Test Biography File.
	 */
	@Test
	public void testGetBioHeadShotImage() {

		Assert.assertNotNull(bioDetailModel.getBioHeadShotImage());
	}

}
