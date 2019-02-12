package pages.com;

import java.util.Properties;

import io.qameta.allure.Step;
import net.bytebuddy.jar.asm.commons.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import utilities.com.Log;

public class ComponentsAllPages extends BasePage {

	private static final String String = null;



	// *********Constructor*********
	public ComponentsAllPages(Properties prop, WebDriver driver, WebDriverWait wait) {
		// TODO Auto-generated constructor stub
		super(driver, wait);

	}

	
	// *********Page Variables*********
	// String baseURL= "https://www.pp.corteva.com/";


		
	//SoftAssert softAssertion= new SoftAssert();
		
	
	// *********Web Elements*********
	//1.
	String globalHeaderXpath = "//div[@data-componentname='globalHeader']"; // globalHeader
	//2.
	String linkListXpath = "//div[@data-componentname='linkList']";//linkList
	//3.
	String navSearchXpath = "//div[@data-componentname='navSearch']";//navSearch
	//4.
	String navigationDropdownXpath = "//div[@data-componentname='navigationDropdown']";//navigationDropdown
	//5.
	String heroCarouselXpath = "//div[@data-componentname='heroCarousel']";//heroCarousel
	//6.
	String cardsContainerXpath = "//div[@data-componentname='cardsContainer']";//cardsContainer
	//7.
	String primaryCardXpath = "//div[@data-componentname='primaryCard']";//primaryCard
	//8.
	String featureLinkListXpath = "//div[@data-componentname='featureLinkList']";//featureLinkList
	//9.	
	String heroXpath = "//div[@data-componentname='hero']";//hero	
	//10.
	String twoColumnFeatureXpath = "//div[@data-componentname='twoColumnFeature']";//twoColumnFeature
	//11.
	String textonimageXpath = "//div[@data-componentname='textonimage']";//textonimage
	//12.
	String accordionListXpath = "//div[@data-componentname='accordionList']";//accordionList
	//12.1
	String accordionListclassName1Xpath = "//div[@class='band det01-accordion-list white no-images isnotAccordion ']";//Found 2 accordionList.To verify 1st accordionList.  
	//12.2
	String accordionListclassName2Xpath = "//div[@class='band det01-accordion-list white thumbnails isnotAccordion ']";//Found 2 accordionList.To verify 2nd accordionList.  
	//13.
	String anchorNavigationXpath = "//div[@data-componentname='anchorNavigation']";//anchorNavigation
	//14.
	String timelineCardXpath = "//div[@data-componentname='timelineCard']";//timelineCard
	//15.
	String tilesContainerXpath = "//div[@data-componentname='tilesContainer']";//navigationDropdown
	//16.
	String fixedGridTilesContainerXpath = "//div[@data-componentname='fixedGridTilesContainer']";//fixedGridTilesContainer
	//17.
	String tilesXpath = "//div[@data-componentname='tiles']";//tiles
	//18.
	String hrTagXpath = "//div[@data-componentname='hrTag']";//hrTag
	//19.
	String rightRailLinkedListXpath = "//div[@data-componentname='rightRailLinkedList']";//rightRailLinkedList
	//20.
	String breadcrumbXpath = "//div[@data-componentname='breadcrumb']"; // breadcrumb
	//21.
	String navHeaderTabsXpath = "//div[@data-componentname='navHeaderTabs']"; // navHeaderTabs
	//22.
	String articleFilterXpath = "//div[@data-componentname='articleFilter']";//articleFilter
	//23.
	String rightRailRteXpath = "//div[@data-componentname='rightRailRte']";//rightRailRte
	//24.
	String bioDetailCardContainerXpath = "//div[@data-componentname='bioDetailCardContainer']";//bioDetailCardContainer
	//25.
	String globalfooterXpath = "//div[@data-componentname='globalfooter']";//globalfooter
	//26.
	String jsFrameworkXpath = "//div[@data-componentname='jsFramework']";//jsFramework
	
	//*****************************************************************
	//27.us
	String productLabelFinderXpath = "//div[@data-componentname='productLabelFinder']";//productLabelFinder
	//28.us
	String eloquaFormCustomXpath = "//div[@data-componentname='eloquaFormCustom']";//eloquaFormCustom
	
