package com.corteva.model.component.test;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.jackrabbit.value.StringValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.model.component.bean.AccordionListItemBean;
import com.corteva.model.component.models.AccordionListModel;


/**
 * This is a test class for Accordion Model...
 * 
 * @author Sapient
 * 
 */
public class AccordionListModelTest extends BaseAbstractTest {

	/** The experience fragment model. */
	@Mock
	private AccordionListModel accordionListModel;

	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The property. */
	private Property property;

	/** The anchor bean list. */
	private List<AccordionListItemBean> accordionListItemBeanList;

	/** The anchor list. */
	private List<AccordionListItemBean> accordionListItemList;

	/** The value. */
	private Value value;

	/** Icon Instantiation. */
	private static final String ICON = "icon";

	/** Image Instantiation. */
	private static final String IMAGE = "image";

	/**
	 * This method instantiates a new instance of Cards Container Sling Model.
	 */
	@Before
	public void setUp() {
		baseService = new BaseConfigurationService();
		getContext().registerInjectActivateService(baseService);
		getContext().addModelsForPackage("com.corteva.model.component.models");
		accordionListModel = getRequest().adaptTo(AccordionListModel.class);
		accordionListItemBeanList = new ArrayList<>();
		AccordionListItemBean accordionListItemBean = new AccordionListItemBean();
		accordionListItemBean.setItemHeader("header");
		accordionListItemBean.setAltText("Alt text");
		property = Mockito.mock(Property.class);
		accordionListItemList = Mockito.mock(List.class);
		accordionListItemBeanList.add(accordionListItemBean);
		try {
			Session mockSession = getResourceResolver().adaptTo(Session.class);
			mockSession.getRootNode()
					.addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/accordionList");

		} catch (RepositoryException e) {
			Assert.fail("RepositoryException occurred in setUp(): " + e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItemForIconSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"altText\":\"Alt text\",\"backgroundColor\":\"green\",\"itemHeader\":\"header\"}"));
			accordionListModel.setItemHeaderAssetType(ICON);
			accordionListModel.setAccordionDisplay(CortevaConstant.TRUE);
			accordionListItemList = accordionListModel.getAccordionListItem(property);
			for (AccordionListItemBean bean : accordionListItemList) {
				for (AccordionListItemBean beanTest : accordionListItemBeanList) {
					Assert.assertEquals(bean.getAltText(), beanTest.getAltText());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAccordionListItemForIconSuccess(): " + e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItemForIconFail() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(""));
			accordionListModel.setItemHeaderAssetType(ICON);
			accordionListModel.setAccordionDisplay(CortevaConstant.TRUE);
			accordionListItemList = accordionListModel.getAccordionListItem(property);
			Assert.assertEquals(0, accordionListItemList.size());
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAccordionListItemForIconFail(): " + e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItem() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"altText\":\"Alt text\",\"backgroundColor\":\"green\",\"itemHeader\":\"header\"}"));
			accordionListModel.setItemHeaderAssetType("noasset");
			accordionListModel.setAccordionDisplay(CortevaConstant.TRUE);
			accordionListItemList = accordionListModel.getAccordionListItem();
			Assert.assertNotNull(accordionListItemList);
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAccordionListItemForIconFail(): " + e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItemForImageSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"title\":\"Title\",\"altText\":\"Alt text\",\"itemHeader\":\"header\",\"itemDetail\":\"detail\"}"));
			accordionListModel.setItemHeaderAssetType(IMAGE);
			accordionListModel.setAccordionDisplay(CortevaConstant.TRUE);
			accordionListItemList = accordionListModel.getAccordionListItem(property);
			for (AccordionListItemBean bean : accordionListItemList) {
				for (AccordionListItemBean beanTest : accordionListItemBeanList) {
					Assert.assertEquals(bean.getAltText(), beanTest.getAltText());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail(
					"Repository Exception occurred in testGetAccordionListItemForImageSuccess(): " + e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItemForImageStaticListSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue(
					"{\"title\":\"Title\",\"altText\":\"Alt text\",\"itemHeader\":\"header\",\"itemDetail\":\"detail\"}"));
			accordionListModel.setAccordionDisplay(CortevaConstant.FALSE);
			accordionListModel.setItemHeaderAssetType(IMAGE);
			accordionListItemList = accordionListModel.getAccordionListItem(property);
			for (AccordionListItemBean bean : accordionListItemList) {
				for (AccordionListItemBean beanTest : accordionListItemBeanList) {
					Assert.assertEquals(bean.getItemHeader(), beanTest.getItemHeader());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail("Repository Exception occurred in testGetAccordionListItemForImageStaticListSuccess(): "
					+ e.getMessage());
		}
	}

	@Test
	public void testGetAccordionListItemForNoAssetSuccess() {
		try {
			Mockito.when(property.getValue())
					.thenReturn(new StringValue("{\"itemHeader\":\"header\",\"itemDetail\":\"<p>detail</p>\"}"));
			accordionListModel.setItemHeaderAssetType("noAsset");
			accordionListModel.setAccordionDisplay(CortevaConstant.TRUE);
			accordionListItemList = accordionListModel.getAccordionListItem(property);
			for (AccordionListItemBean bean : accordionListItemList) {
				for (AccordionListItemBean beanTest : accordionListItemBeanList) {
					Assert.assertEquals(bean.getItemHeader(), beanTest.getItemHeader());
				}
			}
		} catch (RepositoryException e) {
			Assert.fail(
					"Repository Exception occurred in testGetAccordionListItemForNoAssetSuccess(): " + e.getMessage());
		}
	}

	@Test
	public void testGetItemHeaderAssetType() {
		Assert.assertNull(accordionListModel.getItemHeaderAssetType());
	}

	@Test
	public void testGetAccordionDisplay() {
		Assert.assertNull(accordionListModel.getAccordionDisplay());
	}

}
