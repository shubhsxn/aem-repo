package com.corteva.service.product.detail.dto;

/**
 * The Class Document.
 */
public class Document {

	/** The description. */
	private String description;

	/** The filename. */
	private String filename;

	/** The states. */
	private String states;

	/** The type. */
	private String type;

	/** The display date. */
	private String displayDate;

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the filename.
	 * 
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename.
	 * 
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Gets the states.
	 * 
	 * @return the states
	 */
	public String getStates() {
		return states;
	}

	/**
	 * Sets the states.
	 * 
	 * @param states
	 *            the states to set
	 */
	public void setStates(String states) {
		this.states = states;
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

	/**
	 * Gets the display date.
	 *
	 * @return the display date
	 */
	public String getDisplayDate() {
		return displayDate;
	}

	/**
	 * Sets the display date.
	 *
	 * @param displayDate
	 *            the new display date
	 */
	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Document [description=" + description + ", filename=" + filename + ", states=" + states + ", type="
				+ type + ", displayDate=" + displayDate + "]";
	}
}