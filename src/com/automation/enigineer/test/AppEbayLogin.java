/**
 * 
 */
package com.automation.enigineer.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.engineer.utils.Constants;
import com.automation.engineer.utils.DriverUtils;
import com.automation.engineer.utils.LogFacade;
import com.automation.engineer.utils.LogFacadeFactory;
import com.automation.engineer.utils.Utils;

import io.appium.java_client.android.AndroidDriver;

/**
 * @author Rasana_R
 *
 */
public class AppEbayLogin {

	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(AppEbayLogin.class);
	
	AndroidDriver<WebElement> driver = null;
	WebDriverWait we;
	private By signin = By.id("com.ebay.mobile:id/button_sign_in");
	private By register = By.id("com.ebay.mobile:id/button_register");
	
	private By userIdField = By.id("com.ebay.mobile:id/edit_text_username");
	private By passwordField =By.id("com.ebay.mobile:id/edit_text_password");
	private By loginSubmitButton = By.id("com.ebay.mobile:id/button_sign_in");

	@BeforeClass(groups = { "user","failure" })
	public void setUp() {
		logger.debug("AppEbayLogin setUp started ");
		driver = DriverUtils.getDriver();
		logger.debug("AppEbayLogin setUp finished ");
		we = new WebDriverWait(driver, Constants.LOADING_TIME);
	}

	@BeforeMethod(groups = { "user", "failure" })
	public void setUpForEachTest() {
		logger.debug("setUpForEachTest started");
		driver = DriverUtils.getDriver();
		driver.manage().timeouts().implicitlyWait(Constants.MAX_WAIT_TIME, TimeUnit.SECONDS);
		try {
			Utils.ajaxWait(Constants.AJAX_MIN_WAIT_TIME);
		} catch (Exception e) {
			logger.debug(e.toString());
		}

		logger.debug("setUpBeforeMethod finished");
	}


	@Test(priority = 1)
	public void login_TC() throws InterruptedException {

		// click on the element and send value
		// WebElement
		Utils.ajaxWait(Constants.AJAX_MIN_WAIT_TIME);
		//is sign in and register button exists
		WebElement signIn= driver.findElement(signin);
		WebElement registerEbay= driver.findElement(register);
		Utils.clickOnAnElement(signin);
	}
	
	/*
	 * Type user name and password and signin to the application
	 */
	
	@Test(priority = 2)
	public void loginToApp_TC() throws InterruptedException {
		
		logger.debug("Entering user name");
		driver.findElement(userIdField).clear();
		driver.findElement(userIdField).sendKeys(Constants.USER_NAME);
		logger.debug("Entering password");
		driver.findElement(passwordField).clear();
		// Updated password
		driver.findElement(passwordField).sendKeys(Constants.PASSWORD);
        logger.debug("Click on Login button");
    	driver.findElement(loginSubmitButton).click();

		Thread.sleep(500);
	}
	
	
	// Close the session
		@AfterClass
		public void afterClass() {
			if (driver != null) {
				driver.quit();
			}
		}


}
