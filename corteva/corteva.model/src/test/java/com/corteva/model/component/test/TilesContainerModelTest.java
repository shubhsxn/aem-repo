package com.corteva.model.component.test;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.models.TilesContainerModel;

/**
 * This is a test class for Full Bleed Tiles Container Model.
 * 
 * @author Sapient
 * 
 */
public class TilesContainerModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private TilesContainerModel tilesModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The property. */
	private Property mockProp;

	/** The value. */
	private Value mockVal;

	/** The responsive grid name. */
	private String responsiveGridName = StringUtils.EMPTY;
	
	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		Session mockSession;
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		tilesModel = getRequest().adaptTo(TilesContainerModel.class);
		mockProp = Mockito.mock(Property.class);
		mockVal = Mockito.mock(Value.class);
		try {
			mockSession = getResourceResolver().adaptTo(Session.class);
			mockSession.getRootNode()
					.addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/fullbleedtilescontai");
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in setUp(): " + e.getMessage());
		}
	}

	/**
	 * Test responsive grid name success.
	 */
	@Test
	public void testResponsiveGridNameSuccess() {
		String componentPath = "/content/corteva/na/us/en/_jcr_content/root/responsivegrid/fullbleedtilescontai";
		try {
			Mockito.when(mockVal.getString()).thenReturn(componentPath);
			Mockito.when(mockProp.getValue()).thenReturn(mockVal);
			responsiveGridName = tilesModel.getResponsiveGridNodeName(mockProp);
			Assert.assertEquals(CortevaConstant.RESPONSIVE_GRID + "fullbleedtilescontai", responsiveGridName);

		} catch (IllegalStateException | RepositoryException e) {
			Assert.fail("Repository Exception occurred in testResponsiveGridName(): " + e.getMessage());
		}

	}

	/**
	 * Test responsive grid name fail.
	 */
	@Test
	public void testResponsiveGridNameFail() {
		String componentPath = "/content/corteva/testpath";
		try {
			Mockito.when(mockVal.getString()).thenReturn(componentPath);
			Mockito.when(mockProp.getValue()).thenReturn(mockVal);
			responsiveGridName = tilesModel.getResponsiveGridNodeName(mockProp);
			Assert.assertNotEquals(CortevaConstant.RESPONSIVE_GRID + "fullbleedtilescontai", responsiveGridName);
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testResponsiveGridName(): " + e.getMessage());
		}
	}
	
	/**
	 * Test title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(tilesModel.getTitle());
	}
	
	/**
	 * Test expanded title.
	 */
	@Test
	public void testExpandedTitle() {
		Assert.assertNull(tilesModel.getExpandedTitle());
	}
	
	/**
	 * Test body text.
	 */
	@Test
	public void testBodyText() {
		Assert.assertNull(tilesModel.getBodyText());
	}
	
	/**
	 * Test tile type.
	 */
	@Test
	public void testTileType() {
		Assert.assertNull(tilesModel.getTileType());
	}
	
	/**
	 * Test background color.
	 */
	@Test
	public void testBgColor() {
		Assert.assertNull(tilesModel.getBgColor());
	}
	
	/**
	 * Test icon alignment.
	 */
	@Test
	public void testIconAlign() {
		Assert.assertNull(tilesModel.getIconAlign());
	}
}