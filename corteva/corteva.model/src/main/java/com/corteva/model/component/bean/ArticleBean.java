package com.corteva.model.component.bean;
/**
 * The is the Article Bean class.
 * 
 * @author Sapient
 */
public class ArticleBean {
	/** The custom Display Date. */
	private String customDisplayDate;

	/** The article type. */
	private String articleType;
	
	/** The article Title. */
	private String articleTitle;
	
	/** The primary Image. */
	private ImageRenditionBean primaryImage;

	/** The primary Image. */
	private ImageRenditionBean primaryImageMobile;
	
	/** The short Description. */
	private String shortDescription;
	
	/** articlePagePath. */
	private String articlePagePath;
	
	/** The external URL Label. */
	private String externalURLLabel;
	
	/** The external URL Configured. */
	private Boolean isExternalURL;
	
	/**
	 * @return the customDisplayDate
	 */
	public String getCustomDisplayDate() {
		return customDisplayDate;
	}

	/**
	 * @param customDisplayDate the customDisplayDate to set
	 */
	public void setCustomDisplayDate(String customDisplayDate) {
		this.customDisplayDate = customDisplayDate;
	}

	/**
	 * @return the articleType
	 */
	public String getArticleType() {
		return articleType;
	}

	/**
	 * @param articleType the articleType to set
	 */
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	/**
	 * @return the articleTitle
	 */
	public String getArticleTitle() {
		return articleTitle;
	}

	/**
	 * @param articleTitle the articleTitle to set
	 */
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	/**
	 * @return the primaryImage
	 */
	public ImageRenditionBean getPrimaryImage() {
		return primaryImage;
	}

	/**
	 * @param primaryImage the primaryImage to set
	 */
	public void setPrimaryImage(ImageRenditionBean primaryImage) {
		this.primaryImage = primaryImage;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	
	/**
	 * @return the articlePagePath
	 */
	public String getArticlePagePath() {
		return articlePagePath;
	}

	/**
	 * @param articlePagePath the articlePagePath to set
	 */
	public void setArticlePagePath(String articlePagePath) {
		this.articlePagePath = articlePagePath;
	}

	/**
	 * @return the externalURLLabel
	 */
	public String getExternalURLLabel() {
		return externalURLLabel;
	}

	/**
	 * @param externalURLLabel the externalURLLabel to set
	 */
	public void setExternalURLLabel(String externalURLLabel) {
		this.externalURLLabel = externalURLLabel;
	}

	/**
	 * @return the primaryImageMobile
	 */
	public ImageRenditionBean getPrimaryImageMobile() {
		return primaryImageMobile;
	}

	/**
	 * @param primaryImageMobile the primaryImageMobile to set
	 */
	public void setPrimaryImageMobile(ImageRenditionBean primaryImageMobile) {
		this.primaryImageMobile = primaryImageMobile;
	}

	/**
	 * @return the isExternalURL
	 */
	public Boolean getIsExternalURL() {
		return isExternalURL;
	}

	/**
	 * @param isExternalURL the isExternalURL to set
	 */
	public void setIsExternalURL(Boolean isExternalURL) {
		this.isExternalURL = isExternalURL;
	}

	
}
