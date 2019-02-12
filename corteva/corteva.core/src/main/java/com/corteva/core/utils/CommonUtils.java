/*
 * ===========================================================================
 * Copyright 2018,SapientRazorfish_; All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SapientRazorfish_,
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SapientRazorfish_.

 * ===========================================================================
 */
package com.corteva.core.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.i18n.I18n;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The is a common utility class that will contain methods that will be used by
 * the entire application.
 *
 * @author Sapient
 */
public final class CommonUtils {

    /**
     * Logger Instantiation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    /** The Constant EXP_FRAGMENT_ROOT_PATH. */
    private static final String EXP_FRAGMENT_ROOT_PATH = "/content/experience-fragments/corteva";

    /** The Constant EXP_FRAGMENT. */
    private static final String EXPERIENCE_FRAGMENTS = "experience-fragments";

    /**
     * The Constant TWO.
     */
    private static final int TWO = 2;

    /**
     * The Constant THREE.
     */
    private static final int THREE = 3;

    /**
     * The Constant FOUR.
     */
    private static final int FOUR = 4;

    /**
     * The Constant FIVE.
     */
    private static final int FIVE = 5;

    /**
     * The Constant SIX.
     */
    private static final int SIX = 6;

    /**
     * The Constant SEVEN.
     */
    private static final int SEVEN = 7;

    /**
     * The Constant to hold value of property exists
     */
    private static final String PROPERTY_EXISTS = "exists";

    /**
     * The Constant GROUP_1.
     */
    private static final String GROUP_1 = "group.1";

    /**
     * The Constant GROUP_2.
     */
    private static final String GROUP_2 = "group.2";

    /**
     * This method reads through the current page URL and returns a map of
     * region, country and language.
     *
     * @param pagePath the String
     * @param resolver the resolver
     * @return the region country language map
     */
    public static Map<String, String> getRegionCountryLanguage(String pagePath, ResourceResolver resolver) {
        LOGGER.debug("Inside getRegionCountryLanguage() method");
        Map<String, String> regionCountryLangMap = new HashMap<>();
        Resource pageResource = null;
        if (StringUtils.containsIgnoreCase(pagePath, EXPERIENCE_FRAGMENTS)) {
            pageResource = resolver.getResource(pagePath);
        } else {
            pageResource = resolver.getResource(getRegionalPagPath(pagePath));
        }
        if (null != pageResource) {
            Page page = getPageFromResource(resolver, pageResource);
            if (null != page) {
                Resource pageContent = page.getContentResource();
                if (null != pageContent) {
                    regionCountryLangMap = getLangMap(pagePath, pageContent);
                }

            }
        }
        LOGGER.debug("Brand Region Country Language Map :: {}", regionCountryLangMap);
        return regionCountryLangMap;
    }

    /**
     * Gets the regional pag path.
     *
     * @param path the page path
     * @return regionalPagePath
     */
    public static String getRegionalPagPath(String path) {
        int endIndex = StringUtils.ordinalIndexOf(path, CortevaConstant.FORWARD_SLASH, CommonUtils.FOUR);
        String regionalPagePath = StringUtils.substring(path, 0, endIndex);
        LOGGER.debug("regionalPagePath:- {}", regionalPagePath);
        return regionalPagePath;
    }

