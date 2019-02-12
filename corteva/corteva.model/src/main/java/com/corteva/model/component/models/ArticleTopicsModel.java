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

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Property;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

/**
 * This the sling Model for the Article Topics in Article Filter and Right Rail Linked List.
 * 
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleTopicsModel extends AbstractSlingModel {

	/** The Article Facet. */
	@Inject
	private String articleFacet;

	/** The Article Facet child tags. */
	@Inject
	@Named("cq:facetTags")
	private Property facetTags;

	/**
	 * @return the articleFacet
	 */
	public String getArticleFacet() {
		return articleFacet;
	}

	/**
	 * @return the facetTags
	 */
	public Property getFacetTags() {
		return facetTags;
	}
	
}
