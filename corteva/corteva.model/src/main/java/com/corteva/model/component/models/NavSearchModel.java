package com.corteva.model.component.models;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.corteva.core.configurations.BaseConfigurationService;
import com.corteva.core.constants.CortevaConstant;
import com.corteva.core.utils.CommonUtils;
import com.corteva.core.utils.LinkUtil;

/**
 * The is the sling for the Experience Fragments. This sling model will be used
 * by all the components where we need path of experience fragments from the
 * author.
 *
 * @author Sapient
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class NavSearchModel extends AbstractSlingModel {

	/**
	 * The Constant MIN_CHAR_LENGTH.
	 */
	private static final String MIN_CHAR_LENGTH = "minCharacterLength";
	/**
	 * The Constant MAX_SUGGESTION_LENGTH.
	 */
	private static final String MAX_SUGGESTION_LENGTH = "maxSuggestionLength";

	/**  SearchBar Text. */
	@Inject
	@Named("searchBarText")
	@Via("resource")
	@Optional
	private String searchBarText;

	/**  SearchFormAction. */
	@Inject
	@Named("linkUrl")
	@Via("resource")
	@Optional
	private String searchFormAction;

	/**
	 * The Constant AUTO_COMPLETE_URL.
	 */
	private static final String AUTO_COMPLETE_URL = "autoCompleteUrl";

	/**
	 * The base config.
	 */
	@Inject
	private BaseConfigurationService baseConfig;
	/**
	 * The sling request.
	 */
	@Inject
	private SlingHttpServletRequest slingRequest;

	/** The region country lang map. */
	private Map<String, String> regionCountryLangMap;

	/**
	 * Instantiates NavSearch Model.
	 *
	 * @param request            the request
	 */
	public NavSearchModel(SlingHttpServletRequest request) {
		slingRequest = request;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		regionCountryLangMap = CommonUtils.getRegionCountryLanguage(CommonUtils.getPagePathFromRequest(slingRequest),
				getResourceResolver());
	}

	/**
	 * Gets the searchBarText.
	 *
	 * @return the searchBarText
	 */
	public String getSearchBarText() {
		return searchBarText;
	}

	/**
	 * Gets the searchActionUrl.
	 *
	 * @return the searchActionUrl
	 */
	public String getSearchFormAction() {
		if (StringUtils.isNotBlank(searchFormAction)) {
			searchFormAction = LinkUtil.getHref(searchFormAction);
		}
		return searchFormAction;
	}

	/**
	 * Gets the autocomplete url.
	 *
	 * @return the autocomplete url
	 */
	public String getAutoCompleteUrl() {
		return baseConfig.getPropertyValueFromConfiguration(regionCountryLangMap, AUTO_COMPLETE_URL,
				CortevaConstant.SEARCH_CONFIG_NAME);
	}

	/**
	 * Gets the maximum Suggestion.
	 *
	 * @return the maximum suggestion
	 */
	public String getMaximumSuggestion() {
		return baseConfig.getPropertyValueFromConfiguration(regionCountryLangMap, MAX_SUGGESTION_LENGTH,
				CortevaConstant.SEARCH_CONFIG_NAME);
	}

	/**
	 * Gets the minimum characters.
	 *
	 * @return the minimum characters
	 */
	public String getMinimumCharacter() {
		return baseConfig.getPropertyValueFromConfiguration(regionCountryLangMap, MIN_CHAR_LENGTH,
				CortevaConstant.SEARCH_CONFIG_NAME);
	}

	/**
	 * Setter for SearchBarText.
	 *
	 * @param searchBarTxt            Text to be shown as placeholder for search
	 */
	public void setSearchBarText(String searchBarTxt) {
		this.searchBarText = searchBarTxt;
	}

	/**
	 * Setter for Search Form Action.
	 *
	 * @param searchFrmAction            Form Action for Search form
	 */
	public void setSearchFormAction(String searchFrmAction) {
		this.searchFormAction = searchFrmAction;
	}
}
