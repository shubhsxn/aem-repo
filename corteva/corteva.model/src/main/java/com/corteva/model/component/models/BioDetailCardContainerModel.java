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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;
import com.corteva.model.component.bean.BioDetailBean;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

/**
 * The is the sling for the Bio Detail Card Container Component author.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class BioDetailCardContainerModel extends AbstractSlingModel {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BioDetailCardContainerModel.class);
	/** Template Path For Bio Detail Page */
	private static final String BIODETAILPAGE_TEMPLATE_PATH = "/conf/corteva/settings/wcm/templates/biography-detail-page-template";
	/** Sling Resource Type For Bio Detail Component */
	private static final String BIODETAIL_COMPONENT_SLING_RESOURCE_TYPE_PATH = "corteva/components/content/bioDetail/v1/bioDetail";
	/** Default Ranking For Bio Detail Pages with No ranking set */
	private static final String DEFAULT_RANKING = "99999";
	/** Component Name For Bio Detail Component */
	private static final String BIODETAIL_COMPONENT_NAME = "biodetail";
	/** Constant For Bio Detail Filter Tag */
	private static final String BIODETAIL_FILTER_TAG = "cq:biographyTags";
	/** Constant For Bio Head Shot Image Property */
	private static final String PROPERTY_BIO_HEAD_SHOT_IMAGE = "bioHeadShotImagePath";
	/** Constant For Ranking Property */
	private static final String PROPERTY_RANKING = "ranking";
	/** The Constant to hold OFF value of Title. */
	private static final String TITLE = "title";

	/** The Resource Resolver. */
	@Inject
	private ResourceResolver resourceResolver;

	/** The sling request. */
	private SlingHttpServletRequest slingRequest;

	/** The Bio Detail Pages Path. */
	private List<BioDetailBean> bioDetailPages;

	/** The list of tags for dropdown. */
	private Map<String, String> tagsListMap;

	/** The biography File path. */
	@Inject
	@Optional
	@Via("resource")
	@Named("folderPath")
	private String folderPath;

	/**
	 * Instantiates a new Leadership Filter model.
	 *
	 * @param request
	 *            the request
	 */
	public BioDetailCardContainerModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/** The init method. */
	@PostConstruct
	public void init() {
		tagsListMap = new HashMap<>();
		bioDetailPages = new ArrayList<>();
		if (getFolderPath() != null) {
			getBioCards(folderPath);
			try {
				Collections.sort(bioDetailPages, rankingAndAlphabeticalComp);
			} catch (NumberFormatException e) {
				LOGGER.error("Number Format Exception Occured while sorting the list", e);
			}
		}

	}

	/**
	 * List of filter tags
	 * 
	 * @param folderPath
	 *            the folderPath to get components
	 * 
	 */
	private void getBioCards(String folderPath) {
		try {
			BioDetailBean bean = null;
			Iterator<Resource> pageIterator = findAllBioCards(resourceResolver, folderPath);
			while (pageIterator != null && pageIterator.hasNext()) {
				Resource res = pageIterator.next();
				Resource parentRes = res.getParent();
				Page page = parentRes != null ? parentRes.adaptTo(Page.class) : null;
				if (page != null) {
					bean = new BioDetailBean();
					getBioDetailPageData(bean, page);
					Resource pageRootNode = page.getContentResource(CortevaConstant.ROOT);
					Iterator<Resource> childs = pageRootNode.getChildren().iterator();
					while (childs.hasNext()) {
						Node contentNode = (childs.next()).adaptTo(Node.class);
						if (null != contentNode && getChildNodeSlingResourceTypeProperty(contentNode)
								.equalsIgnoreCase(BIODETAIL_COMPONENT_SLING_RESOURCE_TYPE_PATH)) {
							getBioDetailComponentData(contentNode, bean);
							break;
						}
					}
					bioDetailPages.add(bean);
				}
			}
		} catch (RepositoryException ex) {
			LOGGER.error("Repository Exception Occured while fetching bio detail pages", ex);
		}
	}

	/**
	 * 
	 * @param contentNode
	 *            the bioDetaiNode in biodetailpage
	 * @return sling:resourceType property
	 * @exception RepositoryException
	 *                On input error.
	 */
	private String getChildNodeSlingResourceTypeProperty(Node contentNode) throws RepositoryException {
		return contentNode != null && contentNode.hasProperty(CortevaConstant.SLING_RESOURCE_TYPE)
				? contentNode.getProperty(CortevaConstant.SLING_RESOURCE_TYPE).getString()
				: "";
	}

	/**
	 * @param bean
	 *            biodetailbean to set
	 * @param contentNode
	 *            the bioDetaiNode in biodetailpage
	 * @exception RepositoryException
	 *                On input error.
	 */
	private void getBioDetailComponentData(Node contentNode, BioDetailBean bean) throws RepositoryException {
		bean.setName(contentNode.hasProperty(CortevaConstant.NAME)
				? contentNode.getProperty(CortevaConstant.NAME).getString()
				: null);
		bean.setTitle(contentNode.hasProperty(BioDetailCardContainerModel.TITLE)
				? contentNode.getProperty(BioDetailCardContainerModel.TITLE).getString()
				: null);
		bean.setAltText(contentNode.hasProperty(CortevaConstant.ALT_TEXT)
				? contentNode.getProperty(CortevaConstant.ALT_TEXT).getString()
				: null);
		bean.setBioheadSpotImageBean(contentNode.hasProperty(PROPERTY_BIO_HEAD_SHOT_IMAGE)
				? getImageRenditionList(contentNode.getProperty(PROPERTY_BIO_HEAD_SHOT_IMAGE).getString(),
						BIODETAIL_COMPONENT_NAME, slingRequest)
				: null);
	}

	/**
	 * @param bean
	 *            biodetailbean to set
	 * @param page
	 *            the bioDetaiPage retrieved from query
	 */
	private void getBioDetailPageData(BioDetailBean bean, Page page) {
		if (null != page.getProperties().get(BIODETAIL_FILTER_TAG)) {
			String[] bioFilterTag = (String[]) page.getProperties().get(BIODETAIL_FILTER_TAG);
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			if (null != tagManager) {
				for (String tag : bioFilterTag) {
					if (!tagsListMap.containsKey(tagManager.resolve(tag).getName())) {
						tagsListMap.put(tagManager.resolve(tag).getName(), CommonUtils.getTagLocalizedTitle(slingRequest, tagManager.resolve(tag)));
					}

					bean.getTagsList().add((tagManager.resolve(tag)).getName());
				}
			}
		}
		bean.setPagePath(LinkUtil.getHref(page.getPath()));
		bean.setRanking(page.getProperties().get(PROPERTY_RANKING) != null
				? page.getProperties().get(PROPERTY_RANKING).toString()
				: null);
	}

	/**
	 * @return the bioDetailPages
	 */
	public List<BioDetailBean> getBioDetailPages() {
		return bioDetailPages;
	}

	/**
	 * Gets the List of all bio Cards
	 *
	 * @param resourceResolver
	 *            the resourceResolver
	 * @param contentPath
	 *            the contentPath
	 *
	 * @return Iterator
	 */
	private Iterator<Resource> findAllBioCards(ResourceResolver resourceResolver, String contentPath) {
		Iterator<Resource> resultIterator = null;
		Map<String, String> params = new HashMap<>();
		params.put(PathPredicateEvaluator.PATH, contentPath);
		params.put("1_property", CortevaConstant.CQ_TEMPLATE);
		params.put("1_property.value", BIODETAILPAGE_TEMPLATE_PATH);
		params.put("p_limit", "-1");
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		if (queryBuilder != null) {
			Query query = queryBuilder.createQuery(PredicateGroup.create(params),
					resourceResolver.adaptTo(Session.class));
			resultIterator = query.getResult().getResources();
		}
		return resultIterator;
	}

	/**
	 * @return the tagsList
	 */
	public Map<String, String> getTagsList() {
		return tagsListMap;
	}

	/**
	 * Comparator to sort Bio Cards based on Ranking and Name
	 */
	private static Comparator<BioDetailBean> rankingAndAlphabeticalComp = new Comparator<BioDetailBean>() {

		@Override
		public int compare(BioDetailBean o1, BioDetailBean o2) {
			if (o1.getRanking() == null) {
				o1.setRanking(DEFAULT_RANKING);
			}
			if (o2.getRanking() == null) {
				o2.setRanking(DEFAULT_RANKING);
			}
			int flag = Double.compare(Double.parseDouble(o1.getRanking()), Double.parseDouble(o2.getRanking()));
			if (flag == 0 && null != o1.getName() && null != o2.getName()) {
				flag = o1.getName().compareTo(o2.getName());
			}

			return flag;
		}
	};

	/**
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param folderPath
	 *            the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * @param resourceResolver
	 *            the resourceResolver to set
	 */
	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	/**
	 * Gets Locale value
	 *
	 * @return Locale the locale
	 */
	public Locale getLocale() {
		return slingRequest.getLocale();
	}

}