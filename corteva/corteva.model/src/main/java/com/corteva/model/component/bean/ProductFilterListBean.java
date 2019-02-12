package com.corteva.model.component.bean;

import java.util.Map;

/**
 * The Class ProductFilterListBean.
 */
public class ProductFilterListBean {
	
	/** The filter map. */
	private Map<String, String> filterMap;
	
	/** The has sequantial filters. */
	private boolean hasSequantialFilters;
	
	/** The child filters. */
	private Map<String, ProductFilterListBean> childFilters;
	
	/** The data analytics type. */
	private String dataAnalyticsType;
	
	/**
	 * Gets the filter map.
	 * @return the filter map
	 */
	public Map<String, String> getFilterMap() {
		return filterMap;
	}
	
	/**
	 * Sets the filter map.
	 * @param filterMap the filter map
	 */
	public void setFilterMap(Map<String, String> filterMap) {
		this.filterMap = filterMap;
	}
	
	/**
	 * Checks if is checks for sequantial filters.
	 * @return true, if is checks for sequantial filters
	 */
	public boolean isHasSequantialFilters() {
		return hasSequantialFilters;
	}
	
	/**
	 * Sets the checks for sequantial filters.
	 * @param hasSequantialFilters the new checks for sequantial filters
	 */
	public void setHasSequantialFilters(boolean hasSequantialFilters) {
		this.hasSequantialFilters = hasSequantialFilters;
	}
	
	/**
	 * Gets the child filters.
	 * @return the child filters
	 */
	public Map<String, ProductFilterListBean> getChildFilters() {
		return childFilters;
	}
	
	/**
	 * Sets the child filters.
	 * @param childFilters the child filters
	 */
	public void setChildFilters(Map<String, ProductFilterListBean> childFilters) {
		this.childFilters = childFilters;
	}

	/**
	 * Gets the data analytics type.
	 *
	 * @return the dataAnalyticsType
	 */
	public String getDataAnalyticsType() {
		return dataAnalyticsType;
	}

	/**
	 * Sets the data analytics type.
	 *
	 * @param dataAnalyticsType the dataAnalyticsType to set
	 */
	public void setDataAnalyticsType(String dataAnalyticsType) {
		this.dataAnalyticsType = dataAnalyticsType;
	}
	
}
