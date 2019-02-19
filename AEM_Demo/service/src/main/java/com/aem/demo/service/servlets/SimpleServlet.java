/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.demo.service.servlets;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = Servlet.class,
           property = {
                   Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.resourceTypes=" + "aemDemo/components/structure/page",
                   "sling.servlet.extensions=" + "txt"
           })
public class SimpleServlet extends SlingSafeMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2895636444879757358L;

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServlet.class);

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
