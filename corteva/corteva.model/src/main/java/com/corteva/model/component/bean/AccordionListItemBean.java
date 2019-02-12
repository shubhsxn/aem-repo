package com.corteva.model.component.bean;

/**
 * The is the Accordion List Item bean class.
 * 
 * @author Sapient
 */
public class AccordionListItemBean {

	/** The itemHeaderAssetType. */
	private String altText;

	/** The image */
	private String image;

	/** The itemDetail. */
	private String itemDetail;

	/** The imageRenditionbean . */
	private ImageRenditionBean imageRenditionBean = new ImageRenditionBean();

	/** TheitemHeader. */
	private String itemHeader;

	/**
	 * Gets the altText.
	 *
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * Sets the altText.
	 *
	 * @param altText
	 *            the new altText
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}

	/**
	 * Sets the image.
	 *
	 * @param image
	 *            the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the image .
	 *
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Gets the itemDetail path.
	 *
	 * @return the itemDetail path
	 */
	public String getItemDetail() {
		return itemDetail;
	}

	/**
	 * Sets the itemDetail.
	 *
	 * @param itemDetail
	 *            the itemDetail
	 */
	public void setItemDetail(String itemDetail) {
		this.itemDetail = itemDetail;
	}

	/**
	 * Gets the itemHeader path.
	 *
	 * @return the itemHeader path
	 */
	public String getItemHeader() {
		return itemHeader;
	}

	/**
	 * Sets the link label.
	 *
	 * @param itemHeader
	 *            the new item Header
	 */
	public void setItemHeader(String itemHeader) {
		this.itemHeader = itemHeader;
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
