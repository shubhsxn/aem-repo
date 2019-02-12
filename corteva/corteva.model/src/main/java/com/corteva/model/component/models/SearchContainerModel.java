package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

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
import com.corteva.core.utils.CommonUtils;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;


/**
 * The is the sling model for the Related Content Cards.
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class SearchContainerModel extends AbstractSlingModel {

    /** Logger Instantiation. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchContainerModel.class);
    /**
     * The Constant SEARCH_URL.
     */
    private static final String SEARCH_URL = "searchUrl";

    /**
     * The Constant SORT_OPTION.
     */
    private static final String SORT_OPTION = "sortOption";
    /**
     * The Constant FILTER_OPTION.
     */
    private static final String FILTER_OPTION = "filterOption";
    /**
     * The Constant AUTO_COMPLETE_URL.
     */
    private static final String AUTO_COMPLETE_URL = "autoCompleteUrl";
    /**
     * The Constant MIN_CHAR_LENGTH.
     */
    private static final String MIN_CHAR_LENGTH = "minCharacterLength";
    /**
     * The Constant MAX_SUGGESTION_LENGTH.
     */
    private static final String MAX_SUGGESTION_LENGTH = "maxSuggestionLength";

    /** The Constant KEY. */
    private static final String KEY = "key";

    /** The Constant HASH_SEPERATOR. */
    private static final String HASH_SEPERATOR = "###";

    /**
     * The tags selected for filter
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("cq:tags")
    private String[] tagArr;



    /**
     * The custom filters
     */
    @Inject
    @Optional
    @Via("resource")
    @Named("filters")
    private String[] customFilters;

    /**
     * The sling request.
     */
    @Inject
    private SlingHttpServletRequest slingRequest;

    /**
     * The base config.
     */
    @Inject
    private BaseConfigurationService baseConfig;

    /**
     * The resource resolver.
     */
    @Inject
    private ResourceResolver resourceResolver;

    /**
     * The Map of Filter with there value.
     */
    private Map<String, String> filterMap = new HashMap<>();

    /**
     * Method will be called after all injections are done
     *
     * It Create map with filter and  value by reading properties from Config
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("Inside method init() in SearchContainerModel");
        String filterOptionValue = baseConfig.getPropConfigValue(slingRequest, FILTER_OPTION, CortevaConstant.SEARCH_CONFIG_NAME);
        String[] filterOptionArr = StringUtils.split(filterOptionValue, CortevaConstant.COMMA);
        if (filterOptionArr.length > 0) {
            for (String filter:filterOptionArr) {
                String[] filterValue = StringUtils.split(filter, SearchContainerModel.HASH_SEPERATOR);
                filterMap.put(filterValue[0], filterValue[1]);
            }
        }
    }

    /**
     * Gets the search url
     *
     * @return the search url
     */
    public String getSearchUrl() {
        return baseConfig.getPropConfigValue(slingRequest, SEARCH_URL, CortevaConstant.SEARCH_CONFIG_NAME);
    }

    /**
     * Gets the autocomplete url
     *
     * @return the autocomplete url
     */
    public String getAutoCompleteUrl() {
        return baseConfig.getPropConfigValue(slingRequest, AUTO_COMPLETE_URL, CortevaConstant.SEARCH_CONFIG_NAME);
    }
    /**
     * Gets the maximum Suggestion
     *
     * @return the maximum suggestion
     */
    public String getMaximumSuggestion() {
        return baseConfig.getPropConfigValue(slingRequest, MAX_SUGGESTION_LENGTH, CortevaConstant.SEARCH_CONFIG_NAME);
    }
    /**
     * Gets the minimum characters
     *
     * @return the minimum characters
     */
    public String getMinimumCharacter() {
        return baseConfig.getPropConfigValue(slingRequest, MIN_CHAR_LENGTH, CortevaConstant.SEARCH_CONFIG_NAME);
    }
    /**
     * Gets the filter Map
     *
     * @return map of Filter with Value
     */
    public Map<String, String> getFilterMap() {
        return filterMap;
    }
    /**
     * Gets the sort options
     *
     * @return the sort options
     */
    public List<Map<String, String>> getSortOptions() {
        LOGGER.debug("Inside method getSortOptions() in SearchContainerModel");
        List<Map<String, String>> sortList = new ArrayList<>();
        String sortOptionValue = baseConfig.getPropConfigValue(slingRequest, SORT_OPTION, CortevaConstant.SEARCH_CONFIG_NAME);
        String[] sortOptionArr = StringUtils.split(sortOptionValue, CortevaConstant.COMMA);
        if (sortOptionArr.length > 0) {
            for (String sortString:sortOptionArr) {
                Map<String, String> sortMap = new HashMap<>();
                String[] sortValue = StringUtils.split(sortString, SearchContainerModel.HASH_SEPERATOR);
                sortMap.put(SearchContainerModel.KEY, sortValue[0]);
                sortMap.put(CortevaConstant.VALUE, sortValue[1]);
                sortList.add(sortMap);
            }
        }
        return sortList;
    }
    /**
     * Gets the filter options
     *
     * @return the filter options
     */
    public List<Map<String, String>> getFilterOptions() {
        LOGGER.debug("Inside method getFilterOptions() in SearchContainerModel");
        List<Map<String, String>> filterList = new ArrayList<>();
        TagManager tag = resourceResolver.adaptTo(TagManager.class);
        if (tag != null && tagArr != null && tagArr.length > 0) {
            addFilters(filterList, tag);
        }
        if (customFilters != null && customFilters.length > 0) {
            for (String customFilter : customFilters) {
                Map<String, String> tagMap = new HashMap<>();
                tagMap.put(SearchContainerModel.KEY, customFilter);
                tagMap.put(CortevaConstant.VALUE, getFilterValue(customFilter));
                filterList.add(tagMap);
            }

        }
        return filterList;
    }
    /**
     * This method add the filters to the List
     *
     * @param filterList
     * 				Filter List
     * @param tag
     * 				TagManager
     *
     */
    private void addFilters(List<Map<String, String>> filterList, TagManager tag) {
        LOGGER.debug("Inside method addFilters() in SearchContainerModel");
        for (String tagID : tagArr) {
            String parentTagId;
            int i = StringUtils.ordinalIndexOf(tagID, CortevaConstant.FORWARD_SLASH, 2);
            if (i > 0) {
                parentTagId = StringUtils.substring(tagID, 0, i);
            } else {
                parentTagId = tagID;
            }
            Tag resolvedTag = tag.resolve(parentTagId);
            if (resolvedTag != null) {
                Map<String, String> tagMap = new HashMap<>();
                String localizedTagTitle = CommonUtils.getTagLocalizedTitle(slingRequest, resolvedTag);
                tagMap.put(SearchContainerModel.KEY, localizedTagTitle);
                String tagValue = getFilterValue(localizedTagTitle);
                tagMap.put(CortevaConstant.VALUE, tagValue);
                if (!filterList.contains(tagMap)) {
                    filterList.add(tagMap);
                }
            }
        }
    }

    /**
     * Gets the value of filter.
     *
     * @param key the filter whose value need to be computed
     * @return the filter value
     */
    public String getFilterValue(final String key) {
        if (filterMap.containsKey(key)) {
            return filterMap.get(key);
        } else {
            return "";
        }

    }
    /**
     * @param resResolver
     *            the resourceResolver to set
     */
    public void setResourceResolver(ResourceResolver resResolver) {
        this.resourceResolver = resResolver;
    }
    /**
     * Setter for TagArr
     * @param tagsArr
     *            Array of Tags
     */
    public void setTagArr(String[] tagsArr) {
        this.tagArr = Arrays.copyOf(tagsArr,tagsArr.length) ;
    }

    /**
     * Setter for Custom Filters
     * @param customFilter
     *            Array of Custom Filters
     */
    public void setCustomFilters(String[]  customFilter) {
        this.customFilters = Arrays.copyOf(customFilter,customFilter.length);
    }

}
