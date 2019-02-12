package au.corteva.pages;
import java.util.Properties;

import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pages.com.BasePage;

public class AuHomePage extends BasePage {

	// *********Constructor*********
	public AuHomePage(Properties prop, WebDriver driver, WebDriverWait wait) {
		// TODO Auto-generated constructor stub
		super(driver, wait);

	}

	// *********Page Variables*********
	// String baseURL= "https://www.pp.corteva.com/";

	// *********Web Elements*********
	String menuXpath = "//button[contains(text(),'Menu')]";// Mobile and other
															// devices
	String mNavXpath="//div[@class='navigationdropdown navigationDropdown']//nav"; // '>' right next to 'Product & Services' in Mobile view
	//String menuCss = ".main button";
	String topSearchXpath = "//input[@id='top-search']"; // Top right corner
															// 'Search' on page
	String mTopSearchXpath = "//label[@class='submit-search']";
	// topSearch span tag Status After Entering Search Key. Status of tag will
	// be updated something like'5 results are available use up and down arrow
	// keys to navigate.'depending upon values dropped down on topSearch
	String topSearchStatusAfterEnterSearchKeyXpath = "//span[@role='status']";

	
	String whoWeAreGlobalXpath = "//a[contains(text(),'Who We Are')]";
	String ourMergerXpath = "//a[contains(text(),'Our Merger')]";
	String ourPurposeAndValuesXpath = "//a[contains(text(),'Our Purpose & Values')]";
	String historyXpath = "//a[contains(text(),'History')]";
	String diversityXpath = "//a[contains(text(),'Diversity')]";
	String inOurCommunityXpath = "//a[contains(text(),'In Our Community')]";
	String productFinderGlobalXpath = "//a[contains(text(),'Product Finder')]";
	String solutionsGlobalXpath = "//a[contains(text(),'Solutions')]";
	String broadacreXpath = "//a[contains(text(),'Broadacre')]";
	String horticultureXpath = "//a[contains(text(),'Horticulture')]";
	String residentialPestControlXpath = "//a[contains(text(),'Residential Pest control')]";
	String woodyWeedsXpath = "//a[contains(text(),'Woody Weeds')]";
	String grainProtectionXpath = "//a[contains(text(),'Grain Protection')]";
	String agronomyGlobalXpath = "//a[contains(text(),'Agronomy')]";
	String newsGlobalXpath = "//a[contains(text(),'News')]";



	
	String vHomePageXpath = "//h2[contains(text(),'When Farmers Succeed, Everyone Wins.')]";
	//String vWhoWeAreGlobalPageXpath = "//a[contains(text(),'Who We Are')]";
	String vOurMergerPageXpath = "//h2[contains(text(),'Our Merger')]";
	String vOurPurposeAndValuesPageXpath = "//h1[contains(text(),'A Commitment to Growing Progress')]";
	String vHistoryPageXpath = "//h1[contains(text(),'Our History')]";
	String vDiversityPageXpath = "//h1[contains(text(),'Diversity and Inclusion')]";
	String vInOurCommunityPageXpath = "//h1[contains(text(),'Corteva Grows Food Security ANZ')]";	
	String vProductFinderPageXpath = "//label[@for='productTypeSubFilter']";
	String vSolutionsGlobalPageXpath = "//h1[contains(text(),'Sustain the Land and Grow Your Business')]";
	String vBroadacrePageXpath = "//h1[contains(text(),'Broadacre Cropping')]";
	String vHorticulturePageXpath = "//h2[contains(text(),'New Horticulture Solutions')]";
	//TBD later as it is directing to https://www.corteva.com.au/products-and-solutions/crop-solutions/residential-pest-control.html
	String vResidentialPestControlPageXpath = "//h1[contains(text(),'Termites are hungry...')]";
	//TBD later as it is directing to http://www.woodyweedspecialists.com.au/
	String vWoodyWeedsPageXpath = "//a[contains(text(),'Woody Weeds')]";
	//TBD later as it is directing to https://www.conserveonfarm.com.au/
	String vGrainProtectionPageXpath = "//a[contains(text(),'Grain Protection')]";
	
	String vAgronomyGlobalPageXpath = "//h1[contains(text(),'Agronomy')]";
	String vNewsPageXpath = "//h2[contains(text(),'Press Releases')]";


