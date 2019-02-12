/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.service.servlets.test;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.ReorderNodesServlet;

/**
 * The is the test class to test the Reorder Nodes Servlet. /*
 * 
 * @author Sapient
 *
 */
public class ReorderNodesServletTest extends BaseAbstractTest {

    /** The reorder nodes servlet. */
    private ReorderNodesServlet reorderNodesServlet;

    /** The mock request. */
    private MockSlingHttpServletRequest mockRequest;

    /** The mock response. */
    private MockSlingHttpServletResponse mockResponse;
    
    private BaseConfigurationService baseService;

    /**
     * Sets the method parameters and adds nodes to the mock session.
     *
     * @throws RepositoryException that an Repository exception has occurred.
     */
    @Before
    public void setUp() {
    	ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
        reorderNodesServlet = new ReorderNodesServlet();
        mockRequest = getRequest();
        mockResponse = getResponse();
        String[] anchorTitleArr = new String[] { "Test1", "Test2" };
        Map<String, Object> anchorMap = new HashMap<>();
        anchorMap.put("anchorNavResourcePath",
                "/content/corteva/na/us/en/_jcr_content/root/responsivegrid/anchornavigation");
        anchorMap.put("anchorLinkLabelHidden", anchorTitleArr);
        mockRequest.setParameterMap(anchorMap);
        try {
        	myServiceConfig = configAdmin.getConfiguration(CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.FEATURE_JAVA_FRAMEWORK, "true");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
            Session mockSession = getResourceResolver().adaptTo(Session.class);
            mockSession.getRootNode()
                    .addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/anchornavigation");
        } catch (RepositoryException | IOException e) {
            Assert.fail("Exception occurred in setUp(): " + e.getMessage());
        }
    }

    /**
     * Test reorder nodes servlet for success.
     */
    @Test
    public void testReorderNodesServletSuccess() {
        try {
        	reorderNodesServlet.bindBaseConfigurationService(baseService);
            reorderNodesServlet.doPost(mockRequest, mockResponse);
            Assert.assertEquals(200, mockResponse.getStatus());
        } catch (ServletException | IOException e) {
            Assert.fail("Exception occurred in testVerify(): " + e.getMessage());
        }
    }

    /**
     * Test reorder nodes servlet for failure.
     */
    @Test
    public void testReorderNodesServletFail() {
        try {
        	reorderNodesServlet.bindBaseConfigurationService(baseService);
            reorderNodesServlet.doPost(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException | IOException e) {
            Assert.fail("Exception occurred in testVerify(): " + e.getMessage());
        }
    }
}