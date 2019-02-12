package com.corteva.service.listener.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.event.Event;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.service.listener.ProductCacheListener;

public class ProductCacheListenerTest extends BaseAbstractTest{

	/** The Article feed servlet. */
	private ProductCacheListener productCacheListener;


	/** The base service. */
	@Mock
	private BaseConfigurationService baseService;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	/** The resource resolver factory. */
	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	/** The mock resource. */
	@Mock
	private Resource mockResource;

	/**
	 * Sets the method parameters and adds nodes to the mock session.
	 */
	@Before
	public void setUp() {
		try {
			MockitoAnnotations.initMocks(this);
			productCacheListener = new ProductCacheListener();
			context.registerInjectActivateService(new BaseConfigurationService());
			Mockito.when(resourceResolver.resolve(Mockito.any(SlingHttpServletRequest.class), Mockito.anyString()))
					.thenReturn(mockResource);
			productCacheListener.bindBaseConfigurationService(baseService);
			Mockito.when(baseService.getToggleInfo(CortevaConstant.PRODUCT_CACHE_LISTENER,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME)).thenReturn(true);
		} catch (Exception e) {
			Assert.fail("Exception occurred in setUp()" + e.getMessage());
		}
	}

	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testHandleEvent() {
		Map<String, String> properties = new HashMap<>();
		properties.put("", "");
		Event event = new Event("topic", properties);
		productCacheListener.handleEvent(event);
	}
	
	/**
	 * Test create get article feed when article type is All.
	 */
	@Test
	public void testGetResourceResolver() {
		try {
			productCacheListener.bindResourceResolverFactory(resourceResolverFactory);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(ResourceResolverFactory.SUBSERVICE, CortevaConstant.SYSTEM_USER_SERVICE);
			Mockito.when(resourceResolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			Assert.assertNotNull(productCacheListener.getResourceResolver());
		} catch (LoginException e) {
			Assert.fail("Login Exception occured in testHandlEvent() method: " + e.getMessage());
		}
	}
	
    /**
     * Test Unbind base configuration service.
     */
	@Test
    public void unbindBaseConfigurationService() {
		productCacheListener.unbindBaseConfigurationService(baseService);
    }

    /**
     * Test Unbind resource resolver factory.
     */
    @Test
    public void unbindResourceResolverFactory() {
    	productCacheListener.unbindResourceResolverFactory(resourceResolverFactory);
    }

}
