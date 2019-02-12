package com.corteva.model.component.test;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.value.StringValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.corteva.model.component.bean.LinkListBean;

import com.corteva.model.component.models.LinkListModel;
import com.corteva.core.base.tests.BaseAbstractTest;

/**
 * This is the test class to test the Link List Model.
 * 
 * @author Sapient
 */
public class LinkListModelTest extends BaseAbstractTest {

	/** The link list model. */
	@Mock
	private LinkListModel linkListModel;

	/** The anchor bean list. */
	private List<LinkListBean> linkListBeanList;

	/** The property. */
	private Property property;

	/** The anchor list. */
	private List<LinkListBean> linkList;

	/**
	 * Method to set up the test objects.
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		linkListModel = new LinkListModel();
		linkListBeanList = new ArrayList<>();
		LinkListBean linkListBean= new LinkListBean();
		linkListBean.setLinkLabel("HorizontalLinkLabel");
		linkListBean.setLinkUrl("/content/corteva/na/us/en/horizontallinks.html");
		linkListBean.setLinkAction("_self");
		linkListBean.setCountrySel("yes");
		linkListBean.setHasDropdown("true");
		linkListBean.setLinkStyle("internal");
		linkListBean.setLinkColor("Blue");
		linkListBeanList.add(linkListBean);
		property = Mockito.mock(Property.class);
		linkList = Mockito.mock(List.class);
	}

	/**
	 * Test case to test for pass condition.
	 */
	@Test
	public void testGetNavigationLinksSuccess() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"linkLabel\":\"HorizontalLinkLabel\",\"linkUrl\":\"/content/corteva/na/us/en/horizontallinks.html\", \"linkAction\":\"_self\" ,\"countrySel\":\"yes\" , \"hasDropdown\":\"true\" ,\"linkStyle\":\"internal\",\"linkColor\":\"Blue\"}"));
			linkList = linkListModel.getNavigationLinks(property);
			for (LinkListBean bean : linkListBeanList) {
                for (LinkListBean beanTest : linkList) {
                    Assert.assertEquals(bean.getLinkLabel(), beanTest.getLinkLabel());
                    Assert.assertEquals(bean.getLinkAction(), beanTest.getLinkAction());
                    Assert.assertEquals(bean.getLinkUrl(), beanTest.getLinkUrl());
                    Assert.assertEquals(bean.getCountrySel(), beanTest.getCountrySel());
                    Assert.assertEquals(bean.getHasDropdown(), beanTest.getHasDropdown());
                    Assert.assertEquals(bean.getLinkStyle(), beanTest.getLinkStyle());
                    Assert.assertEquals(bean.getLinkColor(), beanTest.getLinkColor());
                }
            }
		} catch(RepositoryException e)
	{
		Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
	}
}
	
	/**
	 * Test case to test for pass condition.
	 */
	@Test
	public void testGetNavigationLinksFail() {
		try {
			Mockito.when(property.getValue()).thenReturn(new StringValue("{\"linkLabel\":\"HorizontalLinkLabelNew\",\"linkUrl\":\"/content/corteva/na/us/en/horizontallinksnew.html\", \"linkAction\":\"_new\", \"countrySel\":\"yes\" ,  \"hasDropdown\":\"false\",\"linkStyle\":\"external\",\"linkColor\":\"White\" }"));
			linkList = linkListModel.getNavigationLinks(property);
			for (LinkListBean bean : linkListBeanList) {
                for (LinkListBean beanTest : linkList) {
                    Assert.assertNotEquals(bean.getLinkLabel(), beanTest.getLinkLabel());
                    Assert.assertNotEquals(bean.getLinkAction(), beanTest.getLinkAction());
                    Assert.assertNotEquals(bean.getLinkUrl(), beanTest.getLinkUrl());
                    Assert.assertEquals(bean.getCountrySel(), beanTest.getCountrySel());
                    Assert.assertNotEquals(bean.getHasDropdown(), beanTest.getHasDropdown());
                    Assert.assertNotEquals(bean.getLinkStyle(), beanTest.getLinkStyle());
                    Assert.assertNotEquals(bean.getLinkColor(), beanTest.getLinkColor());
                }
            }
		} catch(RepositoryException e)
	{
		Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
	}
}

}
