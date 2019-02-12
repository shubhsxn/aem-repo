/**
 * This is the Abstract Sling Model for injecting resource resolver, base
 * configuration service etc. This class should be extended by all the Sling
 * models.
 * 
 * @author Sapient
 *
 */
package com.corteva.model.component.bean;

import java.util.List;

/**
 * This is the Abstract Sling Model for injecting resource resolver, base
 * configuration service etc. This class should be extended by all the Sling
 * models.
 * 
 * @author Sapient
 *
 */

public class SitemapBean {
	/**
	 * title
	 */
	private String title;
	
	/**
	 * path
	 */
	private String path;
	
	/**
	 * depth or level of the child in the hierarchy
	 */
	private Integer level;
	
	/**
	 * children
	 */
	private List<SitemapBean> child;
	
	/**
	 * @return title
	 * for title*/
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title
	 * for title*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 *  @return path
	 * for path*/
	public String getPath() {
		return path;
	}
	
	/**
	 * @param path
	 * for path*/
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return level
	 * 		the depth of the child in the hierarchy
	 */
	public Integer getLevel() {
		return level;
	}
	
	/**
	 * @param level
	 * 		the depth of the child in the hierarchy
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
	 * @return child
	 * 		children
	 */
	
	public List<SitemapBean> getChild() {
		return child;
	}	/**
	 * @param child
	 * 		child page
	 */
	public void setChild(List<SitemapBean> child) {
		this.child = child;
	}


}
