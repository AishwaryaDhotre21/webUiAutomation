package com.qa.store.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.store.factory.DriverFactory;
import com.qa.store.pages.AccountsPage;
import com.qa.store.pages.LoginPage;
import com.qa.store.pages.ProductInfoPage;
import com.qa.store.pages.RegisterationPage;
import com.qa.store.pages.SearchResultsPage;

import io.qameta.allure.Step;

public class BaseTest {
	
	WebDriver driver;
	protected Properties prop;
	DriverFactory df;
	
	protected LoginPage loginPage;
	protected AccountsPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductInfoPage productInfoPage;
	protected RegisterationPage registerationPage;
	protected SoftAssert softAssert;
	
	@Step("Setup: launching {0} browser & init the properties")
	@Parameters({"browser"})
	@BeforeTest
	public void setup(String browserName) {
		df = new DriverFactory();
		prop = df.initProp();
		
		
			if(browserName!=null) {
				prop.setProperty("browser", browserName);
			//	prop.setProperty("browserversion", browserVersion);
			//	prop.setProperty("testname", testName);

			}
		
		
		driver = df.initDriver(prop);
		loginPage = new LoginPage(driver);
		softAssert = new SoftAssert();
	}
	
	@Step("Closing browser")
	@AfterTest
	public void tearDown() {
		driver.quit();
	}
	
	

}
