/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */

package com.corteva.core.mail.imp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.mailer.MessageGatewayService;

/**
 * The Class MailServiceImpl.
 */
@Component(service = MailService.class)
public class MailServiceImpl implements MailService {

	/** The Constant DEFAULT_LOCALE_NODE_TITLE. */
	private static final String DEFAULT_LOCALE_NODE_TITLE = "en";

	/** The Constant NODE_PROPERTY_SUBJECT. */
	private static final String NODE_PROPERTY_SUBJECT = "subject";

	/** The Constant NODE_PROPERTY_FROM. */
	private static final String NODE_PROPERTY_FROM = "from";

	/** The Constant NODE_TEXT. */
	private static final String NODE_TEXT = "text";

	/**
	 * Email from address.
	 */
	private static final String MAIL_FROM = "mail.from";
	/**
	 * Email Subject.
	 */
	private static final String MAIL_SUBJECT = "mail.subject";
	/**
	 * Email body.
	 */
	private static final String MAIL_BODY = "mail.body";

	/**
	 * Setting default subject.
	 */
	private static final String DEFAULT_SUBJECT = "No Subject";
	/**
	 * Setting default email address form which mail will be sent.
	 */
	private static final String DEFAULT_FROM_ADDRESS = "do-not-reply@corteva.com";

	/** The resource resolver factory. */
	private ResourceResolverFactory resourceResolverFactory;

	/** The message gateway service. */
	private MessageGatewayService messageGatewayService;

	/** The base configuration service. */
	private BaseConfigurationService baseService;

	/** The feature flag. */
	private boolean featureFlag = false;

