package com.automation.engineer.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * 
 * @author Rasana_R
 *
 */

public class Utils {

	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(Utils.class);

	private static int screenshotCount = 0;
	private static String fileExt = null;
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static XSSFRow Row;

	/*
	 * wait for element load // you // can // play // with // the // time //
	 * integer // to // wait // for // longer // than // 15 // seconds.` // if
	 * // you // want // to // wait // for // a // particular // title // to //
	 * show // up
	 */
	public static void waitForElementPresent(By element) {
		WebDriverWait wait = new WebDriverWait(DriverUtils.getDriver(), Constants.LOADING_TIME);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
	}

	/*
	 * check whether the element is present or not
	 */
	public static boolean isElementPresent(By by) {
		DriverUtils.getDriver().findElement(by);
		return true;

	}

	/**
	 * Compares two images and outputs the result as true if they match and
	 * false if not
	 * 
	 * @return
	 */
	public static boolean compareImages(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y))
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/*
	 * clear the text field
	 */
	public static void clear(By element) {
		DriverUtils.getDriver().findElement(element).clear();
	}

	public static void enterValue(By element, String value) {
		DriverUtils.getDriver().findElement(element).sendKeys(value);
	}

	public static void clickOnAnElement(By element) {
		try {
			DriverUtils.getDriver().findElement(element).click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static String getElementText(By element) {
		return DriverUtils.getDriver().findElement(element).getText();
	}

	public static void sleep() {
		try {
			Thread.sleep(Constants.MAX_SLEEP_TIME);
		} catch (Exception e) {
			logger.debug("Timeout Exception:  waitForPageLoadComplete()");
		}
	}

	public static boolean isElementVisble(By locator) {

		WebElement element = DriverUtils.getDriver().findElement(locator);
		logger.debug("display :" + element.getCssValue("display"));
		if (element.getCssValue("display").equals("none")) {
			return false;
		}
		return true;
	}

	public static BigDecimal round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.stripTrailingZeros();
	}

	/**
	 * Explicitly wait 
	 * @param waitTime
	 */
	public static void ajaxWait(int waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Capture the screenshot after every success test case results
	 */

	public static String captureScreenshot() {
		WebDriver augmentedDriver = null;
		File scrFile = null;
		Date date = new Date();
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(date);
		String filePath = "Screenshot/Test-Report-" + getFolderExt() + "/" + fileName + ".jpg";
		logger.debug("  \n                   @@@@@@@@@@@@@@@@@@@@@@@@\n                   CAPTURE SCREENSHOT CALLED : "
				+ ++screenshotCount + "\n                   @@@@@@@@@@@@@@@@@@@@@@@@");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!Constants.ENVIRONMENT.equals("Web")) {

			augmentedDriver = new Augmenter().augment(DriverUtils.getDriver());
			scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			logger.debug("Error capturing ");
		} else {
			scrFile = ((TakesScreenshot) DriverUtils.getDriver()).getScreenshotAs(OutputType.FILE);
		}

		try {
			FileUtils.copyFile(scrFile, new File(filePath), true);
		} catch (Exception e) {
			logger.debug("Error capturing screen shot of  test failure.");
			// remove old pic to prevent wrong assumptions
			File f = new File(filePath);
			f.delete(); // don't really care if this doesn't succeed, but would
						// like it to.
		}
		Reporter.log("<a href=\\Selenium\\workspace\\CATVET_Automation_Ver_1.0.0\\" + filePath
				+ "><img src=\\Selenium\\workspace\\CaterPillar\\CATVET_Automation_Ver_1.0.0\\" + filePath
				+ " style=width:100px;height:100px;/>" + filePath + "</a><br/>");
		return "Failed_" + fileName + ".jpg";
	}

	public static String captureScreenshotForFailedTest() {
		WebDriver augmentedDriver = null;
		File scrFile = null;
		Date date = new Date();
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(date);
		String filePath = "Screenshot/Test-Report-" + getFolderExt() + "/" + "Failed_" + fileName + ".jpg";
		logger.debug("  \n                   @@@@@@@@@@@@@@@@@@@@@@@@\n                   CAPTURE SCREENSHOT CALLED : "
				+ ++screenshotCount + "\n                   @@@@@@@@@@@@@@@@@@@@@@@@");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!Constants.ENVIRONMENT.equals("Web")) {

			augmentedDriver = new Augmenter().augment(DriverUtils.getDriver());
			scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		} else {
			scrFile = ((TakesScreenshot) DriverUtils.getDriver()).getScreenshotAs(OutputType.FILE);
		}

		try {
			FileUtils.copyFile(scrFile, new File(filePath), true);
		} catch (Exception e) {
			logger.debug("Error capturing screen shot of  test failure.");
			// remove old pic to prevent wrong assumptions
			File f = new File(filePath);
			f.delete(); // don't really care if this doesn't succeed, but would
						// like it to.
		}
		Reporter.log("<a href=\\selenium\\workspace\\CATVET_Automation_Ver_1.0.0\\" + filePath
				+ "><img src=\\Selenium\\workspace\\CaterPillar\\CATVET_Automation_Ver_1.0.0\\" + filePath
				+ " style=width:100px;height:100px;/>" + filePath + "</a><br/>");
		return "Failed_" + fileName + ".jpg";
	}

	private static String getFolderExt() {
		if (fileExt == null) {
			Date date = new Date();
			fileExt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
			if (Constants.ENVIRONMENT.equals("Mobile")) {
				fileExt = Constants.OPERATING_SYSTEM + "_" + fileExt;
			}
		}
		return fileExt;

	}

	// This method is to set the File path and to open the Excel file, Pass
	// Excel Path and Sheetname as Arguments to this method

	public static void setExcelFile(String Path, String SheetName) throws Exception {

		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(Path);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

		} catch (Exception e) {

			throw (e);

		}

	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num

	public static String getCellData(int RowNum, int ColNum) throws Exception {

		try {

			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);

			String CellData = Cell.getStringCellValue();

			return CellData;

		} catch (Exception e) {

			return "";

		}

	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	public static void setCellData(String Result, int RowNum, int ColNum) throws Exception {

		try {

			Row = ExcelWSheet.getRow(RowNum);

			Cell = Row.getCell(ColNum, null);

			if (Cell == null) {

				Cell = Row.createCell(ColNum);

				Cell.setCellValue(Result);

			} else {

				Cell.setCellValue(Result);

			}

			// Constant variables Test Data path and Test Data file name

			FileOutputStream fileOut = new FileOutputStream(Constants.Path_TestData + Constants.File_TestData);

			ExcelWBook.write(fileOut);

			fileOut.flush();

			fileOut.close();

		} catch (Exception e) {

			throw (e);

		}

	}

}