	//*****************************************************************
	//29.ca
	String oneColumnFeatureXpath = "//div[@data-componentname='oneColumnFeature']";//oneColumnFeature
	//30.ca
	String 	galleryVideoPlayerXpath = "//div[@data-componentname='galleryVideoPlayer']";//galleryVideoPlayer

  
	//*********Page Methods*********
	//1.
	@Step("Verify Whether globalHeader component is available")
	public void verifyExistenceOfglobalHeaderComponent(
			String componentName , String  componentValue) {
		String ActualValue = getAttribute(componentName,By.xpath(globalHeaderXpath));
		String ExpectedValue = componentValue;
		Assert.assertEquals(ActualValue,ExpectedValue,"Message");
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(fixedGridTilesContainerXpath)),componentValue);

	}
	
	//2.
	@Step("Verify Whether linkList component is available")
	public void verifyExistenceOflinkListComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(linkListXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	//3.
	@Step("Verify Whether navSearch component is available")
	public void verifyExistenceOfnavSearchComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(navSearchXpath)),componentValue);
	}
	//4.
	@Step("Verify Whether navigationDropdown component is available")
	public void verifyExistenceOfnavigationDropdownComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(navigationDropdownXpath)),componentValue);
	}
	
	//5.
	@Step("Verify Whether heroCarousel component is available")
	public void verifyExistenceOfheroCarouselComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(heroCarouselXpath)),componentValue);
	}
	

	//6.
	@Step("Verify Whether cardsContainer component is available")
	public void verifyExistenceOfcardsContainerComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(cardsContainerXpath)),componentValue);
	}
	
	//7.
	@Step("Verify Whether primaryCard component is available")
	public void verifyExistenceOfprimaryCardComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(primaryCardXpath)),componentValue);
	}
	
	
	//8.
	@Step("Verify Whether featureLinkList component is available")
	public void verifyExistenceOffeatureLinkListComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(featureLinkListXpath)),componentValue);
	}
	
	//9.
	@Step("Verify Whether hero component is available")
	public void verifyExistenceOfheroComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(heroXpath)),componentValue);
	}
	
	//10.
	@Step("Verify Whether twoColumnFeature component is available")
	public void verifyExistenceOftwoColumnFeatureComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(twoColumnFeatureXpath)),componentValue);
	}
	
	
	//11.
	@Step("Verify Whether textonimage component is available")
	public void verifyExistenceOftextonimageComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(textonimageXpath)),componentValue);
	}
	/*
	//12.To handle single component
	@Step("Verify Whether accordionList component is available")
	public void verifyExistenceOfaccordionListComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(accordionListXpath)),componentValue);
	}
	*/
	
	
	//12. To handle multiple components 
	@Step("Verify Whether accordionList component is available")
	public void verifyExistenceOfaccordionListComponent(
			String componentName, String componentValue,String className1, String classValue1,String className2, String classValue2) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(accordionListXpath)),componentValue);
		//12.1
		if (className1!=""){
			Assert.assertEquals(getAttribute(className1,By.xpath(accordionListclassName1Xpath)),classValue1);
		}
		//12.2
		if (className2!=""){
			Assert.assertEquals(getAttribute(className2,By.xpath(accordionListclassName2Xpath)),classValue2);
		}
	}
	
	//13.
	@Step("Verify Whether anchorNavigation component is available")
	public void verifyExistenceOfanchorNavigationComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(anchorNavigationXpath)),componentValue);
	}
	
	//14.
	@Step("Verify Whether timelineCard component is available")
	public void verifyExistenceOftimelineCardComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(timelineCardXpath)),componentValue);
	}
	
	//15.
	@Step("Verify Whether tilesContainer component is available")
	public void verifyExistenceOftilesContainerComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(tilesContainerXpath)),componentValue);
	}
	
	//16.
	@Step("Verify Whether fixedGridTilesContainer component is available")
	public void verifyExistenceOffixedGridTilesContainerComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(fixedGridTilesContainerXpath)),componentValue);
	}
	
	//17.
	@Step("Verify Whether tiles component is available")
	public void verifyExistenceOftilesComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);
	}
	
	//18.
	@Step("Verify Whether hrTag component is available")
	public void verifyExistenceOfhrTagComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(hrTagXpath)),componentValue);
	}
	
	//19.
	@Step("Verify Whether rightRailLinkedList component is available")
	public void verifyExistenceOfrightRailLinkedListComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(rightRailLinkedListXpath)),componentValue);
	}
	
	//20.
	@Step("Verify Whether Breadcrumb component is available")
	public void verifyExistenceOfBreadcrumbComponent(String componentName,String componentValue) { 
		Assert.assertEquals(getAttribute(componentName,By.xpath(breadcrumbXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(breadcrumbXpath)),componentValue);
		
		//If you want below logs under Log4J & Eclipse Console
		//Log.info("Breadcrumb component is available on Blog page.");
		//Log.fatal("Breadcrumb component is NOT available on Blog page.");	
	}
	
	//21.
	@Step("Verify Whether Existence Of navHeader Tabs Component is available")
	public void verifyExistenceOfnavHeaderTabsComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(navHeaderTabsXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	
	//22.
	@Step("Verify Whether articleFilter component is available")
	public void verifyExistenceOfarticleFilterComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(articleFilterXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	
	//23.
	@Step("Verify Whether right RailRte Component is available")
	public void verifyExistenceOfrightRailRteComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(rightRailRteXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	//24.
	@Step("Verify Whether bioDetailCardContainer Component is available")
	public void verifyExistenceOfbioDetailCardContainerComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(bioDetailCardContainerXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	
	//25.
	@Step("Verify Whether globalfooter component is available")
	public void verifyExistenceOfglobalfooterComponent(String componentName,String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(globalfooterXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);		
	}
	
	//26.
	// getAttribute is not working on google phone
	@Step("Verify Whether jsFramework component is available")
	public void verifyExistenceOfjsFrameworkComponent(String componentName,
			String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(jsFrameworkXpath)),componentValue);
		//softAssertion.assertEquals(getAttribute(componentName,By.xpath(tilesXpath)),componentValue);			
	}
	
	//*****************************************************************
	//27.us
	@Step("Verify Whether productLabelFinder component is available")
	public void verifyExistenceOfproductLabelFinderComponent(String componentName,
			String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(productLabelFinderXpath)),componentValue);
	}
	//28.us
	@Step("Verify Whether eloquaFormCustom component is available")
	public void verifyExistenceOfeloquaFormCustomComponent(String componentName,
			String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(eloquaFormCustomXpath)),componentValue);
	}
	
	
	//*****************************************************************
	//29.ca
	@Step("Verify Whether oneColumnFeature component is available")
	public void verifyExistenceOfoneColumnFeatureComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(oneColumnFeatureXpath)),componentValue);
	}
	
	//30.ca
	@Step("Verify Whether galleryVideoPlayer component is available")
	public void verifyExistenceOfgalleryVideoPlayerComponent(
			String componentName, String componentValue) {
		Assert.assertEquals(getAttribute(componentName,By.xpath(galleryVideoPlayerXpath)),componentValue);
	}
	
}