package com.corteva.model.component.bean;

/**
 * The Class GalleryImageBean.
 */
public class GalleryImageBean {

	/** The imageCaption  */
	private String imageCaption;

	/** The image altText. */
	private String altText;
	
	/** The imageRenditionbean . */
	private ImageRenditionBean imageRenditionBean = new ImageRenditionBean();

	/**
	 * Gets the image caption.
	 *
	 * @return the image caption
	 */
	public String getImageCaption() {
		return imageCaption;
	}

	/**
	 * Sets the image caption.
	 *
	 * @param imageCaption
	 *            the new image caption
	 */
	public void setImageCaption(String imageCaption) {
		this.imageCaption = imageCaption;
	}

	/**
	 * Gets the alt text.
	 *
	 * @return the alt text
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * Sets the alt text.
	 *
	 * @param altText
	 *            the new alt text
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}
	
	/**
	 * @return the imageRenditionBean
	 */
	public ImageRenditionBean getImageRenditionBean() {
		return imageRenditionBean;
	}

	/**
	 * @param imageRenditionBean
	 *            the imageRenditionBean to set
	 */
	public void setImageRenditionBean(ImageRenditionBean imageRenditionBean) {
		this.imageRenditionBean = imageRenditionBean;
	}
}
