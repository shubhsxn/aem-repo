package com.corteva.model.component.test;

import java.lang.reflect.Field;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ProductRegistrationModel;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * This is a test class for Product Registration Model.
 * 
 * @author Sapient
 * 
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProductRegistrationModelTest extends BaseAbstractTest {

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/**
	 * The mocked request object.
	 */
	@Mock
	private SlingHttpServletRequest mockRequest;
	
	/**
	 * The mocked current resource.
	 */
	@Mock
	private Resource currentResource;

	/** The product registartion model. */
	@InjectMocks
	private ProductRegistrationModel productRegistrationModel = new ProductRegistrationModel();

	@Mock
	private ResourceResolver mockResourceResolver;
	@Mock
	private Resource mockResource;
	@Mock
	private Resource regionalPageResource;
	@Mock
	private Resource regionalPageJcrResource;
	@Mock
	private Page mockPage;
	@Mock
	private ValueMap mockValueMap;

	/**
	 * The mocked tagmanager object.
	 */
	@Mock
	private TagManager tagManager;
	
	private String[] stateRegistrationList = {"corteva:consumerRegionCountry/NA/US/AL","corteva:consumerRegionCountry/NA/US/AK"};
	
	private String[] allTagList = {"corteva:consumerRegionCountry/NA/US/AL","corteva:consumerRegionCountry/NA/US/AK", "corteva:cropTypes/abelia"};
	
	private String registrationCountry = "us";
	
	private String pagePath = "/content/corteva/na/us/en/homepage/products-and-solutions/crop-protection/aproach";

	private String regionalpagePath = "/content/corteva/na";
	
	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			getContext().addModelsForPackage("com.corteva.model.component.models");
			final TagManager mockTagManager = Mockito.mock(TagManager.class);
			final PageManager mockPageManager = Mockito.mock(PageManager.class);
			context.registerAdapter(ResourceResolver.class, TagManager.class, mockTagManager);
			context.registerAdapter(ResourceResolver.class, PageManager.class, mockPageManager);			
			Field requestField;
			requestField = productRegistrationModel.getClass().getDeclaredField("slingRequest");
			requestField.setAccessible(true);
			requestField.set(productRegistrationModel, mockRequest);			
			Field resolverField;
			resolverField = productRegistrationModel.getClass().getDeclaredField("resourceResolver");
			resolverField.setAccessible(true);
			resolverField.set(productRegistrationModel, mockResourceResolver);
			Field resourceField;
			resourceField = productRegistrationModel.getClass().getDeclaredField("currentResource");
			resourceField.setAccessible(true);
			resourceField.set(productRegistrationModel, currentResource);
			Field stateRegistrationListField;
			stateRegistrationListField = productRegistrationModel.getClass().getDeclaredField("stateRegistrationList");
			stateRegistrationListField.setAccessible(true);
			stateRegistrationListField.set(productRegistrationModel, stateRegistrationList);
			Field registrationCountryField;
			registrationCountryField = productRegistrationModel.getClass().getDeclaredField("registrationCountry");
			registrationCountryField.setAccessible(true);
			registrationCountryField.set(productRegistrationModel, registrationCountry);		
			mockResource = Mockito.mock(Resource.class);
			regionalPageResource = Mockito.mock(Resource.class);
			regionalPageJcrResource = Mockito.mock(Resource.class);			
			Mockito.when(mockResourceResolver.adaptTo(TagManager.class)).thenReturn(mockTagManager);
			Tag fakeTag = Mockito.mock(Tag.class);
			Mockito.when(mockTagManager.resolve(Mockito.any())).thenReturn(fakeTag);			
			Mockito.when(fakeTag.getTitle()).thenReturn("AL");
			Mockito.when(fakeTag.getName()).thenReturn("Alabama");		
			Mockito.when(mockRequest.getResource()).thenReturn(mockResource);
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
			Mockito.when(mockResource.getPath()).thenReturn(pagePath);
			Mockito.when(mockResourceResolver.getResource(regionalpagePath)).thenReturn(regionalPageResource);
			Mockito.when(mockResourceResolver.adaptTo(PageManager.class)).thenReturn(mockPageManager);
			Mockito.when(mockPageManager.getContainingPage(regionalPageResource)).thenReturn(mockPage);
			Mockito.when(mockPageManager.getContainingPage(currentResource)).thenReturn(mockPage);
			Mockito.when(mockPage.getContentResource()).thenReturn(regionalPageJcrResource);
			Mockito.when(regionalPageJcrResource.getValueMap()).thenReturn(mockValueMap);
			Mockito.when(mockValueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);
			Mockito.when(mockValueMap.get(CortevaConstant.CQ_TAGS, String[].class)).thenReturn(allTagList);
		} catch (Exception e) {
			Assert.fail("ValueFormatException occurred in setUp(): " + e.getMessage());
		}
	}
	
	@Test
	public void testInit() {
		productRegistrationModel.init();
		Assert.assertNotNull(stateRegistrationList);
	}
	
	@Test
	public void testGetRegisteredStates() {
		Assert.assertNotNull(productRegistrationModel.getRegisteredStates());
	}
	
	@Test
	public void testGetRegisteredStatesMap() throws Exception {

		mockResource = context.create()
				.resource(CortevaConstant.PATH_TO_JSON_FILE + CortevaConstant.FORWARD_SLASH + registrationCountry + CortevaConstant.DOT
						+ CortevaConstant.JSON_LOWERCASE + CortevaConstant.FORWARD_SLASH + CortevaConstant.JCR_CONTENT);
		Node node = mockResource.adaptTo(Node.class);
		try {
			node.setProperty("jcr:data", "{\"type\": \"FeatureCollection\",\r\n" + "\"features\": [\r\n"
					+ "{ \"type\": \"Feature\", \"id\": 0, \"properties\": { \"UF\": \"AC\", \"presence\": 1, \"name\": \"Acre\", \"REGIAO\": \"NO\" }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ -68.61895, -11.129709 ], [ -68.685345, -11.156667 ], [ -68.771162, -11.178199 ], [ -68.833969, -11.138409 ], [ -68.862165, -11.080244 ], [ -68.88022, -11.049307 ], [ -68.931174, -11.049095 ],[ -68.61895, -11.129709 ] ] ] } }]}");
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in testGetRegisteredStatesForUS" + e.getMessage());
		}
		Mockito.when(mockResourceResolver.getResource(
				CortevaConstant.PATH_TO_JSON_FILE + CortevaConstant.FORWARD_SLASH + registrationCountry + CortevaConstant.DOT
						+ CortevaConstant.JSON_LOWERCASE + CortevaConstant.FORWARD_SLASH + CortevaConstant.JCR_CONTENT))
				.thenReturn(mockResource);

		Assert.assertNotNull(productRegistrationModel.getRegisteredStatesMap());
	}
	
	@Test
	public void testGetLocaleForInternationalization() {
		Assert.assertNotNull(productRegistrationModel.getLocaleForInternationalization());
	}

}
