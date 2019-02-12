package com.corteva.service.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
import com.day.cq.commons.jcr.JcrUtil;

/**
 * This servlet is used to reorder the nodes in the repository. The servlet
 * first copies all nodes that exist under a parent node to a temporary node and
 * delete nodes from under the parent node. Then the existing nodes are copied
 * back to the parent node based on the new order of tab titles provided by the
 * author. There is mapping between the node name and title provided by the
 * author.
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/reordernodes",
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.POST,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class ReorderNodesServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReorderNodesServlet.class);

	/** The Constant to hold regex. */
	private static final String REGEX = "[^a-zA-Z0-9]+";

	/** The Constant to hold underscore. */
	private static final String UNDERSCORE = "_";

	/** The Constant to store ANCHOR NAV TEMPORARY NODE NAME. */
	private static final String ANCHOR_NAV_TEMP_NODE = "anchorTempNodes";
	
	/** The base service. */
	private transient BaseConfigurationService configService;

	/**
	 * Bind base configuration service.
	 *
	 * @param configService
	 *            the configService
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService configService) {
		this.configService = configService;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param configService
	 *            the configService
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService configService) {
		this.configService = configService;
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
		if (configService != null) {
			featureFlag = configService.getToggleInfo("reorderNodesServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			String[] anchorLinkLabels = request.getParameterValues("anchorLinkLabelHidden");
			String anchorNavResourcePath = request.getParameter("anchorNavResourcePath");
			anchorNavResourcePath = StringUtils.replace(anchorNavResourcePath, CortevaConstant.JCR_UNDERSCORE_CONTENT,
					CortevaConstant.JCR_CONTENT);
			LOGGER.debug("Anchor Navigation Component Resource Path of Reorder Nodes Servlet :: {}",
					anchorNavResourcePath);
			List<String> anchLinkLabelList = new ArrayList<>();
			if (null != anchorLinkLabels && anchorLinkLabels.length > 0) {
				for (String anchorLinkLabel : anchorLinkLabels) {
					anchLinkLabelList.add(anchorLinkLabel.replaceAll(REGEX, UNDERSCORE));
				}
			}
			LOGGER.debug("Anchor Navigation List of Reorder Nodes Servlet :: {}", anchLinkLabelList);
			/**
			 * Reorders the content nodes in the repository.
			 */
			if (!anchLinkLabelList.isEmpty() && StringUtils.isNotBlank(anchorNavResourcePath)) {
				reorderNodes(request, anchLinkLabelList, anchorNavResourcePath);
			}
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}

	}

	/**
	 * Method that first deletes child nodes of anchor navigation and then creates
	 * new nodes in crx.
	 *
	 * @param request
	 *            the request
	 * @param anchLinkDtoList
	 *            the anch link dto list
	 * @param anchorNavResourcePath
	 *            the anchor nav resource path
	 */
	private void reorderNodes(SlingHttpServletRequest request, List<String> anchLinkDtoList,
			String anchorNavResourcePath) {
		LOGGER.debug("Inside reorderNodes() method of Reorder Nodes Servlet");

		final ResourceResolver resolver = request.getResourceResolver();
		final Session session = resolver.adaptTo(Session.class);
		String[] compResPathSplit = StringUtils.split(anchorNavResourcePath, CortevaConstant.FORWARD_SLASH);
		String compnodeName = compResPathSplit[compResPathSplit.length - 1];
		try {
			if (null != session && session.isLive() && session.nodeExists(anchorNavResourcePath)) {
				Node anchNavNode = session.getNode(anchorNavResourcePath);
				String cmpnodeName = anchNavNode.getName();
				LOGGER.debug("Anchor Navigation Node Name :: {}", cmpnodeName);
				if (StringUtils.equalsIgnoreCase(compnodeName, cmpnodeName)) {
					Iterable<Node> anchorNavChildNodes = JcrUtils.getChildNodes(anchNavNode);
					/**
					 * Adding a temporary responsive grid node.
					 */
					Node respGridNode = JcrUtils.getOrAddNode(anchNavNode, ANCHOR_NAV_TEMP_NODE,
							CortevaConstant.NT_UNSTRUCTURED);
					/**
					 * Setting the property for temporary responsive grid node.
					 */
					respGridNode.setProperty(CortevaConstant.SLING_RESOURCE_TYPE,
							CortevaConstant.RESPONSIVE_GRID_RESOURCE);
					for (Node childNode : anchorNavChildNodes) {
						if (!StringUtils.equalsIgnoreCase(childNode.getName(), ANCHOR_NAV_TEMP_NODE)) {
							/**
							 * Copies the content nodes from anchor navigation component to temporary
							 * responsive grid node.
							 */
							JcrUtil.copy(childNode, respGridNode, childNode.getName().replaceAll(REGEX, UNDERSCORE));
							/**
							 * Removes the content node from Anchor Navigation component node.
							 */
							childNode.remove();
						}
					}
					/**
					 * Creating New Nodes under Anchor Navigation Component.
					 */
					createNewNodes(anchLinkDtoList, session, anchNavNode, respGridNode);
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error("RepositoryException occurred in createNewNodes() method of ReorderNodesServlet", e);
		} finally {
			CommonUtils.closeResolverSession(resolver, session);
		}
	}

	/**
	 * Method to create anchor navigation child nodes in crx.
	 *
	 * @param anchLinkBeanList
	 *            the anch link bean list
	 * @param session
	 *            the session
	 * @param anchNavNode
	 *            the anchor navigation component node
	 * @param respGridNode
	 *            the temporary anchor nav responsive grid node
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void createNewNodes(List<String> anchLinkBeanList, final Session session, Node anchNavNode,
			Node respGridNode) throws RepositoryException {
		LOGGER.debug("Inside createNewNodes() method of Reorder Nodes Servlet");
		Iterable<Node> childNodes = JcrUtils.getChildNodes(respGridNode);
		for (String anchLabel : anchLinkBeanList) {
			for (Node anchChildNode : childNodes) {
				if (StringUtils.equalsIgnoreCase(anchChildNode.getName(), anchLabel)) {
					/**
					 * Copies the content from temporary responsive grid to the anchor navigation
					 * component node.
					 */
					JcrUtil.copy(anchChildNode, anchNavNode, anchChildNode.getName());
					anchChildNode.remove(); // Removes the content from temporary responsive grid node.
				}
			}
		}
		/**
		 * Removes the temporary responsive grid node, after all the content is copied
		 * to the anchor navigation component node.
		 */
		respGridNode.remove();
		session.save(); // Saves the nodes in the repository.
	}
}