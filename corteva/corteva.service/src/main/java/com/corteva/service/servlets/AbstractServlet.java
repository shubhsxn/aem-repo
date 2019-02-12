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

import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The is the Abstract Sing Servlet. This class should be extended by all the
 * Sling servlets.
 * 
 * @author Sapient
 */
public class AbstractServlet extends SlingAllMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2895636444879757358L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServlet.class);

	/**
	 * Serializes the response object into JSON.
	 *
	 * @param responseObject
	 *            the response object
	 * @return strResponseObj
	 */
	public static String sendResponse(final Object responseObject) {
		LOGGER.debug("Inside method: sendResponse() :: {}", responseObject);
		String strResponseObj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			strResponseObj = mapper.writeValueAsString(responseObject);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.debug("Exiting method: sendResponse() :: {}", responseObject);
		return strResponseObj;
	}
}
