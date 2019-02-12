package utils.Listeners;

import com.corteva.tests.BaseTest;
import com.relevantcodes.extentreports.LogStatus;

import io.qameta.allure.Attachment;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import utilities.com.Log;
import utils.ExtentReports.ExtentManager;
import utils.ExtentReports.ExtentTestManager;


/* 
ITestListener(TestNG): The working of this listener is also exactly the same as ISuiteListerner but the only difference is that it makes the call before and after the Test not the Suite. 
It has seven methods in it.
*/


public class TestListener extends BaseTest implements ITestListener {
	
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    //Screenshot attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG (WebDriver driver) {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog (String message) {
        return message;
    }
/*
    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }
*/
    
    
    @Override
    //onStart(): Invoked after the test class is instantiated and before any configuration method is called.
    public void onStart(ITestContext iTestContext) {
       // System.out.println("I am in onStart method " + iTestContext.getName()); 
        Log.info("I am in onStart method " + iTestContext.getName()); //log4j
        Log.info("I am in onStart method " +  iTestContext.getFailedTests()); //log4j
        iTestContext.setAttribute("WebDriver", this.driver);
     
    }

    @Override
    //onFinish(): Invoked after all the tests have run and all their Configuration methods have been called.
    public void onFinish(ITestContext iTestContext) {
        //System.out.println("I am in onFinish method " + iTestContext.getName());
        Log.info("I am in onFinish method " + iTestContext.getName());//log4j
        //Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }

    @Override
    //Invoked each time before a test will be invoked.
    public void onTestStart(ITestResult iTestResult) {
        //System.out.println("I am in onTestStart method " +  getTestMethodName(iTestResult) + " start");
        Log.info("I am in onTestStart method " +  getTestMethodName(iTestResult) + " start");//log4j
        //Start operation for extentreports.
        ExtentTestManager.startTest(iTestResult.getMethod().getMethodName(),"");
    }

    @Override
    //onTestSuccess(ITestResult result): Invoked each time a test succeeds.
    public void onTestSuccess(ITestResult iTestResult) {
        //System.out.println("I am in onTestSuccess method " +  getTestMethodName(iTestResult) + " succeed");
        Log.info("I am in onTestSuccess method " +  getTestMethodName(iTestResult) + " succeed");//log4j
        //Extentreports log operation for passed tests.
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
    }

    @Override
    //onTestFailure(ITestResult result): Invoked each time a test fails.
    public void onTestFailure(ITestResult iTestResult) {
        //System.out.println("I am in onTestFailure method " +  getTestMethodName(iTestResult) + " failed");
        Log.info("I am in onTestFailure method " +  getTestMethodName(iTestResult) + " failed");//log4j

    //Get driver from BaseTest and assign to local webdriver variable.
    Object testClass = iTestResult.getInstance();
    WebDriver driver = ((BaseTest) testClass).getDriver();

    //Allure ScreenShotRobot and SaveTestLog
    if (driver instanceof WebDriver) {
        //System.out.println("Screenshot captured for test case:" + getTestMethodName(iTestResult));
        Log.info("Screenshot captured for test case:" + getTestMethodName(iTestResult));
        saveScreenshotPNG(driver);
    }

    //Save a log on allure.
    saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");

    //Take base64Screenshot screenshot for extent reports
    String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)driver).
            getScreenshotAs(OutputType.BASE64);

    //Extentreports log and screenshot operations for failed tests.
    ExtentTestManager.getTest().log(LogStatus.FAIL,"Test Failed", ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
}

    @Override
    //onTestSkipped(ITestResult result): Invoked each time a test is skipped
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method "+  getTestMethodName(iTestResult) + " skipped");
        Log.info("I am in onTestSkipped method "+  getTestMethodName(iTestResult) + " skipped");//log4j
        //Extentreports log operation for skipped tests.
        //ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped"); 
    }

    @Override
    //Invoked each time a method fails but has been annotated with successPercentage and this failure still keeps it within the success percentage requested.
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        Log.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));//log4j
    }

}