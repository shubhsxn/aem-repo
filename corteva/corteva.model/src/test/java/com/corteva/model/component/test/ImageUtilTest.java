package com.corteva.model.component.test;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.ImageUtil;

public class ImageUtilTest extends BaseAbstractTest {
	/**
	 * The Injected Image Util.
	 */
	@InjectMocks
	private ImageUtil imageUtil;
	
	/**
	 * The mock base config
	 */
	@Mock
	private BaseConfigurationService baseService;
	
	/**
	 * The mock request
	 */
	@Mock
	private MockSlingHttpServletRequest mockRequest;
	
	/**
	 * The mock resource resolver.
	 */
	@Mock
	private ResourceResolver mockResourceResolver;
	
	/**
	 * The mock resource
	 */
	@Mock
	private Resource mockResource;
	
	/**
	 * The region country map
	 */
	private Map<String, String> regCountryLangMap;
	
	/**
	 * The image path.
	 */
	private final String imagePath = "/content/dam/cortvea/na/us/en/testImage.png";
	
	/**
	 * the set up method.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		regCountryLangMap = new HashMap<>();
		regCountryLangMap.put(CortevaConstant.BRAND, CortevaConstant.BRAND_NAME_BREVANT);
		regCountryLangMap.put(CortevaConstant.REGION, CortevaConstant.NA);
		regCountryLangMap.put(CortevaConstant.COUNTRY, CortevaConstant.US);
		regCountryLangMap.put(CortevaConstant.LANGUAGE, CortevaConstant.EN);
	}
	
	@Test
	public void testGetImageRenditionList() {
		
		Mockito.when(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
				CortevaConstant.IS_ASSET_FROM_S7, CortevaConstant.IMAGE_CONFIG_NAME)).thenReturn("true");
		Mockito.when(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
					"MockComponent", CortevaConstant.IMAGE_CONFIG_NAME)).thenReturn(imagePath + CortevaConstant.COMMA + imagePath + CortevaConstant.COMMA + imagePath);
		try {
			Mockito.when(mockRequest.getResourceResolver()).thenReturn(mockResourceResolver);
			Mockito.when(mockResourceResolver.resolve(imagePath)).thenReturn(mockResource);
			Node mockNode = Mockito.mock(Node.class);
			Mockito.when(mockResource.adaptTo(Node.class)).thenReturn(mockNode);
			Mockito.when(mockNode.getName()).thenReturn("MockNodeName");
			Assert.assertNotNull(ImageUtil.getImageRenditionList(imagePath, "MockComponent", mockRequest, baseService, regCountryLangMap));
			
			Mockito.when(baseService.getPropertyValueFromConfiguration(regCountryLangMap,
					CortevaConstant.IS_ASSET_FROM_S7, CortevaConstant.IMAGE_CONFIG_NAME)).thenReturn("false");
			Assert.assertNotNull(ImageUtil.getImageRenditionList(imagePath, "MockComponent", mockRequest, baseService, regCountryLangMap));
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception in testGetImageRenditionList() method: "+ e.getMessage()); 
		}
	}
}