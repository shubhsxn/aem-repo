package com.corteva.service.listener;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.workflow.event.WorkflowEvent;

/**
 * The listener interface for receiving productCache events. The class that is
 * interested in processing a productCache event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addProductCacheListener<code> method. When the productCache
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see ProductCacheEvent
 */
@Component(service = EventHandler.class, immediate = true, property = {
	EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
	EventConstants.EVENT_FILTER + "=" + "(paths=/content/corteva/*)" })
public class ProductCacheListener implements EventHandler {

    /** The Constant CACHE_INVALIDATION_PATH. */
    private static final String CACHE_INVALIDATION_PATH = "http://{0}{1}";

    /** The Constant DISPATCHER_CACHE_URL. */
    private static final String DISPATCHER_CACHE_URL = "/dispatcher/invalidate.cache";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCacheListener.class);

    /** The resource resolver factory. */
    private ResourceResolverFactory resourceResolverFactory;

    /** The base service. */
    private transient BaseConfigurationService configurationService;

    /**
     * Bind base configuration service.
     * 
     * @param configurationService
     *            the base service
     */
    @Reference
    public void bindBaseConfigurationService(BaseConfigurationService configurationService) {
	this.configurationService = configurationService;
    }

    /**
     * Unbind base configuration service.
     * 
     * @param configurationService
     *            the configurationService
     */
    public void unbindBaseConfigurationService(BaseConfigurationService configurationService) {
	this.configurationService = configurationService;
    }

    /**
     * Bind resource resolver factory.
     * 
     * @param resourceResolverFactory
     *            the resource resolver factory
     */
    @Reference
    public void bindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
	this.resourceResolverFactory = resourceResolverFactory;
    }

    /**
     * Unbind resource resolver factory.
     * 
     * @param resourceResolverFactory
     *            the resource resolver factory
     */
    public void unbindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
	this.resourceResolverFactory = resourceResolverFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.
     * Event)
     */
    @Override
    public void handleEvent(Event event) {
	boolean featureFlag = true;
	if (null != configurationService) {
	    featureFlag = configurationService.getToggleInfo(CortevaConstant.PRODUCT_CACHE_LISTENER,
		    CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
	}
	if (featureFlag) {
	    LOGGER.debug("Event occurred: {}", event.getProperty(WorkflowEvent.EVENT_TYPE));
	    ReplicationAction action = ReplicationAction.fromEvent(event);
	    if (action != null) {
		LOGGER.debug("Replication action {} occured on {} ", action.getType().getName(), action.getPath());
		ResourceResolver resourceResolver = getResourceResolver();
		Resource resource = resourceResolver.resolve(action.getPath());

		Resource contentResource = CommonUtils.getPageFromResource(resourceResolver, resource)
			.getContentResource();
		ValueMap valueMap = contentResource.getValueMap();
		String resourceType = valueMap.get(CortevaConstant.SLING_RESOURCE_TYPE, String.class);

		clearPDPCache(resource, valueMap, resourceType);
	    }
	}
    }

    /**
     * Clear PDP cache.
     *
     * @param resource
     *            the resource
     * @param valueMap
     *            the value map
     * @param resourceType
     *            the resource type
     */
	private void clearPDPCache(Resource resource, ValueMap valueMap,
			String resourceType) {
		if (CortevaConstant.PDP_RESOURCE_TYPE.equals(resourceType) && valueMap.containsKey(CortevaConstant.PDP_SOURCE)
				&& ((String) valueMap.get(CortevaConstant.PDP_SOURCE))
				.equalsIgnoreCase(CortevaConstant.NON_AGRIAN)) {
				LOGGER.info("Non agrian PDP resource replicated : {}",
						resource.getName());
				String path = CortevaConstant.PDP_SERVLET_URL
						+ CortevaConstant.DOT + resource.getName()
						+ CortevaConstant.DOT + CortevaConstant.NON_AGRIAN
						+ CortevaConstant.DOT + "json";
				clearDispatcherCache(path);
			}
	}

    /**
     * Gets the resourceResolver from resourceresolverfactory.
     * 
     * @return the resourceResolver
     */
    public ResourceResolver getResourceResolver() {
	LOGGER.debug("entering  getResourceResolver method");
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put(ResourceResolverFactory.SUBSERVICE, CortevaConstant.SYSTEM_USER_SERVICE);
	ResourceResolver resourceResolver = null;
	if (resourceResolverFactory != null) {
	    try {
		resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
	    } catch (LoginException loginException) {
		LOGGER.error("LoginException occured in getTemplateMap method of MailServiceImpl", loginException);
	    }
	}
	LOGGER.debug("exiting  getResourceResolver method");
	return resourceResolver;

    }

    /**
     * Clear cache.
     * 
     * @param path
     *            the path
     */
    private void clearDispatcherCache(final String path) {
	try {
	    String[] dispatcherHosts = configurationService.getPropValuesFromConfiguration(
		    CortevaConstant.GLOBAL_CONFIG_NAME, "dispatcherHosts");
	    if (null != dispatcherHosts) {
		for (String hostIp : dispatcherHosts) {
		    HttpClient client = new HttpClient();

		    String cacheInvalidationPath = MessageFormat.format(CACHE_INVALIDATION_PATH, hostIp,
			    DISPATCHER_CACHE_URL);

		    PostMethod post = new PostMethod(cacheInvalidationPath);
		    post.setRequestHeader("cq-action", "Delete");
		    post.setRequestHeader("cq-handle", path);

		    StringRequestEntity body = new StringRequestEntity(path, null, null);
		    post.setRequestEntity(body);
		    post.setRequestHeader("Content-length", String.valueOf(body.getContentLength()));
		    client.executeMethod(post);
		    post.releaseConnection();
		    // log the results
		    LOGGER.info("result: " + post.getResponseBodyAsString());
		}
	    }
	} catch (IOException e) {
	    LOGGER.error("Exception occured during dispatcher cache cleaning", e);
	}
	LOGGER.debug("Exiting method: clearDispatcherCache");
    }

}