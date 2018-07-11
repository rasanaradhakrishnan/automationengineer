package com.automation.enigineer.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
import io.appium.java_client.android.AndroidKeyCode;

public class AppEbayUIScrollable {

	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(AppEbayUIScrollable.class);
	AndroidDriver driver = null;
	WebDriverWait we;

	@BeforeClass(groups = { "user","failure" })
	public void setUp() {
		logger.debug("VETVariable setUp started ");
		driver = DriverUtils.getDriver();
		logger.debug("VETVariable setUp finished ");
		we = new WebDriverWait(driver, 20);
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

	// Close the session
	@AfterClass
	public void afterClass() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test(priority = 1)
	public void search_shoes_TC() throws InterruptedException {

		// click on the element and send value
		// WebElement
		// search=driver.findElementByAndroidUIAutomator("UiSelector().resourceId(\"com.ebay.mobile:id/search_box\")");
		WebElement search = driver.findElement(By.id("com.ebay.mobile:id/search_box"));
		search.click();

		Thread.sleep(500);

		// enter value in search field
		WebElement search_enterValue = driver.findElement(By.id("com.ebay.mobile:id/search_src_text"));
		search_enterValue.sendKeys("Shoes");

		// press enter
		driver.pressKeyCode(AndroidKeyCode.ENTER);

		// check if the shoes results are displayed
		// WebElement sort_btn=;
		we.until(ExpectedConditions.presenceOfElementLocated(By.id("com.ebay.mobile:id/fragmentContainer")));

	}

	@Test(priority = 2)
	public void select_Lancer_TC() throws InterruptedException {

		WebElement Lancer_shoe = driver.findElementByAndroidUIAutomator(
				"UiScrollable(UiSelector().resourceId(\"com.ebay.mobile:id/fragmentContainer\")).scrollIntoView(UiSelector().textContains(\"Lancer\"))");

		Lancer_shoe.click();

		Thread.sleep(3000);
	}

}
