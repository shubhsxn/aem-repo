package com.corteva.model.component.test;

import java.lang.reflect.Field;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.models.CommonElementsModel;

/**
 * This is a test class for Common Elements Model.
 * 
 * @author Sapient
 * 
 */
public class CommonElementsModelTest extends BaseAbstractTest {

	/** The mock base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The mock req. */
	@Mock
	private SlingHttpServletRequest mockReq;

	/** The mock common elements model. */
	@InjectMocks
	private CommonElementsModel commonModel = new CommonElementsModel(mockReq);

	/** The view type. */
	private String viewType;

	/** The variation. */
	private String variation;

	/** The radio button. */
	private String radioButton;

	/** The dropdown. */
	private String dropdown;

	/** The alt text. */
	private String altText;

	/** The body text. */
	private String bodyText;

	/** The button text. */
	private String buttonText;

	/** The title. */
	private String title;

	/** The link label. */
	private String linkLabel;

	/** The link action. */
	private String linkAction;

	/** The link style. */
	private String linkStyle;

	/** The link color. */
	private String linkColor;

	/** The logo alt text. */
	private String logoAltText;

	/** The logo url. */
	private String logoUrl;

	/** The icon title. */
	private String iconTitle;

	/** The icon alt text. */
	private String iconAltText;

	/** The feature. */
	private String feature;

	/** The resource type. */
	private String resourceType;
	
	/** The button link. */
	private String buttonLink;
	
	/** The button action. */
	private String buttonAction;
	
	/** The button size. */
	private String buttonSize;
	
	/** The button color. */
	private String buttonColor;
	
	/** The button style. */
	private String buttonStyle;
	
	/** The link url. */
	private String linkUrl;
	
