package com.corteva.core.base.tests;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Rule;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * This is the Base Test Class for writing the JUnit Test Cases. This class should be extended by all the Test classes.
 * 
 * @author Sapient
 *
 */
public abstract class BaseAbstractTest {

    /**
     * This rule takes care of all initialization and cleanup tasks required to make sure all unit tests can run
     * independently.
     */
    @Rule
    public AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    
    /**
     * Gets the context.
     *
     * @return the context
     */
    protected AemContext getContext() {
        return context;
    }

    /**
     * This method gets the resource resolver from AEM Context. This method will be available to all the Test classes.
     * 
     * @return the resource resolver
     */
    protected ResourceResolver getResourceResolver() {
        return context.resourceResolver();
    }

    /**
     * This method gets the Sling Http Servlet Request from AEM Context. This method will be available to all the Test
     * classes.
     * 
     * @return the sling http servlet request
     */
    protected MockSlingHttpServletRequest getRequest() {
        return context.request();
    }

    /**
     * This method gets the Sling Http Servlet Response from AEM Context. This method will be available to all the Test
     * classes.
     * 
     * @return the sling http servlet response
     */
    protected MockSlingHttpServletResponse getResponse() {
        return context.response();
    }

    /**
     * This method gets the current resource from AEM Context. This method will be available to all the Test classes.
     * 
     * @return the current resource
     */
    protected Resource getCurrentResource() {
        return context.currentResource();
    }
}