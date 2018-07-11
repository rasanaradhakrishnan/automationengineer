package com.automation.enigineer.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.engineer.page.AppEbayPage;
import com.automation.engineer.utils.Constants;
import com.automation.engineer.utils.DriverUtils;
import com.automation.engineer.utils.LogFacade;
import com.automation.engineer.utils.LogFacadeFactory;
import com.automation.engineer.utils.Utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class AppEbaySwipe {

	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(AppEbaySwipe.class);
	AndroidDriver driver = null;
	WebDriverWait we;
	String productDetailsPrice;
	private AppEbayPage appEbayPage ;

	
	@BeforeClass(groups = { "user", "failure" })
	public void setUp() {
		logger.debug("AppEbaySwipe setUp started ");
		driver = DriverUtils.getDriver();
		we = new WebDriverWait(driver, 20);
		logger.debug("AppEbaySwipe setUp finished ");
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
		appEbayPage  = new AppEbayPage(driver);
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
	public void searchTV_TC() throws InterruptedException {
		logger.info("Search TV started", "Searching for TV.......");
		appEbayPage.searchTV();
		// check if the shoes results are displayed
		Utils.captureScreenshot();

	}

	/**
	 * Select a TV named as Sony bravia. Swipe the screen until a match is found
	 * 
	 * @throws InterruptedException
	 */
	@Test(priority = 2)
	public void selectTV_TC() throws InterruptedException {

		// check if the shoes results are displayed
		// WebElement
		// sort_btn=driver.findElementByAndroidUIAutomator("UiSelector().textContains(\"499.00\")");
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/text_slot_1"));
		// Get the area to do swipe action
		WebElement swipe_area = driver.findElement(By.id("com.ebay.mobile:id/fragmentContainer"));

		List<WebElement> searchtext = swipe_area.findElements(By.id("com.ebay.mobile:id/textview_item_title"));

		// getting the max height and width
		int x_max = driver.manage().window().getSize().width;
		int y_max = driver.manage().window().getSize().height;
		String product = "SONY BRAVIA";

		outerloop: 
			for (int i = 0; i < searchtext.size(); i++) {
			if (searchtext.get(i).getText().contains(product)) {
				searchtext.get(i).click();
				break outerloop;
			} else {
				// swiping vertically
				driver.swipe(x_max / 2, y_max - 100, x_max / 2, 100, 2000);
			}
		}

		Thread.sleep(5000);
	}

	/**
	 * save the price in details screen and click on on add to cart
	 * 
	 * @throws InterruptedException
	 */

	@Test(priority = 3)
	public void addTOCart_TC() throws InterruptedException {

		productDetailsPrice = Utils.getElementText(By.id("com.ebay.mobile:id/textview_item_price"));
		assertTrue(Utils.isElementPresent(By.id("com.ebay.mobile:id/button_bin")));
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/button_bin"));
		Thread.sleep(5000);
		Utils.captureScreenshot();
	}

	/*
	 * Type user name and password and signin to the application
	 */

	@Test(priority = 4)
	public void loginToApp_TC() throws InterruptedException {
		logger.debug("testEbayLogin started ");
		appEbayPage.login();
		Utils.captureScreenshot();
		Thread.sleep(500);
	}

	/**
	 * review the item and change quantity as 2
	 * 
	 * @throws InterruptedException
	 */

	@Test(priority = 5)
	public void reviewItem_TC() throws InterruptedException {

		assertTrue(Utils.isElementPresent(By.id("android:id/numberpicker_input")));
		Utils.clickOnAnElement(By.id("android:id/numberpicker_input"));
		Utils.clear(By.id("android:id/numberpicker_input"));
		Utils.enterValue(By.id("android:id/numberpicker_input"),"1");

		driver.pressKeyCode(AndroidKeyCode.ENTER);
		Utils.isElementPresent(By.id("com.ebay.mobile:id/take_action"));
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/take_action"));
		
		Utils.captureScreenshot();
		Thread.sleep(5000);

	}

	/**
	 * scroll to the bottom of the screen and compare the price with the price
	 * in product detail screen
	 * 
	 * @throws InterruptedException
	 */

	@Test(priority = 6)
	public void checkoutScreen_TC() throws InterruptedException {
		// Scroll to the bottom of the screen
		driver.scrollTo("Proceed to Pay");
		// Get the area of checkout screen
		WebElement views = driver.findElement(By.xpath("//android.view.View[@index='2']/android.view.View[@index='10']/android.view.View[@index='2']/"
				+ "android.widget.ListView[@index='0']"
				+ "/android.view.View[@index='1']"));

		/*WebElement views = driver.findElement(By.xpath("//android.view.View[@index='9']/android.view.View[@index='5']/android.widget.ListView[@index='0']"
				+ "/android.view.View[@index='1']"));
		*/System.out.println(driver.getPageSource());
		String itemPrice = views.getText();
		logger.debug(itemPrice);
		logger.debug(productDetailsPrice);
		productDetailsPrice = productDetailsPrice.substring(1).replaceAll(",", "");
		itemPrice = itemPrice.replaceAll("Rs. ", "").replaceAll(",", "").replaceAll(".00", "");
		logger.debug(itemPrice);
		logger.debug(productDetailsPrice);
		assertEquals(productDetailsPrice.compareTo(itemPrice),0);
		
		Utils.captureScreenshot();
		Thread.sleep(5000);

	}
	
	@AfterMethod( groups ={"user", "failure"})
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
	    if (testResult.getStatus() == ITestResult.FAILURE) {
	    	logger.debug("takeScreenShotOnFailure start");
	        Utils.captureScreenshotForFailedTest();
		    logger.debug("takeScreenShotOnFailure finished");
	   }  
	}

}
