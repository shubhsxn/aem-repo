package com.corteva.model.component.bean;

import java.util.List;
import java.util.Map;

/**
 * The Class ProductFilterBean.
 */
public class ProductFilterBean {
	
	/** The product list. */
	private List<ProductFilterChildBean> productList;
	
	/** The filters. */
	private Map<String, ProductFilterListBean> filters;
	
	/** The hide filters. */
	private boolean hideFilters;
	
	/** The show product name filter. */
	private boolean showProductNameFilter;
	
	/**
	 * Gets the product list.
	 * @return the product list
	 */
	public List<ProductFilterChildBean> getProductList() {
		return productList;
	}
	
	/**
	 * Sets the product list.
	 * @param productList the new product list
	 */
	public void setProductList(List<ProductFilterChildBean> productList) {
		this.productList = productList;
	}
	
	/**
	 * @return the filters
	 */
	public Map<String, ProductFilterListBean> getFilters() {
		return filters;
	}
	
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<String, ProductFilterListBean> filters) {
		this.filters = filters;
	}
	
	/**
	 * @return the hideFilters
	 */
	public boolean isHideFilters() {
		return hideFilters;
	}
	
	/**
	 * @param hideFilters the hideFilters to set
	 */
	public void setHideFilters(boolean hideFilters) {
		this.hideFilters = hideFilters;
	}
	
	/**
	 * @return the showProductNameFilter
	 */
	public boolean isShowProductNameFilter() {
		return showProductNameFilter;
	}
	
	/**
	 * @param showProductNameFilter the showProductNameFilter to set
	 */
	public void setShowProductNameFilter(boolean showProductNameFilter) {
		this.showProductNameFilter = showProductNameFilter;
	}
	
}
