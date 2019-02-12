package com.corteva.model.component.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.TagUtil;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The is the sling model for the datasource to fetch facet Tags
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class DynamicFacetTypeListModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicFacetTypeListModel.class);

	/**
	 * The sling request.
	 */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/**
	 * The resource resolver.
	 */
	@Inject
	private ResourceResolver resourceResolver;

	/**
	 * This method create List which have tags of all facets in Taxonomy
	 *
	 *
	 */
	@PostConstruct
	public void init() {
		LOGGER.debug("Inside method init() of DynamicFacetTypeListModel");
		DataSource ds = new SimpleDataSource(
				TagUtil.getMapFromTags(resourceResolver, CortevaConstant.TAXONOMY_ROOT).iterator());
		slingRequest.setAttribute(DataSource.class.getName(), ds);
	}

}
