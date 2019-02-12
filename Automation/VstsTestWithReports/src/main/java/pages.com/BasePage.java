package pages.com;

import java.util.concurrent.TimeUnit;

import io.qameta.allure.Attachment;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

import com.corteva.tests.BaseTest;

import utilities.com.Log;

public class BasePage extends BaseTest {
	public WebDriver driver;
	public WebDriverWait wait;

	// Constructor
	public BasePage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	// Click Method (When you remove thread.sleep for other than Laptop, you can delete try & catch block)
	public void click(By elementLocation) {
		try {
			//elementToBeClickable is not working on mobile , hence implement temporary work around
			if (!prop.getProperty("deviceName").equals("Laptop")) {
				Thread.sleep(200);
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(elementLocation));
			}
		driver.findElement(elementLocation).click();
		} catch (NoSuchElementException e) {
			//Log.error("Register element is not found.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	// Write Text
	public void writeText(By elementLocation, String text) {
		wait.until(ExpectedConditions.elementToBeClickable(elementLocation));
		driver.findElement(elementLocation).sendKeys(text);
	}
	*/

	
	// Write Text (When you remove thread.sleep for other than Laptop, you can use above commented method)
	public void writeText(By elementLocation, String text) {
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			try {
				Thread.sleep(200);
				//driver.manage().timeouts().pageLoadTimeout(200,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			wait.until(ExpectedConditions.elementToBeClickable(elementLocation));
		}
		driver.findElement(elementLocation).sendKeys(text);
	}
	
	
	// Read Text
	public String readText(By elementLocation) {
		if (!prop.getProperty("deviceName").equals("Laptop")) {
			try {
				Thread.sleep(200);
				//driver.manage().timeouts().pageLoadTimeout(200,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			wait.until(ExpectedConditions.elementToBeClickable(elementLocation));
		}
		return driver.findElement(elementLocation).getText();
	}

	// Aravind

	// clear
	public void clear(By elementLocation) {
		driver.findElement(elementLocation).clear();
	}

	// getTagName
	public String getTagName(By elementLocation) {
		return driver.findElement(elementLocation).getTagName();
	}

	// isDisplayed (When you remove thread.sleep for other than Laptop, you can delete try & catch block)
	public Boolean isDisplayed(By elementLocation) {		
		return driver.findElement(elementLocation).isDisplayed();
	}

	// isEnabled
	public Boolean isEnabled(By elementLocation) {
		return driver.findElement(elementLocation).isEnabled();
	}

	// isSelected
	public Boolean isSelected(By elementLocation) {
		return driver.findElement(elementLocation).isSelected();
	}

	// getAttribute(Not working for Google Phone)
	public String getAttribute(String attributeName, By elementLocation) {
		return driver.findElement(elementLocation).getAttribute(attributeName);
	}
	
	// Mouse Hover Method
	public void mouseHover(By elementLocation) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(elementLocation));	
	    	//wait.until(ExpectedConditions.elementToBeSelected(elementLocation));	    	
			WebElement element = driver.findElement(elementLocation);
	        Actions action = new Actions(driver);
	        action.moveToElement(element).perform();	        
		} catch (NoSuchElementException e) {
			//Log.error("Register element is not found.");
		}
	}
	
}

/*
 * Use below link for more info.
 * http://onlinetraining.etestinghub.com/webdriver-methods-web-elements/ &
 * driver.findElement(By.id("element id"))
 * driver.findElement(By.className("element class"))
 * driver.findElement(By.name("element name"))
 * driver.findElement(By.tagName("element html tag name"))
 * driver.findElement(By.cssSelector("css selector"))
 * driver.findElement(By.link("link text"))
 * driver.findElement(By.xpath("xpath expression"))
 * 

   getLocation()
 */