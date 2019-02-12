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
import com.corteva.model.component.models.CardsContainerModel;

/**
 * This is a test class for Cards Container Model.
 * 
 * @author Sapient
 * 
 */
public class CardsContainerModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private CardsContainerModel cardsModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The property. */
	private Property mockProperty;

	/** The value. */
	private Value mockvalue;

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
		cardsModel = getRequest().adaptTo(CardsContainerModel.class);
		mockProperty = Mockito.mock(Property.class);
		mockvalue = Mockito.mock(Value.class);
		try {
			mockSession = getResourceResolver().adaptTo(Session.class);
			mockSession.getRootNode()
					.addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/cardcontainer");
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in setUp(): " + e.getMessage());
		}
	}

	/**
	 * Test responsive grid name success.
	 */
	@Test
	public void testResponsiveGridNameSuccess() {
		String componentPath = "/content/corteva/na/us/en/_jcr_content/root/responsivegrid/cardcontainer";
		try {
			Mockito.when(mockvalue.getString()).thenReturn(componentPath);
			Mockito.when(mockProperty.getValue()).thenReturn(mockvalue);
			responsiveGridName = cardsModel.getResponsiveGridNodeName(mockProperty);
			Assert.assertEquals(CortevaConstant.RESPONSIVE_GRID + "cardcontainer", responsiveGridName);

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
			Mockito.when(mockvalue.getString()).thenReturn(componentPath);
			Mockito.when(mockProperty.getValue()).thenReturn(mockvalue);
			responsiveGridName = cardsModel.getResponsiveGridNodeName(mockProperty);
			Assert.assertNotEquals(CortevaConstant.RESPONSIVE_GRID + "cardcontainer", responsiveGridName);
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testResponsiveGridName(): " + e.getMessage());
		}
	}

	/**
	 * Test title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(cardsModel.getTitle());
	}

	/**
	 * Test optional title.
	 */
	@Test
	public void testOptionalTitle() {
		Assert.assertNull(cardsModel.getOptionalTitle());
	}

	/**
	 * Test body text.
	 */
	@Test
	public void testBodyText() {
		Assert.assertNull(cardsModel.getBodyText());
	}

	/**
	 * Test card type.
	 */
	@Test
	public void testCardType() {
		Assert.assertNull(cardsModel.getCardType());
	}

	/**
	 * Test bg color.
	 */
	@Test
	public void testBgColor() {
		Assert.assertNull(cardsModel.getBgColor());
	}

}