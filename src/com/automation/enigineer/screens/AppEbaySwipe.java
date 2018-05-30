package com.automation.enigineer.screens;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
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
import io.appium.java_client.android.AndroidKeyCode;

public class AppEbaySwipe {

	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(AppEbaySwipe.class);
	AndroidDriver driver = null;
	WebDriverWait we;
	String productDetailsPrice;

	private By signin = By.id("com.ebay.mobile:id/button_sign_in");
	private By register = By.id("com.ebay.mobile:id/button_register");

	private By userIdField = By.id("com.ebay.mobile:id/edit_text_username");
	private By passwordField = By.id("com.ebay.mobile:id/edit_text_password");
	private By loginSubmitButton = By.id("com.ebay.mobile:id/button_sign_in");

	@BeforeClass(groups = { "user", "failure" })
	public void setUp() {
		logger.debug("AppEbaySwipe setUp started ");
		driver = DriverUtils.getDriver();
		logger.debug("AppEbaySwipe setUp finished ");
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
	public void SearchTV() throws InterruptedException {

		// click on the element and send value
		// WebElement
		// search=driver.findElementByAndroidUIAutomator("UiSelector().resourceId(\"com.ebay.mobile:id/search_box\")");
		WebElement search = driver.findElement(By.id("com.ebay.mobile:id/search_box"));
		search.click();

		Thread.sleep(500);

		// enter value in search field
		WebElement search_enterValue = driver.findElement(By.id("com.ebay.mobile:id/search_src_text"));
		search_enterValue.sendKeys("65-inch TV");

		// press enter
		driver.pressKeyCode(AndroidKeyCode.ENTER);

		// check if the shoes results are displayed
		// WebElement sort_btn=;
		// we.until(ExpectedConditions.presenceOfElementLocated(By.id("com.ebay.mobile:id/textview_header_title")));

	}

	/**
	 * Select a TV named as Sony bravia. Swipe the screen until a match is found
	 * 
	 * @throws InterruptedException
	 */
	@Test(priority = 2)
	public void selectTV() throws InterruptedException {

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
	public void addTOCart() throws InterruptedException {

		productDetailsPrice = Utils.getElementText(By.id("com.ebay.mobile:id/textview_item_price"));
		assertTrue(Utils.isElementPresent(By.id("com.ebay.mobile:id/button_bin")));
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/button_bin"));
		Thread.sleep(5000);

	}

	/*
	 * Type user name and password and signin to the application
	 */

	@Test(priority = 4)
	public void loginToApp() throws InterruptedException {

		logger.debug("Entering user name");
		driver.findElement(userIdField).clear();
		driver.findElement(userIdField).sendKeys(Constants.USER_NAME);
		logger.debug("Entering password");
		driver.findElement(passwordField).clear();
		// Updated password
		driver.findElement(passwordField).sendKeys(Constants.PASSWORD);
		logger.debug("Click on Login button");
		driver.findElement(loginSubmitButton).click();

		Utils.ajaxWait(Constants.AJAX_MIN_WAIT_TIME);
		// System.out.println(driver.getPageSource());
		driver.findElement(By.id("com.ebay.mobile:id/button_google_deny")).click();

		Thread.sleep(500);
	}

	/**
	 * review the item and change quantity as 2
	 * 
	 * @throws InterruptedException
	 */

	@Test(priority = 5)
	public void reviewItem() throws InterruptedException {

		assertTrue(Utils.isElementPresent(By.id("android:id/numberpicker_input")));
		Utils.clickOnAnElement(By.id("android:id/numberpicker_input"));
		Utils.clear(By.id("android:id/numberpicker_input"));
		driver.findElement(By.id("android:id/numberpicker_input")).sendKeys("1");

		driver.pressKeyCode(AndroidKeyCode.ENTER);
		Utils.isElementPresent(By.id("com.ebay.mobile:id/take_action"));
		Utils.clickOnAnElement(By.id("com.ebay.mobile:id/take_action"));
		Thread.sleep(5000);

	}

	/**
	 * scroll to the bottom of the screen and compare the price with the price
	 * in product detail screen
	 * 
	 * @throws InterruptedException
	 */

	@Test(priority = 6)
	public void checkoutScreen() throws InterruptedException {
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

		Thread.sleep(5000);

	}

}