	/** The optional body text. */
	private String optionalBodyText;
	
	
	/**
	 * This method instantiates a new instance of Common Elements Sling Model.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		viewType = "testView";
		variation = "testVariation";
		radioButton = "testRadio";
		dropdown = "testDropdown";
		altText = "testAlt";
		bodyText = "testBody";
		buttonText = "testButton";
		title = "testTitle";
		linkLabel = "testLinkLabel";
		linkAction = "testLinkAction";
		linkStyle = "testLinkStyle";
		linkColor = "testLinkColor";
		logoAltText = "testLogoAlt";
		logoUrl = "testLogoUrl";
		iconTitle = "testIconTitle";
		iconAltText = "testIconAltText";
		feature = "testFeature";
		resourceType = "testResourceType";
		buttonLink = "testButtonLink";
		buttonAction = "testButtonAction";
		buttonSize = "testButtonSize";
		buttonColor = "testButtonColor";
		buttonStyle = "testButtonStyle";
		linkUrl = "testLinkUrl";	
		optionalBodyText = "Test Optional Body Text";
		Field viewTypeField;
		Field viewTypeField1;
		Field viewTypeField2;
		Field viewTypeField3;
		Field viewTypeField4;
		Field viewTypeField5;
		Field viewTypeField6;
		Field viewTypeField7;
		Field viewTypeField8;
		Field viewTypeField9;
		Field viewTypeField10;
		Field viewTypeField11;
		Field viewTypeField12;
		Field viewTypeField13;
		Field viewTypeField14;
		Field viewTypeField15;
		Field viewTypeField16;
		Field viewTypeField17;
		Field viewTypeField18;
		Field viewTypeField19;
		Field viewTypeField20;
		Field viewTypeField21;
		Field viewTypeField22;
		Field viewTypeField23;	
		Field viewTypeField24;
		viewTypeField = commonModel.getClass().getDeclaredField("viewType");
		viewTypeField1 = commonModel.getClass().getDeclaredField("variation");
		viewTypeField2 = commonModel.getClass().getDeclaredField("radioButton");
		viewTypeField3 = commonModel.getClass().getDeclaredField("dropdown");
		viewTypeField4 = commonModel.getClass().getDeclaredField("altText");
		viewTypeField5 = commonModel.getClass().getDeclaredField("bodyText");
		viewTypeField6 = commonModel.getClass().getDeclaredField("buttonText");
		viewTypeField7 = commonModel.getClass().getDeclaredField("title");
		viewTypeField8 = commonModel.getClass().getDeclaredField("linkLabel");
		viewTypeField9 = commonModel.getClass().getDeclaredField("linkAction");
		viewTypeField10 = commonModel.getClass().getDeclaredField("linkStyle");
		viewTypeField11 = commonModel.getClass().getDeclaredField("linkColor");
		viewTypeField12 = commonModel.getClass().getDeclaredField("logoAltText");
		viewTypeField13 = commonModel.getClass().getDeclaredField("logoUrl");
		viewTypeField14 = commonModel.getClass().getDeclaredField("iconTitle");
		viewTypeField15 = commonModel.getClass().getDeclaredField("iconAltText");
		viewTypeField16 = commonModel.getClass().getDeclaredField("feature");
		viewTypeField17 = commonModel.getClass().getDeclaredField("resourceType");
		viewTypeField18 = commonModel.getClass().getDeclaredField("buttonLink");
		viewTypeField19 = commonModel.getClass().getDeclaredField("buttonAction");
		viewTypeField20 = commonModel.getClass().getDeclaredField("buttonSize");
		viewTypeField21 = commonModel.getClass().getDeclaredField("buttonColor");
		viewTypeField22 = commonModel.getClass().getDeclaredField("buttonStyle");
		viewTypeField23 = commonModel.getClass().getDeclaredField("linkUrl");
		viewTypeField24 = commonModel.getClass().getDeclaredField("optionalBodyText");
		viewTypeField.setAccessible(true);
		viewTypeField1.setAccessible(true);
		viewTypeField2.setAccessible(true);
		viewTypeField3.setAccessible(true);
		viewTypeField4.setAccessible(true);
		viewTypeField5.setAccessible(true);
		viewTypeField6.setAccessible(true);
		viewTypeField7.setAccessible(true);
		viewTypeField8.setAccessible(true);
		viewTypeField9.setAccessible(true);
		viewTypeField10.setAccessible(true);
		viewTypeField11.setAccessible(true);
		viewTypeField12.setAccessible(true);
		viewTypeField13.setAccessible(true);
		viewTypeField14.setAccessible(true);
		viewTypeField15.setAccessible(true);
		viewTypeField16.setAccessible(true);
		viewTypeField17.setAccessible(true);
		viewTypeField18.setAccessible(true);
		viewTypeField19.setAccessible(true);
		viewTypeField20.setAccessible(true);
		viewTypeField21.setAccessible(true);
		viewTypeField22.setAccessible(true);
		viewTypeField23.setAccessible(true);
		viewTypeField24.setAccessible(true);
		viewTypeField.set(commonModel, viewType);
		viewTypeField1.set(commonModel, variation);
		viewTypeField2.set(commonModel, radioButton);
		viewTypeField3.set(commonModel, dropdown);
		viewTypeField4.set(commonModel, altText);
		viewTypeField5.set(commonModel, bodyText);
		viewTypeField6.set(commonModel, buttonText);
		viewTypeField7.set(commonModel, title);
		viewTypeField8.set(commonModel, linkLabel);
		viewTypeField9.set(commonModel, linkAction);
		viewTypeField10.set(commonModel, linkStyle);
		viewTypeField11.set(commonModel, linkColor);
		viewTypeField12.set(commonModel, logoAltText);
		viewTypeField13.set(commonModel, logoUrl);
		viewTypeField14.set(commonModel, iconTitle);
		viewTypeField15.set(commonModel, iconAltText);
		viewTypeField16.set(commonModel, feature);
		viewTypeField17.set(commonModel, resourceType);
		viewTypeField18.set(commonModel, buttonLink);
		viewTypeField19.set(commonModel, buttonAction);
		viewTypeField20.set(commonModel, buttonSize);
		viewTypeField21.set(commonModel, buttonColor);
		viewTypeField22.set(commonModel, buttonStyle);
		viewTypeField23.set(commonModel, linkUrl);
		viewTypeField24.set(commonModel, optionalBodyText);
	}

	/**
	 * Test get view type.
	 */
	@Test
	public void testGetViewType() {
		Assert.assertNotNull(commonModel.getViewType());
	}

