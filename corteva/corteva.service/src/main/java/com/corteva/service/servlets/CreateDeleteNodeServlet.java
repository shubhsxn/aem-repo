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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.model.component.bean.ExperienceFragmentBean;

/**
 * This servlet is triggered when the author saves experience fragment paths in
 * the component. The servlet first deletes all the existing experience fragment
 * nodes and then creates new experience fragment nodes based on the fields
 * authored by the author.
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/createdeletenode",
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.POST,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class CreateDeleteNodeServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeleteNodeServlet.class);
	
	/** The Constant to hold request parameter expfragPath. */
	private static final String REQUEST_PARAMETER_EXPERIENCE_FRAGMENT_PATH = "expfragPath";
	
	/** The Constant to hold request parameter componentResourcePath. */
	private static final String REQUEST_PARAMETER_COMPONENT_RESOURCE_PATH = "componentResourcePath";
	
	/** The Constant to hold request parameter impactTilePath. */
	private static final String REQUEST_PARAMETER_IMPACT_TILE_PATH = "impactTilePath";
	
	/** The Constant to hold request parameter expandedImpactFirstTilePath. */
	private static final String REQUEST_PARAMETER_EXPANDED_IMPACT_FIRST_TILE_PATH = "expandedImpactFirstTilePath";
	
	/** The Constant to hold request parameter expandedImpactLastTilePath. */
	private static final String REQUEST_PARAMETER_EXPANDED_IMPACT_LAST_TILE_PATH = "expandedImpactLastTilePath";
	
	/** The Constant to hold request parameter fixedGridHidden. */
	private static final String REQUEST_PARAMETER_FIXED_GRID_HIDDEN_PATH = "fixedGridHidden";
	
	/** The Constant to hold experience-fragment prefix. */
	private static final String EXPERIENCE_FRAGMENT_PREFIX = "experience-fragment_";

	/** The Constant EXP_FRAGMENT_COMP_PATH. */
	private static final String EXP_FRAGMENT_COMP_PATH = "corteva/components/content/experienceFragments/v1/experienceFragments";


	/** The base service. */
	private transient BaseConfigurationService confService;

	/**
	 * Bind base configuration service.
	 *
	 * @param confService
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService confService) {
		this.confService = confService;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param confService
	 *            the base service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService confService) {
		this.confService = confService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doPost(org.apache.sling
	 * .api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		boolean featureFlag = true;
		if (confService != null) {
			featureFlag = confService.getToggleInfo("createDeleteNodeServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			/**
			 * Exp Fragment path values from Experience Fragment Container
			 */
			String[] expfragData = request.getParameterValues(REQUEST_PARAMETER_EXPERIENCE_FRAGMENT_PATH);
			String componentResPath = request.getParameter(REQUEST_PARAMETER_COMPONENT_RESOURCE_PATH);
			if (StringUtils.isNotBlank(componentResPath)) {
				componentResPath = StringUtils.replace(componentResPath, CortevaConstant.JCR_UNDERSCORE_CONTENT,
						CortevaConstant.JCR_CONTENT);
			}
			/**
			 * Exp Fragment path values from Fixed Grid Container
			 */
			String firstExpandedTile = request.getParameter(REQUEST_PARAMETER_EXPANDED_IMPACT_FIRST_TILE_PATH);
			String[] impactTileData = request.getParameterValues(REQUEST_PARAMETER_IMPACT_TILE_PATH);
			String lastExpandedTile = request.getParameter(REQUEST_PARAMETER_EXPANDED_IMPACT_LAST_TILE_PATH);
			String fixedGridHiddenPath = request.getParameter(REQUEST_PARAMETER_FIXED_GRID_HIDDEN_PATH);
			if (StringUtils.isNotBlank(fixedGridHiddenPath)) {
				componentResPath = StringUtils.replace(fixedGridHiddenPath, CortevaConstant.JCR_UNDERSCORE_CONTENT,
						CortevaConstant.JCR_CONTENT);
			}
			List<ExperienceFragmentBean> expfragDtoList = new ArrayList<>();
			/**
			 * If ajax is triggered from experience fragment container
			 */
			if (null != expfragData && expfragData.length > 0) {
				expfragDtoList = populateExpFragList(expfragData);
			}
			/**
			 * If ajax is triggered from fixed grid tiles container
			 */
			if (null != impactTileData && impactTileData.length > 0) {
				expfragDtoList = populateFixedGridList(firstExpandedTile, impactTileData, lastExpandedTile);
			}
			LOGGER.debug("Component Resource Path in Create Delete Node Servlet :: {}", componentResPath);
			/**
			 * Deletes existing experience fragment nodes and creates new nodes.
			 */
			createDeleteExpFragNode(request, expfragDtoList, componentResPath);
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * Populates Experience Fragment List based on Experience Fragment Container
	 * content.
	 *
	 * @param expfragData
	 *            the expfrag data
	 * @return the list
	 */
	private List<ExperienceFragmentBean> populateExpFragList(String[] expfragData) {
		List<ExperienceFragmentBean> expfragDtoList = new ArrayList<>();
		LOGGER.debug("Inside populateExpFragList() method of Create Delete Node Servlet");
		ExperienceFragmentBean expfragDto;
		for (String exfrData : expfragData) {
			expfragDto = new ExperienceFragmentBean();
			expfragDto.setExpfragPath(exfrData);
			expfragDtoList.add(expfragDto);
		}
		LOGGER.debug("Experience Fragment Dto List of Create Delete Node Servlet :: {}", expfragDtoList);
		return expfragDtoList;
	}

	/**
	 * Populates the Experience Fragment List based on Fixed Grid Container content.
	 *
	 * @param firstExpandedTile
	 *            the first expanded tile
	 * @param impactTileData
	 *            the impact tile data
	 * @param lastExpandedTile
	 *            the last expanded tile
	 * @return the list
	 */
	private List<ExperienceFragmentBean> populateFixedGridList(String firstExpandedTile, String[] impactTileData,
			String lastExpandedTile) {
		List<ExperienceFragmentBean> expfragDtoList = new ArrayList<>();
		LOGGER.info("Inside populateFixedGridList() method of Create Delete Node Servlet");

		if (StringUtils.isNotBlank(firstExpandedTile)) {
			ExperienceFragmentBean expfragDtoFirst = new ExperienceFragmentBean();
			expfragDtoFirst.setExpfragPath(firstExpandedTile);
			expfragDtoList.add(expfragDtoFirst);
		}

		ExperienceFragmentBean expfragDto;
		for (String impData : impactTileData) {
			expfragDto = new ExperienceFragmentBean();
			expfragDto.setExpfragPath(impData);
			expfragDtoList.add(expfragDto);
		}

		if (StringUtils.isNotBlank(lastExpandedTile)) {
			ExperienceFragmentBean expfragDtoLast = new ExperienceFragmentBean();
			expfragDtoLast.setExpfragPath(lastExpandedTile);
			expfragDtoList.add(expfragDtoLast);
		}
		LOGGER.debug("Fixed Grid List of Create Delete Node Servlet :: {}", expfragDtoList);
		return expfragDtoList;
	}

	/**
	 * Method that first deletes child nodes of experience fragment container and
	 * then creates new nodes in crx.
	 *
	 * @param request
	 *            the request
	 * @param expfragList
	 *            the expfrag list
	 * @param componentResPath
	 *            the component res path
	 */
	private void createDeleteExpFragNode(SlingHttpServletRequest request, List<ExperienceFragmentBean> expfragList,
			String componentResPath) {
		LOGGER.debug("Inside createDeleteExpFragNode() method of Create Delete Node Servlet");
		final ResourceResolver resolver = request.getResourceResolver();
		final Session session = resolver.adaptTo(Session.class);
		String[] compResPathSplit = StringUtils.split(componentResPath, CortevaConstant.FORWARD_SLASH);
		String compnodeName = compResPathSplit[compResPathSplit.length - 1];
		try {
			if (null != session && session.isLive() && session.nodeExists(componentResPath)) {
				Node expfragNode = session.getNode(componentResPath);
				String cmpnodeName = expfragNode.getName();
				LOGGER.debug("Experience Fragment Node Name :: {}", cmpnodeName);
				if (StringUtils.equalsIgnoreCase(compnodeName, cmpnodeName)) {
					Iterable<Node> expfragChildNodes = JcrUtils.getChildNodes(expfragNode);
					/**
					 * Removing the child nodes experience fragment container.
					 */
					removeChildNodes(expfragChildNodes, session);
					/**
					 * Creating Experience Fragment Node
					 */
					createExpFragNode(expfragList, session, expfragNode, cmpnodeName);
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error(
					"Repository Exception occurred in createDeleteExpFragNode() method of Create Delete Node Servlet",
					e);
		} finally {
			CommonUtils.closeResolverSession(resolver, session);
		}
	}

	/**
	 * This method removes the child nodes of the experience fragment container.
	 *
	 * @param expfragChildNodes
	 *            the expfrag child nodes
	 * @param session
	 *            the session
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void removeChildNodes(Iterable<Node> expfragChildNodes, Session session) throws RepositoryException {
		LOGGER.debug("Inside removeChildNodes() method of Create Delete Node Servlet");
		for (Node expfragChildNode : expfragChildNodes) {
			if (StringUtils.startsWith(expfragChildNode.getName(), CortevaConstant.RESPONSIVE_GRID)) {
				expfragChildNode.remove();
			}
			session.save();
		}
	}

	/**
	 * Method to create experience fragment node in crx.
	 *
	 * @param expfragList
	 *            the expfrag list
	 * @param session
	 *            the session
	 * @param expfragNode
	 *            the expfrag node
	 * @param cmpnodeName
	 *            the cmpnode name
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void createExpFragNode(List<ExperienceFragmentBean> expfragList, final Session session, Node expfragNode,
			String cmpnodeName) throws RepositoryException {
		LOGGER.debug("Inside createExpFragNode() method of Create Delete Node Servlet");
		StringBuilder sb = new StringBuilder();
		if (null != expfragList && !expfragList.isEmpty()) {
			String respgripNodeName = sb.append(CortevaConstant.RESPONSIVE_GRID).append(cmpnodeName).toString();
			/**
			 * Adding a new responsive grid node for the experience fragment component.
			 */
			Node respGridNode = expfragNode.addNode(respgripNodeName, CortevaConstant.NT_UNSTRUCTURED);
			/**
			 * Setting the property for responsive grid node.
			 */
			respGridNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, CortevaConstant.RESPONSIVE_GRID_RESOURCE);
			int i = 0; // Counter to keep count of the number of exp. fragment nodes.
			for (ExperienceFragmentBean expfragDto : expfragList) {
				/**
				 * Adding new experience fragment component node.
				 */
				Node expfragNodeNew = respGridNode.addNode(EXPERIENCE_FRAGMENT_PREFIX + i, CortevaConstant.NT_UNSTRUCTURED);
				/**
				 * Setting the experience fragment node properties.
				 */
				expfragNodeNew.setProperty("fragmentPath", expfragDto.getExpfragPath());
				final Calendar lastModified = Calendar.getInstance();
				lastModified.setTimeInMillis(lastModified.getTimeInMillis());
				expfragNodeNew.setProperty("jcr:lastModified", lastModified);
				expfragNodeNew.setProperty(CortevaConstant.SLING_RESOURCE_TYPE, EXP_FRAGMENT_COMP_PATH);
				i++;
				session.save(); // Saves the new nodes in the repository.
			}
		}
	}
}
