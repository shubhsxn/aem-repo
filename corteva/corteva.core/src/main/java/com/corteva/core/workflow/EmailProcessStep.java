/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.workflow;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;

/**
 * The Class EmailProcessStep.
 */
@Component(service = WorkflowProcess.class, immediate = true, property = { "process.label = Email Process Step" })
public class EmailProcessStep implements WorkflowProcess {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailProcessStep.class);

	/** The Constant USER_PROPERTY_EMAIL_ADDRESS. */
	private static final String USER_PROPERTY_EMAIL_ADDRESS = "profile/email";

	/** The Constant USER_PROPERTY_FAMILY_NAME. */
	private static final String USER_PROPERTY_FAMILY_NAME = "profile/familyName";

	/** The Constant USER_PROPERTY_GIVEN_NAME. */
	private static final String USER_PROPERTY_GIVEN_NAME = "profile/givenName";

	/**
	 * The Constant is used to get process arguments at 0 index for workflow process
	 */
	private static final int ZERO = 0;

	/** The Constant ABSOLUTE_DATE_FORMAT. */
	private static final String ABSOLUTE_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	/** The Constant to hold jcr date timeformat */
	private static final String JCR_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/** The Constant REVIEWER. */
	private static final String REVIEWER = "reviewer";

	/** The Constant PUBLISHER. */
	private static final String PUBLISHER = "publisher";

	/** The mail service. */
	private MailService mailService;

	/** The base config service. */
	private BaseConfigurationService baseConfigService;

	/**
	 * Bind mail service.
	 *
	 * @param mailService
	 *            the mail service
	 */
	@Reference
	public void bindMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Unbind mail service.
	 *
	 * @param mailService
	 *            the mail service
	 */
	public void unbindMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Bind base configuration service.
	 *
	 * @param baseConfigService
	 *            the base config service
	 */
	@Reference
	public void bindBaseConfigurationService(BaseConfigurationService baseConfigService) {
		this.baseConfigService = baseConfigService;
	}

	/**
	 * Unbind base configuration service.
	 *
	 * @param baseConfigService
	 *            the base config service
	 */
	public void unbindBaseConfigurationService(BaseConfigurationService baseConfigService) {
		this.baseConfigService = baseConfigService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.adobe.granite.workflow.exec.WorkflowProcess#execute(com.adobe.granite.
	 * workflow.exec.WorkItem, com.adobe.granite.workflow.WorkflowSession,
	 * com.adobe.granite.workflow.metadata.MetaDataMap)
	 */
	@Override
	public void execute(WorkItem workItem, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		LOGGER.info("entering execute method of custom email process step");
		String templatePath = "";
		ResourceResolver resolver = session.adaptTo(ResourceResolver.class);
		List<String> processArgs = Arrays.asList(
				metaDataMap.get(CortevaConstant.WORKFLOW_METADATA_PROCESS_ARGS, "").split(";"));
		// this method is called to get the user/groups set in process arguments to
		// which mail need to be sent
		List<String> receiverList = getReceiverList(processArgs, workItem);
		// this method is called to get the email address from receiver list
		List<String> emailList = this.populateEmailList(receiverList, workItem);
		// email template path is being received from process arguments
		for (int i = 0; i < processArgs.size(); i++) {
			templatePath = processArgs.get(ZERO).substring(
					processArgs.get(ZERO).indexOf(CortevaConstant.EQUAL) + CortevaConstant.ONE);
		}
		// this method is called to get value of variables which needs to be replaced in
		// email body
		Map<String, Object> propertiesMap = getMailVariables(workItem, resolver);
		LOGGER.debug("calling sendMail method of mailservice");
		mailService.sendMail(emailList, propertiesMap, templatePath);
		LOGGER.info("exiting execute method of custom email process step");

	}

	/**
	 * method to create map to replace place holder variable in email body.
	 *
	 * @param workItem
	 *            the work item
	 * @param resolver
	 *            the resolver
	 * @return the mail variables
	 */
	private Map<String, Object> getMailVariables(WorkItem workItem, ResourceResolver resolver) {
		Map<String, Object> paramMap = new HashMap<>();
		String payload = workItem.getWorkflowData().getPayload().toString();
		LOGGER.debug("payload on which workflow is called {}", payload);
		Node workItemNode = mailService.getNodeFromResource(payload);
		if (workItemNode != null) {
			String link = getLink(workItemNode, resolver);
			String absoluteDate = getAbsoluteDate();
			String name = getAuthorName(workItem);
			try {
				paramMap.put(CortevaConstant.PAYLOAD_LINK, link);
				paramMap.put(CortevaConstant.PAYLOAD_TITLE, workItemNode.getName());
				paramMap.put(CortevaConstant.ABSOLUTE_DATE, absoluteDate);
				paramMap.put(CortevaConstant.AUTHOR_NAME, name);
			} catch (RepositoryException repositoryException) {
				LOGGER.error("repostory exception occurred in getMailVariables method", repositoryException);
			}
		}
		return paramMap;

	}

	/**
	 * Gets the absolute date.
	 *
	 * @return the absolute date
	 */
	private String getAbsoluteDate() {
		String absoluteDate = getClientDate(new SimpleDateFormat(EmailProcessStep.JCR_DATE_TIME_FORMAT)
						.format(Calendar.getInstance().getTime()));
			LOGGER.debug("property found on workflow metadata {}", absoluteDate);

		return absoluteDate;
	}

	/**
	 * Gets the client date.
	 *
	 * @param absoluteDate
	 *            the absolute date
	 * @return the client date
	 */
	private String getClientDate(String absoluteDate) {
		ZonedDateTime zDateTime = ZonedDateTime.parse(absoluteDate, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(EmailProcessStep.ABSOLUTE_DATE_FORMAT);
		return timeFormatter.format(zDateTime);
	}

	/**
	 * Gets the link.
	 *
	 * @param workItemNode
	 *            the work item node
	 * @param resolver
	 *            the resolver
	 * @return the link
	 */
	private String getLink(Node workItemNode, ResourceResolver resolver) {
		StringBuilder link = new StringBuilder();
		try {
			String payload = workItemNode.getPath();
			String hostPrefix = baseConfigService.getPropertyValueFromConfiguration(
					CommonUtils.getRegionCountryLanguage(payload, resolver), CortevaConstant.HOST_PREFIX,
					CortevaConstant.GLOBAL_CONFIG_NAME);
			payload = LinkUtil.getHref(payload);
			link.append(hostPrefix);
			link.append("/");
			link.append("editor.html");
			link.append(payload);
		} catch (RepositoryException repositoryException) {
			LOGGER.error("repostory exception occurred in getMailVariables method", repositoryException);
		}
		return link.toString();
	}

	/**
	 * this method fetch the email from group or user and set that in list.
	 *
	 * @param receiverList
	 *            the receiver list
	 * @param item
	 *            the item
	 * @return the list
	 */
	private List<String> populateEmailList(final List<String> receiverList, WorkItem item) {
		LOGGER.info("entering the populateEmailList method");
		ResourceResolver resourceResolver = mailService.getResourceResolver();
		List<String> emailList = new ArrayList<>();
		Authorizable auth = null;
		if (null != resourceResolver) {
			// getting the user manager to fetch email
			UserManager manager = resourceResolver.adaptTo(UserManager.class);
			try {
				if (null != manager) {
					// if condition is being called when user/group set in process arguments and
					// else is called when mail need to be sent on initiator
					if (null != receiverList && receiverList.size() > 0) {
						createEmailList(receiverList, emailList, manager);

					} else {
						auth = manager.getAuthorizable(item.getWorkflow().getInitiator());
						LOGGER.debug("authId of a initiator {}", item.getWorkflow().getInitiator());
						this.addUserEmailToList(emailList, auth);
					}
				}
			} catch (AccessDeniedException accessDeniedException) {
				LOGGER.error("accessDeniedException occurred in populateEmailList method", accessDeniedException);
			} catch (UnsupportedRepositoryOperationException unsupportedRepoOpEx) {
				LOGGER.error("UnsupportedRepositoryOperationException occurred in populateEmailList method",
						unsupportedRepoOpEx);
			} catch (RepositoryException repoException) {
				LOGGER.error("RepositoryException occurred in populateEmailList method", repoException);
			}
		}

		LOGGER.info("exiting the populateEmailList method");
		return emailList;
	}

	/**
	 * Create email list.
	 *
	 * @param receiverList
	 *            the receiver list
	 * @param emailList
	 *            the email list
	 * @param manager
	 *            the manager
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private void createEmailList(final List<String> receiverList, List<String> emailList, UserManager manager)
			throws RepositoryException {
		String authId;
		Authorizable auth;
		for (int i = 0; i < receiverList.size(); i++) {
			authId = receiverList.get(i);
			if (null != manager) {
				auth = manager.getAuthorizable(authId);
				if (auth.isGroup()) {
					LOGGER.debug("calling the method to fetch email in case of group");
					addGroupEmailsToList(emailList, auth);
				} else {
					LOGGER.debug("calling the method to fetch email in case of user");
					this.addUserEmailToList(emailList, auth);
				}
			}
		}
	}

	/**
	 * this method add email of all user of group in list.
	 *
	 * @param emailList
	 *            the email list
	 * @param auth
	 *            the auth
	 */
	private void addGroupEmailsToList(final List<String> emailList, final Authorizable auth) {
		LOGGER.info("entering the addGroupEmailsToList method");
		try {
			boolean isGroupMailAdded = this.addUserEmailToList(emailList, auth);
			if (!isGroupMailAdded) {
				Iterator<Authorizable> users;
				// getting member of a group
				users = ((Group) auth).getMembers();
				while (users.hasNext()) {
					Authorizable authReceiver = users.next();
					// if member is group again then call method to add group email else call method
					// to add user's email
					if (authReceiver.isGroup()) {
						this.addGroupEmailsToList(emailList, authReceiver);
					} else {
						this.addUserEmailToList(emailList, authReceiver);
					}
				}
			}

		} catch (RepositoryException repoException) {
			LOGGER.error("RepositoryException occurred in addGroupEmailsToList method", repoException);
		}
		LOGGER.info("exiting the addGroupEmailsToList method");
	}

	/**
	 * Adds the user email to list.
	 *
	 * @param emailList
	 *            the email list
	 * @param auth
	 *            the auth
	 * @return true, if successful
	 * @throws RepositoryException
	 *             the repository exception
	 */
	private boolean addUserEmailToList(final List<String> emailList, final Authorizable auth)
			throws RepositoryException {
		LOGGER.info("entering the addUserEmailToList method");
		boolean emailAdded = false;
		Value[] emails = auth.getProperty(USER_PROPERTY_EMAIL_ADDRESS);
		if (emails != null && emails.length > 0) {
			for (int j = 0; j < emails.length; j++) {
				emailList.add(emails[j].getString());
			}
			emailAdded = true;
		}
		LOGGER.info("exiting the addUserEmailToList method");
		return emailAdded;
	}

	/**
	 * Gets the author name.
	 *
	 * @param item
	 *            the item
	 * @return the author name
	 */
	private String getAuthorName(WorkItem item) {
		LOGGER.info("entering the getAuthorName method");
		ResourceResolver resourceResolver = mailService.getResourceResolver();
		StringBuilder name = new StringBuilder();
		if (null != resourceResolver) {
			// getting the user manager to fetch email
			UserManager manager = resourceResolver.adaptTo(UserManager.class);
			try {
				if (null != manager) {
					Authorizable auth = manager.getAuthorizable(item.getWorkflow().getInitiator());
					LOGGER.debug("authId of a initiator {}", item.getWorkflow().getInitiator());
					String firstName = StringUtils.EMPTY;
					if (auth.hasProperty(USER_PROPERTY_GIVEN_NAME)) {
						firstName = auth.getProperty(USER_PROPERTY_GIVEN_NAME)[0].getString();
					}
					LOGGER.debug("firstName of a initiator {}", firstName);
					String lastName = auth.getProperty(USER_PROPERTY_FAMILY_NAME)[0].getString();
					LOGGER.debug("lastName of a initiator {}", lastName);
					if (StringUtils.isNotBlank(firstName)) {
						name.append(firstName);
						name.append(CortevaConstant.SPACE);
					}
					name.append(lastName);
				}

			} catch (RepositoryException repoException) {
				LOGGER.error("RepositoryException occurred in getAuthorName method", repoException);
			}
		}
		LOGGER.info("exiting the getAuthorName method");
		return name.toString();
	}

	/**
	 * Gets the receiver list.
	 *
	 * @param processArgs
	 *            the process args
	 * @param item
	 *            the item
	 * @return the receiver list
	 */
	private List<String> getReceiverList(List<String> processArgs, WorkItem item) {
		LOGGER.debug("Entering getReceiverList() method");
		List<String> receiverList = new ArrayList<>();
		ResourceResolver resourceResolver = mailService.getResourceResolver();
		receiverList = new ArrayList<>(receiverList);
		if (resourceResolver != null) {
			if (processArgs.size() > 1) {
				receiverList = Arrays.asList(processArgs.get(CortevaConstant.ONE).substring(
						processArgs.get(CortevaConstant.ONE).indexOf(CortevaConstant.EQUAL) + CortevaConstant.ONE)
						.split(CortevaConstant.COMMA));
				LOGGER.debug("user or group set in process arguments {}", receiverList);
			}
			Map<String, String> countryLangMap = CommonUtils.getRegionCountryLanguage(item.getContentPath(),
					resourceResolver);
			// to get a group name of a reviewer for this content path
			if (receiverList.size() > 0 && receiverList.contains(EmailProcessStep.REVIEWER)) {
				receiverList = createReceiverList(countryLangMap, EmailProcessStep.REVIEWER);
				// to get a group name of a publisher for this content path
			} else if (receiverList.size() > 0 && receiverList.contains(EmailProcessStep.PUBLISHER)) {
				receiverList = createReceiverList(countryLangMap, EmailProcessStep.PUBLISHER);
			}
		}
		LOGGER.debug("Receiver List :: {}", receiverList);
		return receiverList;
	}

	/**
	 * Creates the receiver list.
	 *
	 * @param countryLangMap
	 *            the country lang map
	 * @param userGroupProperty
	 *            the user group property
	 * @return the list
	 */
	private List<String> createReceiverList(Map<String, String> countryLangMap, String userGroupProperty) {
		LOGGER.debug("Entering createReceiverList() method");
		List<String> receiverList = new ArrayList<>();
		if (!countryLangMap.isEmpty()) {
			String receiver = baseConfigService.getPropertyValueFromConfiguration(countryLangMap, userGroupProperty,
					CortevaConstant.USER_CONFIG_NAME);
			boolean hasBrandRegCountLang = countryLangMap.containsKey(CortevaConstant.BRAND)
					&& countryLangMap.containsKey(CortevaConstant.REGION)
					&& countryLangMap.containsKey(CortevaConstant.COUNTRY)
					&& countryLangMap.containsKey(CortevaConstant.LANGUAGE);
			if (StringUtils.isNotBlank(receiver)) {
				receiverList = Arrays.asList(receiver);
			} else if (hasBrandRegCountLang) {
				receiverList = Arrays.asList(createFallbackReceiverList(countryLangMap, userGroupProperty).toString());
			}
		}
		LOGGER.debug("Exiting createReceiverList() method");
		return receiverList;
	}

	/**
	 * Creates the fallback receiver list.
	 *
	 * @param countryLangMap
	 *            the country lang map
	 * @param userGroupProperty
	 *            the user group property
	 * @return the string builder
	 */
	private StringBuilder createFallbackReceiverList(Map<String, String> countryLangMap, String userGroupProperty) {
		StringBuilder sb = new StringBuilder();
		if (null == countryLangMap.get(CortevaConstant.REGION)) {
			sb.append(countryLangMap.get(CortevaConstant.BRAND)).append(CortevaConstant.DASH)
					.append(CortevaConstant.CORPORATE_CONTENT_NODE_NAME).append(CortevaConstant.DASH)
					.append(userGroupProperty);
		} else {
			sb.append(countryLangMap.get(CortevaConstant.BRAND)).append(CortevaConstant.DASH)
					.append(countryLangMap.get(CortevaConstant.REGION)).append(CortevaConstant.DASH)
					.append(StringUtils.lowerCase(countryLangMap.get(CortevaConstant.COUNTRY)))
					.append(CortevaConstant.DASH).append(userGroupProperty);
		}
		return sb;
	}
}