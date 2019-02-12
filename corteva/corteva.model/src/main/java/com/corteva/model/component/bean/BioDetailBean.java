package com.corteva.model.component.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * The is the Bio Detail bean class.
 * 
 * @author Sapient
 */
public class BioDetailBean  {

	/** The title. */
	private String title;

	/** The page Path. */
	private String pagePath;

	/** The Ranking. */
	private String ranking;

	/** The name. */
	private String name;

	/** The tags. */
	private List<String> tagsList = new ArrayList<>();

	/** The imageRenditionbean . */
	private ImageRenditionBean bioheadSpotImageBean = new ImageRenditionBean();

	/** The Image Alt Text. */
	private String altText;

		/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the bioheadSpotImageBean
	 */
	public ImageRenditionBean getBioheadSpotImageBean() {
		return bioheadSpotImageBean;
	}

	/**
	 * @param bioheadSpotImageBean
	 *            the bioheadSpotImageBean to set
	 */
	public void setBioheadSpotImageBean(ImageRenditionBean bioheadSpotImageBean) {
		this.bioheadSpotImageBean = bioheadSpotImageBean;
	}

	/**
	 * @return the pagePath
	 */
	public String getPagePath() {
		return pagePath;
	}

	/**
	 * @param pagePath
	 *            the pagePath to set
	 */
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

	/**
	 * @return the ranking
	 */
	public String getRanking() {
		return ranking;
	}

	/**
	 * @param ranking
	 *            the ranking to set
	 */
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the tagsList
	 */
	public List<String> getTagsList() {
		return tagsList;
	}

	/**
	 * @param tagsList
	 *            the tagsList to set
	 */
	public void setTagsList(List<String> tagsList) {
		this.tagsList = tagsList;
	}

	/**
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * @param altText the altText to set
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}
	
	
}
