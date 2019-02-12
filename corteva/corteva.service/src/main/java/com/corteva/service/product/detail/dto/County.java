package com.corteva.service.product.detail.dto;

/**
 * The Class County.
 */
public class County {
	
	/** The content. */
	private String content;
	
	/** The state. */
	private String state;
	
	/**
	 * Gets the content.
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the content.
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Gets the state.
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "County [content=" + content + ", state=" + state + "]";
	}
	
}
