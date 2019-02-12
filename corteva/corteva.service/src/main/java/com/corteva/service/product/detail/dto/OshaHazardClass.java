package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class OshaHazardClass.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OshaHazardClass {

	/** The content. */
	private String content;

	/** The type. */
	private String type;

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OshaHazardClass [content=" + content + ", type=" + type + "]";
	}
}
