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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;

/**
 * This servlet reads the (inherited) metadataschema property of dam folder path
 * passed in request
 * 
 * @author Sapient
 */
@Component(immediate = true, service = Servlet.class, property = {
		CortevaConstant.SLING_SERVLET_PATHS + "/bin/corteva/getschematype",
		CortevaConstant.SLING_SERVLET_METHODS + CortevaConstant.POST,
		CortevaConstant.SLING_SERVLET_EXTENSIONS + CortevaConstant.JSON })
public class GetMetadataSchemaServlet extends AbstractServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GetMetadataSchemaServlet.class);

	/** This Constant holds the metadata schema property for dam folders */
	private static final String METADATA_SCHEMA_PROPERTY = "metadataSchema";

	/** This Constant holds the parameter for dam folder path */
	private static final String DAM_FOLDER_PATH = "folderPath";

	/** The base service. */
	private transient BaseConfigurationService baseServiceConfig;

	/**
	 * Bind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.baseServiceConfig = baseServiceConfig;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseServiceConfig
	 *            the base service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseServiceConfig) {
		this.baseServiceConfig = baseServiceConfig;
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
		if (baseServiceConfig != null) {
			featureFlag = baseServiceConfig.getToggleInfo("getMetadataSchemaServlet",
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			if (StringUtils.isNotBlank(request.getParameter(DAM_FOLDER_PATH))) {
				getSchemaType(request, response);
			}

		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * This method fetches dam folder schema type
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             the IO Exception
	 */
	private void getSchemaType(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {

		String damFolderPath = request.getParameter(DAM_FOLDER_PATH);
		String metadataSchema = StringUtils.EMPTY;
		String metadataSchemaType = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(damFolderPath)) {

			Resource folderResource = request.getResourceResolver().resolve(damFolderPath);
			Resource jcrResource = null;
			if (!ResourceUtil.isNonExistingResource(folderResource)) {
				jcrResource = folderResource.getChild(CortevaConstant.JCR_CONTENT);
				if (null != jcrResource) {
					metadataSchema = CommonUtils.getInheritedProperty(METADATA_SCHEMA_PROPERTY,
							jcrResource);
				}
			}
			LOGGER.debug("Metadata Schema is {}", metadataSchema);

			if (StringUtils.isNotBlank(metadataSchema) && metadataSchema.contains(CortevaConstant.FORWARD_SLASH)) {
				metadataSchemaType = metadataSchema.substring(metadataSchema.lastIndexOf(CortevaConstant.FORWARD_SLASH) + 1);
			}
			Map<String, String> schemaPropMap = new HashMap<>();
			schemaPropMap.put(METADATA_SCHEMA_PROPERTY, metadataSchemaType);

			LOGGER.debug("Metadata Schema Type is {}", metadataSchemaType);

			response.setContentType(CortevaConstant.CONTENT_TYPE_JSON);
			response.setCharacterEncoding(CortevaConstant.CHARACTER_ENCODING_UTF_8);
			response.getWriter().write(sendResponse(schemaPropMap));
		}
	}

}