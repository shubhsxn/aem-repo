package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.corteva.core.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.TagUtil;
import com.corteva.model.component.bean.TagBean;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.corteva.core.utils.LinkUtil;
import com.day.cq.wcm.api.Page;


/**
 * The is the sling model for Eloqua Form component.
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class EloquaFormModel extends AbstractSlingModel {

    /** Logger Instantiation. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EloquaFormModel.class);

    /** The Constant PROTOCOL. */
    private static final String PROTOCOL = "protocol";

    /** The Constant SUBMIT_URL. */
    private static final String SUBMIT_URL = "submitUrl";

    /** The Constant CONFIRMATION_URL. */
    private static final String CONFIRMATION_URL = "confirmationUrl";

    /** The Constant SITE_PREFIX. */
    private static final String SITE_PREFIX = "s";

    /** The Constant ELOQUA_CONFIG_NAME. */
    private static final String ELOQUA_CONFIG_NAME = "com.corteva.core.configurations.EloquaConfigurationService";

    /** The sling request. */
    @Inject
    private SlingHttpServletRequest slingRequest;

    /** The resource resolver. */
    @Inject
    private ResourceResolver resourceResolver;

    /** The base config. */
    @Inject
    private BaseConfigurationService baseConfig;

    /** The roles path */
    @Inject
    @Optional
    @Via("resource")
    @Named("rolesPath")
    private String rolesPath;


    /**
     * Gets the countries list.
     *
     * @return the countries list
     */
    public List<TagBean> getCountriesList() {
        return getTagsTitles(baseConfig.getPropConfigValue(slingRequest, "countryTags", EloquaFormModel.ELOQUA_CONFIG_NAME), false);

    }

    /**
     * Gets the roles list.
     *
     * @return the roles list
     */
    public List<TagBean> getRolesList() {
        return StringUtils.isNotBlank(rolesPath) ? getTagsTitles(rolesPath, true) : null;
    }

    /**
     * Gets the country codes.
     *
     * @return the country codes
     */
    public Set<String> getCountryCodes() {
        Set<String> countryCodes = new LinkedHashSet<>();
        for (final TagBean tag : getCountriesList()) {
            if (StringUtils.isNotBlank(tag.getDescription())) {
                countryCodes.add(tag.getDescription());
            }
        }
        return countryCodes;
    }

    /**
     * Gets the country states list json.
     *
     * @return the country states list json
     */
    public String getCountryStatesListJson() {
        LOGGER.debug("Inside getCountryStatesListJson method ");
        final ObjectMapper mapper = new ObjectMapper();
        String listJson = null;
        try {
            listJson = mapper.writeValueAsString(getCountriesList());
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception occurred in getCountryStatesListJson method" + e);
        }
        LOGGER.debug("Exiting from getCountryStatesListJson() method with parameter :: {}", listJson);
        return listJson;

    }

    /**
     * Gets the tags titles.
     *
     * @param tagPath the tag path
     * @param includeOnlyLocalizedTags flag to determine whether to only include tags where a localized title is found
     * @return the tags titles
     */
    public List<TagBean> getTagsTitles(String tagPath, boolean includeOnlyLocalizedTags) {

        LOGGER.debug("Inside getTagsTitles method ");
        final List<TagBean> listTagTitles = new ArrayList<>();
        final TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        final List<Tag> tags = TagUtil.listChildren(tagManager, tagPath);

        for (final Tag tag : tags) {
            TagBean tagBean = processTags(tag, includeOnlyLocalizedTags);

            if (tagBean != null) {
                listTagTitles.add(tagBean);
            }
        }
        return listTagTitles;

    }

    /**
     * Process tags.
     *
     * @param tag the tag
     * @param includeOnlyLocalizedTags flag to determine whether to only include tags where a localized title is found
     * @return the tag bean
     */
    private TagBean processTags(final Tag tag, boolean includeOnlyLocalizedTags) {
        if (includeOnlyLocalizedTags) {
            Locale locale = CommonUtils.getFullLocale(slingRequest);
            String localizedTitle = tag.getLocalizedTitle(locale);

            // Short circuit if localized title is not found and locale is not English
            if (locale == null || StringUtils.isBlank(localizedTitle)
                    && !StringUtils.equalsIgnoreCase(locale.getLanguage(), CortevaConstant.EN)) {
                return null;
            }
        }
        final TagBean tagDTO = new TagBean();
        tagDTO.setTitle(tag.getTitle());
        tagDTO.setLocalizedTitle(CommonUtils.getTagLocalizedTitle(slingRequest, tag));
        tagDTO.setValue(tag.getName());

        if (StringUtils.isNotBlank(tag.getDescription())) {
            tagDTO.setDescription(tag.getDescription());
        }
        if (null != TagUtil.listChildren(tag)) {
            final List<TagBean> listChildTagTitles = new ArrayList<>();
            for (final Tag childTag : TagUtil.listChildren(tag)) {
                listChildTagTitles.add(processTags(childTag, includeOnlyLocalizedTags));
            }
            tagDTO.setChildTags(listChildTagTitles);
        }

        LOGGER.debug("Exiting from processTags method with parameter :: {}", tag);
        return tagDTO;
    }

    /**
     * Gets the global submit url for form submission.
     *
     * @return the global submit url
     */
    public String getGlobalSubmitUrl() {
        return getFormSubmitUrl(getGlobalSiteId());
    }

    /**
     * Gets the North America submit url for form submission.
     *
     * @return the NA submit url
     */
    public String getNASubmitUrl() {
        return getFormSubmitUrl(getNASiteId());
    }

    /**
     * Gets the Eloqua form submit url.
     *
     * @param siteId the site id
     * @return the form submit url
     */
    public String getFormSubmitUrl(String siteId) {
        StringBuilder sb = new StringBuilder();
        String protocol = baseConfig.getPropConfigValue(slingRequest, PROTOCOL, EloquaFormModel.ELOQUA_CONFIG_NAME);
        String elqSiteId = siteId;
        String formSubmitUrl = baseConfig.getPropConfigValue(slingRequest, SUBMIT_URL,
                EloquaFormModel.ELOQUA_CONFIG_NAME);
        sb.append(protocol);
        sb.append(SITE_PREFIX);
        sb.append(elqSiteId);
        sb.append(formSubmitUrl);
        return sb.toString();
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public String getProtocol() {
        return baseConfig.getPropConfigValue(slingRequest, PROTOCOL, EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the site domain.
     *
     * @return the site domain
     */
    public String getSiteDomain() {
        return baseConfig.getPropConfigValue(slingRequest, "siteDomain", EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the global site id.
     *
     * @return the global site id
     */
    public String getGlobalSiteId() {
        return baseConfig.getPropConfigValue(slingRequest, "globalSiteId", EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the NA site id.
     *
     * @return the NA site id
     */
    public String getNASiteId() {
        return baseConfig.getPropConfigValue(slingRequest, "naSiteId", EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the email address validation pattern.
     *
     * @return the email validation pattern
     */
    public String getEmailValidationPattern() {
        return baseConfig.getPropConfigValue(slingRequest, "emailAddPattern", EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the zip validation API url.
     *
     * @return the zip validation API url
     */
    public String getZipValidationAPIUrl() {
        StringBuilder sb = new StringBuilder();
        String zipApiUrl = baseConfig.getPropConfigValue(slingRequest, "zipValidationAPIUrl",
                EloquaFormModel.ELOQUA_CONFIG_NAME);
        String siteDomain = baseConfig.getPropConfigValue(slingRequest, "siteDomain",
                EloquaFormModel.ELOQUA_CONFIG_NAME);
        return sb.append(zipApiUrl).append(siteDomain).toString();
    }

    /**
     * Gets the mobile validation pattern.
     *
     * @return the mobile validation pattern
     */
    public String getMobileValidationPattern() {
        return baseConfig.getPropConfigValue(slingRequest, "mobileValidationPattern",
                EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     *
     * @return the current page path
     */
    public String getCurrentPagePath() {
        Page currentPage = CommonUtils.getPageFromResource(resourceResolver, slingRequest.getResource());
        String currentPageURL = getResourceResolver().map(LinkUtil.getHref(currentPage.getPath()));
        if (StringUtils.isNotBlank(currentPageURL) && slingRequest.isSecure()) {
            currentPageURL = currentPageURL.replace(CortevaConstant.HTTP, CortevaConstant.HTTPS);
        }
        return currentPageURL;
    }

    /**
     * Gets the google captcha site key.
     *
     * @return the google captcha site key
     */
    public String getGoogleCaptchaSiteKey() {
        return baseConfig.getPropConfigValue(slingRequest, "captchaSiteKey", CortevaConstant.GLOBAL_CONFIG_NAME);
    }

    /**
     * Gets the google captcha secret key.
     *
     * @return the google captcha secret key
     */
    public String getGoogleCaptchaSecretKey() {
        return baseConfig.getPropConfigValue(slingRequest, "captchaSecretKey", CortevaConstant.GLOBAL_CONFIG_NAME);
    }

    /**
     * Gets the confirmation url
     *
     * @return the confirmationUrl
     */
    public String getConfirmationUrl() {
        return baseConfig.getPropConfigValue(slingRequest, CONFIRMATION_URL,
                EloquaFormModel.ELOQUA_CONFIG_NAME);
    }

    /**
     * Gets the locale for internationalization.
     * @return the i18nLocale
     */
    public String getLocaleForInternationalization() {
        return CommonUtils.getI18nLocale(CommonUtils.getPagePath(slingRequest), getResourceResolver());
    }

    /**
     * Gets the formatted origin name
     *
     * @return the formatted origin name
     */
    public String getOriginName() {
        String companyId = baseConfig.getPropConfigValue(slingRequest, CortevaConstant.PAGE_PROPERTY_COMPANY,
                CortevaConstant.GLOBAL_CONFIG_NAME);

        Locale locale = CommonUtils.getFullLocale(slingRequest);

        if (null != locale && StringUtils.isNotBlank(companyId)) {
            return String.format("%s_%s", companyId, locale.toString());
        }
        return StringUtils.EMPTY;
    }

}