	// *********Page Methods*********

	// Go to Corteva Home page
	@Step("Open Corteva.com.au Home page")
	public void goToAuCortevaHomePage() {
		driver.navigate().to(prop.getProperty("baseURL"));
	}

	// Verify Verifying Whether we are in Home Page
	@Step("Verifying Whether we are in Home Page")
	public void verifyAuHomePage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vHomePageXpath)),expectedText);	
	}
	
	// Click on WHO WE ARE (On GLOBAL NAVIGATION)
	@Step("Click on WHO WE ARE")
	public void goToAuWhoWeArePage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(whoWeAreGlobalXpath));
		} else
			click(By.xpath(whoWeAreGlobalXpath));
	}	
		
	// Mouse Hover on WHO WE ARE (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on WHO WE ARE")
	public void mouseHoverOnAuWhoWeAre() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));				
			click(By.xpath(mNavXpath));			
			//click(By.linkText(whoWeAreGlobalXpath));
		} else			
			mouseHover(By.xpath(whoWeAreGlobalXpath));
	}
	
	
	// Go to OUR MERGER Page
	@Step("Go to OUR MERGER Page")
	public void goToAuOurMergerPage() {
		click(By.xpath(ourMergerXpath));
	}
				
	// Verify Verifying Whether we are in OUR MERGER Page
	@Step("Verifying Whether we are in OUR MERGER Page")
	public void verifyAuOurMergerPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vOurMergerPageXpath)),expectedText,"We are NOT in OUR MERGER Page.");
	}
	
	
	// Go to Our Purpose And Value Page
	@Step("Go to Our Purpose And Value Page")
	public void goToAuOurPurposeAndValuesPage() {
		click(By.xpath(ourPurposeAndValuesXpath));
	}
		
	// Verify Verifying Whether we are in Our Purpose And Value Page
	@Step("Verifying Whether we are in Our Purpose And Value Page")
	public void verifyAuOurPurposeAndValuesPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vOurPurposeAndValuesPageXpath)),expectedText);		
	}
	
	// Go to History Page
	@Step("Go to History Page")
	public void goToAuHistoryPage() {
		click(By.xpath(historyXpath));
	}
		
	// Verify Verifying Whether we are in History Page
	@Step("Verifying Whether we are in History Page")
	public void verifyAuHistoryPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vHistoryPageXpath)),expectedText);		
	}
	
	// Go to Diversity Page
	@Step("Go to Diversity Page")
	public void goToAuDiversityPage() {
		click(By.xpath(diversityXpath));
	}
		
	// Verify Verifying Whether we are in Diversity Page
	@Step("Verifying Whether we are in Diversity Page")
	public void verifyAuDiversityPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vDiversityPageXpath)),expectedText);		
	}
	
	// Go to In Our Community Page
	@Step("Go to In Our Community Page")
	public void goToAuInOurCommunityPage() {
		click(By.xpath(inOurCommunityXpath));
	}
		
	// Verify Verifying Whether we are in In Our Community Page
	@Step("Verifying Whether we are in In Our Community Page")
	public void verifyAuInOurCommunityPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vInOurCommunityPageXpath)),expectedText);		
	}
	
	
	
	// Click on PRODUCT FINDER (On GLOBAL NAVIGATION)
	@Step("Click on PRODUCT FINDER")
	public void goToAuProductFinderPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(productFinderGlobalXpath));
		} else
			click(By.xpath(productFinderGlobalXpath));
	}	
		
	// Mouse Hover on PRODUCT FINDER (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on PRODUCT FINDER")
	public void mouseHoverOnAuProductFinder() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));				
			click(By.xpath(mNavXpath));			
			//click(By.linkText(whoWeAreGlobalXpath));
		} else			
			mouseHover(By.xpath(productFinderGlobalXpath));
	}

	// Verify Verifying Whether we are in PRODUCT FINDER Page
	@Step("Verifying Whether we are in PRODUCT FINDER Page")
	public void verifyAuProductFinderPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vProductFinderPageXpath)),expectedText);		
	}
	
	// Click on SOLUTIONS (On GLOBAL NAVIGATION)
	@Step("Click on SOLUTIONS")
	public void goToAuSolutionsPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(solutionsGlobalXpath));
		} else
			click(By.xpath(solutionsGlobalXpath));
	}	
		
	// Mouse Hover on SOLUTIONS (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on SOLUTIONS")
	public void mouseHoverOnAuSolutions() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));				
			click(By.xpath(mNavXpath));			
			//click(By.linkText(whoWeAreGlobalXpath));
		} else			
			mouseHover(By.xpath(solutionsGlobalXpath));
	}

	// Verify Verifying Whether we are in SOLUTIONS Page
	@Step("Verifying Whether we are in SOLUTIONS Page")
	public void verifyAuSolutionsPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vSolutionsGlobalPageXpath)),expectedText);		
	}
	

	// Go to Broadacre Page
	@Step("Go to Broadacre Page")
	public void goToAuBroadacrePage() {
		click(By.xpath(broadacreXpath));
	}
	
	// Verify Verifying Whether we are in Broadacre Page
	@Step("Verifying Whether we are in Broadacre Page")
	public void verifyAuBroadacrePage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vBroadacrePageXpath)),expectedText);		
	}
	
	
	// Go to Horticulture Page
	@Step("Go to Horticulture Page")
	public void goToAuHorticulturePage() {
		click(By.xpath(horticultureXpath));
	}
	
	// Verify Verifying Whether we are in Horticulture Page
	@Step("Verifying Whether we are in Horticulture Page")
	public void verifyAuHorticulturePage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vHorticulturePageXpath)),expectedText);		
	}
	
	// Go to Residential Pest control Page
	@Step("Go to Residential Pest control Page")
	public void goToAuResidentialPestControlPage() {
		click(By.xpath(residentialPestControlXpath));
	}
	
	// Verify Verifying Whether we are in Residential Pest control Page
	@Step("Verifying Whether we are in Residential Pest control Page")
	public void verifyAuResidentialPestControlPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vResidentialPestControlPageXpath)),expectedText);		
	}
	
	// Go to Woody Weeds Page
	@Step("Go to Woody Weeds Page")
	public void goToAuWoodyWeedsPage() {
		click(By.xpath(woodyWeedsXpath));
	}
	
	// Verify Verifying Whether we are in Grain Protection Page
	@Step("Verifying Whether we are in Woody Weeds Page")
	public void verifyAuWoodyWeedsPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vWoodyWeedsPageXpath)),expectedText);		
	}
	
	// Go to Grain Protection Page
	@Step("Go to Grain Protection Page")
	public void goToAuGrainProtectionPage() {
		click(By.xpath(grainProtectionXpath));
	}
	
	// Verify Verifying Whether we are in Grain Protection Page
	@Step("Verifying Whether we are in Grain Protection Page")
	public void verifyAuGrainProtectionPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vGrainProtectionPageXpath)),expectedText);		
	}
	
	
	// Click on agronomy (On GLOBAL NAVIGATION)
	@Step("Click on agronomy")
	public void goToAuAgronomyPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(agronomyGlobalXpath));
		} else
			click(By.xpath(agronomyGlobalXpath));
	}	
		
	// Mouse Hover on agronomy (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on agronomy")
	public void mouseHoverOnAuAgronomy() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));				
			click(By.xpath(mNavXpath));			
			//click(By.linkText(whoWeAreGlobalXpath));
		} else			
			mouseHover(By.xpath(agronomyGlobalXpath));
	}

	// Verify Verifying Whether we are in agronomy Page
	@Step("Verifying Whether we are in agronomy Page")
	public void verifyAuAgronomyPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vAgronomyGlobalPageXpath)),expectedText);		
	}
	
	// Click on News (On GLOBAL NAVIGATION)
	@Step("Click on News")
	public void goToAuNewsPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(newsGlobalXpath));
		} else
			click(By.xpath(newsGlobalXpath));
	}	
		
	// Mouse Hover on News (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on News")
	public void mouseHoverOnAuNews() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));				
			click(By.xpath(mNavXpath));			
			//click(By.linkText(whoWeAreGlobalXpath));
		} else			
			mouseHover(By.xpath(newsGlobalXpath));
	}

	// Verify Verifying Whether we are in News Page
	@Step("Verifying Whether we are in News Page")
	public void verifyAuNewsPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(vNewsPageXpath)),expectedText);		
	}
}