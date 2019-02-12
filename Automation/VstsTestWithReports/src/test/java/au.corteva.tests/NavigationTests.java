package au.corteva.tests;
import java.lang.reflect.Method;
import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import au.corteva.pages.AuHomePage;
import com.corteva.tests.BaseTest;
import utils.ExtentReports.ExtentTestManager;
import utils.Listeners.TestListener;
import utilities.com.Log;

@Listeners({ TestListener.class })
@Epic("Regression Tests")
@Feature("Feature : Navigation")
@Story("Story 1: Navigation from Home Page")

public class NavigationTests extends BaseTest {
	
	@Test(description = " Verify home page - corteva.com.au")
	@Description("Verify whether Home page is opened")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_GoToAuHomePageMethod(Method method)   
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		// Verify Global Header on Home page
		auHomePage.goToAuCortevaHomePage();
		auHomePage.verifyAuHomePage("When Farmers Succeed, Everyone Wins.");
	}	
	
	@Test(description = "Navigation to OUR MERGER page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether OUR MERGER page is displaying")
	@Severity(SeverityLevel.CRITICAL)	
	public void verify_OurMergerNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurMergerPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuOurMergerPage("Our Merger");
	}
		
	@Test(description = "Test Name : Navigation to Our Purpose & Values page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Our Purpose & Values page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_OurPurposeAndValuesNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurPurposeAndValuesPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuOurPurposeAndValuesPage("A Commitment to Growing Progress");
	}
	
	@Test(description = "Test Name : Navigation to History page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether History page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HistoryNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuHistoryPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuHistoryPage("Our History");
	}
	
	@Test(description = "Test Name : Navigation to Diversity page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Diversity page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_DiversityNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuDiversityPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuDiversityPage("Diversity and Inclusion");
	}
	
	
	@Test(description = "Test Name : Navigation to In Our Community page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether In Our Community page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_InOurCommunityNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuInOurCommunityPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuInOurCommunityPage("Corteva Grows Food Security ANZ");
	}
	
	@Test(description = "Test Name : Navigation to Product Finder page", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Product Finder page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_ProductFinderNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuProductFinderPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuProductFinderPage("Filter by Product Category");
	}
	
	
	@Test(description = "Test Name : Navigation to Solutions page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Solutions page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_SolutionsNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuSolutionsPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuSolutionsPage("Sustain the Land and Grow Your Business");
	}
	
	
	@Test(description = "Test Name : Navigation to Broadacre page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Broadacre page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_BroadacreNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuBroadacrePage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuBroadacrePage("Broadacre Cropping");
	}
	
	
	@Test(description = "Test Name : Navigation to Horticulture page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Horticulture page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HorticultureNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuHorticulturePage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuHorticulturePage("New Horticulture Solutions");
	}
	
	// TBD-Residential Pest control,Woody Weeds & Grain Protection as they are redirecting to diff.page for now
	
	@Test(description = "Test Name : Navigation to Agronomy page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether Agronomy page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_AgronomyNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuAgronomyPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuAgronomyPage("Agronomy");
	}
	
	@Test(description = "Test Name : Navigation to News page - corteva.com.au", dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	@Description("Verify whether News page is displaying")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_NewsNavigationMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);		
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuNewsPage();
		// *************ASSERTIONS***********************
		auHomePage.verifyAuNewsPage("Press Releases");
	}
	
}