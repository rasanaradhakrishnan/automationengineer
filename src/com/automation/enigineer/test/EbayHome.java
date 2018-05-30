package com.automation.enigineer.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automation.engineer.utils.LogFacade;
import com.automation.engineer.utils.LogFacadeFactory;

public class EbayHome {
	
final static LogFacade logger = LogFacadeFactory.getInstance().createLogFacade(EbayHome.class);
	
	private WebDriver driver;
	private By createdByMe = By.id("createdByMe");
	
	public EbayHome(WebDriver driver){
		this.driver = driver;
	}
	
	
	public By getCreatedByMe() {
		logger.debug("createdByMe");
		return createdByMe;
	}

	
	

}
