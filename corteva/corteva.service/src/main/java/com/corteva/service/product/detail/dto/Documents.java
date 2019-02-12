package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class Documents.
 */
public class Documents {
	
	/** The document. */
	private List<Document> document;
	
	/**
	 * Gets the document.
	 * @return the document
	 */
	public List<Document> getDocument() {
		return document;
	}
	
	/**
	 * Sets the document.
	 * @param document the document to set
	 */
	public void setDocument(List<Document> document) {
		this.document = document;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Documents [document=" + document + "]";
	}
}