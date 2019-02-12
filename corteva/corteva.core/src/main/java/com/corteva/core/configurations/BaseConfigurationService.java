/*
 * =========================================================================== 
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.
 
 * ===========================================================================
 */
package com.corteva.core.configurations;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;

/**
 * The is the Base Configuration Service class. This service should be injected
 * in all the classes where we need to read any fields from OSGI configuration
 * like site root-path, DAM root-path etc.
 * 
 * @author Sapient
 */
@Component(name = "Corteva Base Configuration Service", service = BaseConfigurationService.class, immediate = true)
public class BaseConfigurationService {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseConfigurationService.class);

	/** The Configuration Admin. */
	private ConfigurationAdmin configAdmin;
	
	/** The property value. */
	private String propValue;

	/**
	 * Bind config admin service.
	 *
	 * @param configAdmin
	 *            the config admin
	 */
	@Reference
	public void bindConfigAdminService(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	/**
	 * Unbind config admin service.
	 *
	 * @param configAdmin
	 *            the config admin
	 */
	public void unbindConfigAdminService(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	/**
	 * This method is used to get the property value from the OSGI configuration.
	 *
	 * @param countryLangMap
	 *            the country language region map.
	 * @param property
	 *            the property that needs to be read from the OSGI Configuration.
	 * @param configPid
	 *            the configuration persistent identity.
	 * @return the property value after reading from the OSGI configuration.
	 */
	public String getPropertyValueFromConfiguration(Map<String, String> countryLangMap, String property,
			String configPid) {
		LOGGER.debug("Inside getPropertyValueFromConfiguration() method");
		propValue = StringUtils.EMPTY;
		if (null != countryLangMap && !countryLangMap.isEmpty()) {
			
			String brandSuffix = getSuffix(countryLangMap, CortevaConstant.BRAND);
			String regionSuffix = getSuffix(countryLangMap, CortevaConstant.REGION);
			String countrySuffix = getSuffix(countryLangMap, CortevaConstant.COUNTRY);
			String languageSuffix = getSuffix(countryLangMap, CortevaConstant.LANGUAGE);
			
			String brandPid = configPid + brandSuffix;
			String brandRegCountLangPid = configPid + languageSuffix + countrySuffix + regionSuffix + brandSuffix;
			String regLangCountryConfigPid = configPid + languageSuffix + countrySuffix + regionSuffix;
			String countryLangConfigPid = configPid + languageSuffix + countrySuffix;
			String langConfigPid = configPid + languageSuffix;
			
			try {
				/**
				 * Condition to get property value from configuration pid for brands other than
				 * Corteva.
				 */
				if (!StringUtils.equalsIgnoreCase(CortevaConstant.CORTEVA, countryLangMap.get(CortevaConstant.BRAND))) {
					propValue = getPropValueFromConfiguration(brandRegCountLangPid, property);
					if (StringUtils.isBlank(propValue)) {
						LOGGER.error("NOT EQUAL CORTEVA {}", brandPid);
						propValue = getPropValueFromConfiguration(brandPid, property);
					}
				}
				/**
				 * Condition to get property value from configuration pid for Corteva as well as
				 * fallback for other brands.
				 */
				if (StringUtils.isBlank(propValue)) {
					propValue = getPropValue(property, configPid, regLangCountryConfigPid, countryLangConfigPid,
							langConfigPid);
				}
			} catch (IOException e) {
				LOGGER.error("IO Exception occurred in getPropertyValueFromConfiguration()", e);
			}
		}
		LOGGER.debug("Property Value from configurationm file :: {}", propValue);
		return propValue;
	}
	
	
	/**
	 * @param countryLangMap
	 * 			the country language region map.
	 * @param key
	 * 			the key in countryLangMap
	 * @return keySuffix
	 * 				the suffix for config pid
	 */
	private String getSuffix(Map<String, String> countryLangMap, String key) {
		String keySuffix = StringUtils.EMPTY;
		boolean hasKey = countryLangMap.containsKey(key)
				&& StringUtils.isNotBlank(countryLangMap.get(key));
		if (hasKey) {
			keySuffix = CortevaConstant.UNDERSCORE + countryLangMap.get(key);
		}
		return keySuffix;
	}
	

	/**
	 * Gets the prop value.
	 *
	 * @param property
	 *            the property
	 * @param configPid
	 *            the config pid
	 * @param regLangCountryConfigPid
	 *            the reg lang country config pid
	 * @param countryLangConfigPid
	 *            the country lang config pid
	 * @param langConfigPid
	 *            the lang config pid
	 * @return the prop value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String getPropValue(String property, String configPid, String regLangCountryConfigPid,
			String countryLangConfigPid, String langConfigPid) throws IOException {
		LOGGER.debug("Inside getPropValue() method");
		propValue = getPropValueFromConfiguration(regLangCountryConfigPid, property);
		if (StringUtils.isBlank(propValue)) {
			propValue = getPropValueFromConfiguration(countryLangConfigPid, property);
			if (StringUtils.isBlank(propValue)) {
				propValue = getPropValueFromConfiguration(langConfigPid, property);
				if (StringUtils.isBlank(propValue)) {
					propValue = getPropValueFromConfiguration(configPid, property);
				}
			}
		}
		LOGGER.debug("Property Value :: {}", propValue);
		return propValue;
	}

	/**
	 * Gets the prop value from configuration.
	 *
	 * @param configPid
	 *            the config pid
	 * @param property
	 *            the property
	 * @return the prop value from configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getPropValueFromConfiguration(String configPid, String property) throws IOException {
		LOGGER.debug("Inside getPropValFromConfiguration() method.");
		propValue = StringUtils.EMPTY;
		Configuration config = getConfiguration(configPid);
		if (null != config) {
			propValue = getProperty(config, property);
		}
		return propValue;
	}

	/**
	 * This method gets the configuration based configuration PID.
	 *
	 * @param configPid
	 *            the configuration PID
	 * @return the configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Configuration getConfiguration(String configPid) throws IOException {
		return configAdmin.getConfiguration(configPid);
	}

	/**
	 * This method gets the value of an OSGi configuration string property for a
	 * given PID.
	 *
	 * @param config
	 *            the configuration
	 * @param property
	 *            the property of the configuration to retrieve
	 * @return the property value
	 */
	private String getProperty(final Configuration config, final String property) {
		LOGGER.debug("Inside getProperty() method.");
		propValue = StringUtils.EMPTY;
		Dictionary<String, Object> props = config.getProperties();
		if (null != props) {
			propValue = PropertiesUtil.toString(props.get(property), "");
		}
		return propValue;
	}

	/**
	 * This method gets the property value from given config PID.
	 *
	 * @param slingRequest
	 *            the sling request
	 * @param propertyName
	 *            the property name
	 * @param configPid
	 *            the config pid
	 * @return the prop config value
	 */
	public String getPropConfigValue(SlingHttpServletRequest slingRequest, String propertyName, String configPid) {
		return getPropertyValueFromConfiguration(CommonUtils.getRegionCountryLanguage(
				CommonUtils.getPagePath(slingRequest), slingRequest.getResourceResolver()), propertyName, configPid);
	}

	/**
	 * This method gets the feature flag toggle information for the component.
	 *
	 * @param componentName
	 *            the component name
	 * @param configPid
	 *            the configuration persistent identity
	 * @return the feature flag toggle state
	 */
	public boolean getToggleInfo(String componentName, String configPid) {
		LOGGER.debug("Inside getToggleInfo() method.");
		boolean featureFlagToggle = true;
		if (StringUtils.isNotBlank(componentName)) {
			try {
				propValue = getPropValueFromConfiguration(configPid, componentName);
				LOGGER.debug("Flag state for {} is {}.", componentName, propValue);
				if (StringUtils.isNotBlank(propValue)) {
					featureFlagToggle = Boolean.parseBoolean(propValue);
				}
			} catch (IOException e) {
				LOGGER.error("IO Exception occurred in getToggleInfo()", e);
			}
		}
		return featureFlagToggle;
	}

	/**
	 * Gets the prop values from configuration.
	 * 
	 * @param configPid
	 *            the config pid
	 * @param property
	 *            the property
	 * @return the prop values from configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String[] getPropValuesFromConfiguration(String configPid, String property) throws IOException {
		LOGGER.debug("Inside getPropValFromConfiguration() method.");
		String[] propertyVal = null;
		Configuration config = getConfiguration(configPid);
		if (null != config) {
			propertyVal = getPropertys(config, property);
		}
		return propertyVal;
	}

	/**
	 * Gets the propertys.
	 * 
	 * @param config
	 *            the config
	 * @param property
	 *            the property
	 * @return the propertys
	 */
	private String[] getPropertys(final Configuration config, final String property) {
		LOGGER.debug("Inside getProperty() method.");
		String[] propertyValue = null;
		Dictionary<String, Object> props = config.getProperties();
		if (null != props) {
			propertyValue = PropertiesUtil.toStringArray(props.get(property));
		}
		return propertyValue;
	}
}
