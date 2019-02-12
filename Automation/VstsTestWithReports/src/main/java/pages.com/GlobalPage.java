package pages.com;

import java.util.Properties;

import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utilities.com.Log;

public class GlobalPage extends BasePage {

	// *********Constructor*********
	public GlobalPage(Properties prop, WebDriver driver, WebDriverWait wait) {
		// TODO Auto-generated constructor stub
		super(driver, wait);

	}

	// *********Web Elements*********
	String MenuXpath = "//button[contains(text(),'Menu')]";// Mobile and other
															// devices
	// Global Header
	String globalHeaderClassName = "global-nav"; // Global header class name to
													// verify whether header in
													// displayed
	String hLogoXpath = "//a[@title='Logo']//img[@alt='Logo']"; // Corteva
																		// Logo
																		// on
																		// top
																		// left
																		// (right
																		// above
																		// Who
																		// we
																		// are)
	String hToplineClassName = "topline";
	String hToplineInvestorsCssSelector = ".global-nav [title='Investors']"; // Investors
																				// top
																				// right
																				// corner
	String hToplineCareersCssSelector = ".topline [title='Careers']"; // Careers
																		// top
																		// right
																		// corner
	String hToplineCountrySelectorCssSelector = "[title='Country Selector']"; // Country
																				// Selector
																				// top
																				// right
																				// corner
	//String hToplineSearchCssSelector = "[for='top-search']"; // Search
	
	
	//For Mobile - When user click on submit-search , top-search will be displayed 
	String hToplineMSearchXpath = "//label[@class='submit-search']"; 	
	//For Lattop - top-search will be displayed directly 
	String hToplineSearchXpath = "//input[@id='top-search']"; 
	
	
	String hClearSearchFormCssSelector = "v.reset-search"; // Enter something in
															// search then you
															// will see X to
															// clear

	// Global Footer
	String globalFooterClassName = "//footer[@class='band']"; // Global Footer
																// class name to
																// verify
																// whether
																// Footer in
																// displayed
	String fLogoXpath = "//a[@class='logo']"; // Logo on Footer
	// Social
	String fSocialXpath = "//nav[@class='social']"; // social on Footer
	String fFacebookXpath = "//a[@class='Facebook']"; // social-Facebook on
														// Footer
	String fTwitterXpath = "//a[@class='Twitter']"; // social-Twitter on Footer
	String fInstagramXpath = "//a[@class='Instagram']"; // social-Instagram on
														// Footer
	String fLinkedinXpath = "//a[@class='Linkedin']"; // social-Linkedin on
														// Footer
	String fYouTubeXpath = "//a[@class='YouTube']"; // social-YouTube on Footer
	// Primary
	String fContactCortevaAgriscienceXpath = "//a[@title='Contact Corteva Agriscience™']"; // Contact
																							// Corteva
																							// Agriscience
																							// on
																							// Footer
	String fMediaCenterXpath = "//a[@title='Media Center']"; // Media Center on
																// Footer
	String fInvestorsXpath = "//div[@class='primary']//a[@title='Investors'][contains(text(),'Investors')]"; // Investors
																												// on
																												// Footer
	String fCareersXpath = "//div[@class='primary']//a[@title='Careers'][contains(text(),'Careers')]"; // Accessibility
																										// on
																										// Footer
	String fAccessibilityXpath = "//a[@title='Accessibility']"; // social-YouTube
																// on Footer
	// Secondary
	String fTermsofUseXpath = "//a[@title='Terms of Use']"; // Terms of Use on
															// Footer
	String fPrivacyPolicyXpath = "//a[@title='Privacy Policy']"; // Privacy
																	// Policy on
																	// Footer
	String fCorporateGovernanceXpath = "//a[@title='Corporate Governance']"; // Corporate
																				// Governance
																				// on
																				// Footer
	String fCookiePreferencesLinktext = "Cookie Preferences"; // Cookie
																// Preferences
																// on
																// Footer
	String fCopyrightXpath = "//p[contains(text(),'Copyright © DuPont. All rights reserved.')]"; // Copyright
																									// on
																									// Footer
	String fTrademarkXpath = "//p[contains(text(),'® ™ Trademarks of DuPont, Dow AgroSciences and Pio')]"; // Trademark
																											// on
																											// Footer

	// Below links are not tested under Global Header as they are included in
	// Home page & don't want to duplicate them again. Below 5 lines of code can
	// be deleted
	String whoWeAreLinkText = "Who We Are";
	String ourMergerLinkText = "OUR MERGER";
	String productsAndServicesLinkText = "PRODUCTS & SERVICES";
	String blogLinkText = "BLOG";
	String mediaCenterLinkText = "MEDIA CENTER";

	// *********Page Methods*********

	// Verify Whether Global Header is displayed

	@Step("Verify Global Header")
	public void verifyGlobalHeader() {
		isDisplayed(By.className(globalHeaderClassName));
		isDisplayed(By.xpath(hLogoXpath));
		isDisplayed(By.className(hToplineClassName));
		isDisplayed(By.cssSelector(hToplineInvestorsCssSelector));
		isDisplayed(By.cssSelector(hToplineCareersCssSelector));
		isDisplayed(By.cssSelector(hToplineCountrySelectorCssSelector));
		isDisplayed(By.xpath(hToplineSearchXpath));
		// isDisplayed(By.cssSelector(hClearSearchFormCssSelector));
	}

	// Verify Whether Global Footer is displayed
	@Step("Verify Global Footer")
	public void verifyGlobalFooter() {
		isDisplayed(By.xpath(globalFooterClassName));
		isDisplayed(By.xpath(fLogoXpath));
		isDisplayed(By.xpath(fSocialXpath));
		isDisplayed(By.xpath(fFacebookXpath));
		isDisplayed(By.xpath(fTwitterXpath));
		isDisplayed(By.xpath(fInstagramXpath));
		isDisplayed(By.xpath(fLinkedinXpath));
		isDisplayed(By.xpath(fYouTubeXpath));

		isDisplayed(By.xpath(fContactCortevaAgriscienceXpath));
		isDisplayed(By.xpath(fMediaCenterXpath));
		isDisplayed(By.xpath(fInvestorsXpath));
		isDisplayed(By.xpath(fCareersXpath));
		isDisplayed(By.xpath(fAccessibilityXpath));

		isDisplayed(By.xpath(fTermsofUseXpath));
		isDisplayed(By.xpath(fPrivacyPolicyXpath));
		isDisplayed(By.xpath(fCorporateGovernanceXpath));
		isDisplayed(By.linkText(fCookiePreferencesLinktext));
		isDisplayed(By.xpath(fCopyrightXpath));
		isDisplayed(By.xpath(fTrademarkXpath));
	}
	
	// Verify Whether Top-Search is displayed
	@Step("Verify Top-Search on Gloabal Header")
	public void verifyTopSearchOnlyOnGlobalFooter() {
		isDisplayed(By.xpath(hToplineSearchXpath));
	}
	
	// Verify Whether submit-search is Enabled (For other than Laptop, Not required for Laptop). Note : isDisplayed is NOT WORKING on mobile
	@Step("Verify submit-search on Gloabal Header")
	public void verifySubmitSearchOnlyOnGlobalFooter() {
		isEnabled(By.xpath(hToplineMSearchXpath));
	}
}