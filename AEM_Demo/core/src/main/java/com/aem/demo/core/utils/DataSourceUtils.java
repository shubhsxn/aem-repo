package com.aem.demo.core.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

/**
 * The is a common utility class that will contain methods that will be used by
 * the entire application.
 *
 * @author Sapient
 */
public class DataSourceUtils {
	/**
     * Logger Instantiation.
     */
   // private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtils.class);
    
	/**
     * This method gets the resource for path browser field of dialog.
     *
     * @param request 		  the sling request 
     * @param currentResource the current resource
     * @param resourceResolver        the resolver
     * @return ds			  the datasource object
     */
	public static DataSource getStateDropdownValues(SlingHttpServletRequest request, Resource currentResource, ResourceResolver resourceResolver) {
		Map<String, String> languages = new LinkedHashMap<>();
		languages.put("", "Select");
		languages.put("ar", "Arabic");
		languages.put("en", "English");
		languages.put("de", "German");
		
		
		TransformIterator transformIterator = new TransformIterator(languages.keySet().iterator(), new Transformer() {
			@Override
			public Object transform(Object o) {
				String language = (String) o;
				
				Map<String, Object> map = new HashMap<>();
				map.put("text", languages.get(language));
				map.put("value", language);
				
				ValueMap valueMap = new ValueMapDecorator(map);
				
				return new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", valueMap);
			}
		});
		DataSource ds = new SimpleDataSource(transformIterator);
		
		request.setAttribute(DataSource.class.getName(), ds);
		return ds;
		
	}
}
