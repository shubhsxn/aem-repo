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
import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.bean.NavHeaderTabsBean;
import com.corteva.model.component.models.NavHeaderTabsModel;

/**
 * The Class NavHeaderTabsModelTest.
 */
public class NavHeaderTabsModelTest extends BaseAbstractTest {

	/** The Nav HeaderTabs model. */
	@Mock
	private NavHeaderTabsModel navHeaderTabsModel;
    
    /** The Nav Header Tabs  bean list. */
    private List<NavHeaderTabsBean> navHeaderTabsBeanList;

    /** The property. */
    private Property property;
    
    /** The nav header tabs model list. */
    private List<NavHeaderTabsBean> navHeaderTabsList;
    
    /**
     * Method to set up the test objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        navHeaderTabsModel = new NavHeaderTabsModel();
        navHeaderTabsBeanList = new ArrayList<>();
        NavHeaderTabsBean navHeaderTabsBean = new NavHeaderTabsBean();
        navHeaderTabsBean.setTabLabel("Tab1");
        navHeaderTabsBeanList.add(navHeaderTabsBean);
        property = Mockito.mock(Property.class);
        navHeaderTabsList = Mockito.mock(List.class);
    }
    
    /**
     * Test case to test for pass condition.
     */
    @Test
    public void testGetNavHeaderTabsSuccess() {
        try {
        	 Mockito.when(property.getValue()).thenReturn(new StringValue("{\"tabLabel\":\"Tab1\" }"));
        	 
        	 navHeaderTabsList = navHeaderTabsModel.getNavHeaderTabs(property);
            for (NavHeaderTabsBean bean : navHeaderTabsBeanList) {
                for (NavHeaderTabsBean beanTest : navHeaderTabsList) {
                    Assert.assertEquals(bean.getTabLabel(), beanTest.getTabLabel());
                }
            }
        } catch (RepositoryException e) {
            Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
        }
    }
    
    /**
     * Test case to test for pass condition.
     */
    @Test
    public void testGetNavHeaderTabsFail() {
        try {
        	 Mockito.when(property.getValue()).thenReturn(new StringValue("{\"tabLabel\":\"Tab2\" }"));
        	 
        	 navHeaderTabsList = navHeaderTabsModel.getNavHeaderTabs(property);
            for (NavHeaderTabsBean bean : navHeaderTabsBeanList) {
                for (NavHeaderTabsBean beanTest : navHeaderTabsList) {
                    Assert.assertNotEquals(bean.getTabLabel(), beanTest.getTabLabel());
                }
            }
        } catch (RepositoryException e) {
            Assert.fail("Repository Exception occurred in testGetAnchorLinksContainer(): " + e.getMessage());
        }
    }
    
    /**
	 * Test Title.
	 */
	@Test
	public void testTitle() {
		Assert.assertNull(navHeaderTabsModel.getTitle());
	}
	
	/**
	 * Test Sub Title.
	 */
	@Test
	public void testSubTitle() {
		Assert.assertNull(navHeaderTabsModel.getSubTitle());
	}
}
