package com.corteva.model.component.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.utils.ArticleUtils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The is the sling model for the datasource to fetch Article Tags
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class DynamicArticleTypeListModel {

    /** Logger Instantiation. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicArticleTypeListModel.class);

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
     * This method create List which have L1 tags of Content Type Taxonomy
     *
     *
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("Inside method init() of DynamicArticleTypeListModel");     
        DataSource ds = new SimpleDataSource(TagUtil.getMapFromTags(resourceResolver, ArticleUtils.ARITCLE_L2_TAXONOMY_STRUCTURE).iterator());
        slingRequest.setAttribute(DataSource.class.getName(), ds);
    }


}
