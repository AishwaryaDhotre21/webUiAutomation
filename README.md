# SeleniumFramework
This is a test framework which uses Selenium for browser automation uses other dependencies:
1) TestNG - used for testing framework
2) Selenium API
3) Apache Commons
4) log4j
5) Extent Reports
6) Maven
7) Java

Setup for framework - uses Page Object Model for Design Pattern
1) Each page for the application has its class (pages package)
2) Utils package - utility functions to read the excel and csv files
3) ExtentReportListener - used to create the extent report object (single object is created and used for multiple test scenarios)
4) Retry class - implements IRetryAnalyzer - which helps in retrying failed test cases

5) Base package is used which has following classes - 

  i) SetupDriver - this helps to different drivers like chrome.firefox and edge which will trigger using browserType key.

  ii) CustomWaits - I have convereted Thread.sleep into a easy to use function (can be used similarly for Explicit and Implicit wait)

4) TestConfiguration package - BaseTest - used for setting up before and after functions.

5) TestData package - for setting up the csv and excel file which contains the testData

6) TestNG - priority, description, dataProvider, groups are covered