	/**
	 * Test get variation.
	 */
	@Test
	public void testGetVariation() {
		Assert.assertNotNull(commonModel.getVariation());
	}

	/**
	 * Test get radio button.
	 */
	@Test
	public void testGetRadioButton() {
		Assert.assertNotNull(commonModel.getRadioButton());
	}

	/**
	 * Test get dropdown.
	 */
	@Test
	public void testGetDropdown() {
		Assert.assertNotNull(commonModel.getDropdown());
	}

	/**
	 * Test get alt text.
	 */
	@Test
	public void testGetAltText() {
		Assert.assertNotNull(commonModel.getAltText());
	}

	/**
	 * Test get body text.
	 */
	@Test
	public void testGetBodyText() {
		Assert.assertNotNull(commonModel.getBodyText());
	}

	/**
	 * Test get button text.
	 */
	@Test
	public void testGetButtonText() {
		Assert.assertNotNull(commonModel.getButtonText());
	}

	/**
	 * Test get title.
	 */
	@Test
	public void testGetTitle() {
		Assert.assertNotNull(commonModel.getTitle());
	}

	/**
	 * Test get link label.
	 */
	@Test
	public void testGetLinkLabel() {
		Assert.assertNotNull(commonModel.getLinkLabel());
	}

	/**
	 * Test get link action.
	 */
	@Test
	public void testGetLinkAction() {
		Assert.assertNotNull(commonModel.getLinkAction());
	}

	/**
	 * Test get link style.
	 */
	@Test
	public void testGetLinkStyle() {
		Assert.assertNotNull(commonModel.getLinkStyle());
	}

	/**
	 * Test get link color.
	 */
	@Test
	public void testGetLinkColor() {
		Assert.assertNotNull(commonModel.getLinkColor());
	}

	/**
	 * Test get logo alt text.
	 */
	@Test
	public void testGetLogoAltText() {
		Assert.assertNotNull(commonModel.getLogoAltText());
	}

	/**
	 * Test get logo url.
	 */
	@Test
	public void testGetLogoUrl() {
		Assert.assertNotNull(commonModel.getLogoUrl());
	}

	/**
	 * Test get icon title.
	 */
	@Test
	public void testGetIconTitle() {
		Assert.assertNotNull(commonModel.getIconTitle());
	}

	/**
	 * Test get feature flag state.
	 */
	@Test
	public void testGetFeatureFlagState() {
		Assert.assertNotNull(commonModel.getFeatureFlagState());
	}

	/**
	 * Test get component name.
	 */
	@Test
	public void testGetComponentName() {
		Assert.assertNotNull(commonModel.getComponentName());
	}
	
	/**
	 * Test get button link.
	 */
	@Test
	public void testGetButtonLink() {
		Assert.assertNotNull(commonModel.getButtonLink());
	}
	
	/**
	 * Test get button action.
	 */
	@Test
	public void testGetButtonAction() {
		Assert.assertNotNull(commonModel.getButtonAction());
	}
	
	/**
	 * Test get button color.
	 */
	@Test
	public void testGetButtonColor() {
		Assert.assertNotNull(commonModel.getButtonColor());
	}
	
	/**
	 * Test get button size.
	 */
	@Test
	public void testGetButtonSize() {
		Assert.assertNotNull(commonModel.getButtonSize());
	}
	
	/**
	 * Test get button style.
	 */
	@Test
	public void testGetButtonStyle() {
		Assert.assertNotNull(commonModel.getButtonStyle());
	}
	
	/**
	 * Test get link url.
	 */
	@Test
	public void testGetLinkUrl() {
		Assert.assertNotNull(commonModel.getLinkUrl());
	}
	
	/**
	 * Test get optional body text.
	 */
	@Test
	public void testGetOptionalBodyText() {
		Assert.assertNotNull(commonModel.getOptionalBodyText());
	}
}