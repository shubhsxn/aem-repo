package com.corteva.core.utils;

import java.util.List;

/**
 * The Class SearchInput.
 */
public class SearchInput {
	
	/** The root path. */
	private String rootPath;
	
	/** The type. */
	private String type;
	
	/** The tag list. */
	private List<String> tagList;
	
	/** The offset for search results. */
	private long offset;
	
	/**
	 * Gets the root path.
	 * @return the root path
	 */
	public String getRootPath() {
		return rootPath;
	}
	
	/**
	 * Sets the root path.
	 * @param rootPath the new root path
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Gets the tag list.
	 * @return the tag list
	 */
	public List<String> getTagList() {
		return tagList;
	}
	
	/**
	 * Sets the tag list.
	 * @param tagList the new tag list
	 */
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	/**
	 * Gets the offset
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * Sets the offset
	 * @param offset the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}
	
}
