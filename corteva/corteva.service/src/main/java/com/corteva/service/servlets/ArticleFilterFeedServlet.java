/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.service.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.ArticleBean;
import com.corteva.model.component.utils.ArticleUtils;
import com.corteva.core.utils.AEMUtils;
import com.corteva.model.component.models.ArticleTopicsModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This servlet is triggered when the author need to get the articles filtered
 * based on the article type , year and month and sorted based on custom display
 * date and title
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/articleFilter",
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class ArticleFilterFeedServlet extends AbstractServlet {

	/** The Constant to hold value Error message string. */
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

	/**
	 * The Constant to hold value of Artile folder path stored in article filter
	 * component.
	 */

	/** The Constant to hold value of JSON constant that holds error message. */
	private static final String ERROR_MESSAGE_IN_RESPONSE_JSON = "errorMessage";

	/** The Constant to hold value article filter resource type component. */
	private static final String ARTICAL_FILTER_SLING_RESOURCE_TYPE = "corteva/components/content/articleFilter/v1/articleFilter";

	/** The Constant to hold value article filter component. */
	private static final String ARTICLE_FILTER_COMPONENT_NAME = "articleFilter";

	/** The Constant to hold value default article filter component. */
	private static final String DEFAULT_NO_OF_ARTICLES = "noOfArticlesToDisplay";

	/** The Constant to hold value article type. */
	private static final String ARTICLE_TYPE = "articleType";

	/** The Constant to hold response parameter dateMapFilter. */
	private static final String DATE_MAP_FILTER = "dateMapFilter";

	/** The Constant to hold response parameter showLoadMore. */
	private static final String SHOW_LOAD_MORE = "showLoadMore";

	/** The Constant to hold response parameter articlePagesList. */
	private static final String ARTICLE_PAGES_LIST = "articlePagesList";

	/** The Constant to hold intraFacetLogicalOperation. */
	private static final String INTRA_FACET_LOGICAL_OPERATION = "intraFacetLogicalOperation";
	
	/** The Constant to hold interFacetLogicalOperation. */
	private static final String INTER_FACET_LOGICAL_OPERATION = "interFacetLogicalOperation";
	
	/** The Constant to hold articleTopics node. */
	private static final String ARTICLE_TOPICS = "articleTopics";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleFilterFeedServlet.class);

	/** The Constant REFERER. */
	private static final String REFERER = "referer";

	/** The base service. */
	private transient BaseConfigurationService baseService;

	/**
	 * Bind base configuration service.
	 *
	 * @param baseService
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseService) {
		this.baseService = baseService;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseService
	 *            the base service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseService) {
		this.baseService = baseService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.
	 * sling .api.SlingHttpServletRequest,
	 * org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		boolean featureFlag = true;
		String[] selector = request.getRequestPathInfo().getSelectors();
		String noOfArticlesToDisplay = "";
		boolean displayTime = false;
		if (baseService != null) {
			featureFlag = baseService.getToggleInfo(CortevaConstant.ARTICLE_FILTER_FEED_SERVLET,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
			noOfArticlesToDisplay = baseService.getPropValueFromConfiguration(CortevaConstant.COMPONENT_CONFIG_NAME,
					DEFAULT_NO_OF_ARTICLES);
			LOGGER.debug("Feature Flag Value is {} ", featureFlag);
			LOGGER.debug("No of articles to display is {} ", noOfArticlesToDisplay);
		}
		if (featureFlag) {
			JsonObject responseJSON = new JsonObject();
			ResourceResolver resourceResolver = request.getResourceResolver();
			try {
				if (validateRequest(selector, resourceResolver, noOfArticlesToDisplay)) {
					responseJSON = getArticleFeed(request, baseService, selector, resourceResolver,
							noOfArticlesToDisplay, displayTime);
					if (responseJSON.has(ERROR_MESSAGE_IN_RESPONSE_JSON)) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
					response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
					response.getWriter().write(responseJSON.toString());

				} else {
					LOGGER.error("Invalid Request {} ", selector.length);
					responseJSON.addProperty(ERROR_MESSAGE_IN_RESPONSE_JSON, INTERNAL_SERVER_ERROR);
					response.getWriter().write(responseJSON.toString());
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} catch (RepositoryException e) {
				LOGGER.error("Error occured while fetching Article feed data", e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * @param request
	 *            the request
	 * @param baseService
	 *            the baseService
	 * @param selector
	 *            the selector
	 * @param resourceResolver
	 *            the resourceResolver
	 * @param noOfArticlesToDisplay
	 *            the noOfArticlesToDisplay
	 * @param displayTime
	 *            the flag dispalTime
	 * @return jsonObject the responseJSON
	 * @throws RepositoryException
	 *             the repositoryException
	 */
	private JsonObject getArticleFeed(SlingHttpServletRequest request, BaseConfigurationService baseService,
			String[] selector, ResourceResolver resourceResolver, String noOfArticlesToDisplay, boolean displayTime)
			throws RepositoryException {
		JsonObject responseJSON = new JsonObject();
		String pagePath = getPagePathFromRequest(request, selector[4]);
		if (!pagePath.isEmpty()) {
			Node node = getComponentNodeFromPage(request, resourceResolver, pagePath);
			String folderPath;
			if (null != node) {
				folderPath = ArticleUtils.getDefaultArticleFolderPath(pagePath, resourceResolver, baseService);
				LOGGER.debug("Folder Path under which to find articles is {} ", folderPath);
				Iterator<Resource> pageIterator = getArticlePageIterator(node, resourceResolver, folderPath, noOfArticlesToDisplay, selector);
				if (null != pageIterator) {
					responseJSON = createResponseJSON(request, baseService, resourceResolver, noOfArticlesToDisplay,
							pagePath, pageIterator, displayTime);
				}

			} else {
				responseJSON.addProperty(ERROR_MESSAGE_IN_RESPONSE_JSON, INTERNAL_SERVER_ERROR);
				LOGGER.error("Error occured while fetching component within the page {}", pagePath);
			}
		} else {
			responseJSON.addProperty(ERROR_MESSAGE_IN_RESPONSE_JSON, INTERNAL_SERVER_ERROR);
			LOGGER.error("Page could not be retireved from the URL Referrer {}", selector[4]);
		}
		return responseJSON;
	}

	/**
	 * @param selector
	 *            the selector
	 * @param resourceResolver
	 *            the resource resolver
	 * @param noOfArticlesToDisplay
	 *            the noOfArticlesToDisplay
	 * @return the validateArticleTypeSelector
	 * 
	 */
	private Boolean validateRequest(String[] selector, ResourceResolver resourceResolver,
			String noOfArticlesToDisplay) {
		Boolean requestValid = false;
		if (selector.length >= 5 && !StringUtils.isEmpty(noOfArticlesToDisplay)) {
			if (selector[0].equalsIgnoreCase(CortevaConstant.ALL)) {
				requestValid = true;
			} else {
				requestValid = isArticleTypeValid(selector[0], resourceResolver);
			}
		}
		return requestValid;
	}

	/**
	 * @param articleType
	 *            the articleType
	 * @param resourceResolver
	 *            the resourceResolver
	 * @return requestValid
	 */
	private Boolean isArticleTypeValid(String articleType, ResourceResolver resourceResolver) {
		Boolean requestValid = false;
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (null != tagManager) {
			List<Tag> tags = TagUtil.listChildren(tagManager, CortevaConstant.ARITCLE_L2_TAXONOMY_STRUCTURE);
			for (final Tag tag : tags) {
				if (articleType.equalsIgnoreCase(tag.getName())) {
					requestValid = true;
					break;
				}
			}
		}
		return requestValid;
	}

	/**
	 * @param request
	 *            the request
	 * @param baseService
	 *            the baseService
	 * @param resourceResolver
	 *            the resource resolver
	 * @param noOfArticlesToDisplay
	 *            the no of article to display
	 * 
	 * @param pagePath
	 *            the path of page
	 * @param pageIterator
	 *            the page iterator
	 * @param displayTime
	 *            the flag displayTime
	 * @return responseJSON the response JSON
	 */
	private JsonObject createResponseJSON(SlingHttpServletRequest request, BaseConfigurationService baseService,
			ResourceResolver resourceResolver, String noOfArticlesToDisplay, String pagePath,
			Iterator<Resource> pageIterator, boolean displayTime) {
		JsonObject responseJSON = new JsonObject();
		int index = 0;
		ArticleBean articleBean;
		Calendar cal = null;
		Map<String, Map<String, String>> dateMap = new LinkedHashMap<>();
		List<ArticleBean> articlePageList = new ArrayList<>();
		Boolean showLoadMore = false;
		Gson gson = new Gson();
		while (pageIterator.hasNext()) {
			Resource pageResource = pageIterator.next();
			if (pageResource != null) {
				Page page = pageResource.adaptTo(Page.class);
				if (page != null) {
					articleBean = new ArticleBean();
					index++;
					if (null != page.getProperties().get(CortevaConstant.DISPLAY_DATE)) {
						cal = (Calendar) page.getProperties().get(CortevaConstant.DISPLAY_DATE);
						ArticleUtils.createDateMapFilter(dateMap, cal, index);
					}
					if (index <= Integer.parseInt(noOfArticlesToDisplay)) {
						ArticleUtils.getArticlePageData(page, articleBean, resourceResolver, request,
								ARTICLE_FILTER_COMPONENT_NAME, baseService, pagePath, displayTime);
						articlePageList.add(articleBean);
					}
				}
			}
		}
		if (Integer.parseInt(noOfArticlesToDisplay) < index) {
			showLoadMore = true;
		}
		JsonArray array = ArticleUtils.formatDateMapResponse(dateMap);
		responseJSON.add(DATE_MAP_FILTER, array);
		JsonElement json = new JsonParser().parse(gson.toJson(articlePageList));
		responseJSON.addProperty(SHOW_LOAD_MORE, showLoadMore);
		responseJSON.add(ARTICLE_PAGES_LIST, json);
		return responseJSON;
	}

	/**
	 * @param request
	 *            the request
	 * @param resourceResolver
	 *            the resourceResolver
	 * @param pagePath
	 *            the pagePath
	 * @return contentNode component node
	 */
	public Node getComponentNodeFromPage(SlingHttpServletRequest request, ResourceResolver resourceResolver,
			String pagePath) {
		Node componentNode = null;
		LOGGER.debug("Fetching first matching Article Filter component from Current Page {}", pagePath);
		Iterator<Resource> resultIterator = null;
		Map<String, String> params = new HashMap<>();
		params.put(PathPredicateEvaluator.PATH, pagePath);
		params.put("1_property", CortevaConstant.SLING_RESOURCE_TYPE);
		params.put("1_property.value", ARTICAL_FILTER_SLING_RESOURCE_TYPE);
		params.put("p_limit", "-1");
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		if (queryBuilder != null) {
			Query query = queryBuilder.createQuery(PredicateGroup.create(params),
					resourceResolver.adaptTo(Session.class));
			resultIterator = query.getResult().getResources();
			Resource res = resultIterator.next();
			if (res != null) {
				componentNode = res.adaptTo(Node.class);
			}
		}
		return componentNode;
	}

	/**
	 * @param request
	 *            the request
	 * @param pageName
	 *            the pageName
	 * @return the path of the page
	 */
	private String getPagePathFromRequest(SlingHttpServletRequest request, String pageName) {
		String pagePath = "";
		if (null != request.getHeader(REFERER)) {
			pagePath = request.getHeader(REFERER);
			ResourceResolver resourceResolver = request.getResourceResolver();
			try {
				String resourcePath;
				resourcePath = new URI(pagePath).getPath();
				Resource res = resourceResolver.resolve(request, resourcePath);
				pagePath = res.getPath();
				LOGGER.debug("Path of the referred page is {} ", pagePath);
				if (!res.getName().equalsIgnoreCase(pageName)) {
					pagePath = "";
				}
			} catch (URISyntaxException e) {
				LOGGER.error("Exception occured while fetching page path from referrer", e);
			}
		}
		return pagePath;
	}

	/**
	 * @param contentNode
	 *            the contentNode
	 * 
	 * @return List the article Tags
	 * @throws RepositoryException
	 *             the exception
	 */
	private List<String> getArticleTypeFromArticleFilterComponent(Node contentNode) throws RepositoryException {
		Property articleType = null;
		List<String> articleTypeTagList = new ArrayList<>();
		if (contentNode.hasProperty(ARTICLE_TYPE)) {
			articleType = contentNode.getProperty(ARTICLE_TYPE);
			articleTypeTagList = ArticleUtils.getArticleTypePropertyList(articleType);
		}
		return articleTypeTagList;
	}
	
	/**
	 * @param node
	 *            the node
	 * @param resourceResolver
	 *            the resource resolver
	 * @param noOfArticlesToDisplay
	 *            the no of article to display
	 * 
	 * @param folderPath
	 *            the path of folderPath
	 * @param selector
	 *            the selector
	 * @return pageIterator the Page Iterator
	 * @throws RepositoryException
	 * 			  the exception
	 */
	private Iterator<Resource> getArticlePageIterator(Node node, ResourceResolver resourceResolver, String folderPath, String noOfArticlesToDisplay, String[] selector) throws RepositoryException {
		String year = selector[1];
		String month = selector[2];
		String offset = selector[3];
		List<String> articleTypeList = new ArrayList<>();
		articleTypeList.add(selector[0]);
		String intraFacetLogicalOperation = node.hasProperty(INTRA_FACET_LOGICAL_OPERATION) ? node.getProperty(INTRA_FACET_LOGICAL_OPERATION).getString() : "or";
		String interFacetLogicalOperation = node.hasProperty(INTER_FACET_LOGICAL_OPERATION) ? node.getProperty(INTER_FACET_LOGICAL_OPERATION).getString() : "and";
		if (articleTypeList.get(0).equalsIgnoreCase(CortevaConstant.ALL)) {
			articleTypeList = getArticleTypeFromArticleFilterComponent(node);
		}
		List<ArticleTopicsModel> articleTopicsList = new ArrayList<>();
		if (node.hasNode(ARTICLE_TOPICS)) {
			Node topics = node.getNode(ARTICLE_TOPICS);
			Resource topicsRes = resourceResolver.getResource(topics.getPath());
			if (null != topicsRes) {
				articleTopicsList = AEMUtils.getMultifieldItemList(topicsRes, ArticleTopicsModel.class);
			}
		}
		Iterator<Resource> pageIterator = ArticleUtils.findArticleFeedFilterData(resourceResolver, folderPath,
				articleTypeList, articleTopicsList, intraFacetLogicalOperation, interFacetLogicalOperation,
				noOfArticlesToDisplay, offset, year, month);
		
		return pageIterator;
	}

}