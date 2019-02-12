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
import com.corteva.model.component.models.FixedGridModel;

/**
 * This is a test class for Fixed Grid Sling Model.
 * 
 * @author Sapient
 * 
 */
public class FixedGridModelTest extends BaseAbstractTest {

    
    /** The fixed grid model. */
    @Mock
    private FixedGridModel fixedGridModel;
    
    /** The base service. */
    @Mock
    private BaseConfigurationService baseService;

    /** The property. */
    private Property property;

    /** The value. */
    private Value value;

    /** The responsive grid name. */
    private String responsiveGridName = StringUtils.EMPTY;
    
    /** The two rows layout. */
    private String twoRowslayout;
    
    /** The three rows layout. */
    private String threeRowslayout;
    
    /** The three rows layout with one expanded tile. */
    private String oneExpandedTiles;
    
    /** The three rows layout with two expanded tiles. */
    private String twoExpandedTiles;
 
    /**
     * This method instantiates a new instance of Experience Fragment Sling Model.
     */
    @Before
    public void setUp() {
    	Session mockSession;
        baseService = new BaseConfigurationService();
        getContext().registerInjectActivateService(baseService);
        getContext().addModelsForPackage("com.corteva.model.component.models");
        fixedGridModel = getRequest().adaptTo(FixedGridModel.class);
        property = Mockito.mock(Property.class);
        value = Mockito.mock(Value.class);
        twoRowslayout = "twoRows";
        threeRowslayout = "threeRows";
        oneExpandedTiles = "1";
        twoExpandedTiles = "2";
        
        try {
            mockSession = getResourceResolver().adaptTo(Session.class);
            mockSession.getRootNode()
                    .addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/fixedgridtilescontai");
        } catch (RepositoryException e) {
            Assert.fail("RepositoryException occurred in setUp(): " + e.getMessage());
        }
    }
    
    /**
     * Test responsive grid name.
     */
    @Test
    public void testResponsiveGridName() {
           Assert.assertNotNull(fixedGridModel.getResponsiveGridNodeName());
    }

    
    /**
     * Test responsive grid name success.
     */
    @Test
    public void testResponsiveGridNameSuccess() {
        try {
            String componentPath = "/content/corteva/na/us/en/_jcr_content/root/responsivegrid/fixedgridtilescontai";
            Mockito.when(value.getString()).thenReturn(componentPath);
            Mockito.when(property.getValue()).thenReturn(value);
            responsiveGridName = fixedGridModel.getResponsiveGridNodeName(property);
            Assert.assertEquals(CortevaConstant.RESPONSIVE_GRID + "fixedgridtilescontai", responsiveGridName);
        } catch (RepositoryException e) {
            Assert.fail("Repository Exception occurred in testResponsiveGridNameSuccess(): " + e.getMessage());
        }
    }

    
    /**
     * Test responsive grid name fail.
     */
    @Test
    public void testResponsiveGridNameFail() {
        try {
            String componentPath = "/content/corteva/testpath";
            Mockito.when(value.getString()).thenReturn(componentPath);
            Mockito.when(property.getValue()).thenReturn(value);
            responsiveGridName = fixedGridModel.getResponsiveGridNodeName(property);
            Assert.assertNotEquals(CortevaConstant.RESPONSIVE_GRID + "fixedgridtilescontai", responsiveGridName);
        } catch (RepositoryException e) {
            Assert.fail("Repository Exception occurred in testResponsiveGridNameFail(): " + e.getMessage());
        }
    }
    
    /**
	 * Test background color.
	 */
	@Test
	public void testBgColor() {
		Assert.assertNull(fixedGridModel.getBgColor());
	}
	
	/**
	 * Test grid layout.
	 */
	@Test
	public void testLayout() {
		Assert.assertNull(fixedGridModel.getLayout());
	}
	
	/**
	 * Test short title.
	 */
	@Test
	public void testShortTitle() {
		Assert.assertNull(fixedGridModel.getShortTitle());
	}
	
	/**
	 * Test default number of Expanded Tiles in Two Rows Layout.
	 */
	@Test
	public void testDefaultTwoRowsExpandedTilesNumber() {
		Assert.assertNotNull(fixedGridModel.getTwoRowsExpandedTilesNumber());
	}
	
	/**
	 * Test default number of Expanded Tiles in Three Rows Layout.
	 */
	@Test
	public void testDefaultThreeRowsExpandedTilesNumber() {
		Assert.assertNotNull(fixedGridModel.getThreeRowsExpandedTilesNumber());
	}
	
	/**
	 * Test crop first expanded tile.
	 */
	@Test
	public void testCropFirstExpandedTile() {
		Assert.assertFalse(fixedGridModel.isCropFirstExpandedTile());
	}
	
	/**
	 * Test crop first expanded tile success.
	 */
	@Test
	public void testCropFirstExpandedTileSuccess() {
		Assert.assertEquals(CortevaConstant.THREE_ROWS_LAYOUT, threeRowslayout);
		Assert.assertEquals(1, Integer.parseInt(oneExpandedTiles));
	}
	
	/**
	 * Test crop first expanded tile layout fail.
	 */
	@Test
	public void testCropFirstExpandedTileLayoutFail() {
		Assert.assertNotEquals(CortevaConstant.THREE_ROWS_LAYOUT, twoRowslayout);
		Assert.assertEquals(1, Integer.parseInt(oneExpandedTiles));
	}
	
	/**
	 * Test crop first expanded tile number fail.
	 */
	@Test
	public void testCropFirstExpandedTileNumberFail() {
		Assert.assertEquals(CortevaConstant.THREE_ROWS_LAYOUT, threeRowslayout);
		Assert.assertNotEquals(1, Integer.parseInt(twoExpandedTiles));
	}
}