	/**
	 * Bind resource resolver factory.
	 *
	 * @param resourceResolverFactory
	 *            the resource resolver factory
	 */
	@Reference
	public void bindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
		this.resourceResolverFactory = resourceResolverFactory;
	}

	/**
	 * Unbind resource resolver factory.
	 *
	 * @param resourceResolverFactory
	 *            the resource resolver factory
	 */
	public void unbindResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
		this.resourceResolverFactory = resourceResolverFactory;
	}

	/**
	 * Bind resource resolver factory.
	 *
	 * @param messageGatewayService
	 *            the message gateway service
	 */
	@Reference
	public void bindMessageGatewayService(MessageGatewayService messageGatewayService) {
		this.messageGatewayService = messageGatewayService;
	}

	/**
	 * Unbind message gateway service.
	 *
	 * @param messageGatewayService
	 *            the message gateway service
	 */
	public void unbindMessageGatewayService(MessageGatewayService messageGatewayService) {
		this.messageGatewayService = messageGatewayService;
	}

	/**
	 * Bind base configuration service.
	 *
	 * @param baseService
	 *            the base configuration service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseService) {
		this.baseService = baseService;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseService
	 *            the base configuration service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseService) {
		this.baseService = baseService;
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.corteva.core.mail.MailService#sendMail(java.lang.String[],
	 * java.util.Map, java.lang.String)
	 */
	@Override
	public void sendMail(List<String> emailList, Map<String, Object> properties, String templatePath) {
		if (baseService != null) {
			featureFlag = baseService.getToggleInfo(CortevaConstant.FEATURE_JAVA_FRAMEWORK,
					CortevaConstant.FEATURE_FLAG_CONFIG_NAME);
		}
		if (featureFlag) {
			LOGGER.info("entering sendMail Function of MailServiceImpl");
			Map<String, String> templateMap = this.getTemplateMap(templatePath, DEFAULT_LOCALE_NODE_TITLE);
			Email email = this.createEmail(emailList, properties, templateMap);
			if (this.messageGatewayService == null || this.messageGatewayService.getGateway(Email.class) == null) {
				LOGGER.error("message gateservice is not present");
			} else {
				// going to send the mail
				this.messageGatewayService.getGateway(Email.class).send(email);
			}
			LOGGER.info("exiting sendMail Function of MailServiceImpl");
		} else {
			LOGGER.debug("Feature Flag is turned off for {}", this.getClass().getName());
		}
	}

	/**
	 * Creates the email.
	 *
	 * @param emailList
	 *            the email list
	 * @param properties
	 *            the properties
	 * @param templateMap
	 *            the template map
	 * @return the email
	 */
	protected Email createEmail(final List<String> emailList, final Map<String, Object> properties,
			final Map<String, String> templateMap) {
		LOGGER.info("entering createEmail Function of MailServiceImpl");
		// fetch subject, from address, content
		String subject = templateMap.containsKey(MAIL_SUBJECT) ? templateMap.get(MAIL_SUBJECT) : "";
		String fromAddress = templateMap.containsKey(MAIL_FROM) ? templateMap.get(MAIL_FROM) : "";

		Email email = new HtmlEmail();
		// creates body of the email
		String body = templateMap.containsKey(MAIL_BODY) ? templateMap.get(MAIL_BODY) : StringUtils.EMPTY;
		// this replace the placeholder variable from email template body
		StrSubstitutor substitutor = new StrSubstitutor(properties);
		String substitutedBody = substitutor.replace(body);
		// this replace the placeholder variable from email template subject
		subject = substitutor.replace(subject);

		try {
			LOGGER.debug("going to set the email properties");
			email.setFrom(fromAddress);
			email.setSubject(subject);
			email.setMsg(substitutedBody);
			for (int emailidcount = 0; emailidcount < emailList.size(); emailidcount++) {
				email.addTo(emailList.get(emailidcount));
			}
		} catch (EmailException emailException) {
			LOGGER.error("emailException occurred in createEmail");
		}
		LOGGER.info("exiting createEmail Function of MailServiceImpl");
		return email;
	}

	/**
	 * Gets the template map.
	 *
	 * @param templatePath
	 *            the template path
	 * @param language
	 *            the language
	 * @return the template map
	 */
	private Map<String, String> getTemplateMap(final String templatePath, final String language) {
		LOGGER.info("entering into getTemplateMap method");
		Map<String, String> templateMap = new HashMap<>();
		Node content = null;
		try {
			content = getNodeFromResource(templatePath);
			LOGGER.debug("read the email template node{}", content);
			if (content != null) {
				if (language != null && content.hasNode(language)) {
					content = content.getNode(language);
				} else if (content.hasNode(DEFAULT_LOCALE_NODE_TITLE)) {
					LOGGER.debug("read language node", content);
					content = content.getNode(DEFAULT_LOCALE_NODE_TITLE);
				}
				// this block read the subject and from address language node
				String subject = content.hasProperty(NODE_PROPERTY_SUBJECT)
						? content.getProperty(NODE_PROPERTY_SUBJECT).getString()
						: DEFAULT_SUBJECT;

				String fromAddress = content.hasProperty(NODE_PROPERTY_FROM)
						? content.getProperty(NODE_PROPERTY_FROM).getString()
						: DEFAULT_FROM_ADDRESS;
				// end of block
				// going to read the mail template body
				content = content.getNode(NODE_TEXT).getNode(JcrConstants.JCR_CONTENT);

				InputStream is = content.getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
				String encoding = (content.hasProperty(JcrConstants.JCR_ENCODING))
						? content.getProperty(JcrConstants.JCR_ENCODING).getString()
						: "utf-8";
				String body = IOUtils.toString(is, encoding);

				templateMap.put(MAIL_SUBJECT, subject);
				templateMap.put(MAIL_FROM, fromAddress);
				templateMap.put(MAIL_BODY, body);
			}
		} catch (PathNotFoundException pathNotFound) {
			LOGGER.error("PathNotFoundException occured in getTemplateMap method of MailServiceImpl", pathNotFound);
		} catch (RepositoryException repositoryException) {
			LOGGER.error("RepositoryException occured in getTemplateMap method of MailServiceImpl",
					repositoryException);
		} catch (IOException ioException) {
			LOGGER.error("IOException occured in getTemplateMap method of MailServiceImpl", ioException);
		}
		LOGGER.info("exiting  getTemplateMap method");
		return templateMap;

	}

	/**
	 * Gets the node from resource.
	 *
	 * @param resourcePath
	 *            the resourcePath
	 * 
	 * @return the node
	 */
	public Node getNodeFromResource(String resourcePath) {
		LOGGER.info("entering  getNodeFromResource method");
		Node content = null;
		ResourceResolver resourceResolver = getResourceResolver();

		if (resourceResolver != null) {
			Resource resource = resourceResolver.getResource(resourcePath);
			if (resource != null) {
				content = resource.adaptTo(Node.class);
			}
		}
		LOGGER.info("exiting  getNodeFromResource method");
		return content;
	}

	/**
	 * Gets the resourceResolver from resourceresolverfactory.
	 *
	 * 
	 * @return the resourceResolver
	 */
	public ResourceResolver getResourceResolver() {
		LOGGER.info("entering  getResourceResolver method");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "mailService");

		ResourceResolver resourceResolver = null;
		if (resourceResolverFactory != null) {

			try {
				resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
			} catch (LoginException loginException) {
				LOGGER.error("LoginException occured in getTemplateMap method of MailServiceImpl", loginException);
			}
		}
		LOGGER.info("exiting  getResourceResolver method");
		return resourceResolver;

	}

}
