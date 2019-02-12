package com.corteva.model.component.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.TagUtil;
import org.apache.commons.lang.StringUtils;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * The is the sling model for the Related Content Cards.
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class DynamicPageTypeModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicPageTypeModel.class);
	
	/** The CREATE_PAGE_WIZARD_URL */
	private static final String CREATE_PAGE_WIZARD_URL = "createpagewizard.html";

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
	 * @return the resource list
	 *
	 */
	@PostConstruct
	public List<Resource> init() {
		LOGGER.debug("Inside method init() of DynamicPageTypeModel");
		List<Resource> resourceList = new ArrayList<>();
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		List<Tag> tags = TagUtil.listChildren(tagManager, CortevaConstant.CONTENT_TYPES_TAXONOMY_STRUCTURE);
		for (final Tag tag : tags) {
			ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
			vm.put(CortevaConstant.VALUE, (null != tag.getLocalizedTitle(getLocale(slingRequest)))
					? tag.getLocalizedTitle(getLocale(slingRequest)) : CommonUtils.getTagTitle(slingRequest, tag));
			vm.put(CortevaConstant.TEXT, (null != tag.getLocalizedTitle(getLocale(slingRequest)))
					? tag.getLocalizedTitle(getLocale(slingRequest)) : CommonUtils.getTagTitle(slingRequest, tag));
			resourceList
					.add(new ValueMapResource(resourceResolver, new ResourceMetadata(), CortevaConstant.NODE_TYPE, vm));
		}
		DataSource ds = new SimpleDataSource(resourceList.iterator());
		slingRequest.setAttribute(DataSource.class.getName(), ds);
		return resourceList;
	}

	/**
	 * @param resourceResolver
	 *            the resourceResolver to set
	 */
	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	/**
	 * @param request
	 *            the sling request
	 * @return locale the locale
	 */
	public Locale getLocale(SlingHttpServletRequest request) {
		LOGGER.debug("Inside getLocale() method");
		String pagePath = "";
		if (StringUtils.isNotEmpty(request.getParameter(CortevaConstant.ITEM))) {
			pagePath = request.getParameter(CortevaConstant.ITEM);
		} else {
			if (null != request.getHeader(CortevaConstant.REFERER)) {
			String fullPagePath = request.getHeader(CortevaConstant.REFERER);
			pagePath = fullPagePath.contains(CREATE_PAGE_WIZARD_URL) ? fullPagePath.split(CREATE_PAGE_WIZARD_URL)[1] : pagePath;
			}
		}
		Locale locale = null;
		locale = new Locale(CommonUtils.getI18nLocale(pagePath, request.getResourceResolver()));
		return locale;
	}

}
