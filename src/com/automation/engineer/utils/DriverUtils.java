package com.automation.engineer.utils;

import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * 
 * @author Rasana_R
 *
 */

public class DriverUtils {
	final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(DriverUtils.class);

	public static AndroidDriver<WebElement> driver = null;

	public static AndroidDriver<WebElement> getDriver() {
		
		if (driver == null){
         if(Constants.ENVIRONMENT.equals("Mobile") ){
        	try{
        		if(Constants.OPERATING_SYSTEM.equals("Android")){	
            		DesiredCapabilities capabilities = new DesiredCapabilities();
            		capabilities.setPlatform(Platform.ANDROID);
            		capabilities.setCapability("deviceName", "Sansung On7 Pro");
            		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            		capabilities.setCapability("appPackage","com.ebay.mobile");
            		capabilities.setCapability("appActivity","com.ebay.mobile.activities.MainActivity");
            		capabilities.setCapability("platformVersion","6.0"); 
            		//capabilities.setCapability("autoWebview", true); 
               		driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            		logger.debug("DriverUtils : Andrid Chrome Driver created ");

            	}else if(Constants.OPERATING_SYSTEM.equals("iOS")){
            		
            	}
        	}catch (Exception e){
        		logger.debug("DriverUtils :Exception while creating driver " ,e);
        	}
        	
        }

		}
		return driver;

	}
	
	public static void resetWebdriver() {
		driver = null;
	}

}
