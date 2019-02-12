package com.corteva.model.component.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.core.utils.SQLQueryUtil;
import com.corteva.core.utils.SearchInput;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.ProductFilterBean;
import com.corteva.model.component.bean.ProductFilterChildBean;
import com.corteva.model.component.bean.ProductFilterListBean;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The Class ProductFilterHelper.
 */
public class ProductFilterHelper {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFilterHelper.class);

	/** The Constant DA_PRODUCT_SUB_TYPE_FILTER. */
	private static final String DA_PRODUCT_SUB_TYPE_FILTER = "product-sub-type-filter";

	/** The Constant CROP_FILTER_PROP. */
	private static final String CROP_FILTER_PROP = "cropFilter";

	/** The Constant STATE_FILTER_PROP. */
	private static final String STATE_FILTER_PROP = "stateFilter";

	/** The Constant PESTS_FILTER_PROP. */
	private static final String PESTS_FILTER_PROP = "pestsFilter";

	/** The Constant PESTS_FILTER_TAG_PATH. */
	private static final String PESTS_FILTER_TAG_PATH = "pestsFilterTagPath";

	/** The Constant WEEDS_FILTER_PROP. */
	private static final String WEEDS_FILTER_PROP = "weedsFilter";

	/** The Constant WEEDS_FILTER_TAG_PATH. */
	private static final String WEEDS_FILTER_TAG_PATH = "weedsFilterTagPath";

	/** The Constant DISEASE_FILTER_PROP. */
	private static final String DISEASE_FILTER_PROP = "diseaseFilter";

	/** The Constant DISEASE_FILTER_TAG_PATH. */
	private static final String DISEASE_FILTER_TAG_PATH = "diseaseFilterTagPath";

	/** The Constant USESITE_FILTER_PROP. */
	private static final String USESITE_FILTER_PROP = "useSiteFilter";

	/** The Constant MARKET_FILTER_PROP. */
	private static final String MARKET_FILTER_PROP = "marketFilter";

	/** The Constant PRODUCT_TYPE_FILTER_PROP. */
	private static final String PRODUCT_TYPE_FILTER_PROP = "productTypeFilter";

	/** The Constant PRODUCT_TYPE_SUB_PROP. */
	private static final String PRODUCT_TYPE_SUB_PROP = "productTypeSubFilter";

	/** The Constant REGEX. */
	private static final String REGEX = "\\<.*?\\>";

	/**
	 * Gets the product filter list.
	 *
	 * @param request
	 *            the request
	 * @param currentResource
	 *            the current resource
	 * @param configurationService
	 *            the configuration service
	 * @return the product filter list
	 */
	public static ProductFilterBean getProductFilterList(SlingHttpServletRequest request, Resource currentResource,
			BaseConfigurationService configurationService) {

		ResourceResolver resourceResolver = currentResource.getResourceResolver();
		ValueMap valueMapCurrentResource = currentResource.getValueMap();

		String[] productListTags = (String[]) valueMapCurrentResource.get("productListTags");

		Iterator<Resource> documents = getPdpResources(request, resourceResolver, productListTags,
				configurationService);
		List<ProductFilterChildBean> productList = new ArrayList<>();
		List<String> productTypeTags = new ArrayList<>();
		ProductFilterBean filterBean = new ProductFilterBean();
		List<String> allProductTagList = processPdpPages(request, resourceResolver, productList, documents, filterBean,
				productTypeTags);

		Map<String, ProductFilterListBean> filters = getFilters(request, resourceResolver, valueMapCurrentResource,
				filterBean, allProductTagList, configurationService, productTypeTags);

		Collections.sort(productList);
		filterBean.setProductList(productList);
		filterBean.setFilters(filters);
		return filterBean;
	}

	/**
	 * Gets the pdp resources.
	 *
	 * @param request
	 *            the request
	 * @param resourceResolver
	 *            the resource resolver
	 * @param productListTags
	 *            the product list tags
	 * @param configurationService
	 *            the configuration service
	 * @return the pdp resources
	 */
	private static Iterator<Resource> getPdpResources(SlingHttpServletRequest request,
			ResourceResolver resourceResolver, String[] productListTags,
			BaseConfigurationService configurationService) {
		LOGGER.info("productListTags : {}", Arrays.asList(productListTags));

		SearchInput searchInput = new SearchInput();

		searchInput.setRootPath(configurationService.getPropConfigValue(request, "productContentRootPath",
				CortevaConstant.PRODUCT_CONFIG_NAME));
		searchInput.setType(configurationService.getPropConfigValue(request, "pdpResourceType",
				CortevaConstant.PRODUCT_CONFIG_NAME));
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		List<String> allTags = new ArrayList<>();
		if (null != tagManager) {
			for (String tagId : productListTags) {
				Tag tag = tagManager.resolve(tagId);
				populateChildTags(allTags, tag);
			}
		}
		searchInput.setTagList(allTags);
		long offset = 0;
		String[] selectors = request.getRequestPathInfo().getSelectors();
		String extension = request.getRequestPathInfo().getExtension();
		if (StringUtils.equalsIgnoreCase(CortevaConstant.HTML_EXTENSION, extension) && selectors.length == 1) {
			try {
				offset = Long.parseLong(selectors[0]);
			} catch (NumberFormatException e) {
				LOGGER.error("NumberFormatException occurred in getPdpResources()", e);
			}
		}
		searchInput.setOffset(offset * 10);
		return SQLQueryUtil.getDocuments(resourceResolver, searchInput);
	}

	/**
	 * Process pdp pages.
	 *
	 * @param request
	 *            the request
	 * @param resourceResolver
	 *            the resource resolver
	 * @param productList
	 *            the product list
	 * @param documents
	 *            the documents
	 * @param filterBean
	 *            the filter bean
	 * @param productTypeTags
	 *            the product type tag list
	 * @return the list
	 */
	private static List<String> processPdpPages(SlingHttpServletRequest request, ResourceResolver resourceResolver,
			List<ProductFilterChildBean> productList, Iterator<Resource> documents, ProductFilterBean filterBean,
			List<String> productTypeTags) {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Session session = resourceResolver.adaptTo(Session.class);
		List<String> allProductTagList = new ArrayList<>();
		Set<String> productTypeTagSet = new HashSet<>();
		if (null == session || null == pageManager) {
			return allProductTagList;
		}
		while (documents.hasNext()) {
			Resource resource = documents.next();
			ProductFilterChildBean filterChildBean = new ProductFilterChildBean();
			Page page = pageManager.getContainingPage(resource);
			if (null != page) {
				ValueMap valueMap = page.getContentResource().getValueMap();
				LOGGER.info("Processing information for PDP page : {}", page.getPath());
				try {
					boolean continueFlag = false;
					List<String> stateList = new ArrayList<>();
					continueFlag = createProductNode(session, filterChildBean, page, valueMap, stateList);
					if (continueFlag) {
						continue;
					}
					String ignoreTagRoot = null;
					if (stateList.size() > 0) {
						ignoreTagRoot = CortevaConstant.STATE_TAG_ROOT;
					}
					filterChildBean.setProductPath(resourceResolver.map(request,
							page.getPath() + CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION));
					filterChildBean.setDescription((String) valueMap.get("jcr:description"));
					List<String> taggedList = new ArrayList<>();
					Map<String, String> productTypeTagList = getTagList(resourceResolver.adaptTo(TagManager.class),
							taggedList, valueMap, request, ignoreTagRoot);
					taggedList.addAll(stateList);
					productTypeTagSet.addAll(productTypeTagList.keySet());
					Collection<String> productTypeTagValues = productTypeTagList.values();
					productTypeTagValues.removeIf(tagName -> (StringUtils.isBlank(tagName)));
					filterChildBean.setProductTypeTags(new ArrayList<>(productTypeTagValues));
					filterChildBean.setAllTags(taggedList);
					productList.add(filterChildBean);
					allProductTagList.addAll(taggedList);
				} catch (RepositoryException e) {
					LOGGER.error("RepositoryException occurred in processPdpPages()", e);
				}
			}
		}
		productTypeTags.addAll(productTypeTagSet);
		filterBean.setProductList(productList);
		return allProductTagList;
	}

	/**
	 * Creates the product node.
	 *
	 * @param session
	 *            the session
	 * @param filterChildBean
	 *            the filter child bean
	 * @param page
	 *            the page
	 * @param valueMap
	 *            the value map
	 * @param stateList
	 *            the state list
	 * @return true, if successful
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private static boolean createProductNode(Session session, ProductFilterChildBean filterChildBean, Page page,
			ValueMap valueMap, List<String> stateList) throws RepositoryException {

		if (!valueMap.containsKey(CortevaConstant.PDP_SOURCE)) {
			return true;
		}
		if (((String) valueMap.get(CortevaConstant.PDP_SOURCE)).equalsIgnoreCase(CortevaConstant.AGRIAN)) {

			String productId = (String) valueMap.get(CortevaConstant.PRODUCT_ID);
			if (StringUtils.isEmpty(productId)) {
				LOGGER.info("Product ID is blank for: {}", LinkUtil.getHref(page.getPath()));
				return true;
			}
			Node node = session.getNode(CortevaConstant.PRODUCT_NODE_PATH + CortevaConstant.FORWARD_SLASH + productId);
			if (null == node) {
				LOGGER.info("Product Data doesn't exist for : {}", LinkUtil.getHref(page.getPath()));
				return true;
			}
			if (node.hasProperty(CortevaConstant.PRODUCT_LICENSED_STATES_PROPERTY)) {
				Property property = node.getProperty(CortevaConstant.PRODUCT_LICENSED_STATES_PROPERTY);
				populateListFormattedTags(property, stateList, null);
			}

			if (null == valueMap.get(CortevaConstant.PRODUCT_NAME_PROPERTY)) {
				LOGGER.info("Product Name is blank for Agrian product: {}", LinkUtil.getHref(page.getPath()));

				filterChildBean.setProductName(node.getProperty(CortevaConstant.PRODUCT_NAME_PROPERTY).getString());
				filterChildBean.setProductAnalyticsName(filterChildBean.getProductName());
			} else {
				filterChildBean.setProductName((String) valueMap.get(CortevaConstant.PRODUCT_NAME_PROPERTY));
				filterChildBean
						.setProductAnalyticsName(filterChildBean.getProductName().replaceAll(REGEX, StringUtils.EMPTY));
			}
		} else if (((String) valueMap.get(CortevaConstant.PDP_SOURCE)).equalsIgnoreCase(CortevaConstant.NON_AGRIAN)) {
			LOGGER.debug("pdpSrc is not agrian");
			if (null == valueMap.get(CortevaConstant.PRODUCT_NAME_PROPERTY)) {
				LOGGER.info("Product Name is blank for Non Agrian product: {}", LinkUtil.getHref(page.getPath()));
				return true;
			}
			filterChildBean.setProductName((String) valueMap.get(CortevaConstant.PRODUCT_NAME_PROPERTY));
			filterChildBean
					.setProductAnalyticsName(filterChildBean.getProductName().replaceAll(REGEX, StringUtils.EMPTY));
		}
		return false;
	}

	/**
	 * Gets the tag list.
	 * 
	 * @param tagManager
	 *            the tag manager
	 * @param taggedList
	 *            the tagged list
	 * @param valueMap
	 *            the value map
	 * @param request
	 *            the request
	 * @param ignoreTagRoot 
	 * 			  the tags to ignore
	 * @return the tag list
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private static Map<String, String> getTagList(TagManager tagManager, List<String> taggedList, ValueMap valueMap,
			SlingHttpServletRequest request, String ignoreTagRoot) throws RepositoryException {
		Map<String, String> productTypeTagList = new HashMap<>();
		Property property = valueMap.get(CortevaConstant.CQ_TAGS, Property.class);
		populateListFormattedTags(property, taggedList, ignoreTagRoot);
		property = valueMap.get("cq:productType", Property.class);
		if (null != property) {
			createTagList(tagManager, taggedList, productTypeTagList, property, request);
		}
		return productTypeTagList;
	}

	/**
	 * Populates list with formatted tagId.
	 *
	 * @param property
	 *            the property to be fetched
	 * @param list
	 *            the list to be populated
	 * @param ignoreTagRoot 
	 * 			  the tags to ignore
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private static void populateListFormattedTags(Property property, List<String> list, String ignoreTagRoot) throws RepositoryException {
		if (null != property) {
			if (property.isMultiple()) {
				Value[] values = property.getValues();
				for (Value v : values) {
					if (!StringUtils.startsWithIgnoreCase(v.getString(), ignoreTagRoot)) {
						list.add(v.getString().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
								.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY));
					}
				}
			} else {
				if (!StringUtils.startsWithIgnoreCase(property.getString(), ignoreTagRoot)) {
					list.add(property.getString().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
						.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY));
				}
			}
		}
	}

	/**
	 * Populates all tag lists from PDP.
	 * 
	 * @param productTag
	 *            the product type tag
	 * @param taggedList
	 *            the tagged list
	 * @param productTypeTagList
	 *            the product type tag list
	 * @param request
	 *            the request
	 */
	private static void populateProductAllTagList(Tag productTag, List<String> taggedList,
			Map<String, String> productTypeTagList, SlingHttpServletRequest request) {
		Tag parentTag = productTag.getParent();
		Tag parentRootTag = parentTag.getParent();

		productTypeTagList.put(
				productTag.getTagID().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
						.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY),
				CommonUtils.getTagLocalizedTitle(request, productTag));
		taggedList.add(productTag.getTagID().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
				.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY));

		while (null != parentRootTag
				&& !StringUtils.equalsIgnoreCase(parentRootTag.getName(), CortevaConstant.CORTEVA)) {
			taggedList.add(parentTag.getTagID().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
					.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY));
			productTypeTagList.put(parentTag.getTagID().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
					.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY), StringUtils.EMPTY);

			productTag = parentTag;
			parentTag = productTag.getParent();
			parentRootTag = parentTag.getParent();
		}
	}

	/**
	 * Creates the tag list.
	 *
	 * @param tagManager
	 *            the tag manager
	 * @param taggedList
	 *            the tagged list
	 * @param productTypeTagList
	 *            the product type tag list
	 * @param property
	 *            the property
	 * @param request
	 *            the request
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private static void createTagList(TagManager tagManager, List<String> taggedList,
			Map<String, String> productTypeTagList, Property property, SlingHttpServletRequest request)
			throws RepositoryException {
		Tag resolvedTag = null;
		if (null != tagManager) {
			if (property.isMultiple()) {
				for (Value value : property.getValues()) {
					resolvedTag = tagManager.resolve(value.getString());
					populateProductAllTagList(resolvedTag, taggedList, productTypeTagList, request);
				}
			} else {
				resolvedTag = tagManager.resolve(property.getString());
				populateProductAllTagList(resolvedTag, taggedList, productTypeTagList, request);
			}
		}
	}

	/**
	 * Gets the filters.
	 *
	 * @param request
	 *            the request
	 * @param resourceResolver
	 *            the resource resolver
	 * @param valueMapCurrentResource
	 *            the value map current resource
	 * @param filterBean
	 *            the filter bean
	 * @param allProductTagList
	 *            the all product tag list
	 * @param configurationService
	 *            the configuration service
	 * @param productTypeTags
	 *            the product type tag list
	 * @return the filters
	 */
	private static Map<String, ProductFilterListBean> getFilters(SlingHttpServletRequest request,
			ResourceResolver resourceResolver, ValueMap valueMapCurrentResource, ProductFilterBean filterBean,
			List<String> allProductTagList, BaseConfigurationService configurationService,
			List<String> productTypeTags) {
		Map<String, ProductFilterListBean> filters = new LinkedHashMap<>();
		List<String> aggregatedProductTypeList = new ArrayList<>();
			if ("hideFilters".equals((String) valueMapCurrentResource.get("variationType"))) {
				filterBean.setHideFilters(true);
			} else {
				filterBean.setHideFilters(false);
				populateProductTypeFilter(request, valueMapCurrentResource, resourceResolver, filters,
						configurationService, productTypeTags, aggregatedProductTypeList);
				populateProductSubTypeFilter(request, valueMapCurrentResource, resourceResolver, allProductTagList,
						filters, configurationService, productTypeTags, aggregatedProductTypeList);
				filters.put(CROP_FILTER_PROP,
						populateFilterTagList(CROP_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, "cropFilterTagPath",
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(STATE_FILTER_PROP,
						populateFilterTagList(STATE_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, "stateFilterTagPath",
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(PESTS_FILTER_PROP,
						populateFilterTagList(PESTS_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, PESTS_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(WEEDS_FILTER_PROP,
						populateFilterTagList(WEEDS_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, WEEDS_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(DISEASE_FILTER_PROP,
						populateFilterTagList(DISEASE_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, DISEASE_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(USESITE_FILTER_PROP,
						populateFilterTagList(USESITE_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, "useSiteFilterTagPath",
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filters.put(MARKET_FILTER_PROP,
						populateFilterTagList(MARKET_FILTER_PROP, valueMapCurrentResource,
								configurationService.getPropConfigValue(request, "marketFilterTagPath",
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				if (Boolean.valueOf((String) valueMapCurrentResource.get("productNameFilter"))) {
					filterBean.setShowProductNameFilter(true);
				}

			}
		return filters;
	}

	/**
	 * Gets the product type filter.
	 *
	 * @param request
	 *            the request
	 * @param valueMap
	 *            the value map
	 * @param resourceResolver
	 *            the resource resolver
	 * @param filters
	 *            the filters
	 * @param configurationService
	 *            the configuration service
	 * @param productTypeTags
	 *            the product type tag list
	 * @param aggregatedProductTypeList
	 *            the list of all Product Type Filter values
	 */
	private static void populateProductTypeFilter(SlingHttpServletRequest request, final ValueMap valueMap,
			final ResourceResolver resourceResolver, final Map<String, ProductFilterListBean> filters,
			BaseConfigurationService configurationService, List<String> productTypeTags,
			List<String> aggregatedProductTypeList) {

		String productTypeFilterTagPath = configurationService.getPropConfigValue(request, "productTypeFilterTagPath",
				CortevaConstant.PRODUCT_CONFIG_NAME);
		Map<String, String> filterTagList = getFilterTagList(new String[] { productTypeFilterTagPath },
				resourceResolver, productTypeTags, aggregatedProductTypeList, request);

		if (Boolean.valueOf((String) valueMap.get(PRODUCT_TYPE_FILTER_PROP))) {

			ProductFilterListBean filterListBean = new ProductFilterListBean();
			filterListBean.setFilterMap(filterTagList);
			if (Boolean.valueOf((String) valueMap.get("productTypeSequentialFilter"))) {
				filterListBean.setHasSequantialFilters(true);
				Map<String, ProductFilterListBean> filtersSeq = new LinkedHashMap<>();
				for (String productTag : aggregatedProductTypeList) {
					Map<String, String> filterTagListSeq = getFilterTagList(new String[] { productTag },
							resourceResolver, productTypeTags, null, request);
					ProductFilterListBean filterListBeanSeq = new ProductFilterListBean();
					filterListBeanSeq.setFilterMap(filterTagListSeq);
					filterListBeanSeq.setDataAnalyticsType(DA_PRODUCT_SUB_TYPE_FILTER);
					filtersSeq.put(productTag.replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
							.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY), filterListBeanSeq);
				}
				filterListBean.setChildFilters(filtersSeq);
			}
			filterListBean.setDataAnalyticsType("product-type-filter");
			filters.put(PRODUCT_TYPE_FILTER_PROP, filterListBean);
		}
	}

	/**
	 * Gets the product sub type filter.
	 *
	 * @param request
	 *            the request
	 * @param valueMap
	 *            the value map
	 * @param resourceResolver
	 *            the resource resolver
	 * @param allProductTagList
	 *            the all product tag list
	 * @param filters
	 *            the filters
	 * @param configurationService
	 *            the configuration service
	 * @param productTypeTags
	 *            the product type tag list
	 * @param aggregatedProductTypeList
	 *            the list of all Product Type Filter values
	 */
	private static void populateProductSubTypeFilter(SlingHttpServletRequest request, final ValueMap valueMap,
			final ResourceResolver resourceResolver, final List<String> allProductTagList,
			final Map<String, ProductFilterListBean> filters, BaseConfigurationService configurationService,
			List<String> productTypeTags, List<String> aggregatedProductTypeList) {
		if (Boolean.valueOf((String) valueMap.get(PRODUCT_TYPE_SUB_PROP))) {
			String[] subTypeParentPaths = aggregatedProductTypeList
					.toArray(new String[aggregatedProductTypeList.size()]);
			Map<String, String> filterTagList = getFilterTagList(subTypeParentPaths, resourceResolver, productTypeTags,
					null, request);

			ProductFilterListBean filterListBean = new ProductFilterListBean();
			filterListBean.setFilterMap(filterTagList);
			if (Boolean.valueOf((String) valueMap.get("productTypeSubSequentialFilter"))) {
				filterListBean.setHasSequantialFilters(true);
				Map<String, ProductFilterListBean> filtersSeq = new LinkedHashMap<>();
				filtersSeq.put("pests",
						populateProductSubTypeSeqFilters(
								configurationService.getPropConfigValue(request, PESTS_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filtersSeq.put("weeds",
						populateProductSubTypeSeqFilters(
								configurationService.getPropConfigValue(request, WEEDS_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filtersSeq.put("disease",
						populateProductSubTypeSeqFilters(
								configurationService.getPropConfigValue(request, DISEASE_FILTER_TAG_PATH,
										CortevaConstant.PRODUCT_CONFIG_NAME),
								resourceResolver, allProductTagList, request));
				filterListBean.setChildFilters(filtersSeq);
			}
			filterListBean.setDataAnalyticsType(DA_PRODUCT_SUB_TYPE_FILTER);
			filters.put(PRODUCT_TYPE_SUB_PROP, filterListBean);
		}

	}

	/**
	 * Populate product sub type seq filters.
	 * 
	 * @param tagPath
	 *            the tag path
	 * @param resourceResolver
	 *            the resource resolver
	 * @param allProductTagList
	 *            the all product tag list
	 * @param request
	 *            the request
	 * @return the product filter list bean
	 */
	private static ProductFilterListBean populateProductSubTypeSeqFilters(final String tagPath,
			final ResourceResolver resourceResolver, final List<String> allProductTagList,
			final SlingHttpServletRequest request) {
		Map<String, String> filterTagListSeq = getFilterTagList(new String[] { tagPath }, resourceResolver,
				allProductTagList, null, request);
		ProductFilterListBean filterListBeanSeq = new ProductFilterListBean();
		filterListBeanSeq.setFilterMap(filterTagListSeq);
		filterListBeanSeq.setDataAnalyticsType("pests-weeds-diseases-filter");
		return filterListBeanSeq;
	}

	/**
	 * Gets the filter tag list.
	 * 
	 * @param filterName
	 *            the filter name
	 * @param valueMap
	 *            the value map
	 * @param tagPath
	 *            the tag path
	 * @param resourceResolver
	 *            the resource resolver
	 * @param allProductTagList
	 *            the all product tag list
	 * @param request
	 *            the request
	 * @return the product filter list bean
	 */
	private static ProductFilterListBean populateFilterTagList(final String filterName, final ValueMap valueMap,
			final String tagPath, final ResourceResolver resourceResolver, final List<String> allProductTagList,
			final SlingHttpServletRequest request) {
		ProductFilterListBean filterListBean = new ProductFilterListBean();
		if (Boolean.valueOf((String) valueMap.get(filterName))) {
			Map<String, String> filterTagList = getFilterTagList(new String[] { tagPath }, resourceResolver,
					allProductTagList, null, request);
			filterListBean.setFilterMap(filterTagList);
		}

		filterListBean.setDataAnalyticsType(getDataAnalyticsType(filterName));
		return filterListBean;
	}

	/**
	 * Gets the data analytics type.
	 * 
	 * @param filterName
	 *            the filter name
	 * @return the data analytics type
	 */
	private static String getDataAnalyticsType(String filterName) {
		String dataAnalyticsType = null;
		switch (filterName) {
		case CROP_FILTER_PROP:
			dataAnalyticsType = "crop-filter";
			break;
		case STATE_FILTER_PROP:
			dataAnalyticsType = "state-filter";
			break;
		case PESTS_FILTER_PROP:
			dataAnalyticsType = "pests-filter";
			break;
		case WEEDS_FILTER_PROP:
			dataAnalyticsType = "weeds-filter";
			break;
		case DISEASE_FILTER_PROP:
			dataAnalyticsType = "disease-filter";
			break;
		case USESITE_FILTER_PROP:
			dataAnalyticsType = "use-site-filter";
			break;
		case MARKET_FILTER_PROP:
			dataAnalyticsType = "market-filter";
			break;
		default:
			break;
		}
		return dataAnalyticsType;

	}

	/**
	 * populate the filter map
	 * 
	 * @param filterMap
	 *            the filter map
	 * @param tagList
	 *            the list of tags
	 * @param allProductTagList
	 *            the list of all product tags
	 * @param aggregatedProductTypeList
	 *            the list of all Product Type Filter values
	 * @param request
	 *            the request
	 */
	private static void populateFilterMap(Map<String, String> filterMap, List<Tag> tagList,
			List<String> allProductTagList, List<String> aggregatedProductTypeList, SlingHttpServletRequest request) {
		for (Tag tag : tagList) {
			String tagId = tag.getTagID().replaceAll(CortevaConstant.COLON, StringUtils.EMPTY)
					.replaceAll(CortevaConstant.FORWARD_SLASH, StringUtils.EMPTY);
			if (allProductTagList.contains(tagId)) {
				filterMap.put(tagId, CommonUtils.getTagLocalizedTitle(request, tag));
				if (null != aggregatedProductTypeList) {
					aggregatedProductTypeList.add(tag.getTagID());
				}
			}
		}
	}

	/**
	 * 
	 * Gets the filter tag list.
	 * 
	 * @param tagPaths
	 *            the tag paths
	 * @param resourceResolver
	 *            the resource resolver
	 * @param allProductTagList
	 *            the all product tag list
	 * @param aggregatedProductTypeList
	 *            the list of all Product Type Filter values
	 * @param request
	 *            the request
	 * @return the filter tag list
	 */
	private static Map<String, String> getFilterTagList(final String[] tagPaths,
			final ResourceResolver resourceResolver, final List<String> allProductTagList,
			List<String> aggregatedProductTypeList, SlingHttpServletRequest request) {

		Map<String, String> filterMap = new HashMap<>();
		TagManager tagManager = null;
		if (null != resourceResolver) {
			tagManager = resourceResolver.adaptTo(TagManager.class);
		}
		if (null != tagManager && null != tagPaths) {
			for (String tagPath : tagPaths) {
				Tag rootTag = tagManager.resolve(tagPath);
				List<Tag> listChildren = TagUtil.listChildren(rootTag);
				if (StringUtils.equalsIgnoreCase(rootTag.getName(), CortevaConstant.CORTEVA)) {
					for (Tag levelOneTag : listChildren) {
						List<Tag> listLevelTwo = TagUtil.listChildren(levelOneTag);
						populateFilterMap(filterMap, listLevelTwo, allProductTagList, aggregatedProductTypeList,
								request);
					}
				} else {
					populateFilterMap(filterMap, listChildren, allProductTagList, null, request);
				}
			}
		}
		return sortByValue(filterMap);
	}

	/**
	 * Gets the all tags.
	 * 
	 * @param allTags
	 *            the all tags
	 * @param tag
	 *            the tag
	 */
	private static void populateChildTags(List<String> allTags, Tag tag) {
		if (null != tag) {
			allTags.add(tag.getTagID());
			List<Tag> listChildren = TagUtil.listChildren(tag);
			for (Tag chilTag : listChildren) {
				populateChildTags(allTags, chilTag);
			}
		}
	}

	/**
	 * Sort by value.
	 * 
	 * @param unsortMap
	 *            the unsort map
	 * @return the map
	 */
	private static Map<String, String> sortByValue(Map<String, String> unsortMap) {

		List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Map.Entry<String, String> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}
