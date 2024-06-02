package com.qa.store.factory;

import com.qa.store.errors.AppError;
import com.qa.store.exceptions.BrowserException;
import com.qa.store.exceptions.FrameworkException;
import com.qa.store.logger.Log;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DriverFactory {
    WebDriver driver;
    Properties prop;
    OptionsManager optionsManager;

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

    public static String highlight;

    public WebDriver initDriver(Properties prop) {

        String browserName = prop.getProperty("browser");
        System.out.println("browser name is : " + browserName);

        Log.info("Browser name is : " + browserName);

        highlight = prop.getProperty("highlight");

        optionsManager = new OptionsManager(prop);

        switch (browserName.trim().toLowerCase()) {
            case "chrome":

                if (! Boolean.parseBoolean(prop.getProperty("remote"))) {
                    // remote - grid execution:
                    init_remoteDriver("chrome");
                } else {
                    // run it on local:
                    WebDriverManager.chromedriver().setup();
                    tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
                }

                break;

            case "firefox":
                if (! Boolean.parseBoolean(prop.getProperty("remote"))) {
                    // remote - grid execution:
                    init_remoteDriver("firefox");
                } else {
                    WebDriverManager.firefoxdriver().setup();
                    tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
                }

                break;

            case "edge":
                if (Boolean.parseBoolean(prop.getProperty("remote"))) {
                    // remote - grid execution:
                    init_remoteDriver("edge");
                } else {
                    WebDriverManager.edgedriver().setup();
                    tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
                }
                break;

            case "safari":
                WebDriverManager.safaridriver().setup();
                tlDriver.set(new SafariDriver());

                break;

            default:
                // System.out.println("plz pass the right browser...." + browserName);
                Log.error("plz pass the right browser...." + browserName);
                throw new BrowserException("NO BROWSER FOUND..." + browserName);
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        getDriver().get(prop.getProperty("url"));

        return getDriver();
    }
    public static WebDriver getDriver() {
        return tlDriver.get();
    }
    private void init_remoteDriver(String browserName) {

        System.out.println("Running tests on Remote GRID on browser: " + browserName);

        try {
            switch (browserName.toLowerCase().trim()) {
                case "chrome":

                    tlDriver.set(
                            new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
                    break;

                case "firefox":

                    tlDriver.set(
                            new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
                    break;

                case "edge":
                    tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
                    break;

                default:
                    System.out.println("plz pass thr right supported browser on GRID....");
                    break;
            }

        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
    public Properties initProp() {

        // envName=qa,stage,prod,uat,dev
        // mvn clean install -Denv="uat" -DsuiteXmlFiles="//"
        FileInputStream ip = null;
        prop = new Properties();

        String envName = System.getProperty("env");
        System.out.println("Running tests on Env: " + envName);

        try {
            if (envName == null) {
                System.out.println("No env is given...hence running it on QA env...");
                ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
            } else {

                switch (envName.toLowerCase().trim()) {
                    case "qa":
                        ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
                        break;
                    case "dev":
                        ip = new FileInputStream("./src/test/resources/config/config.dev.properties");
                        break;
                    case "stage":
                        ip = new FileInputStream("./src/test/resources/config/config.stage.properties");
                        break;
                    case "uat":
                        ip = new FileInputStream("./src/test/resources/config/config.uat.properties");
                        break;
                    case "prod":
                        ip = new FileInputStream("./src/test/resources/config/config.properties");
                        break;

                    default:
                        System.out.println("plz pass the right env name... " + envName);
                        throw new FrameworkException(AppError.ENV_NAME_NOT_FOUND + " : " + envName);
                }
            }
        } catch (FrameworkException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    /**
     * take screenshot
     */

    public static String getScreenshot(String methodName) {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp directory
        String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "_" + System.currentTimeMillis()
                + ".png";

        File destination = new File(path);

        try {
            FileHandler.copy(srcFile, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }


}
