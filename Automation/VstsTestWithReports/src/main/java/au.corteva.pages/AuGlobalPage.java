package au.corteva.pages;

import java.util.Properties;

import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utilities.com.Log;
//Corteva Packages
import pages.com.BasePage;

public class AuGlobalPage extends BasePage {
	public AuGlobalPage(Properties prop, WebDriver driver, WebDriverWait wait) {
		// TODO Auto-generated constructor stub
		super(driver, wait);
	}
	// *********Web Elements*********
	// Mobile and other devices
	String MenuXpath = "//button[contains(text(),'Menu')]";
	
	//**** Global header class name to verify whether header in displayed
	String globalHeaderClassName = "global-nav"; 
	// Corteva Logo on top left (right above Who we are)
	String hLogoXpath = "//a[@title='Logo']//img[@alt='Logo']"; 
	String hToplineClassName = "topline";
	String hToplineLabelsAndSDSXpath = "//nav[@class='topline']//a[@title='Labels & SDS'][contains(text(),'Labels & SDS')]"; 
	String hToplineContactUsGlobalXpath = "//nav[@class='topline']//a[@title='Contact Us'][contains(text(),'Contact Us')]"; 
	String hToplineAustraliaXpath = "//nav[@class='topline']//a[@title='Australia']"; 	
	
	//For Mobile - When user click on submit-search , top-search will be displayed 
	String hToplineMSearchXpath = "//label[@class='submit-search']"; 	
	//For Lattop - top-search will be displayed directly 
	String hToplineSearchXpath = "//input[@id='top-search']"; 
	 // Enter something in  search then you will see X to clear
	String hClearSearchFormXpath = "//input[@title='Clear search form']";

	
	//****Global Footer class name to verify whether Footer in displayed
	String globalFooterClassName = "//footer[@class='band']"; 
	String fLogoXpath = "//a[@class='logo']"; // Logo on Footer
	// Social
	String fSocialXpath = "//nav[@class='social']"; // social on Footer
	String fFacebookXpath = "//a[@class='Facebook']"; // social-Facebook on	footer
	String fTwitterXpath = "//a[@class='Twitter']"; // social-Twitter on Footer	
	//String fYouTubeXpath = "//a[@class='YouTube']"; // social-YouTube on Footer
	
	
	//****Primary
	// News And Media
	String fNewsAndMediaXpath = "//a[@title='News & Media']";	
	//Online Learning
	String fOnlineLearningXpath = "//a[@title='Online Learning']";
	//Investors
	String fInvestorsXpath = "//a[@title='Investors']";
	//Careers
	String fCareersXpath = "//a[@title='Careers']";
	
	// ****Secondary	
	String fTermsOfUseXpath = "//a[@title='Terms of Use']"; // Terms of Use
	String fPrivacyPolicyXpath = "//a[@title='Privacy Policy']"; // PrivacyPolicy
	String fCookiePreferencesLinktext = "Cookie Preferences";
	
	
	String fCopyRightXpath = "//p[contains(text(),'® ™ Trademarks of DuPont, Dow AgroSciences and Pio')]";
	//String fTrademarkSupXpath = "//p[contains(text(),'® ™ DuPont, Dow AgroSciences 및 Pioneer 및 계열사 또는 해당')]";
	 

	// *********Page Methods*********
	// Verify Whether Global Header is displayed
	@Step("Verify Global Header")
	public void verifyAuGlobalHeader() {
		//Assert.assertEquals(isDisplayed(By.className(globalHeaderClassName)), Boolean.valueOf("true"),"We are NOT in OUR MERGER Page. ");
		//Assert.assertTrue(isDisplayed(By.className(globalHeaderClassName))); //Result is like - expected [true] but found [false]. Will Error instead of Broken
		isDisplayed(By.xpath(hLogoXpath));  //You will get Broken (No error)
		isDisplayed(By.className(hToplineClassName));
		isDisplayed(By.xpath(hToplineLabelsAndSDSXpath));
		isDisplayed(By.xpath(hToplineContactUsGlobalXpath));
		isDisplayed(By.xpath(hToplineAustraliaXpath));
		isDisplayed(By.xpath(hToplineSearchXpath));
		isDisplayed(By.xpath(hClearSearchFormXpath));		
	}

	// Verify Whether Global Footer is displayed
	@Step("Verify Global Footer")
	public void verifyAuGlobalFooter() {
		isDisplayed(By.xpath(globalFooterClassName));
		isDisplayed(By.xpath(fLogoXpath));		
		isDisplayed(By.xpath(fSocialXpath));
		isDisplayed(By.xpath(fFacebookXpath));
		isDisplayed(By.xpath(fTwitterXpath));
		//isDisplayed(By.xpath(fYouTubeXpath));  		
		isDisplayed(By.xpath(fNewsAndMediaXpath  ));
		isDisplayed(By.xpath(fOnlineLearningXpath  ));
		isDisplayed(By.xpath(fInvestorsXpath ));
		isDisplayed(By.xpath(fCareersXpath ));
		
		isDisplayed(By.xpath(fTermsOfUseXpath ));
		isDisplayed(By.xpath(fPrivacyPolicyXpath));
		isDisplayed(By.linkText(fCookiePreferencesLinktext));
		
		isDisplayed(By.xpath(fCopyRightXpath ));
		//isDisplayed(By.linkText(fTrademarkSupXpath ));
	}
	
	// Verify Whether Top-Search is displayed
	@Step("Verify Top-Search on Gloabal Header")
	public void verifyAuTopSearchOnlyOnGlobalFooter() {
		isDisplayed(By.xpath(hToplineSearchXpath));
	}
	
	// Verify Whether submit-search is Enabled (For other than Laptop, Not required for Laptop). Note : isDisplayed is NOT WORKING on mobile
	@Step("Verify submit-search on Gloabal Header")
	public void verifyAuSubmitSearchOnlyOnGlobalFooter() {
		isEnabled(By.xpath(hToplineMSearchXpath));
	}
}