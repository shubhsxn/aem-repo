package com.corteva.service.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Registration.
 */
public class Registration {
	
	/** The content. */
	@JsonIgnore
	private String content;
	
	/** The type. */
	@JsonIgnore
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
	 * @param content the new content
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
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[content = " + content + ", type = " + type + "]";
	}
	
}
