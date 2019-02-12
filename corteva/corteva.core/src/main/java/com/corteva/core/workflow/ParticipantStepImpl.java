package com.corteva.core.workflow;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.mail.MailService;
import com.corteva.core.utils.CommonUtils;

/**
 * This is the dynamic participant step.
 */

@Component(service = ParticipantStepChooser.class, immediate = true, property = {
		"chooser.label = Workflow Participant Chooser" })
public class ParticipantStepImpl implements ParticipantStepChooser {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantStepImpl.class);

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
	 * com.adobe.granite.workflow.exec.ParticipantStepChooser#getParticipant(com.
	 * adobe.granite. workflow.exec.WorkItem,
	 * com.adobe.granite.workflow.WorkflowSession,
	 * com.adobe.granite.workflow.metadata.MetaDataMap)
	 */
	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		LOGGER.debug("Entering getParticipant() method of ParticipantStepImpl");
		ResourceResolver resourceResolver = mailService.getResourceResolver();
		String processArgs = metaDataMap.get(CortevaConstant.WORKFLOW_METADATA_PROCESS_ARGS, "");
		String[] roleArray = StringUtils.split(processArgs, CortevaConstant.EQUAL);
		String participant = StringUtils.EMPTY;
		Map<String, String> countryLangMap = CommonUtils.getRegionCountryLanguage(workItem.getContentPath(),
				resourceResolver);
		if (!countryLangMap.isEmpty() && roleArray.length > 1) {
			participant = baseConfigService.getPropertyValueFromConfiguration(countryLangMap, roleArray[1],
					CortevaConstant.USER_CONFIG_NAME);
			if (StringUtils.isBlank(participant) && countryLangMap.containsKey(CortevaConstant.COUNTRY)) {
				StringBuilder sb = new StringBuilder();
				sb.append(StringUtils.lowerCase(countryLangMap.get(CortevaConstant.COUNTRY)))
						.append(CortevaConstant.DASH).append(roleArray[1]);
				participant = sb.toString();
			}
		}
		LOGGER.debug("Participant :: {} ", participant);
		return participant;
	}
}