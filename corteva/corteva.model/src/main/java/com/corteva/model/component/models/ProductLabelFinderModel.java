/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.model.component.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ProductDetailBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * The is the sling model for the Product Label Finder - US and Non US.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductLabelFinderModel extends ProductSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLabelFinderModel.class);

	/** The request. */
	@Inject
	private SlingHttpServletRequest request;

	/** The region country lang map. */
	private Map<String, String> regionCountryLangMap;

	/** The Constant STATE_KEY. */
	private static final String STATE_KEY = "pdp.productLabelFinder.stateKey";

	/** The Constant SPECIMEN_LABEL_KEY. */
	private static final String SPECIMEN_LABEL_KEY = "pdp.productLabelFinder.productLabelKey";

	/** The Constant SAFETY_LABEL_KEY. */
	private static final String SAFETY_LABEL_KEY = "pdp.productLabelFinder.safetyLabelKey";

	/** The Constant SEARCH_TEXT_KEY. */
	private static final String SEARCH_TEXT_KEY = "pdp.productLabelFinder.noResultsText";

	/** The Constant SUGGESTION_TEXT_KEY. */
	private static final String SUGGESTION_TEXT_KEY = "pdp.productLabelFinder.suggestionText";

	/** The base service. */
	@Inject
	private BaseConfigurationService baseService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		regionCountryLangMap = CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePath(request),
				getResourceResolver());

	}

	/**
	 * Gets the product data.
	 *
	 * @return the product data
	 */
	public String getProductData() {
		LOGGER.debug("Entering getProductData() method");
		String productData = StringUtils.EMPTY;
		boolean containsKey = regionCountryLangMap.containsKey(CortevaConstant.COUNTRY)
				&& regionCountryLangMap.containsKey(CortevaConstant.LANGUAGE);
		try {
			if (containsKey) {
				String country = regionCountryLangMap.get(CortevaConstant.COUNTRY);
				String language = regionCountryLangMap.get(CortevaConstant.LANGUAGE);
				boolean isPageFromUS = StringUtils.equalsIgnoreCase(country, CortevaConstant.US)
						&& StringUtils.equalsIgnoreCase(language, CortevaConstant.EN);
				List<ProductDetailBean> allProductsFromAem = getAllProductsFromAem();
				ObjectMapper objectMapper = new ObjectMapper();
				if (isPageFromUS) {
					productData = objectMapper.writeValueAsString(getProductListForUSProducts(allProductsFromAem));
					LOGGER.debug("Product Name and Product ID JSON for US Products :: {}", productData);
				} else {
					productData = objectMapper
							.writeValueAsString(getProductListForNonAgrianProducts(allProductsFromAem));
					LOGGER.debug("Product Name and Product ID JSON for Non US Products :: {}", productData);
				}
			}
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occurred in getProductData()", e);
		}
		LOGGER.debug("Exiting getProductData() method");
		return productData;
	}

	/**
	 * Gets the product list for US products.
	 *
	 * @param allProductsFromAem
	 *            the all products from aem
	 * @return the product list for US products
	 */
	private List<ProductDetailBean> getProductListForUSProducts(List<ProductDetailBean> allProductsFromAem) {
		LOGGER.debug("Inside getProductListForUSProducts() method");
		List<ProductDetailBean> agrianProductList = getAllProductListFromAgrian();
		List<String> hiddenIds = getHiddenAgrianProductIds(allProductsFromAem);
		for (ProductDetailBean bean : agrianProductList) {
			if (hiddenIds.contains(bean.getProductId())) {
				agrianProductList.remove(bean);
			}
		}
		List<ProductDetailBean> productList = Lists.newCopyOnWriteArrayList(
				Iterables.concat(agrianProductList, getProductListForNonAgrianProducts(allProductsFromAem)));
		Collections.sort(productList);
		LOGGER.debug("Product List for US Products :: {}", productList);
		return productList;
	}

	/**
	 * Gets the all product list from agrian.
	 *
	 * @return the all product list from agrian
	 */
	private List<ProductDetailBean> getAllProductListFromAgrian() {
		LOGGER.debug("Inside getAllProductListFromAgrian() method");
		List<ProductDetailBean> productList = new CopyOnWriteArrayList<>();
		try {
			String productNodeRootPath = baseService.getPropConfigValue(request,
					CortevaConstant.PRODUCT_NODE_PATH_PROPERTY, CortevaConstant.PRODUCT_CONFIG_NAME);
			if (StringUtils.isNotBlank(productNodeRootPath)) {
				Resource agrianRootResource = getResourceResolver().resolve(productNodeRootPath);
				if (!ResourceUtil.isNonExistingResource(agrianRootResource)) {
					productList = createProductListForAgrianProducts(agrianRootResource);
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error("Repository Exception occurred in getProductList() method", e);
		}
		LOGGER.debug("All Product List for Agrian Products :: {}", productList);
		return productList;
	}

	/**
	 * Creates the product list for agrian products.
	 *
	 * @param agrianRootResource
	 *            the agrian root resource
	 * @return the list
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private List<ProductDetailBean> createProductListForAgrianProducts(Resource agrianRootResource)
			throws RepositoryException {
		LOGGER.debug("Inside createProductListForAgrianProducts() method");
		List<ProductDetailBean> productList = new CopyOnWriteArrayList<>();
		Node agrianRootNode = agrianRootResource.adaptTo(Node.class);
		Iterable<Node> children = JcrUtils.getChildNodes(agrianRootNode);
		if (null != children) {
			ProductDetailBean productBean;
			for (Node childNode : children) {
				productBean = new ProductDetailBean();
				if (childNode.hasProperty(CortevaConstant.PRODUCT_ID)) {
					productBean.setProductId(childNode.getProperty(CortevaConstant.PRODUCT_ID).getString());
				}
				if (childNode.hasProperty(CortevaConstant.PRODUCT_NAME)) {
					productBean.setProductName(childNode.getProperty(CortevaConstant.PRODUCT_NAME).getString());
				}
				productBean.setProductSource(CortevaConstant.AGRIAN);
				productList.add(productBean);
			}
		}
		LOGGER.debug("Product List for Agrian Products :: {}", productList);
		return productList;
	}

	/**
	 * Gets the all pdp resources.
	 *
	 * @return the all pdp resources
	 */
	private Iterator<Resource> getAllPdpResources() {
		LOGGER.debug("Entering getAllPdpResources() method");
		String pdpRootPath = baseService.getPropConfigValue(request, CortevaConstant.PDP_ROOT_PATH,
				CortevaConstant.PRODUCT_CONFIG_NAME);
		pdpRootPath = CommonUtils.getPdpRootPath(regionCountryLangMap, pdpRootPath);
		LOGGER.debug("Exiting getAllPdpResources() method");
		return CommonUtils.findAllPdpResources(getResourceResolver(), pdpRootPath);
	}

	/**
	 * Gets the all products from aem.
	 *
	 * @return the all products from aem
	 */
	private List<ProductDetailBean> getAllProductsFromAem() {
		LOGGER.debug("Inside getAllProductsFromAem() method");
		List<ProductDetailBean> productList = new CopyOnWriteArrayList<>();
		Iterator<Resource> pdpPageResources = getAllPdpResources();
		if (null != pdpPageResources) {
			ProductDetailBean productBean;
			while (pdpPageResources.hasNext()) {
				Resource res = pdpPageResources.next();
				ValueMap vMap = res.getValueMap();
				if (!vMap.isEmpty()) {
					/**
					 * Assuming product Id will not be authored for Non Agrian products.
					 */
					productBean = createProductBean(res, vMap);
					productList.add(productBean);
				}
			}
		}
		LOGGER.debug("Product List for products in AEM :: {}", productList);
		return productList;
	}

	/**
	 * Creates the product bean.
	 *
	 * @param res
	 *            the res
	 * @param vMap
	 *            the v map
	 * @return the product detail bean
	 */
	private ProductDetailBean createProductBean(Resource res, ValueMap vMap) {
		LOGGER.debug("Inside createProductBean() method");
		ProductDetailBean productBean = new ProductDetailBean();
		productBean.setProductId(
				vMap.containsKey(CortevaConstant.PRODUCT_ID) ? vMap.get(CortevaConstant.PRODUCT_ID, String.class)
						: CommonUtils.getPageFromResource(getResourceResolver(), res).getName());
		productBean.setProductName(
				vMap.containsKey(CortevaConstant.PRODUCT_NAME) ? vMap.get(CortevaConstant.PRODUCT_NAME, String.class)
						: null);
		productBean.setProductSource(
				vMap.containsKey(CortevaConstant.PDP_SOURCE) ? vMap.get(CortevaConstant.PDP_SOURCE, String.class)
						: null);
		productBean.setHideProduct(
				vMap.containsKey(CortevaConstant.HIDE_PRODUCT) ? vMap.get(CortevaConstant.HIDE_PRODUCT, String.class)
						: CortevaConstant.FALSE);
		LOGGER.debug("Product Detail Bean :: {}", productBean);
		return productBean;
	}

	/**
	 * Gets the hidden agrian product ids.
	 *
	 * @param allProductsFromAem
	 *            the all products from aem
	 * @return the hidden agrian product ids
	 */
	private List<String> getHiddenAgrianProductIds(List<ProductDetailBean> allProductsFromAem) {
		LOGGER.debug("Inside getHiddenAgrianProductIds() method");
		List<String> hiddenAgrianProductIds = new CopyOnWriteArrayList<>();
		for (ProductDetailBean bean : allProductsFromAem) {
			boolean isProductHidden = Boolean.parseBoolean(bean.getHideProduct());
			boolean isProductAgrian = StringUtils.equalsIgnoreCase(CortevaConstant.AGRIAN, bean.getProductSource());
			if (isProductHidden && isProductAgrian) {
				hiddenAgrianProductIds.add(bean.getProductId());
			}
		}
		LOGGER.debug("Product Id List for Agrian Products from AEM that are hidden :: {}", hiddenAgrianProductIds);
		return hiddenAgrianProductIds;
	}

	/**
	 * Gets the product list for non agrian products.
	 *
	 * @param allProductsFromAem
	 *            the all products from aem
	 * @return the product list for non agrian products
	 */
	private List<ProductDetailBean> getProductListForNonAgrianProducts(List<ProductDetailBean> allProductsFromAem) {
		LOGGER.debug("Inside getProductListForNonAgrianProducts() method");
		List<ProductDetailBean> productList = new CopyOnWriteArrayList<>();
		ProductDetailBean productBean;
		for (ProductDetailBean bean : allProductsFromAem) {
			boolean isProductHidden = Boolean.parseBoolean(bean.getHideProduct());
			boolean isNonAgrianProduct = StringUtils.equalsIgnoreCase(CortevaConstant.NON_AGRIAN,
					bean.getProductSource());
			if (!isProductHidden && isNonAgrianProduct) {
				productBean = new ProductDetailBean();
				productBean.setProductId(bean.getProductId());
				productBean.setProductName(bean.getProductName());
				productBean.setProductSource(bean.getProductSource());
				productList.add(productBean);
			}
		}
		Collections.sort(productList);
		LOGGER.debug("Product List for Non Agrian Products :: {}", productList);
		return productList;
	}

	/**
	 * Default value state map.
	 *
	 * @return the map
	 */
	public Map<String, String> getDefaultValueStateMap() {
		LOGGER.debug("Inside getStateMap() method");
		Map<String, String> stateMap = new HashMap<>();
		String stateValue = CommonUtils.getI18nValue(request, getResourceResolver(), STATE_KEY);
		stateMap.put("defaultValue", stateValue);
		LOGGER.debug("Default Value State Map for Product Label Finder :: {}", stateMap);
		return stateMap;
	}

	/**
	 * Gets the state map.
	 *
	 * @return the state map
	 */
	public Map<String, String> getStateMap() {
		return getStateMap(regionCountryLangMap, request);
	}

	/**
	 * Gets the locale for internationalization.
	 *
	 * @return the i18nLocale
	 */
	public String getLocaleForInternationalization() {
		return getLocaleForInternationalization(request);
	}

	/**
	 * Gets the maximum Suggestion.
	 *
	 * @return the maximum suggestion
	 */
	public String getMaximumSuggestion() {
		return baseService.getPropConfigValue(request, CortevaConstant.MAX_SUGGESTION_LENGTH,
				CortevaConstant.PRODUCT_CONFIG_NAME);
	}

	/**
	 * Gets the minimum characters.
	 *
	 * @return the minimum characters
	 */
	public String getMinimumCharacter() {
		return baseService.getPropConfigValue(request, CortevaConstant.MIN_CHAR_LENGTH,
				CortevaConstant.PRODUCT_CONFIG_NAME);
	}

	/**
	 * Gets the product document base path.
	 *
	 * @return the product document base path
	 */
	public String getProductDocumentBasePath() {
		LOGGER.debug("Inside getProductDocumentBasePath() method");
		return baseService.getPropConfigValue(request, CortevaConstant.PDF_DOCUMENT_ROOT_PATH,
				CortevaConstant.PRODUCT_CONFIG_NAME);
	}

	/**
	 * Gets the specimen label I 18 n value.
	 *
	 * @return the specimen label I 18 n value
	 */
	public String getSpecimenLabelI18nValue() {
		return CommonUtils.getI18nValue(request, getResourceResolver(), SPECIMEN_LABEL_KEY);
	}

	/**
	 * Gets the safety label I 18 n value.
	 *
	 * @return the safety label I 18 n value
	 */
	public String getSafetyLabelI18nValue() {
		return CommonUtils.getI18nValue(request, getResourceResolver(), SAFETY_LABEL_KEY);
	}

	/**
	 * Gets the search text I 18 n value.
	 *
	 * @return the search text I 18 n value
	 */
	public String getSearchTextI18nValue() {
		return CommonUtils.getI18nValue(request, getResourceResolver(), SEARCH_TEXT_KEY);
	}

	/**
	 * Gets the suggestion text I 18 n value.
	 *
	 * @return the suggestion text I 18 n value
	 */
	public String getSuggestionTextI18nValue() {
		return CommonUtils.getI18nValue(request, getResourceResolver(), SUGGESTION_TEXT_KEY);
	}

	/**
	 * Gets the product details servlet path.
	 *
	 * @return the product details servlet path
	 */
	public String getProductDetailsServletPath() {
		return CortevaConstant.PDP_SERVLET_URL;
	}
}