package com.corteva.service.product.detail.dto;

/**
 * The Class Ai.
 */
public class Ai {
	
	/** The content. */
	private String content;
	
	/** The perc. */
	private String perc;
	
	/**
	 * Gets the content.
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the content.
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Gets the perc.
	 * @return the perc
	 */
	public String getPerc() {
		return perc;
	}
	
	/**
	 * Sets the perc.
	 * @param perc the perc to set
	 */
	public void setPerc(String perc) {
		this.perc = perc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ai [content=" + content + ", perc=" + perc + "]";
	}
}
