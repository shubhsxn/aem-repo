package au.corteva.tests;

import io.qameta.allure.*;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.corteva.tests.BaseTest;

import utilities.com.Log;
import utils.Listeners.TestListener;

import java.lang.reflect.Method;

//Corteva Packages
import au.corteva.pages.AuGlobalPage;
import au.corteva.pages.AuHomePage;

@Listeners({ TestListener.class })
@Epic("Regression Tests")
@Feature("Feature : Verify Global Header And Footer on every Page")
@Test(description = " Verify home page", singleThreaded = true)
public class GlobalHeaderAndFooterTest extends BaseTest {
	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify home page - corteva.com.au")
	@Description("Verify whether Home page is opened")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_GoToAuHomePageMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		// *************PAGE METHODS********************
		// Verify Global Header on Home page
		auHomePage.goToAuCortevaHomePage();
		auHomePage.verifyAuHomePage("When Farmers Succeed, Everyone Wins.");
	}

	// **********Header**********
	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Home page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Home page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HomePageGlobalHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Our Merger page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Our Merger page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_ProductsAndServicesPageGlobalHeaderMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurMergerPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Our Purpose & Values page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Our Purpose & Values page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_OurPurposeAndValuesPageHeaderMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurPurposeAndValuesPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on History page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on History page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HistoryPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuHistoryPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Diversity page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Diversity page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_DiversityPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuDiversityPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on In Our Community page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on In Our Community page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_InOurCommunityPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuInOurCommunityPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Product Finder page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Product Finder page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_ProductFinderPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuProductFinderPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Solutions page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Solutions page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_SolutionsPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuSolutionsPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Broadacre page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Broadacre page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_BroadacrePageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuBroadacrePage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Horticulture page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Horticulture page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HorticulturePageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuHorticulturePage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	/*
	 * @Story("Story 1 - Verify Global Header on every Page")
	 * @Test(description = " Verify Global Header on Residential Pest control page" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Header is appearing properly on Residential Pest control page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_ResidentialPestControlPageHeaderMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS*************
	 * AuHomePage auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage();
	 * auHomePage.mouseHoverOnAuSolutions(); auHomePage.goToAuResidentialPestControlPage(); auGlobalPage.verifyAuGlobalHeader(); }
	 * @Story("Story 1 - Verify Global Header on every Page")
	 * @Test(description = " Verify Global Header on Woody Weeds page - corteva.com.au" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Header is appearing properly on Woody Weeds page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_WoodyWeedsPageHeaderMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS************* AuHomePage
	 * auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage(); auHomePage.mouseHoverOnAuSolutions();
	 * auHomePage.goToAuWoodyWeedsPage(); auGlobalPage.verifyAuGlobalHeader(); }
	 * @Story("Story 1 - Verify Global Header on every Page")
	 * @Test(description = " Verify Global Header on Grain Protection page - corteva.com.au" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Header is appearing properly on Grain Protection page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_GrainProtectionPageHeaderMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS************* AuHomePage
	 * auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage(); auHomePage.mouseHoverOnAuSolutions();
	 * auHomePage.goToAuGrainProtectionPage(); auGlobalPage.verifyAuGlobalHeader(); }
	 */

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on Agronomy page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on Agronomy page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_AgronomyPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuAgronomyPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	@Story("Story 1 - Verify Global Header on every Page")
	@Test(description = " Verify Global Header on News page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Header is appearing properly on News page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_NewsPageHeaderMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuNewsPage();
		auGlobalPage.verifyAuGlobalHeader();
	}

	// **********Footer**********

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Home page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Home page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HomePageGlobalFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Our Merger page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Our Merger page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_ProductsAndServicesPageGlobalFooterMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurMergerPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Our Purpose & Values page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Our Purpose & Values page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_OurPurposeAndValuesPageFooterMethod(Method method)
			throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuOurPurposeAndValuesPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on History page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on History page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HistoryPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuHistoryPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Diversity page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Diversity page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_DiversityPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuDiversityPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on In Our Community page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on In Our Community page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_InOurCommunityPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuWhoWeAre();
		auHomePage.goToAuInOurCommunityPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Product Finder page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Product Finder page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_ProductFinderPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuProductFinderPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Solutions page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Solutions page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_SolutionsPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuSolutionsPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Broadacre page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Broadacre page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_BroadacrePageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuBroadacrePage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Horticulture page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Horticulture page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_HorticulturePageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.mouseHoverOnAuSolutions();
		auHomePage.goToAuHorticulturePage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	/*
	 * @Story("Story 2 - Verify Global Footer on every Page")
	 * @Test(description = " Verify Global Footer on Residential Pest control page - corteva.com.au" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Footer is appearing properly on Residential Pest control page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_ResidentialPestControlPageFooterMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS*************
	 * AuHomePage auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage();
	 * auHomePage.mouseHoverOnAuSolutions(); auHomePage.goToAuResidentialPestControlPage(); auGlobalPage.verifyAuGlobalFooter(); }
	 * @Story("Story 2 - Verify Global Footer on every Page")
	 * @Test(description = " Verify Global Footer on Woody Weeds page - corteva.com.au" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Footer is appearing properly on Woody Weeds page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_WoodyWeedsPageFooterMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS************* AuHomePage
	 * auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage(); auHomePage.mouseHoverOnAuSolutions();
	 * auHomePage.goToAuWoodyWeedsPage(); auGlobalPage.verifyAuGlobalFooter(); }
	 * @Story("Story 2 - Verify Global Footer on every Page")
	 * @Test(description = " Verify Global Footer on Grain Protection page - corteva.com.au" , dependsOnMethods = {"verify_GoToAuHomePageMethod"})
	 * @Description("Verify whether Global Footer is appearing properly on Grain Protection page")
	 * @Severity(SeverityLevel.CRITICAL) public void verify_GrainProtectionPageFooterMethod(Method method) throws InterruptedException { Log.info(method.getName() + " test is started."); // *************PAGE INSTANTIATIONS************* AuHomePage
	 * auHomePage = new AuHomePage(prop, driver, wait); AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait); // *************PAGE METHODS******************** auHomePage.goToAuCortevaHomePage(); auHomePage.mouseHoverOnAuSolutions();
	 * auHomePage.goToAuGrainProtectionPage(); auGlobalPage.verifyAuGlobalFooter(); }
	 */

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on Agronomy page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on Agronomy page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_AgronomyPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuAgronomyPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

	@Story("Story 2 - Verify Global Footer on every Page")
	@Test(description = " Verify Global Footer on News page - corteva.com.au", dependsOnMethods = { "verify_GoToAuHomePageMethod" })
	@Description("Verify whether Global Footer is appearing properly on News page")
	@Severity(SeverityLevel.CRITICAL)
	public void verify_NewsPageFooterMethod(Method method) throws InterruptedException {
		Log.info(method.getName() + " test is started.");
		// *************PAGE INSTANTIATIONS*************
		AuHomePage auHomePage = new AuHomePage(prop, driver, wait);
		AuGlobalPage auGlobalPage = new AuGlobalPage(prop, driver, wait);
		// *************PAGE METHODS********************
		auHomePage.goToAuCortevaHomePage();
		auHomePage.goToAuNewsPage();
		auGlobalPage.verifyAuGlobalFooter();
	}

}