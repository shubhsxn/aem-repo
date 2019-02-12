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
import com.corteva.model.component.models.TimelineCardModel;

/**
 * This is a test class for Timeline Card Model.
 * 
 * @author Sapient
 * 
 */
public class TimelineCardModelTest extends BaseAbstractTest {
	
	/** The time line card model. */
	@Mock
	private TimelineCardModel timeLineCardModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The property. */
	private Property prop;

	/** The value. */
	private Value val;

	/** The responsive grid name. */
	private String responsiveGridName = StringUtils.EMPTY;

	/** The mock session. */
	private Session mockSession;

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		timeLineCardModel = getRequest().adaptTo(TimelineCardModel.class);
		prop = Mockito.mock(Property.class);
		val = Mockito.mock(Value.class);
		try {
			mockSession = getResourceResolver().adaptTo(Session.class);
			mockSession.getRootNode()
					.addNode("/content/corteva/na/us/en/test-blank/jcr:content/root/responsivegrid/anchornavigation/Anchorgrid_Test2/timelinecard");
		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in setUp(): " + e.getMessage());
		}
	}

	/**
	 * Test responsive grid name success.
	 */
	@Test
	public void testResponsiveGridNameSuccess() {
		String componentPath = "/content/corteva/na/us/en/test-blank/_jcr_content/root/responsivegrid/anchornavigation/Anchorgrid_Test2/timelinecard";
		try {
			Mockito.when(val.getString()).thenReturn(componentPath);
			Mockito.when(prop.getValue()).thenReturn(val);
			responsiveGridName = timeLineCardModel.getResponsiveGridNodeName(prop);
			Assert.assertEquals(CortevaConstant.RESPONSIVE_GRID + "timelinecard", responsiveGridName);

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
			Mockito.when(val.getString()).thenReturn(componentPath);
			Mockito.when(prop.getValue()).thenReturn(val);
			responsiveGridName = timeLineCardModel.getResponsiveGridNodeName(prop);
			Assert.assertNotEquals(CortevaConstant.RESPONSIVE_GRID + "timelinecard", responsiveGridName);
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testResponsiveGridName(): " + e.getMessage());
		}
	}
	
	/**
	 * Test title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(timeLineCardModel.getTitle());
	}
	
	/**
	 * Test teaser text.
	 */
	@Test
	public void testTeaserText() {
		Assert.assertNull(timeLineCardModel.getTeaserText());
	}
	
	/**
	 * Test flag label.
	 */
	@Test
	public void testFlagLabel() {
		Assert.assertNull(timeLineCardModel.getFlagLabel());
	}
	
	/**
	 * Test timeline label.
	 */
	@Test
	public void testTimelineLabel() {
		Assert.assertNull(timeLineCardModel.getTimelineLabel());
	}
	
	/**
	 * Test modal cta text.
	 */
	@Test
	public void testModalCtaText() {
		Assert.assertNull(timeLineCardModel.getModalCtaText());
	}
	
	/**
	 * Test associate modal.
	 */
	@Test
	public void testAssociateModalDetail() {
		Assert.assertNull(timeLineCardModel.getAssociateModalDetail());
	}
}