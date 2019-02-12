package com.corteva.model.component.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corteva.core.constants.CortevaConstant;
import com.corteva.model.component.bean.SitemapBean;
import com.day.cq.wcm.api.Page;

/**
 * This is the Sitemap Generation Model for generating the 
 * HTML sitemap for the root path given for a website.
 * 
 * @author Sapient
 *
 */

@Model(
		adaptables = {
				SlingHttpServletRequest.class,
				Resource.class
				},
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
		)
public class SitemapGenerationModel {
	
	/** 
	 * Logger Instantiation. */
	private static Logger log = LoggerFactory.getLogger(SitemapGenerationModel.class);
	
	/**
	 * Sitemap Page Title
	 */
	@Inject
	@Via("resource")
	@Named("title")
	private String title;
	
	/**
	 * The root path
	 */
	@Inject
	@Via("resource")
	@Named("rootPath")
	private String rootPath;
	
	/**
	 *  The resource resolver.
	 */
	@Inject
	private ResourceResolver resourceResolver;
	
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 * 		Sitemap page title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return rootPath
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * @param rootPath
	 * 		the root page from where the sitemap should be computed.
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	/**
	 * List for sitemap
	 */
	private List<SitemapBean> siteMapList = new ArrayList<>();
	 
	
	/**
	 *  @return siteMap
	 *  	get the list of elements in the HTML sitemap.
	 */
	public List<SitemapBean> getSitemap() {
		Resource root = resourceResolver.getResource(rootPath);
				
		if (root != null) {
			childPages(root);
		}
		return siteMapList;
	}
	
	/**
	 * @param root
	 * 		the root page from where the sitemap should be computed.
	 */
	private void childPages(Resource root) {
		Page rootPage = root.adaptTo(Page.class);
		if (rootPage != null) {
			Iterator<Page> listRootChildren = rootPage.listChildren();
			while (listRootChildren.hasNext()) {
				Page childPage = listRootChildren.next();
				Boolean isValid = childPage.isValid(); // check if the on/off spans the current time
				if (!isHidePage(childPage) && isValid) {
					siteMapList.add(addChildToList(childPage, isHidePagePath(childPage), CortevaConstant.ONE));
				}
			}
		}
	}
	
	/**
	 * @param childPage
	 * 		the current page
	 * @return hidePathPath
	 * 		to check if page path has to be hidden
	 */
	private Boolean isHidePagePath(Page childPage) {
		Boolean hidePagePath = false;
		Boolean isHideInNav = childPage.isHideInNav();
		String resourceType = childPage.getProperties().get("sling:resourceType", String.class);
		/**
		 * if the page has hideInNav property as true
		 * 							OR
		 * if page is made from redirect template, i.e, it is a non-landing page
		 * then HTML sitemap should not show only label and not be hyperlinked.
		 */
		if ((isHideInNav != null && isHideInNav) || (resourceType != null && resourceType.contains("redirect-page"))) {
				hidePagePath = true;
			}
		return hidePagePath;
	}
	
	/**
	 * @param childPage
	 * 		the current page
	 * @return hidePageFromHTMLSitemap
	 * to check if the page has to be hidden
	 */
	private Boolean isHidePage(Page childPage) {
		Boolean hidePage = childPage.getProperties().get("hideInHtmlSitemap", Boolean.class);
		if (hidePage != null) {
			return hidePage;
		}
		/** if the property is not set 
		 * the page should be shown by default **/
		return false;
	}
	
	/**
	 * @param childPage
	 * 			the current page
	 * @param hidePagePath
	 * 			if the page label should be hyper-linked
	 * @param level
	 * 			the level of the page in the hierarchy
	 * @return list of children 
	 * Add Child
	 */
	private SitemapBean addChildToList(Page childPage, boolean hidePagePath, Integer level) {
		SitemapBean childSiteMapBean = new SitemapBean();
		
		// Setting title, path and level of the current page
		childSiteMapBean.setTitle(childPage.getPageTitle());
		if (hidePagePath) {
			log.debug("Setting path to empty");
			childSiteMapBean.setPath("");
		} else {
			log.debug("Setting path to value");
			childSiteMapBean.setPath(childPage.getPath());
		}
		childSiteMapBean.setLevel(level);
		
		//Checking if the current page has child pages
		Iterator<Page> children = childPage.listChildren();
		if (children.hasNext()) {
			Integer childLevel = level + CortevaConstant.ONE; // the hierarchy of the child page will be 1 more than the parent
			List<SitemapBean> innerChildList = new ArrayList<>();
			while (children.hasNext()) {
				Page child = children.next();
				Boolean isValid = child.isValid(); //check if the on/off spans the current time
				if (!isHidePage(child) && isValid) {
					//recursive call
					innerChildList.add(addChildToList(child, isHidePagePath(child), childLevel));
				}
			}
			childSiteMapBean.setChild(innerChildList); //setting child to the current page
			
		}
		return childSiteMapBean;
	}
}
