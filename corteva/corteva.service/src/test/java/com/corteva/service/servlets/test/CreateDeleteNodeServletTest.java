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
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.servlets.CreateDeleteNodeServlet;

/**
 * The is the test class to test the Create Delete Node Servlet. /*
 * 
 * @author Sapient
 *
 */
public class CreateDeleteNodeServletTest extends BaseAbstractTest {

    /** The create delete servlet. */
    private CreateDeleteNodeServlet createDeleteServlet;

    /** The mock request. */
    private MockSlingHttpServletRequest mockRequest;

    /** The mock response. */
    private MockSlingHttpServletResponse mockResponse;

    /** The mock session. */
    private Session mockSession;
    
    /** The base service. */
    private BaseConfigurationService baseService;

    /**
     * Sets the method parameters and adds nodes to the mock session.
     */
    @Before
    public void setUp() {
    	ConfigurationAdmin configAdmin = context.getService(ConfigurationAdmin.class);
		context.registerInjectActivateService(new BaseConfigurationService());
		Configuration myServiceConfig;
        createDeleteServlet = new CreateDeleteNodeServlet();
        MockitoAnnotations.initMocks(this);
        mockRequest = getRequest();
        mockResponse = getResponse();

        String[] expfragPathArr = new String[] {
                "/content/experience-fragments/corteva/dupont-india/tileProfile/tile-profile",
                "/content/experience-fragments/corteva/dupont-india/cardA/card-a" };
        Map<String, Object> expfrMap = new HashMap<>();
        expfrMap.put("expfragPath", expfragPathArr);
        expfrMap.put("componentResourcePath",
                "/content/corteva/na/us/en/_jcr_content/root/responsivegrid/experiencefragmentco");
        mockRequest.setParameterMap(expfrMap);
        try {
        	myServiceConfig = configAdmin.getConfiguration(CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(CortevaConstant.FEATURE_JAVA_FRAMEWORK, "true");
			myServiceConfig.update(props);
			baseService = context.getService(BaseConfigurationService.class);
            mockSession = getResourceResolver().adaptTo(Session.class);
            mockSession.getRootNode()
                    .addNode("/content/corteva/na/us/en/jcr:content/root/responsivegrid/experiencefragmentco");
        } catch (RepositoryException | IOException e) {
            Assert.fail("Exception occurred in setUp(): " + e.getMessage());
        }
    }

    /**
     * Test create delete node servlet for success.
     */
    @Test
    public void testCreateDeleteNodeServletSuccess() {
        try {
        	createDeleteServlet.bindBaseConfigurationService(baseService);
            createDeleteServlet.doPost(mockRequest, mockResponse);
            Assert.assertEquals(200, mockResponse.getStatus());
        } catch (ServletException | IOException e) {
            Assert.fail("Exception occurred in testCreateDeleteNodeServletSuccess(): " + e.getMessage());
        }
    }

    /**
     * Test create delete node servlet for failure.
     */
    @Test
    public void testCreateDeleteNodeServletFail() {
        try {
        	createDeleteServlet.bindBaseConfigurationService(baseService);
            createDeleteServlet.doPost(mockRequest, mockResponse);
            Assert.assertNotEquals(500, mockResponse.getStatus());
        } catch (ServletException | IOException e) {
            Assert.fail("Exception occurred in testCreateDeleteNodeServletFail(): " + e.getMessage());
        }

    }
}