    /**
     * Gets the region country language map.
     *
     * @param pagePath     the page path
     * @param pageResource the page resource
     * @return the lang map for corporate
     */
    private static Map<String, String> getLangMap(String pagePath, Resource pageResource) {
        LOGGER.debug("Inside getLangMapForCorporate() method");
        Map<String, String> regionCountryLangMap = new HashMap<>();
        ValueMap valueMap = pageResource.getValueMap();
        LOGGER.debug("valueMap in getLangMap:- {}", valueMap);
        String pageTag = valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY);
        if (StringUtils.equalsIgnoreCase(pageTag, CortevaConstant.CORPORATE_TAG)) {
            regionCountryLangMap.put(CortevaConstant.BRAND, CortevaConstant.CORTEVA);
            regionCountryLangMap.put(CortevaConstant.GLOBAL_FLAG, CortevaConstant.TRUE);
            regionCountryLangMap.put(CortevaConstant.REGION, null);
            regionCountryLangMap.put(CortevaConstant.COUNTRY, CortevaConstant.US);
            regionCountryLangMap.put(CortevaConstant.LANGUAGE, CortevaConstant.EN);
        } else {
            String[] pageNameString = StringUtils.split(pagePath, CortevaConstant.FORWARD_SLASH);
            getCountryLangMapForOthers(regionCountryLangMap, pageNameString, pageTag);
        }
        LOGGER.debug("Country Language Map :: {}", regionCountryLangMap);
        return regionCountryLangMap;
    }

    /**
     * Gets the country language map for others.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     * @param pageTag              the page tag
     */
    private static void getCountryLangMapForOthers(Map<String, String> regionCountryLangMap, String[] pageNameString,
                                                   String pageTag) {
        LOGGER.debug("Inside getCountryLangMapForOthers() method");
        if (StringUtils.equalsIgnoreCase(pageNameString[CortevaConstant.ONE], EXPERIENCE_FRAGMENTS)) {
            regionCountryLangMap.put(CortevaConstant.BRAND, pageNameString[CommonUtils.TWO]);
            createMapForExpFrag(regionCountryLangMap, pageNameString);
        } else if (StringUtils.equalsIgnoreCase(CortevaConstant.LANGUAGE_MASTER_TAG, pageTag)) {
            createMapForLanguageMaster(regionCountryLangMap, pageNameString);
        } else {
            createMapForCommercial(regionCountryLangMap, pageNameString);
        }
        LOGGER.debug("Country Language Map :: {}", regionCountryLangMap);
    }

    /**
     * Creates the map for exp frag.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForExpFrag(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        if (StringUtils.equalsIgnoreCase(CortevaConstant.EN, pageNameString[CommonUtils.THREE])) {
            createMapForExpFragCorpUs(regionCountryLangMap, pageNameString);
        } else if (StringUtils.equalsIgnoreCase(CortevaConstant.LANGUAGE_MASTERS, pageNameString[CommonUtils.THREE])) {
        	createMapForExpFragLangMasters(regionCountryLangMap, pageNameString);
        }  else {
            createMapForExpFragOthers(regionCountryLangMap, pageNameString);
        }
    }

    /**
     * Creates the map for exp frag corp us.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForExpFragCorpUs(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        regionCountryLangMap.put(CortevaConstant.REGION, null);
        regionCountryLangMap.put(CortevaConstant.COUNTRY, CortevaConstant.US);
        regionCountryLangMap.put(CortevaConstant.LANGUAGE,
                StringUtils.lowerCase(StringUtils.replace(pageNameString[CommonUtils.THREE],
                        CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, "")));
    }
    
    /**
     * Creates the map for exp frag language masters.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForExpFragLangMasters(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        regionCountryLangMap.put(CortevaConstant.REGION, StringUtils.upperCase(pageNameString[CommonUtils.THREE]));
        regionCountryLangMap.put(CortevaConstant.COUNTRY, StringUtils.EMPTY);
        regionCountryLangMap.put(CortevaConstant.LANGUAGE,
                StringUtils.lowerCase(StringUtils.replace(pageNameString[CommonUtils.FOUR],
                        CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, "")));
    }
    
    /**
     * Creates the map for exp frag others.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForExpFragOthers(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        regionCountryLangMap.put(CortevaConstant.REGION, StringUtils.upperCase(pageNameString[CommonUtils.THREE]));
        regionCountryLangMap.put(CortevaConstant.COUNTRY, StringUtils.upperCase(pageNameString[CommonUtils.FOUR]));
        regionCountryLangMap.put(CortevaConstant.LANGUAGE,
                StringUtils.lowerCase(StringUtils.replace(pageNameString[CommonUtils.FIVE],
                        CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, "")));
    }

    /**
     * Creates the map for language master.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForLanguageMaster(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        regionCountryLangMap.put(CortevaConstant.BRAND, pageNameString[CortevaConstant.ONE]);
        regionCountryLangMap.put(CortevaConstant.REGION, null);
        regionCountryLangMap.put(CortevaConstant.COUNTRY, null);
        regionCountryLangMap.put(CortevaConstant.LANGUAGE, StringUtils.replace(pageNameString[CommonUtils.THREE],
                CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, ""));
    }

    /**
     * Creates the map for commercial.
     *
     * @param regionCountryLangMap the region country lang map
     * @param pageNameString       the page name string
     */
    private static void createMapForCommercial(Map<String, String> regionCountryLangMap, String[] pageNameString) {
        regionCountryLangMap.put(CortevaConstant.BRAND, pageNameString[CortevaConstant.ONE]);
        regionCountryLangMap.put(CortevaConstant.REGION, StringUtils.upperCase(pageNameString[CommonUtils.TWO]));
        regionCountryLangMap.put(CortevaConstant.COUNTRY, StringUtils.upperCase(pageNameString[CommonUtils.THREE]));
        regionCountryLangMap.put(CortevaConstant.LANGUAGE, StringUtils.replace(pageNameString[CommonUtils.FOUR],
                CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION, ""));
    }

    /**
     * This method returns the page path from the sling http request. This
     * method should be used only for sling models.
     *
     * @param request the Sling Http Servlet request
     * @return the page path
     */
    public static String getPagePath(SlingHttpServletRequest request) {
        return request.getResource().getPath();
    }

    /**
     * This method returns the page path from the sling http request. This
     * method should be used only for sling models.
     *
     * @param request the Sling Http Servlet request
     * @return the page path
     */
    public static String getPagePathFromPageProperties(SlingHttpServletRequest request) {
        return request.getParameter(CortevaConstant.ITEM);
    }

    /**
     * This method closes the resource resolver and log outs the session.
     *
     * @param resolver the resource resolver
     * @param session  the session
     */
    public static void closeResolverSession(final ResourceResolver resolver, final Session session) {
        LOGGER.debug("Inside closeResolverSession() method");
        /**
         * If session is live, log out the session
         */
        if (null != session && session.isLive()) {
            session.logout();
        }
        /**
         * If resource resolver is live, close the resource resolver.
         */
        if (null != resolver && resolver.isLive()) {
            resolver.close();
        }
    }

    /**
     * This method gets the relative page path from the absolute page path.
     *
     * @param absolutePath the absolute path
     * @return the relative page path
     */
    public static String getRelativePagePath(String absolutePath) {
        LOGGER.debug("Inside getRelativePagePath() method");
        String relativePagePath = StringUtils.EMPTY;
        try {
            URL url = new URL(absolutePath);
            relativePagePath = url.getPath();
        } catch (MalformedURLException e) {
            LOGGER.error("Malformed URL Exception occurred in getRelativePagePath()", e);
        }
        LOGGER.debug("Relative Page Path :: {}", relativePagePath);
        return relativePagePath;
    }

    /**
     * This method gets the file name without extension.
     *
     * @param fileName the file name
     * @return the file name without extn
     */
    public static String getFileNameWithoutExtn(String fileName) {
        LOGGER.debug("Inside getFileNameWithoutExtn() method");
        String fileNameWithoutExtn = StringUtils.EMPTY;
        if (StringUtils.indexOf(fileName, CortevaConstant.DOT) > 0) {
            fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf(CortevaConstant.DOT));
        }
        LOGGER.debug("File Name Without Extension :: {}", fileNameWithoutExtn);
        return fileNameWithoutExtn;
    }

    /**
     * This method gets the resource for path browser field of dialog.
     *
     * @param request         the SlingHTTPServletRequest
     * @param pagePath        the page path
     * @param currentResource the current resource
     * @param resolver        the resolver
     * @param contentRootPath the contentRootPath
     * @return the resource
     */
    public static Resource getResource(SlingHttpServletRequest request, String pagePath, Resource currentResource, ResourceResolver resolver,
                                       String contentRootPath) {
        LOGGER.debug("Inside getResource() method");
        if (StringUtils.startsWith(pagePath, "/conf")) {
            String referrer = request.getHeader("Referer");
            pagePath = StringUtils.isNotBlank(referrer) ? StringUtils.substringAfter(referrer, CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION) : StringUtils.EMPTY;
        } else if (StringUtils.containsIgnoreCase(pagePath, EXPERIENCE_FRAGMENTS)) {
            pagePath = request.getRequestPathInfo().getSuffix();
        }
        Map<String, String> regionCountryLangMap = CommonUtils.getRegionCountryLanguage(pagePath, resolver);
        String brand = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.BRAND));
        String language = regionCountryLangMap.get(CortevaConstant.LANGUAGE);
        String country = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.COUNTRY));
        String region = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.REGION));
        StringBuilder sb = new StringBuilder();
        Map<String, Object> mergedProperties = new HashMap<>();
        ValueMap vMap = currentResource.getValueMap();
        String rootPath = "";

        if (StringUtils.isBlank(contentRootPath)) {
            /**
             * Condition for creating experience fragments root-path for all brands.
             */
            rootPath = createExpFragRootPath(brand, language, country, region, sb, vMap);
        } else if (StringUtils.containsIgnoreCase(pagePath, EXPERIENCE_FRAGMENTS) && StringUtils.containsIgnoreCase(contentRootPath, CortevaConstant.CONTENT_DAM_PATH)) {
            sb = sb.append(CortevaConstant.CONTENT_ROOT_PATH_DAM_DPAGCO).append(CortevaConstant.FORWARD_SLASH);
            appendIfNotNull(sb, brand, false);
            if (StringUtils.containsIgnoreCase(pagePath, CortevaConstant.LANGUAGE_MASTERS)) {
            	sb.append(CortevaConstant.FORWARD_SLASH);
            	appendIfNotNull(sb, CortevaConstant.LANGUAGE_MASTERS, true);
            	appendIfNotNull(sb, language, false);
            }
            rootPath = sb.toString();
        } else if (StringUtils.containsIgnoreCase(pagePath, EXPERIENCE_FRAGMENTS) && StringUtils.containsIgnoreCase(contentRootPath, CortevaConstant.CONTENT_ROOT)) {
            sb = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH);
            appendIfNotNull(sb, brand, false);
            if (StringUtils.containsIgnoreCase(pagePath, CortevaConstant.LANGUAGE_MASTERS)) {
            	sb.append(CortevaConstant.FORWARD_SLASH);
            	appendIfNotNull(sb, CortevaConstant.LANGUAGE_MASTERS, true);
            	appendIfNotNull(sb, language, false);
            }
            rootPath = sb.toString();
        } else {
            /**
             * Condition for creating root-path for content and dam for all brands.
             */
            String regionalPagePath = getRegionalPagPath(pagePath);
            Resource pageResource = resolver.resolve(regionalPagePath);
            Page page = getPageFromResource(resolver, pageResource);
            if (null != page) {
                ValueMap valueMap = page.getContentResource().getValueMap();
                LOGGER.debug("valueMap in getLangMap:- {}", valueMap);
                rootPath = createContentRootPath(contentRootPath, brand, language, country, region, sb, valueMap);
            }
        }
        LOGGER.debug("Root Path ::{}", rootPath);
        mergedProperties.putAll(vMap);
        mergedProperties.put("rootPath", rootPath);
        final ValueMap newValueMap = new ValueMapDecorator(mergedProperties);
        Resource resourceWrapper = new ResourceWrapper(currentResource) {
            @Override
            public ValueMap getValueMap() {
                return newValueMap;
            }
        };
        return resourceWrapper;
    }

    /**
     * This method appends to stringbuilder value if not null
     *
     * @param sb            the StringBuilder
     * @param strToAppend   the StringToAppend
     * @param toAppendSlash to append slash
     */
    private static void appendIfNotNull(StringBuilder sb, String strToAppend, boolean toAppendSlash) {
        if (StringUtils.isNotBlank(strToAppend)) {
            sb.append(strToAppend);
            if (toAppendSlash) {
                sb.append(CortevaConstant.FORWARD_SLASH);
            }
        }
    }

    /**
     * Creates the content root path.
     *
     * @param contentRootPath the content root path
     * @param brand           the brand
     * @param language        the language
     * @param country         the country
     * @param region          the region
     * @param sb              the sb
     * @param valueMap        the value map
     * @return the string
     */
    private static String createContentRootPath(String contentRootPath, String brand, String language, String country,
                                                String region, StringBuilder sb, ValueMap valueMap) {
        String rootPath;
        String corporateTag = valueMap.get(CortevaConstant.CQ_TAGS, "");
        if (StringUtils.equalsIgnoreCase(contentRootPath, CortevaConstant.CONTENT_ROOT_PATH_DAM_DPAGCO) && StringUtils.isBlank(brand)) {
            rootPath = CortevaConstant.CONTENT_ROOT_PATH_DAM_DPAGCO;
        } else if (StringUtils.isBlank(brand)) {
            rootPath = CortevaConstant.CONTENT_ROOT;
        } else if (StringUtils.equalsIgnoreCase(contentRootPath, CortevaConstant.CONTENT_ROOT_PATH_DAM_DPAGCO)) {
            rootPath = sb.append(contentRootPath).append(CortevaConstant.FORWARD_SLASH).append(brand).toString();
        } else if (StringUtils.equalsIgnoreCase(contentRootPath, CortevaConstant.CONTENT_ROOT + CortevaConstant.FORWARD_SLASH + EXPERIENCE_FRAGMENTS)) {
            sb = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(EXPERIENCE_FRAGMENTS).append(CortevaConstant.FORWARD_SLASH);
            appendIfNotNull(sb, brand, false);
            rootPath = sb.toString();
        } else if (StringUtils.equalsIgnoreCase(corporateTag, CortevaConstant.CORPORATE_TAG)) {
            rootPath = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand).append(CortevaConstant.FORWARD_SLASH)
                    .append(CortevaConstant.CORPORATE_CONTENT_NODE_NAME).toString();
        } else if (StringUtils.equalsIgnoreCase(contentRootPath, "brand-region-country")) {
            rootPath = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand).append(CortevaConstant.FORWARD_SLASH).append(region).append(CortevaConstant.FORWARD_SLASH).append(country).toString();
        } else if (StringUtils.isBlank(region) && StringUtils.isBlank(country) && StringUtils.isNotBlank(language) && StringUtils.isNotBlank(brand)) {
            rootPath = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand).append(CortevaConstant.FORWARD_SLASH).append(CortevaConstant.LANGUAGE_MASTERS).append(CortevaConstant.FORWARD_SLASH).append(language).toString();
        } else {
            rootPath = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand)
                    .append(CortevaConstant.FORWARD_SLASH).append(region).append(CortevaConstant.FORWARD_SLASH)
                    .append(country).append(CortevaConstant.FORWARD_SLASH).append(language).toString();
        }
        return rootPath;
    }

    /**
     * Creates the exp frag root path.
     *
     * @param brand    the brand
     * @param language the language
     * @param country  the country
     * @param region   the region
     * @param sb       the sb
     * @param vMap     the v map
     * @return the string
     */
    private static String createExpFragRootPath(String brand, String language, String country, String region,
                                                StringBuilder sb, ValueMap vMap) {
        String rootPath;
        String view = vMap.get("cardView", String.class);
        boolean isPageFromCorporateOrUS = (null == region || StringUtils.equalsIgnoreCase("NA", region))
                && (StringUtils.equalsIgnoreCase(CortevaConstant.US, country));
        boolean isPageFromLanguageMasters = null == region && null == country && null != brand && null != language;
        if (isPageFromCorporateOrUS) {
            rootPath = sb.append(CortevaConstant.EXP_FRAGMENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand).append(CortevaConstant.FORWARD_SLASH)
                    .append(language).append(CortevaConstant.FORWARD_SLASH).append(view).toString();
        } else if (isPageFromLanguageMasters) {
            rootPath = sb.append(CortevaConstant.EXP_FRAGMENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand)
                    .append(CortevaConstant.FORWARD_SLASH).append(CortevaConstant.LANGUAGE_MASTERS)
                    .append(CortevaConstant.FORWARD_SLASH).append(language)
                    .append(CortevaConstant.FORWARD_SLASH).append(view).toString();
        } else {
            rootPath = sb.append(CortevaConstant.EXP_FRAGMENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand)
                    .append(CortevaConstant.FORWARD_SLASH).append(region).append(CortevaConstant.FORWARD_SLASH)
                    .append(country).append(CortevaConstant.FORWARD_SLASH).append(language)
                    .append(CortevaConstant.FORWARD_SLASH).append(view).toString();
        }
        return rootPath;
    }

    /**
     * This method will return formatted date.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String formatDate(Date date, String format) {
        LOGGER.debug("Inside formatDate method.");
        String formattedDate = StringUtils.EMPTY;
        try {
            if (null != date) {
                formattedDate = new SimpleDateFormat(format).format(date);
            }
        } catch (final IllegalArgumentException e) {
            LOGGER.error("IllegalArgumentException from formate Date method of Common Utils :: {}", e);
        }
        LOGGER.debug("Exit formatDate method. Formatted Date :: {}", formattedDate);
        return formattedDate;
    }

    /**
     * Gets the date from value map.
     *
     * @param key the key
     * @return the date from value map
     */
    public static Date getDateFromValueMap(Object key) {
        LOGGER.debug("Inside getDateFromValueMap() method");
        Date date = null;
        GregorianCalendar newGregCal = (GregorianCalendar) key;
        if (null != newGregCal) {
            date = newGregCal.getTime();
        }
        LOGGER.debug("Date :: {}", date);
        return date;
    }

    /**
     * This method gets the component name from the resourceType.
     *
     * @param resourceType the resource type
     * @return the component name
     */
    public static String getComponentName(String resourceType) {
        LOGGER.debug("Inside getComponentName() method");
        String componentName = StringUtils.substringAfterLast(resourceType, CortevaConstant.FORWARD_SLASH);
        LOGGER.debug("Component Name :: {}", componentName);
        return componentName;
    }

    /**
     * Gets the page from resource.
     *
     * @param resolver the resolver
     * @param resource the resource
     * @return the page from resource
     */
    public static Page getPageFromResource(ResourceResolver resolver, Resource resource) {
        LOGGER.debug("Inside getPageFromResource() method");
        PageManager pageMgr = resolver.adaptTo(PageManager.class);
        Page page = null;
        if (null != pageMgr) {
            page = pageMgr.getContainingPage(resource);
        }
        LOGGER.debug("Page :: {}", page);
        return page;
    }

    /**
     * Gets the property of a page.
     *
     * @param property     the property
     * @param pageResource the page resource
     * @return the propertyValue for page
     */
    public static String getInheritedProperty(String property, Resource pageResource) {
        LOGGER.debug("Inside getInheritedProperty() method");
        InheritanceValueMap iValueMap = new HierarchyNodeInheritanceValueMap(pageResource);
        LOGGER.debug("IvalueMap in getInheritedProperty - {}", iValueMap);
        String propertyValue = iValueMap.getInherited(property, "");
        LOGGER.debug("Property Value :: {}", propertyValue);
        return propertyValue;
    }

    /**
     * This method reads through the current page URL and returns a map of
     * category, and subcategory.
     *
     * @param pagePath the String
     * @param resolver the resolver
     * @return the category subcategory map
     */
    public static Map<String, String> getCategorySubcategory(String pagePath, ResourceResolver resolver) {
        LOGGER.debug("Inside getCategorySubcategory() method");
        Map<String, String> categorySubcategoryMap = null;
        String regionalPagePath = getRegionalPagPath(pagePath);
        Resource pageResource = resolver.resolve(regionalPagePath);
        Page pageFromResource = getPageFromResource(resolver, pageResource);
        if (null != pageFromResource) {
            categorySubcategoryMap = getCategoryMap(pagePath, pageFromResource.getContentResource());
        }
        return categorySubcategoryMap;
    }

    /**
     * Gets the category map.
     *
     * @param pagePath     the page path
     * @param pageResource the page resource
     * @return the the category subcategory map
     */
    private static Map<String, String> getCategoryMap(String pagePath, Resource pageResource) {
        LOGGER.debug("Inside getLangMapForCorporate() method");
        Map<String, String> categorySubcategoryMap = new HashMap<>();
        ValueMap valueMap = pageResource.getValueMap();
        LOGGER.debug("valueMap in getCategoryMap: {}", valueMap);
        String corporateTag = valueMap.get(CortevaConstant.CQ_TAGS, StringUtils.EMPTY);
        String[] pageNameString = StringUtils.split(pagePath, CortevaConstant.FORWARD_SLASH);
        int pageNameStringLength = pageNameString.length;
        if (StringUtils.equalsIgnoreCase(corporateTag, CortevaConstant.CORPORATE_TAG)) {
            categorySubcategoryMap.put(CortevaConstant.CATEGORY,
                    (pageNameStringLength > CommonUtils.FOUR) ? pageNameString[CommonUtils.FOUR] : StringUtils.EMPTY);
            categorySubcategoryMap.put(CortevaConstant.SUBCATEGORY,
                    (pageNameStringLength > CommonUtils.FIVE) ? pageNameString[CommonUtils.FIVE] : StringUtils.EMPTY);
        } else {
            categorySubcategoryMap.put(CortevaConstant.CATEGORY,
                    (pageNameStringLength > CommonUtils.SIX) ? pageNameString[CommonUtils.SIX] : StringUtils.EMPTY);
            categorySubcategoryMap.put(CortevaConstant.SUBCATEGORY,
                    (pageNameStringLength > CommonUtils.SEVEN) ? pageNameString[CommonUtils.SEVEN] : StringUtils.EMPTY);
        }
        LOGGER.debug("Category Subcategory Map :: {}", categorySubcategoryMap);
        return categorySubcategoryMap;
    }

    /**
     * Gets the local date.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the local date
     */
    public static String getLocalDate(String date, String pattern) {
        LOGGER.debug("entering getLocalDate of AbsoluteTimeProcessStep");
        ZonedDateTime zDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        LOGGER.debug("time zone of client:{}", zDateTime.getZone());
        ZonedDateTime serverDateTime = zDateTime.withZoneSameInstant(ZoneId.of(TimeZone.getDefault().getID()));
        LOGGER.debug("time zone of server:{}", serverDateTime.getZone());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(pattern);
        LOGGER.debug("exiting getLocalDate of AbsoluteTimeProcessStep");
        return timeFormatter.format(serverDateTime);
    }

    /**
     * Gets the pdp root path.
     *
     * @param regionCountryLangMap the region Country Lang Map
     * @param pdpRootPath          the pdp root path
     * @return the pdp root path
     */
    public static String getPdpRootPath(Map<String, String> regionCountryLangMap, String pdpRootPath) {
        LOGGER.debug("Inside getPdpRootPath() method");
        if (StringUtils.isBlank(pdpRootPath)) {
            boolean containsKey = regionCountryLangMap.containsKey(CortevaConstant.REGION)
                    && regionCountryLangMap.containsKey(CortevaConstant.COUNTRY)
                    && regionCountryLangMap.containsKey(CortevaConstant.LANGUAGE);
            if (containsKey) {
                StringBuilder sb = new StringBuilder();
                String region = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.REGION));
                String country = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.COUNTRY));
                String language = regionCountryLangMap.get(CortevaConstant.LANGUAGE);
                String brand = StringUtils.lowerCase(regionCountryLangMap.get(CortevaConstant.BRAND));
                pdpRootPath = sb.append(CortevaConstant.CONTENT_ROOT).append(CortevaConstant.FORWARD_SLASH).append(brand)
                        .append(CortevaConstant.FORWARD_SLASH).append(region).append(CortevaConstant.FORWARD_SLASH).append(country)
                        .append(CortevaConstant.FORWARD_SLASH).append(language).append(CortevaConstant.FORWARD_SLASH)
                        .append("homepage").append(CortevaConstant.FORWARD_SLASH).append("products").toString();
            }
        }
        LOGGER.debug("PDP Root Path :: {}", pdpRootPath);
        return pdpRootPath;
    }

    /**
     * Returns one resource of given type in tree of rootResource. If no
     * resource is found, returns null
     *
     * @param rootResource the root resource
     * @param resourceType Resource Type to be matched (without /apps or /libs)
     * @return Returns a resource having given resourceType, null in case of no
     * matching resource
     */
    public static Resource getResourceOfType(Resource rootResource, String resourceType) {
        LOGGER.debug("Inside getResourceOfType() method");
        final List<Resource> list = new ArrayList<>();
        if (!StringUtils.equalsIgnoreCase(CortevaConstant.ROOT, rootResource.getName())) {
            return null;
        }
        getAllResourcesUnderPage(rootResource, resourceType, list, true);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Internal method called only from getAllResourcesOfType to allow
     * recursion.
     *
     * @param rootResource Resource under which resources are to be found
     * @param resourceType Resource type to be matched
     * @param list         Resources are added to this list as and when they're found.
     * @param breakAtFirst the break at first
     */
    private static void getAllResourcesUnderPage(Resource rootResource, String resourceType, List<Resource> list,
                                                 boolean breakAtFirst) {
        LOGGER.debug("Inside getAllResourcesUnderPage() method");
        if (rootResource.isResourceType(resourceType)) {
            list.add(rootResource);
        }
        if (breakAtFirst && !list.isEmpty()) {
            return;
        }
        if (rootResource.hasChildren()) {
            final Iterable<Resource> resourceIterable = rootResource.getChildren();
            for (final Resource res : resourceIterable) {
                getAllResourcesUnderPage(res, resourceType, list, breakAtFirst);
            }
        }
    }

    /**
     * Find all pdp resources.
     *
     * @param resolver    the resolver
     * @param contentPath the content path
     * @return the iterator
     */
    public static Iterator<Resource> findAllPdpResources(ResourceResolver resolver, String contentPath) {
        LOGGER.debug("Entering findAllPdpResources of CommonUtils");
        Iterator<Resource> resultIterator = null;
        Map<String, String> params = new HashMap<>();
        params.put(PathPredicateEvaluator.PATH, contentPath);
        params.put("1" + CortevaConstant.PROPERTY_CONSTANT, CortevaConstant.SLING_RESOURCE_TYPE);
        params.put("1" + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.PDP_RESOURCE_TYPE);
        params.put("group.p.or", CortevaConstant.TRUE);
        params.put(CommonUtils.GROUP_1 + CortevaConstant.PROPERTY_CONSTANT, CortevaConstant.PDP_SOURCE);
        params.put(CommonUtils.GROUP_1 + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.NON_AGRIAN);
        params.put(CommonUtils.GROUP_2 + CortevaConstant.PROPERTY_CONSTANT, CortevaConstant.HIDE_PRODUCT);
        params.put(CommonUtils.GROUP_2 + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.TRUE);
        params.put(CortevaConstant.PROPERTY_LIMIT, "-1");
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        if (queryBuilder != null) {
            Query query = queryBuilder.createQuery(PredicateGroup.create(params), resolver.adaptTo(Session.class));
            resultIterator = query.getResult().getResources();
        }
        LOGGER.debug("Exiting findAllPdpResources of CommonUtils");
        return resultIterator;
    }

    /**
     * Find all unhidden child resources of resource type.
     *
     * @param resolver     the resolver
     * @param resourceType the resourceType
     * @param rootPath     the root path
     * @return resultIterator the iterator
     */
    public static Iterator<Resource> findUnhiddenChildResources(ResourceResolver resolver, String resourceType,
                                                                String rootPath) {
        LOGGER.debug("Entering findUnhiddenChildResources of CommonUtils");
        Iterator<Resource> resultIterator = null;
        Map<String, String> params = new HashMap<>();
        params.put(PathPredicateEvaluator.PATH, rootPath);
        params.put("type", CortevaConstant.PROPERTY_PAGE_TYPE);
        params.put("1" + CortevaConstant.PROPERTY_CONSTANT,
                CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.SLING_RESOURCE_TYPE);
        params.put("1" + CortevaConstant.PROPERTY_VALUE_CONSTANT, resourceType);
        params.put("2" + CortevaConstant.PROPERTY_CONSTANT, CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + "hideInNav");
        params.put("2" + CortevaConstant.PROPERTY_OPERATION, PROPERTY_EXISTS);
        params.put("2" + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.FALSE);
        params.put(CortevaConstant.PROPERTY_LIMIT, "-1");
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        if (queryBuilder != null) {
            Query query = queryBuilder.createQuery(PredicateGroup.create(params), resolver.adaptTo(Session.class));
            resultIterator = query.getResult().getResources();
        }
        LOGGER.debug("Exiting findUnhiddenChildResources of CommonUtils");
        return resultIterator;
    }

    /**
     * Find non agrian pdp resource.
     *
     * @param resolver    the resolver
     * @param contentPath the content path
     * @param productId   the product id
     * @return the resource
     */
    public static Resource findNonAgrianPdpResource(ResourceResolver resolver, String contentPath, String productId) {
        LOGGER.debug("Entering findNonAgrianPdpResource of CommonUtils");
        Resource resultResource = null;
        Map<String, String> params = new HashMap<>();
        params.put(PathPredicateEvaluator.PATH, contentPath);
        params.put("1" + CortevaConstant.PROPERTY_CONSTANT,
                CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.SLING_RESOURCE_TYPE);
        params.put("1" + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.PDP_RESOURCE_TYPE);
        params.put("group.p.and", CortevaConstant.TRUE);
        params.put(CommonUtils.GROUP_1 + CortevaConstant.PROPERTY_CONSTANT,
                CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.PDP_SOURCE);
        params.put(CommonUtils.GROUP_1 + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.NON_AGRIAN);
        params.put(CommonUtils.GROUP_2 + CortevaConstant.PROPERTY_CONSTANT,
                CortevaConstant.JCR_CONTENT + CortevaConstant.FORWARD_SLASH + CortevaConstant.HIDE_PRODUCT);
        params.put(CommonUtils.GROUP_2 + CortevaConstant.PROPERTY_VALUE_CONSTANT, CortevaConstant.FALSE);
        params.put("nodename", productId);
        params.put(CortevaConstant.PROPERTY_LIMIT, "-1");
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        if (queryBuilder != null) {
            Query query = queryBuilder.createQuery(PredicateGroup.create(params), resolver.adaptTo(Session.class));
            Iterator<Resource> resultIterator = query.getResult().getResources();
            if (null != resultIterator) {
                resultResource = resultIterator.next();
            }
        }
        LOGGER.debug("Exiting findAllPdpResources of CommonUtils");
        return resultResource;
    }

    /**
     * This method concat the language and country to extract the locale,.
     *
     * @param pagePath the String
     * @param resolver the resolver
     * @return the i18nLocale
     */
    public static String getI18nLocale(String pagePath, ResourceResolver resolver) {
        String locale = StringUtils.EMPTY;
        Map<String, String> countryLanguageMap = CommonUtils.getRegionCountryLanguage(pagePath, resolver);
        String language = countryLanguageMap.get(CortevaConstant.LANGUAGE);
        String country = countryLanguageMap.get(CortevaConstant.COUNTRY);
        if (StringUtils.isNotEmpty(language) && StringUtils.isNotEmpty(country)) {
            locale = language.concat(CortevaConstant.UNDERSCORE).concat(country);
        }
        return locale;
    }

    /**
     * Gets the i 18 n value.
     *
     * @param request  the request
     * @param resolver the resolver
     * @param key      the key
     * @return the i 18 n value
     */
    public static String getI18nValue(SlingHttpServletRequest request, ResourceResolver resolver, String key) {
        LOGGER.debug("Entering getI18nValue method of Common Utils");
        String value = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(key)) {
            try {
                Locale locale = new Locale(getI18nLocale(CommonUtils.getPagePath(request), resolver));
                ResourceBundle resourceBundle = request.getResourceBundle(locale);
                I18n i18n = new I18n(resourceBundle);
                value = i18n.get(key);
            } catch (MissingResourceException e) {
                LOGGER.error("MissingResourceException occurred in getI18nValue()", e);
            }
        }
        LOGGER.debug("I18n Value :: {}", value);
        return value;
    }

    /**
     * Gets the page path from request.
     *
     * @param request the request
     * @return the path of the page
     */
    public static String getPagePathFromRequest(SlingHttpServletRequest request) {
        String pagePath = "";
        if (null != request.getRequestURL()) {
            pagePath = request.getRequestURL().toString();
            ResourceResolver resourceResolver = request.getResourceResolver();
            try {
                String resourcePath;
                resourcePath = new URI(pagePath).getPath();
                Resource res = resourceResolver.resolve(request, resourcePath);
                /*
                 * derive valid parent resource for non-existing resource, i.e.
                 * for error page scenario
                 */
                while (ResourceUtil.isNonExistingResource(res)
                        && resourcePath.contains(CortevaConstant.FORWARD_SLASH)) {
                    resourcePath = resourcePath.substring(0, resourcePath.lastIndexOf(CortevaConstant.FORWARD_SLASH));
                    res = resourceResolver.resolve(request, resourcePath);
                }
                pagePath = res.getPath();
                LOGGER.debug("Path of the referred page is {} ", pagePath);
            } catch (URISyntaxException e) {
                LOGGER.error("Exception occured while fetching page path from referrer", e);
            }
        }
        return pagePath;
    }

    /**
     * Gets the property of a page.
     *
     * @param property     the property
     * @param pageResource the page resource
     * @return the propertyValue for page
     * @throws RepositoryException the repository exception
     */
    public static String getProductTypeProperty(String property, Resource pageResource) throws RepositoryException {
        LOGGER.debug("Inside getInheritedProperty() method");
        InheritanceValueMap iValueMap = new HierarchyNodeInheritanceValueMap(pageResource);
        LOGGER.debug("IvalueMap in getInheritedProperty - {}", iValueMap);
        Property productTags = iValueMap.getInherited(property, Property.class);
        TagManager tagManager = pageResource.getResourceResolver().adaptTo(TagManager.class);
        String productType = StringUtils.EMPTY;
        if (null != tagManager && null != productTags) {
            productType = getProductType(productTags, tagManager);
        }
        return productType;
    }

    /**
     * Gets the product type.
     *
     * @param productTags the product tags
     * @param tagManager  the tag manager
     * @return the product type
     * @throws RepositoryException the repository exception
     */
    private static String getProductType(Property productTags, TagManager tagManager) throws RepositoryException {
        String productType = StringUtils.EMPTY;
        Tag resolvedTag = null;
        if (productTags.isMultiple()) {
            for (Value value : productTags.getValues()) {
                resolvedTag = tagManager.resolve(value.getString());
                if (null != resolvedTag) {
                    productType = resolvedTag.getTitle();
                    break;
                }
            }
        } else {
            resolvedTag = tagManager.resolve(productTags.getString());
            productType = resolvedTag.getTitle();
        }
        return productType;
    }

    /**
     * Gets the locale for internationalization.
     *
     * @param request the request
     * @return the i18nLocale
     */
    public static Locale getLocale(SlingHttpServletRequest request) {
        Locale locale = null;
        locale = new Locale(getI18nLocale(getPagePath(request), request.getResourceResolver()));
        return locale;

    }

    /**
     * Gets the locale from open page property url for internationalization.
     *
     * @param request the request
     * @return the i18nLocale
     */
    public static Locale getLocaleFromPageProperties(SlingHttpServletRequest request) {
        Locale locale = null;
        locale = new Locale(getI18nLocale(getPagePathFromPageProperties(request), request.getResourceResolver()));
        return locale;

    }

    /**
     * @param request the sling request
     * @return locale the locale
     */
    public static Locale getLangLocale(SlingHttpServletRequest request) {
        LOGGER.debug("Inside getLocale() method");
        Map<String, String> countryLangMap = getRegionCountryLanguage(getPagePath(request),
                request.getResourceResolver());

        Locale locale = null;
        if (!countryLangMap.isEmpty()) {
            String language = countryLangMap.get(CortevaConstant.LANGUAGE);
            LOGGER.debug("Language from countryLangMap :: {}", language);
            if (StringUtils.isNotBlank(language)) {
                locale = new Locale(language);
            }
        }
        return locale;
    }

    /**
     * Builds a locale object using country and language from request
     * @param request the sling request
     * @return locale the locale
     */
    public static Locale getFullLocale(SlingHttpServletRequest request) {
        LOGGER.debug("Inside getFullLocale() method");
        Map<String, String> countryLangMap = getRegionCountryLanguage(getPagePath(request),
                request.getResourceResolver());

        Locale locale = null;
        if (!countryLangMap.isEmpty()) {
            String language = countryLangMap.get(CortevaConstant.LANGUAGE);
            String country = countryLangMap.get(CortevaConstant.COUNTRY);
            LOGGER.debug("Language from countryLangMap :: {}", language);
            LOGGER.debug("Country from countryLangMap :: {}", country);

            if (StringUtils.isNotBlank(language)) {
                locale = new Locale(language, country);
            }
        }
        return locale;
    }

    /**
     * Gets the tag title.
     *
     * @param request the request
     * @param tag     the tag
     * @return the tag title
     */
    public static String getTagTitle(SlingHttpServletRequest request, Tag tag) {
        String tagTitle;
        Locale locale = getLangLocale(request);
        if (StringUtils.isNotBlank(tag.getLocalizedTitle(locale))) {
            tagTitle = tag.getLocalizedTitle(locale);
        } else {
            tagTitle = tag.getTitle();
        }

        return tagTitle;
    }

    /**
     * Gets the tag localized title.
     *
     * @param request the request
     * @param tag     the tag
     * @return the tag localized title
     */
    public static String getTagLocalizedTitle(SlingHttpServletRequest request, Tag tag) {
        if (StringUtils.isNotBlank(tag.getLocalizedTitle(getLocale(request)))) {
            return tag.getLocalizedTitle(getLocale(request));

        } else if (StringUtils.isNotBlank(tag.getLocalizedTitle(getLangLocale(request)))) {
            return tag.getLocalizedTitle(getLangLocale(request));

        } else {
            return tag.getTitle();
        }

    }

    /**
     * This method provides the locale object of the language and country to extract the locale,.
     *
     * @param pagePath the String
     * @param resolver the resolver
     * @return the Locale object;
     */
    public static Locale getLocaleObject(String pagePath, ResourceResolver resolver) {
        Locale locale = null;
        Map<String, String> countryLanguageMap = CommonUtils.getRegionCountryLanguage(pagePath, resolver);
        String language = countryLanguageMap.get(CortevaConstant.LANGUAGE);
        String country = countryLanguageMap.get(CortevaConstant.COUNTRY);
        if (StringUtils.isNotEmpty(language) && StringUtils.isNotEmpty(country)) {
            locale = new Locale(language, country);
        }
        return locale;
    }

    /**
     * Gets the formatted date.
     *
     * @param localDateTime  the date
     * @param locale         the locale
     * @param isTimeRequired the flag for whether timestamp is required
     * @param request        the sling request
     * @param baseService    the base configuration service
     * @return the formatted date
     */
    public static String getFormattedLocalizedDate(LocalDateTime localDateTime, Locale locale, boolean isTimeRequired, SlingHttpServletRequest request, BaseConfigurationService baseService) {
        String pattern = baseService.getPropConfigValue(request, CortevaConstant.DATE_PATTERN, CortevaConstant.GLOBAL_CONFIG_NAME);
        pattern = StringUtils.trim(pattern);
        LOGGER.debug("Pattern from config is : {}", pattern);
        if (isTimeRequired) {
            pattern = StringUtils.isNotBlank(pattern) ? pattern : DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.MEDIUM,
                    IsoChronology.INSTANCE, locale);
        } else {
            pattern = StringUtils.substringBefore(pattern, " ");
            pattern = StringUtils.isNotBlank(pattern) ? pattern : DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, null,
                    IsoChronology.INSTANCE, locale);
        }
        LOGGER.debug("DateTimeFormatterBuilder pattern for {} :: {}", locale.toString(), pattern);
        return yearFormattedDate(pattern, localDateTime);
    }

    /**
     * Gets the formatted date from predefined date pattern from servlet.
     *
     * @param localDateTime  the date
     * @param locale         the locale
     * @param isTimeRequired the flag for whether timestamp is required
     * @param datePattern     the date pattern from configuration
     * @return the formatted date
     */
    public static String getFormattedLocalizedDateForServlet(LocalDateTime localDateTime, Locale locale, boolean isTimeRequired, String datePattern) {
        String pattern;
        if (isTimeRequired) {
            pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.MEDIUM,
                    IsoChronology.INSTANCE, locale);
        } else {
            pattern = StringUtils.isNotBlank(datePattern) ?  StringUtils.substringBefore(datePattern, " ") : DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, null,
                    IsoChronology.INSTANCE, locale);
        }
        LOGGER.debug("DateTimeFormatterBuilder pattern for {} :: {}", locale.toString(), pattern);
        return yearFormattedDate(pattern, localDateTime);
    }

    /**
     * @param pattern
     * 		  the date pattern
     * @param localDateTime
     * 		  the local date time
     * @return the formatted date
     */
    private static String yearFormattedDate(String pattern, LocalDateTime localDateTime) {
        if (!pattern.contains("yyyy")) {
            pattern = pattern.replace("yy", "yyyy");
            LOGGER.debug("formatted pattern :: {}", pattern);
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateFormatter.format(localDateTime);
    }

    /**
     * Method to get the Environment of the current AEM installation.
     *
     * @param runModes the set of runModes
     * @return env the Environment of the instance
     */
    public static String getEnvironment(Set<String> runModes) {
        String env = CortevaConstant.DEV;
        if (runModes.contains(CortevaConstant.QA)) {
            env = CortevaConstant.QA;
        } else if ((runModes.contains(CortevaConstant.STAGE))) {
            env = CortevaConstant.STAGE;
        } else if ((runModes.contains(CortevaConstant.CORTEVA_QA))) {
            env = CortevaConstant.CORTEVA_QA;
        } else if (runModes.contains(CortevaConstant.PROD)) {
            env = CortevaConstant.PROD;
        } else {
            LOGGER.debug("Default environment is set as: {}", env);
        }

        return env;
    }

    /**
     * Gets the pagination url for Load More Button
     *
     * @param resourceResolver
     * 			the resource resolver
     * @param slingRequest
     * 			the request
     * @param pagePath
     * 			the current page path
     *
     * @return paginationUrl the pagination url
     */
    public static String getPaginationUrl(ResourceResolver resourceResolver, SlingHttpServletRequest slingRequest, String pagePath) {
        return resourceResolver.map(slingRequest, pagePath + CortevaConstant.DOT + getPaginationIndex(slingRequest)
                + CortevaConstant.DOT + CortevaConstant.HTML_EXTENSION);
    }

    /**
     * Gets the pagination index for Load More Button
     *
     * @param slingRequest
     * 			the request
     * @return paginationIndex the pagination index
     */
    public static int getPaginationIndex(SlingHttpServletRequest slingRequest) {
        int paginationIndex = 1;
        String[] selectors = slingRequest.getRequestPathInfo().getSelectors();
        if (selectors.length == 1) {
            try {
                paginationIndex = Integer.parseInt(selectors[0]) + 1;
            } catch (NumberFormatException e) {
                LOGGER.error("NumberFormatException occurred in getPaginationIndex()", e);
            }
        }
        LOGGER.debug("Pagination Index: {}", paginationIndex);
        return paginationIndex;
    }

}
