package com.automation.engineer.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

/**
 * 
 * @author Rasana_R
 *
 */

public class Utils {
	
	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(Utils.class);
	
	private static   int screenshotCount =0;
	private static  String fileExt = null;
	private static Select dropdown = null;

	/*
	 * wait for element load
	 */
	public static void waitForElementPresent(By element){
		WebDriverWait wait = new WebDriverWait(DriverUtils.getDriver(), Constants.LOADING_TIME); //you can play with the time integer  to wait for longer than 15 seconds.`
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element)); //if you want to wait for a particular title to show up
	}
	
	/*
	 * check whether the element is present or not
	 */
	public static boolean isElementPresent(By by) {
	      DriverUtils.getDriver().findElement(by);
	      return true;
	     
	  }
	
	/**
	 * Compares two images and outputs the result as true if they match and false if not
	 * @return
	 */
	 public static boolean compareImages(BufferedImage img1, BufferedImage img2){
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
	public static void clear(By element){
    	DriverUtils.getDriver().findElement(element).clear();
    }
	public static void enterValue(By element,String value){
    	DriverUtils.getDriver().findElement(element).sendKeys(value);
    }
	
	public static void clickOnAnElement(By element){
		try{
    	DriverUtils.getDriver().findElement(element).click();}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
    }
	public static String getElementText(By element){
    	return DriverUtils.getDriver().findElement(element).getText();
    }
	
	
	
	public static void sleep() {
		try { 
			Thread.sleep(Constants.MAX_SLEEP_TIME);	
		}catch (Exception e) {
			logger.debug("Timeout Exception:  waitForPageLoadComplete()");
		}
	}
	
	
	public static boolean isElementVisble( By locator) {

		WebElement element = DriverUtils.getDriver().findElement(locator);
		logger.debug("display :"+element.getCssValue("display"));
		if (element.getCssValue("display").equals("none")){
			return false;
		}
		return true;
	}
	
    public static void accept_alert(){
  
    	Alert alert = DriverUtils.getDriver().switchTo().alert();
    	alert.accept();
    }
    
    public static boolean verify_data_saved(By locator, String value){
    	try{
    	WebElement element = DriverUtils.getDriver().findElement(locator);
    	if(element.getAttribute("value").equals(value.toString())){
    		System.out.println("VERIFY_DATA_SAVED :: PASSED");
    		return true;
    	}
		return false;
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
		return false;
    }
    public static boolean verify_data_present(By locator, String value){
    	try{
    	WebElement element = DriverUtils.getDriver().findElement(locator);
    	if(element.getText().equals(value.toString())){
    		System.out.println("VERIFY_DATA_SAVED :: PASSED");
    		return true;
    	}
		return false;
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
		return false;
    }
    
    
    public static BigDecimal round(float d, int decimalPlace) {
    	BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);  
        return bd.stripTrailingZeros();
    }
	
	/*
	 * Checking the dropdown filter is working properly
	 * @dropdownLocator   which dropdown want to use
	 * @selectedOption    which option should select
	 * @tableLocator   table locator 
	 * @verifyColumnIndex  which column of the table shold verify
	 * 
	 * 
	 * @retrun  number of rows verified
	 */
	public static int verifyFilteredData( By dropdownLocator, String selectedOption, By tableLocator, int verifyColumnIndex ){
		new Select(DriverUtils.getDriver().findElement(dropdownLocator)).selectByVisibleText(selectedOption);
		logger.debug("selectedOption :"+selectedOption);
		try {
			Thread.sleep(Constants.AJAX_MAX_WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        Utils.captureScreenshot();
		WebElement table = DriverUtils.getDriver().findElement(tableLocator); 
		//  get all the tbody elements from the table 
		List<WebElement> tableBody = table.findElements(By.tagName("tbody"));
		// get all the tr elements from the table 
		List<WebElement> allRows = tableBody.get(0).findElements(By.tagName("tr")); 
		logger.debug("Number of rows : "+allRows.size());
		
		// iterate over them, getting the cells 
		if(! selectedOption.equals("All")){
			for (int i = 0; i < allRows.size(); i++) {
				List<WebElement> cells = allRows.get(i).findElements(By.tagName("td")); 
				for (int j = 0; j < cells.size(); j++) {
					if(j == verifyColumnIndex){
						Assert.assertEquals(cells.get(j).getText(), selectedOption);
						//System.out.println(cells.get(j).getText());
						logger.debug("verified Option :"+cells.get(j).getText());
					}
				}
			}
		}

		return allRows.size();
	}
	
	public static int getTableRowCount( By tableLocator){
		Utils.ajaxWait(Constants.AJAX_MAX_WAIT_TIME);
		WebElement table = DriverUtils.getDriver().findElement(tableLocator); 
		//  get all the tbody elements from the table 
		List<WebElement> tableBody = table.findElements(By.tagName("tbody"));
		// get all the tr elements from the table 
		List<WebElement> allRows = tableBody.get(0).findElements(By.tagName("tr")); 
		logger.debug("Number of rows : "+allRows.size());
		return allRows.size();
	}
	
	public static void Select_dropdown (By Locator){
		try{
		WebElement element = DriverUtils.getDriver().findElement(Locator);
		dropdown=new Select(element);
		dropdown.selectByIndex(1);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void Select_dropdown_ID (By Locator, int index){
		try{
		WebElement element = DriverUtils.getDriver().findElement(Locator);
		dropdown=new Select(element);
		dropdown.selectByIndex(index);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void Select_dropdown_value (By Locator, String value){
		try{
		WebElement element = DriverUtils.getDriver().findElement(Locator);
		dropdown=new Select(element);
		dropdown.selectByValue(value);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
	
	public static boolean hasNextPage( By countLabelLocator, By pageRightNavigation){
		try{
		logger.debug("hasNextPage");
		System.out.println(countLabelLocator.toString());
		System.out.println(pageRightNavigation.toString());
		System.out.println();
		WebElement element = DriverUtils.getDriver().findElement(countLabelLocator); 
		System.out.println(element.getText());
		String labelString = element.getText();
		labelString = labelString.replaceAll("\\s+","");
		System.out.println(labelString);
        String labelArray[] = labelString.split("of");
        System.out.println(labelArray[0]);
    	System.out.println(labelArray[1]);
        if(labelArray[0].trim().equals(labelArray[1])){
        	System.out.println(labelArray[0]);
        	System.out.println(labelArray[1]);
        	return false;
        }else{
        	ajaxWait(Constants.AJAX_MIN_WAIT_TIME);	
          DriverUtils.driver.findElement(pageRightNavigation).click();	
          //ajaxWait(Constants.AJAX_MIN_WAIT_TIME);	
        }
		return true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public static void ajaxWait(int waitTime){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String captureScreenshot() {
		WebDriver augmentedDriver = null;
		File scrFile =  null;
		Date date = new Date();
		String fileName= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(date);
		String filePath = "Screenshot/Test-Report-"+getFolderExt()+"/"+fileName+".jpg";
		logger.debug("  \n                   @@@@@@@@@@@@@@@@@@@@@@@@\n                   CAPTURE SCREENSHOT CALLED : "+ ++screenshotCount+"\n                   @@@@@@@@@@@@@@@@@@@@@@@@");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!Constants.ENVIRONMENT.equals("Web")){
	        
	        augmentedDriver = new Augmenter().augment(DriverUtils.getDriver());
	        scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
	        logger.debug("Error capturing ");
		}else{
			scrFile = ((TakesScreenshot)DriverUtils.getDriver()).getScreenshotAs(OutputType.FILE);
		}

	    try {
	        FileUtils.copyFile(scrFile, new File(filePath), true);
	    } catch (Exception e) {
	    	logger.debug("Error capturing screen shot of  test failure.");
	        // remove old pic to prevent wrong assumptions
	        File f = new File(filePath);
	        f.delete(); // don't really care if this doesn't succeed, but would like it to.
	    }
	    Reporter.log("<a href=\\Selenium\\workspace\\CATVET_Automation_Ver_1.0.0\\" + filePath + "><img src=\\Selenium\\workspace\\CaterPillar\\CATVET_Automation_Ver_1.0.0\\" + filePath + " style=width:100px;height:100px;/>" + filePath + "</a><br/>");
	    return "Failed_"+fileName+".jpg"; 
	}
	
	
	public static String captureScreenshotForFailedTest() {
		WebDriver augmentedDriver = null;
		File scrFile =  null;
		Date date = new Date();
		String fileName= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(date);
		String filePath = "Screenshot/Test-Report-"+getFolderExt()+"/"+"Failed_"+fileName+".jpg";
		logger.debug("  \n                   @@@@@@@@@@@@@@@@@@@@@@@@\n                   CAPTURE SCREENSHOT CALLED : "+ ++screenshotCount+"\n                   @@@@@@@@@@@@@@@@@@@@@@@@");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!Constants.ENVIRONMENT.equals("Web")){
	        
	        augmentedDriver = new Augmenter().augment(DriverUtils.getDriver());
	        scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		}else{
			scrFile = ((TakesScreenshot)DriverUtils.getDriver()).getScreenshotAs(OutputType.FILE);
		}

	    try {
	        FileUtils.copyFile(scrFile, new File(filePath), true);
	    } catch (Exception e) {
	    	logger.debug("Error capturing screen shot of  test failure.");
	        // remove old pic to prevent wrong assumptions
	        File f = new File(filePath);
	        f.delete(); // don't really care if this doesn't succeed, but would like it to.
	    }
	    Reporter.log("<a href=\\selenium\\workspace\\CATVET_Automation_Ver_1.0.0\\" + filePath + "><img src=\\Selenium\\workspace\\CaterPillar\\CATVET_Automation_Ver_1.0.0\\" + filePath + " style=width:100px;height:100px;/>" + filePath + "</a><br/>");
	    return "Failed_"+fileName+".jpg";
	}	
	
	public static void verifyAlphabeticalFilter( By alphabeticalContainerLocator , By tableLocator ){
		logger.debug("verifyAlphabeticalFilter Started");
		WebElement alphabeticalContainer = DriverUtils.getDriver().findElement(alphabeticalContainerLocator); 
		List<WebElement> navContainer = alphabeticalContainer.findElements(By.tagName("nav"));
		List<WebElement> ulContainer = navContainer.get(0).findElements(By.tagName("ul")); 
		List<WebElement> liList = ulContainer.get(0).findElements(By.tagName("li")); 
		List<WebElement> hRef = liList.get(26).findElements(By.tagName("a"));
		hRef.get(0).click();
		logger.debug("Click on :"+hRef.get(0).getText());	
		int totalRowCount = getTableRowCount(tableLocator);
		logger.debug("totalRowCount " +totalRowCount);
		int filteredRowCount = 0 ;
		
		for (int i = 0; i < liList.size()-1; i++) {
			hRef = liList.get(i).findElements(By.tagName("a"));
			hRef.get(0).click();
			logger.debug("Click on :"+hRef.get(0).getText());
			Utils.ajaxWait(Constants.AJAX_MIN_WAIT_TIME);
			List<WebElement> filteredRows= getFilteredTableRow(tableLocator);
			filteredRowCount = filteredRowCount + filteredRows.size();
			logger.debug("filtered Row Sum Count :" +filteredRowCount);
			for(int j =0; j< filteredRows.size(); j++){
				logger.debug("loop :" +j);
				List<WebElement> cells = filteredRows.get(j).findElements(By.tagName("td"));
				String cellValue = cells.get(0).getText();
				logger.debug("cellValue :" +cellValue);
				Assert.assertEquals(String.valueOf(cellValue.charAt(0)), hRef.get(0).getText());    
			}
		}
		Assert.assertEquals(filteredRowCount,totalRowCount);   
		logger.debug("verifyAlphabeticalFilter finished");
	}
	
	private static List<WebElement> getFilteredTableRow(By tableLocator){
		logger.debug("getTableRowCells started");
		WebElement table = DriverUtils.getDriver().findElement(tableLocator); 
		//  get all the tbody elements from the table 
		List<WebElement> tableBody = table.findElements(By.tagName("tbody"));
		// get all the tr elements from the table 
		List<WebElement> allRows = tableBody.get(0).findElements(By.tagName("tr")); 
		List<WebElement> filteredRows = new ArrayList<WebElement>();
		for(WebElement element : allRows){
			if("dataRow".equals(element.getAttribute("class"))){
				filteredRows.add(element);
			}
		}
		logger.debug("filteredRows count :"+filteredRows.size());
	    logger.debug("getTableRowCells finished");
		return filteredRows;
	}
	
	public static List<WebElement> getTableRow(By tableLocator){
		logger.debug("getTableRowCells started");
		WebElement table = DriverUtils.getDriver().findElement(tableLocator); 
		//  get all the tbody elements from the table 
		List<WebElement> tableBody = table.findElements(By.tagName("tbody"));
		// get all the tr elements from the table 
		List<WebElement> allRows = tableBody.get(0).findElements(By.tagName("tr")); 
	    logger.debug("getTableRowCells finished");
		return allRows;
	}
	
	
	public static void doAutoComplteOperation(By autoComplteSuggestionLocator){
		logger.debug("doAutoComplteOperation started");
		WebElement autoComplteContainer = DriverUtils.getDriver().findElement(autoComplteSuggestionLocator); 
		List<WebElement> liList = autoComplteContainer.findElements(By.tagName("li"));
		if(liList.size() >= 1){
			int randumNum = randInt(0, liList.size());
			logger.debug("randum item selected index :"+randumNum);
			liList.get(randumNum).click();
			logger.debug("item selected from auto complet list");
		}
		logger.debug("doAutoComplteOperation finished");
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
	
/*	public void verifySearchFiler(String searchQuary, By dropdownLocator, String selectedOption ){
		logger.debug("verifySearchFiler started");
		logger.debug("Entering search value ");
		driver.findElement(getSearchField()).clear();
		driver.findElement(getSearchField()).sendKeys(searchQuary);
        logger.debug("Click on search button");
    	driver.findElement(getSearchIconButton()).click();
    	Utils.ajaxWait(Constants.AJAX_MAX_WAIT_TIME);
    	List<WebElement> allRows = Utils.getTableRow(getResultTable());
    	Assert.assertEquals(allRows.size(), 1);
	    List<WebElement> cells = allRows.get(0).findElements(By.tagName("td"));
	    Assert.assertEquals(cells.get(3).getText(), searchQuary);
	    logger.debug("verifySearchFiler finished");
	}*/
	
	
	public boolean checkOptions(String[] expected){
	  /*  WebElement select = driver.findElement(By.id("ctl00_cphMainContent_dq14_response"));
	    List<WebElement> options = select.findElement(By.xpath(".//option"));
	    int k = 0;
	    for (WebElement opt : options){
	        if (!opt.getText().equals(expected[k]){
	            return false;
	        }
	        k = k + 1;
	    }*/
	    return true;
	}
	
	private static String getFolderExt(){
		if (fileExt == null){
			Date date = new Date();
			fileExt= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
			if(Constants.ENVIRONMENT.equals("Mobile")){
				fileExt = Constants.OPERATING_SYSTEM +"_" + fileExt;
			}
		}
		return 	fileExt;

	}
	
}
