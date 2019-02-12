package com.corteva.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import utilities.com.Log;
//import utils.ExtentReports.ExtentTestManager;

public class BaseTest {
	public WebDriver driver;
	public WebDriverWait wait;

	// Aravind
	public static Properties prop;

	// WebDriver on Laptop
	public WebDriver getDriver() {
		return driver;
	}

	// Aravind-Start

	@BeforeSuite(description = "Config Level Setup!")
	public static void LoadConfig() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java"
							+ "/config/config.properties");
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	// Aravind-End
	
	//@BeforeSuite(description = "Class Level Device Setup!")
	@BeforeClass(description = "Class Level Device Setup!")
	public void setup() throws MalformedURLException {
		// Write a Log when tests is starting
		Log.startLog("Test is starting!");

		// Aravind-Start
		// Select a Device & Browser
		String baseURL = prop.getProperty("baseURL");
		String deviceName = prop.getProperty("deviceName");
		String browserName = prop.getProperty("browserName");
		String appiumURL = prop.getProperty("appiumURL");
		String platform = prop.getProperty("platform");
		//String version = prop.getProperty("version");

		switch (deviceName) {
		case "Laptop":
			switch (browserName) {
			case "chrome":
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir")
								+ "/src/main/java/resources/chromedriver.exe");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
				break;
			case "ie":
				
				System.setProperty("webdriver.ie.driver",System.getProperty("user.dir") + "/src/main/java/resources/IEDriverServer.exe");
			 	DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				// Settings to Accept the SSL Certificate in the Capability object
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				//Hadle ie zoom issue
				capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

				/*
				capabilities.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR,true);
				capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
				capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING,true);
				 */
			
				driver = new InternetExplorerDriver(capabilities);
				//driver = new RemoteWebDriver(capabilities);
				driver.manage().window().maximize();
				break;
				
			case "FF":
				System.out.println("write something");
				break;
			default:
				System.out.println("write something");
			}

			break;
		case "GooglePhone":

			switch (browserName) {
			case "chrome":
				DesiredCapabilities caps = DesiredCapabilities.chrome();
				caps.setCapability("platform", platform);
				caps.setCapability("deviceName", deviceName);
				//caps.setCapability("version", version);
				caps.setCapability("browserName", browserName);
				System.out.println(appiumURL);
				driver = new RemoteWebDriver(new URL(appiumURL), caps);
				break;
			case "ie":
				System.out.println("ie is not implemented for now");
				break;
			default:
				System.out.println("default is not implemented for now");
			}
		}
		// Create a wait. All test classes use this.
		wait = new WebDriverWait(driver, 30);
		driver.manage().deleteAllCookies();
	}

	//@AfterSuite(description = "Class Level Teardown!")
	@AfterClass(description = "Class Level Teardown!")
	public void teardown() {
		// Write a Log when tests are ending
		Log.endLog("Test is ending!");
		driver.quit();
	}
	
	

}
