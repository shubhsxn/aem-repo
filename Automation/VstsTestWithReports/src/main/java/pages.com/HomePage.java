package pages.com;

import java.util.Properties;

import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class HomePage extends BasePage {

	// *********Constructor*********
	public HomePage(Properties prop, WebDriver driver, WebDriverWait wait) {
		// TODO Auto-generated constructor stub
		super(driver, wait);

	}

	// *********Page Variables*********
	// String baseURL= "https://www.pp.corteva.com/";

	// *********Web Elements*********
	String menuXpath = "//button[contains(text(),'Menu')]";// Mobile and other devices
	String mNavXpath="//div[@class='navigationdropdown navigationDropdown']//nav"; // '>' right next to 'Product & Services' in Mobile view
						
	String topSearchXpath = "//input[@id='top-search']"; // Top right corner
															// 'Search' on page
	String mTopSearchXpath = "//label[@class='submit-search']";
	String topSearchStatusAfterEnterSearchKeyXpath = "//span[@role='status']"; // topSearch
																				// span
																				// tag
																				// Status
																				// After
																				// Entering
																				// Search
																				// Key.
																				// Status
																				// of
																				// tag
																				// will
																				// be
																				// updated
																				// something
																				// like
																				// '5
																				// results
																				// are
																				// available,
																				// use
																				// up
																				// and
																				// down
																				// arrow
																				// keys
																				// to
																				// navigate.'
																				// depending
																				// upon
																				// values
																				// dropped
																				// down
																				// on
																				// topSearch

	String whoWeAreGlobalXpath = "//a[contains(text(),'Who We Are')]";
	String ourMergerLinkText = "OUR MERGER";
	String ourPurposeAndValuesLinkText = "OUR PURPOSE & VALUES";
	String leadershipLinkText = "LEADERSHIP";
	String historyLinkText = "HISTORY";
	String diversityLinkText = "DIVERSITY";
	String communitiesLinkText = "COMMUNITIES";
	String ourImpactLinkText = "OUR IMPACT";  //
	
	String ourImpactGlobalXpath = "//a[@target='_self'][contains(text(),'Our Impact')]";
	String ourImpactSubXpath = "//h2[contains(text(),'Protecting and preserving the source of our food a')]";
	String innovationLinkText = "INNOVATION";
	String sustainabilityLinkText = "SUSTAINABILITY";
	String productsAndServicesLinkText = "PRODUCTS & SERVICES";
	String blogLinkText = "BLOG";
	String mediaCenterLinkText = "MEDIA CENTER";

	
	String homePageXpath = "//h2[contains(text(),'Protecting and preserving the source of our food a')]";
	String verifyOurMergerPageXpath = "//h2[contains(text(),'Our Merger')]";
	String ourPurposeAndValuePageXpath = "//h1[contains(text(),'A Commitment to Growing Progress')]";
	String leadershipPageXpath = "//label[@for='Corteva's Leadership Teams']";
	String historyPageXpath = "//h1[contains(text(),'Our History')]";
	String diversityPageXpath = "//h1[contains(text(),'Diversity and Inclusion')]";
	String communitiesPageXpath = "//h1[contains(text(),'Community Commitments and Education Outreach')]";
	String ourImpactPageXpath = "//h1[contains(text(),'Enriching Lives Across the World')]";
	String innovationPageXpath = "//h1[contains(text(),'Powering Innovation in Agriscience')]";
	String sustainabilityPageXpath = "//h1[contains(text(),'Our Approach to Sustainability')]";
	String productsAndServicesPageXpath = "/h1[contains(text(),'Products & Services')]";
	String blogPageXpath = "//h1[contains(text(),'Our Perspective')]";
	String mediaCenterPageXpath = "//h1[contains(text(),'Media Center')]";

	// *********Page Methods*********

	// Go to Corteva Home page
	@Step("Open Corteva Home page")
	public void goToCortevaHomePage() {
		//driver.get(prop.getProperty("baseURL"));
		driver.navigate().to(prop.getProperty("baseURL"));
	}
	
	
	// Verify Verifying Whether we are in Home Page
	@Step("Verifying Whether we are in Home Page")
	public void verifyHomePage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(homePageXpath)),expectedText);		
	}
	
	// Click on WHO WE ARE (On GLOBAL NAVIGATION)
	@Step("Click on WHO WE ARE")
	public void clickOnWhoWeAre() {
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
	public void mouseHoverOnWhoWeAre() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			click(By.xpath(mNavXpath));			
			//click(By.linkText(productsAndSolutionsLinkText));
		} else			
			mouseHover(By.xpath(whoWeAreGlobalXpath));
	}
	
	
	
	@Step("Go to OUR MERGER Page")
	// Go to OUR MERGER Page
	public void goToOurMergerPage() {
		click(By.linkText(ourMergerLinkText));
	}

	// Verify Verifying Whether we are in Our Merger Page
	@Step("Verifying Whether we are in Our Merger Page")
	public void verifyOurMergerPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(verifyOurMergerPageXpath)),
				expectedText);
	}

	// Go to OUR PURPOSE & VALUES Page
	@Step("Go to OUR PURPOSE & VALUES Page")
	public void goToOurPurposeAndValuesPage() {
		click(By.linkText(ourPurposeAndValuesLinkText));
	}

	// Verify Verifying Whether we are in OUR PURPOSE & VALUES Page
	@Step("Verifying Whether we are in LEADERSHIP Page")
	public void verifyOurPurposeAndValuePage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(ourPurposeAndValuePageXpath)),
				expectedText);
	}

	// Go to LEADERSHIP Page
	@Step("Go to LEADERSHIP Page")
	public void goToLeadershipPage() {
		click(By.linkText(leadershipLinkText));
	}

	// Verify Whether we are in leadership Page
	@Step("Verifying Whether we are in LEADERSHIP Page")
	public void verifyLeadershipPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(leadershipPageXpath)),
				expectedText);
	}

	// Go to HISTORY Page
	@Step("Go to HISTORY Page")
	public void goToHistoryPage() {
		click(By.linkText(historyLinkText));
	}

	// Verify Whether we are in OUR PURPOSE & VALUES Page
	@Step("Verifying Whether we are in HISTORY Page")
	public void verifyHistoryPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(historyPageXpath)),
				expectedText);
	}

	// Go to DIVERSITY Page
	@Step("Go to DIVERSITY Page")
	public void goToDiversityPage() {
		click(By.linkText(diversityLinkText));
	}

	// Verify Whether we are in OUR PURPOSE & VALUES Page
	@Step("Verifying Whether we are in DIVERSITY Page")
	public void verifyDiversityPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(diversityPageXpath)),
				expectedText);
	}

	// Go to COMMUNITIES Page
	@Step("Go to COMMUNITIES Page")
	public void goToCommunitiesPage() {
		click(By.linkText(communitiesLinkText));
	}

	// Verify Whether we are in OUR PURPOSE & VALUES Page
	@Step("Verifying Whether we are in COMMUNITIES Page")
	public void verifyCommunitiesPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(communitiesPageXpath)),
				expectedText);
	}

	
	
	// Click on OUR IMPACT (On GLOBAL NAVIGATION)
	@Step("Click on OUR IMPACT")
	public void clickOnOurImpact() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			//click(By.xpath("//button[contains(text(),'Menu')]"));
			click(By.xpath(ourImpactGlobalXpath));
		} else
			click(By.xpath(ourImpactGlobalXpath));
	}
		
	// Mouse Hover on OUR IMPACT (On GLOBAL NAVIGATION)
	@Step("Mouse Hover on OUR IMPACT")
	public void mouseHoverOnOurImpact() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			click(By.xpath(mNavXpath));			
			//click(By.linkText(productsAndSolutionsLinkText));
		} else			
			mouseHover(By.xpath(ourImpactGlobalXpath));
	}
	
	// Go to OUR IMPACT (On SUB NAVIGATION)
	@Step("Go to OUR IMPACT Page")
	// Go to OUR IMPACT Page
	public void goToOurImpactPage() {
		click(By.xpath(ourImpactSubXpath));
	}
	

	// Verify Whether we are in OUR IMPACT Page
	@Step("Verifying Whether we are in COMMUNITIES Page step...")
	public void verifyOurImpactPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(ourImpactPageXpath)),
				expectedText);
	}

	// Go to INNOVATION Page
	@Step("Go to INNOVATION Page")
	public void goToInnovationPage() {
		click(By.linkText(innovationLinkText));
	}

	// Verify Whether we are in INNOVATION Page
	@Step("Verifying Whether we are in INNOVATION Page")
	public void verifyInnovationPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(innovationPageXpath)),
				expectedText);
	}

	// Go to SUSTAINABILITY Page
	@Step("Go to SUSTAINABILITY Page")
	public void goToSustainabilityPage() {
		click(By.linkText(sustainabilityLinkText));
	}

	// Verify Whether we are in SUSTAINABILITY Page
	@Step("Verifying Whether we are in SUSTAINABILITY Page")
	public void verifySustainabilityPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(sustainabilityPageXpath)),
				expectedText);
	}

	// Go to PRODUCTS & SERVICES Page
	@Step("Go to PRODUCTS & SERVICES Step...")
	public void goToProductsAndServicesPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			click(By.linkText(productsAndServicesLinkText));
		} else
			click(By.linkText(productsAndServicesLinkText));
	}

	// Verify Whether we are in PRODUCTS & SERVICES Page
	@Step("Verifying Whether we are in PRODUCTS & SERVICES Page")
	public void verifyProductsAndServicesPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(productsAndServicesPageXpath)),
				expectedText);
	}

	// Go to BLOG Page
	@Step("Go to BLOG Page")
	public void goToBlogPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			click(By.linkText(blogLinkText));
		} else
			click(By.linkText(blogLinkText));

	}

	// Verify Whether we are in BLOG Page
	@Step("Verifying Whether we are in BLOG Page")
	public void verifyBlogPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(blogPageXpath)), expectedText);
	}

	// Go to MEDIA CENTER Page
	@Step("Go to MEDIA CENTER Page")
	public void goToMediaCenterPage() {
		// If condition for Mobile and other devices
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			click(By.xpath(menuXpath));
			click(By.linkText(mediaCenterLinkText));
		} else
			click(By.linkText(mediaCenterLinkText));
	}

	// Verify Whether we are in MEDIA CENTER Page
	@Step("Verifying Whether we are in MEDIA CENTER Page")
	public void verifyMediaCenterPage(String expectedText) {
		Assert.assertEquals(readText(By.xpath(mediaCenterPageXpath)),
				expectedText);
	}

	@Step("Click on top-search")
	// Click on top-search
	public void clickOnTopSearch() {
	// If condition for Mobile and other devices
	if (!prop.getProperty("deviceName").equals("Laptop")) {
		click(By.xpath(mTopSearchXpath));
	} else 
		click(By.xpath(topSearchXpath));		
	}

	@Step("Enter Search Key in top-search")
	// Click on top-search
	public void enterSearchKeyOnTopSearch(String searchKey) {
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(topSearchXpath)));		
		writeText(By.xpath(topSearchXpath), searchKey);
	}

	@Step("Verify results on top-search")
	// Click on top-search
	public void verifyResultsOnTopSearch(String expectedText) {
		// TBD - Handle wait(textToBe) within 'readtext' method by passing
		// 'textToBe' as a parameter
		wait.until(ExpectedConditions.textToBe(
				By.xpath(topSearchStatusAfterEnterSearchKeyXpath),
				expectedText));
		Assert.assertEquals(readText(By.xpath(topSearchStatusAfterEnterSearchKeyXpath)),
				expectedText);

	}
}