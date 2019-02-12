/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */

package com.corteva.core.mail;

import java.util.List;
import java.util.Map;

import javax.jcr.Node;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * The Interface MailService.
 */
public interface MailService {

	/**
	 * Send mail.
	 *
	 * @param emailList
	 *            the email list
	 * @param properties
	 *            the properties
	 * @param templatePath
	 *            the template path
	 */
	void sendMail(List<String> emailList, Map<String, Object> properties, String templatePath);

	/**
	 * Gets the node from resource.
	 *
	 * @param resourcePath
	 *            the resource path
	 * @return the node from resource
	 */
	Node getNodeFromResource(String resourcePath);

	/**
	 * Gets the resource resolver.
	 *
	 * @return the resource resolver
	 */
	ResourceResolver getResourceResolver();

}
