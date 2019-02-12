package com.corteva.model.component.models;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.AEMUtils;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * The is the sling model for the Page Meta Model.
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class PageMetaModel extends ProductSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PageMetaModel.class);

	/**
	 * The Constant PDP template type.
	 */
	private static final String PDP_TEMPLATE = "/conf/corteva/settings/wcm/template-types/pdp";

	/** The Constant PDP template. */
	private static final String PRODUCT_AND_SOLUTION_PAGE_NAME = "products-and-solutions";

	/** The Constant CANONICAL_URL. */
	private static final String CANONICAL_URL = "canonicalUrl";

	/** The current resource. */
	@SlingObject
	private Resource currentResource;

	/** The slingSettingsService. */
	@OSGiService
	private SlingSettingsService slingSettingsService;

	/** The sling request. */
	@Inject
	private SlingHttpServletRequest request;

	/** The sling request. */
	@Inject
	private transient BaseConfigurationService configurationService;

	/**
	 * Gets the siteMapContent.
	 *
	 * @return siteMapContent
	 */
	public String getSiteMapContent() {
		LOGGER.debug("Inside method getSiteMapContent() in PageMetaModel");
		String crawlStr = CortevaConstant.CRAWL_STRING;
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		String siteMapValue = currentPage.getProperties().get(CortevaConstant.SITE_MAP_INDEX, String.class);
		if (!StringUtils.isEmpty(siteMapValue)) {
			Boolean includeInSiteMap = Boolean
					.parseBoolean(currentPage.getProperties().get(CortevaConstant.SITE_MAP_INDEX, String.class));
			if (!includeInSiteMap) {
				crawlStr = CortevaConstant.NO_CRAWL_STRING;
			}
		}
		return crawlStr;
	}

	/**
	 * Gets page LastModifiedDate.
	 *
	 * @return lastModifiedDate
	 */
	public String getLastModifiedDate() {
		LOGGER.debug("Inside method getLastModifiedDate() in PageMetaModel");
		String pageLastModifiedDate = StringUtils.EMPTY;
		SimpleDateFormat format = new SimpleDateFormat(CortevaConstant.DATE_FORMAT);
		Page page = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		if (page.getLastModified() != null) {
			pageLastModifiedDate = format.format(page.getLastModified().getTime());
		}
		return pageLastModifiedDate;
	}

	/**
	 * Setter for Current Resource.
	 *
	 * @param currentResource
	 *            currentResource
	 */
	public void setCurrentResource(Resource currentResource) {
		this.currentResource = currentResource;
	}

	/**
	 * Gets the company.
	 *
	 * @return company
	 */
	public String getCompany() {
		return configurationService.getPropConfigValue(request, CortevaConstant.PAGE_PROPERTY_COMPANY,
				CortevaConstant.GLOBAL_CONFIG_NAME);
	}

	/**
	 * Gets the contentType.
	 *
	 * @return contentType
	 */
	public String getContentType() {
		String contentType = CommonUtils.getInheritedProperty(CortevaConstant.CONTENT_TYPE, currentResource);
		TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
		if (StringUtils.isNotBlank(contentType) && tagManager != null) {
			Tag[] contentTypeTags = tagManager.findTagsByTitle(contentType, CommonUtils.getLocale(request));
			if (null != contentTypeTags) {
				contentType = getContentTypeTitle(contentType, contentTypeTags);
			}
		}
		LOGGER.debug("Content Type is: {}", contentType);
		return contentType;
	}

	/**
	 * @param contentType
	 * 			the Content Type
	 * @param contentTypeTags
	 * 			the content type tag array
	 * @return contentType
	 * 
	 */
	private String getContentTypeTitle(String contentType, Tag[] contentTypeTags) {
			LOGGER.debug("Content Type Tag is: {}", contentTypeTags);
			for (Tag contentTag : contentTypeTags) {
				if (StringUtils.equalsIgnoreCase(contentType, CommonUtils.getTagLocalizedTitle(request, contentTag))) {
					contentType = contentTag.getTitle();
				}
			}
		return contentType;
	}
	
	/**
	 * Method to get the Environment of the current AEM installation.
	 *
	 * @return String Environment of the instance
	 */
	public String getEnvironment() {
		return CommonUtils.getEnvironment(slingSettingsService.getRunModes());
	}

	/**
	 * Gets the country.
	 *
	 * @return country of the page
	 */
	public String getCountry() {
		return StringUtils.lowerCase(resolveCountryLanguage().get(CortevaConstant.COUNTRY));
	}

	/**
	 * Gets the language.
	 *
	 * @return language of the page
	 */
	public String getLanguage() {
		return resolveCountryLanguage().get(CortevaConstant.LANGUAGE);
	}

	/**
	 * Resolve country language.
	 *
	 * @return map of country, language and region
	 */
	private Map<String, String> resolveCountryLanguage() {
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		return CommonUtils.getRegionCountryLanguage(currentPage.getPath(), getResourceResolver());

	}

	/**
	 * Gets the category.
	 *
	 * @return category of the page
	 */
	public String getCategory() {
		return resolveCategorySubcategory().get(CortevaConstant.CATEGORY);
	}

	/**
	 * Gets the subcategory.
	 *
	 * @return subcategory of the page
	 */
	public String getSubcategory() {
		return resolveCategorySubcategory().get(CortevaConstant.SUBCATEGORY);
	}

	/**
	 * Resolve category subcategory.
	 *
	 * @return map of country, language and region
	 */
	private Map<String, String> resolveCategorySubcategory() {
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		return CommonUtils.getCategorySubcategory(currentPage.getPath(), getResourceResolver());

	}

	/**
	 * Gets the product src.
	 *
	 * @return the product src
	 */
	public String getProductSrc() {
		return getPdpSource(currentResource);
	}

	/**
	 * Gets the pdp url.
	 *
	 * @return the pdp url
	 */
	public String getPdpUrl() {
		LOGGER.debug("Entering getPdpUrl() method");
		String productId = getProductId(currentResource, getProductSrc());
		StringBuilder sb = new StringBuilder();
		sb.append(CortevaConstant.PDP_SERVLET_URL).append(CortevaConstant.DOT);
		if (StringUtils.isNotBlank(productId)) {
			sb.append(productId).append(CortevaConstant.DOT).append(getProductSrc()).append(CortevaConstant.DOT);
		}
		LOGGER.debug("Exiting getPdpUrl() method");
		return sb.append(CortevaConstant.JSON_LOWERCASE).toString();
	}

	/**
	 * Gets the pdp product id.
	 *
	 * @return the pdp product id
	 */
	public String getPdpProductId() {
		return getProductId(currentResource, getProductSrc());
	}

	/**
	 * Gets the product type.
	 *
	 * @return the product type
	 * @throws RepositoryException
	 *             the repository exception
	 */
	public String getProductType() throws RepositoryException {
		return CommonUtils.getProductTypeProperty(CortevaConstant.CQ_PRODUCT_TYPE, currentResource);
	}

	/**
	 * Gets the product type tags.
	 *
	 * @return the product type tags
	 */
	public List<String> getProductTypeTags() {
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		return getProductTypeTags(currentPage);
	}

	/**
	 * Gets the product name.
	 *
	 *
	 * @return the product name
	 */
	public String getProductName() {
		if (StringUtils.equalsIgnoreCase(CortevaConstant.NON_AGRIAN, getProductSrc())) {
			return getProductName(currentResource);
		} else {
			return "";
		}
	}

	/**
	 * Gets the product category.
	 *
	 *
	 * @return the product category
	 */
	public String getProductCategory() {
		LOGGER.debug("Inside method getProductCategory() in PageMetaModel");
		String productCategory = "";
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		if (currentPage != null) {
			String currentPageTemplateType = getCurrentPageTemplateType(currentPage);
			if (currentPageTemplateType.equals(PDP_TEMPLATE)) {
				productCategory = currentPage.getParent().getTitle();
			} else if (currentPage.getParent().getName().equals(PRODUCT_AND_SOLUTION_PAGE_NAME)) {
				productCategory = currentPage.getTitle();
			}
		}
		return productCategory;
	}

	/**
	 * Gets the crop category.
	 *
	 *
	 * @return the crop category
	 */
	public List<String> getCropCategory() {
		LOGGER.debug("Inside method getCropCategory() in PageMetaModel");
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		if (null != currentPage && null != currentPage.getTemplate()) {
			String currentPageTemplateType = getCurrentPageTemplateType(currentPage);
			if (currentPageTemplateType.equals(PDP_TEMPLATE)) {
				return getCropTypeTags(currentResource);
			}
		}
		return null;
	}

	/**
	 * Gets the headerExpFragmentPath.
	 *
	 * @return headerExpFragmentPath
	 */
	public String getHeaderExpFragmentPath() {
		LOGGER.debug("Inside method getHeaderExpFragmentPath() in PageMetaModel");

		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		String headerExpFragment = currentPage.getProperties()
				.get(CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_HEADER, String.class);

		if (StringUtils.isBlank(headerExpFragment)) {
			headerExpFragment = getLanguageFragmentPath(currentPage,
					CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_HEADER);
		}
		if (StringUtils.isNotBlank(headerExpFragment)) {
			headerExpFragment = getExpFragmentNodePath(headerExpFragment);
		}
		return headerExpFragment;
	}

	/**
	 * Gets the footerExpFragmentPath.
	 *
	 * @return footerExpFragmentPath
	 */
	public String getFooterExpFragmentPath() {
		LOGGER.debug("Inside method getFooterExpFragmentPath() in PageMetaModel");

		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		String footerExpFragment = currentPage.getProperties()
				.get(CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_FOOTER, String.class);

		if (StringUtils.isBlank(footerExpFragment)) {
			footerExpFragment = getLanguageFragmentPath(currentPage,
					CortevaConstant.PAGE_PROPERTY_EXPERIENCE_FRAGMENT_FOOTER);
		}
		if (StringUtils.isNotBlank(footerExpFragment)) {
			footerExpFragment = getExpFragmentNodePath(footerExpFragment);
		}
		return footerExpFragment;
	}

	/**
	 * Gets the languageFragmentPath.
	 *
	 * @param currentPage
	 *            currentPage
	 * @param propertyName
	 *            propertyName
	 * @return languageFragmentPath
	 */
	private String getLanguageFragmentPath(Page currentPage, String propertyName) {
		LOGGER.debug("Inside method getLanguageFragmentPath() in PageMetaModel");
		String languagePagePath;
		if (StringUtils.startsWith(currentPage.getPath(), CortevaConstant.CONTENT_CORPORATE_ROOT_PATH)) {
			languagePagePath = CortevaConstant.CONTENT_CORPORATE_ROOT_PATH;
		} else {
			languagePagePath = CortevaConstant.CONTENT_ROOT + CortevaConstant.FORWARD_SLASH + getBrandName()
					+ CortevaConstant.FORWARD_SLASH
					+ StringUtils.lowerCase(resolveCountryLanguage().get(CortevaConstant.REGION))
					+ CortevaConstant.FORWARD_SLASH + getCountry() + CortevaConstant.FORWARD_SLASH + getLanguage();
		}
		LOGGER.debug("language path {}", languagePagePath);
		Resource languagePageRes = getResourceResolver().resolve(languagePagePath);
		Page languagePage = CommonUtils.getPageFromResource(getResourceResolver(), languagePageRes);
		return languagePage.getProperties().get(propertyName, String.class);
	}

	/**
	 * Gets the canonical url.
	 *
	 * @return the canonical url
	 */
	public String getCanonicalUrl() {
		String canonicalURL = StringUtils.EMPTY;
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		if (null != currentPage) {
			ValueMap pageValueMap = currentPage.getProperties();
			if (null != pageValueMap && pageValueMap.containsKey(CANONICAL_URL)) {
				canonicalURL = getResourceResolver().map(LinkUtil.getHref((String) pageValueMap.get(CANONICAL_URL)));
			} else {
				canonicalURL = getResourceResolver().map(LinkUtil.getHref(currentPage.getPath()));
			}
		}
		if (StringUtils.isNotBlank(canonicalURL) && request.isSecure()) {
			canonicalURL = canonicalURL.replace(CortevaConstant.HTTP, CortevaConstant.HTTPS);
		}
		return canonicalURL;
	}

	/**
	 * Gets the expFragmentPath.
	 *
	 * @param expFragmentPath
	 *            expFragmentPath
	 * @return expFragmentPath
	 */
	private String getExpFragmentNodePath(String expFragmentPath) {
		Resource expFragmentResource = getResourceResolver().resolve(expFragmentPath);
		if (!ResourceUtil.isNonExistingResource(expFragmentResource)) {
			Node expFragmentNode = expFragmentResource.adaptTo(Node.class);
			String childnode = "";
			try {
				if (null != expFragmentNode) {
					expFragmentNode = expFragmentNode.getNode(CortevaConstant.JCR_CONTENT)
							.getNode(CortevaConstant.ROOT);
					Iterable<Node> children = JcrUtils.getChildNodes(expFragmentNode);
					if (null != children && null != children.iterator().next()) {
						childnode = children.iterator().next().getName();
					}
					if (StringUtils.isNotBlank(childnode)) {
						expFragmentPath = expFragmentPath.concat(CortevaConstant.FORWARD_SLASH
								+ CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.ROOT
								+ CortevaConstant.FORWARD_SLASH + childnode);
					}
				}
			} catch (PathNotFoundException e) {
				LOGGER.error("PathNotFoundException occured in getExpFragmentNodePath()", e);
			} catch (RepositoryException e) {
				LOGGER.error("RepositoryException occured in getExpFragmentNodePath()", e);
			}
		}
		return expFragmentPath;

	}

	/**
	 * Gets the alternate HREF.
	 *
	 * @return the alternate HREF
	 */
	public Map<String, String> getAlternateHREF() {
		Map<String, String> alternateHrefMap = new HashMap<>();
		ResourceResolver resourceResolver = getResourceResolver();
		ValueMap valueMap = currentResource.getValueMap();
		String[] alternateHrefProp = valueMap.get(CortevaConstant.ALTERNATE_HREF_PROPERTY, String[].class);
		if (null != alternateHrefProp && alternateHrefProp.length > 0) {
			for (String alternateHref : alternateHrefProp) {
				Map<String, String> regionCountryLanguage = CommonUtils.getRegionCountryLanguage(alternateHref,
						resourceResolver);
				if (!regionCountryLanguage.isEmpty()) {
					alternateHrefMap.put(resourceResolver.map(request, LinkUtil.getHref(alternateHref)),
							regionCountryLanguage.get(CortevaConstant.LANGUAGE) + CortevaConstant.HYPHEN
									+ StringUtils.lowerCase(regionCountryLanguage.get(CortevaConstant.COUNTRY)));
				}
			}
			Page currentPage = CommonUtils.getPageFromResource(resourceResolver, currentResource);
			if (null != currentPage) {
				String currentPagePath = currentPage.getPath();
				Map<String, String> regionCountryLanguage = CommonUtils.getRegionCountryLanguage(currentPagePath,
						resourceResolver);
				if (!regionCountryLanguage.isEmpty()) {
					alternateHrefMap.put(resourceResolver.map(request, LinkUtil.getHref(currentPagePath)),
							regionCountryLanguage.get(CortevaConstant.LANGUAGE) + CortevaConstant.HYPHEN
									+ StringUtils.lowerCase(regionCountryLanguage.get(CortevaConstant.COUNTRY)));
				}
			}
		}
		return alternateHrefMap;
	}

	/**
	 * This method returns the brand name from current page.
	 * 
	 * @return brandName
	 */
	public String getBrandName() {
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		String brandName = currentPage.getPath().split(CortevaConstant.FORWARD_SLASH)[2];
		LOGGER.debug("Brand Name is ::: {}", brandName);
		return brandName;
	}

	/**
	 * This method returns whether current page is experience fragment.
	 * 
	 * @return experienceFragment
	 */
	public boolean isExperienceFragmentPath() {
		String resourcePath = currentResource.getPath();
		boolean experienceFragment = false;
		if (resourcePath.contains(CortevaConstant.EXP_FRAGMENT_ROOT)) {
			experienceFragment = true;
		}
		LOGGER.debug("Is Experience Fragment Path ::: {}", experienceFragment);
		return experienceFragment;
	}

	/**
	 * Gets the Region.
	 *
	 * @return region of the page
	 */
	public String getRegion() {
		return StringUtils.lowerCase(resolveCountryLanguage().get(CortevaConstant.REGION));
	}

	/**
	 * This method returns Template type of current page
	 *
	 * @param currentPage
	 *            the current Page
	 * @return the template Type
	 */
	public String getCurrentPageTemplateType(Page currentPage) {
		Resource templateResource = currentPage.getTemplate().adaptTo(Resource.class);
		Resource tempContentRes = null;
		if (null != templateResource && null != templateResource.getChild(CortevaConstant.JCR_CONTENT)) {
			tempContentRes = templateResource.getChild(CortevaConstant.JCR_CONTENT);
		}
		if (null != tempContentRes) {
			return tempContentRes.getValueMap().get("cq:templateType").toString();
		}
		return "";
	}

	/**
	 * Gets the SEO schema namespace.
	 *
	 * @return SEO schema definition
	 */
	public String getSeoSchema() {
		return CortevaConstant.HTTP + CortevaConstant.SEO_SCHEMA + CortevaConstant.FORWARD_SLASH;
	}

	/**
	 * Gets the SEO Image type.
	 *
	 * @return SEO Image object type definition
	 */
	public String getSeoImageObject() {
		return CortevaConstant.SEO_IMAGE_OBJECT;
	}

	/**
	 * Gets the SEO Organisation.
	 *
	 * @return SEO Organisation object type
	 */
	public String getSeoOrganisation() {
		return CortevaConstant.SEO_ORGANISATION;
	}

	/**
	 * Gets the SEO Article.
	 *
	 * @return SEO Article object type
	 */
	public String getSeoArticle() {
		return CortevaConstant.SEO_ARTICLE_OBJECT;
	}

	/**
	 * Gets the Scene 7 image path of logo image dragged and dropped using
	 * Global header component in the experience fragment.
	 * 
	 * @return get image path from header experience fragment.
	 */
	public String getGlobalHeaderLogoPath() {
		String componentNodePath = getHeaderExpFragmentPath();
		return getImagePathFromComponent(componentNodePath);
	}

	/**
	 * Gets the Scene 7 image path of image dragged and dropped using Article
	 * header component in Article Page template.
	 * 
	 * @return get image path from component
	 */
	public String getArticleHeaderImagePath() {
		String componentNodePath = CortevaConstant.ARTICLE_HEADER_COMPONENT_NODE;
		Page currentPage = CommonUtils.getPageFromResource(getResourceResolver(), currentResource);
		String componentResourcePath = currentPage.getPath() + componentNodePath;
		return getImagePathFromComponent(componentResourcePath);
	}

	/**
	 * Gets the Scene 7 path for the component node passed. any component with a
	 * generic image property - fileReference.
	 * 
	 * @param componentNodePath
	 *            componentNodePath
	 * 
	 * @return Image Scene 7 path
	 */
	public String getImagePathFromComponent(String componentNodePath) {

		String imagePath = null;
		String imageScene7Path = null;

		try {
			Session session = getSession();
			Node componentNodeInArticlePage = session.getNode(componentNodePath);
			if (componentNodeInArticlePage != null) {
				imagePath = AEMUtils.getNodePropertyAsString(componentNodeInArticlePage,
						CortevaConstant.GENERIC_IMAGE_PROPERTY);
				if (StringUtils.isNotBlank(imagePath)) {
					imageScene7Path = getSceneSevenImagePath(imagePath, request);
				}
			}
		} catch (PathNotFoundException e) {
			LOGGER.error("PathNotFoundException occured in getExpFragmentNodePath()", e);
		} catch (RepositoryException e) {
			LOGGER.error("RepositoryException occured in getExpFragmentNodePath()", e);
		}

		return imageScene7Path;
	}

}
