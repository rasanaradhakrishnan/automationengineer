/**
 * 
 */
package com.automation.engineer.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.automation.engineer.utils.Constants;
import com.automation.engineer.utils.LogFacade;
import com.automation.engineer.utils.LogFacadeFactory;
import com.automation.engineer.utils.Utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * @author Rasana_R
 *
 */
public class AppEbayPage {

	private AndroidDriver driver;
	private By userIdField = By.id("com.ebay.mobile:id/edit_text_username");
	private By passwordField = By.id("com.ebay.mobile:id/edit_text_password");
	private By loginSubmitButton = By.id("com.ebay.mobile:id/button_sign_in");
	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(AppEbayPage.class);

	/* private By home = By.id("home"); */

	public AppEbayPage(AndroidDriver driver) {
		this.driver = driver;
	}

	public void login() {
		logger.debug("Entering user name");
		Utils.clear(userIdField);
		Utils.enterValue(userIdField, Constants.USER_NAME);
		logger.debug("Entering password");
		Utils.clear(passwordField);
		// Updated password
		Utils.enterValue(passwordField, Constants.PASSWORD);
		logger.debug("Click on Login button");
		Utils.clickOnAnElement(loginSubmitButton);

		Utils.ajaxWait(Constants.AJAX_MIN_WAIT_TIME);
		// System.out.println(driver.getPageSource());
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/button_google_deny"));

	}

	public void searchTV() throws InterruptedException {
		// click on the element and send value
		// WebElement
		// search=driver.findElementByAndroidUIAutomator("UiSelector().resourceId(\"com.ebay.mobile:id/search_box\")");
		WebElement search = driver.findElement(By.id("com.ebay.mobile:id/search_box"));
		search.click();

		Thread.sleep(500);

		// enter value in search field
		Utils.enterValue(By.id("com.ebay.mobile:id/search_src_text"), Constants.SEARCH_TV);

		// press enter
		driver.pressKeyCode(AndroidKeyCode.ENTER);

	}

}
