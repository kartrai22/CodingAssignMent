package Base;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.restassured.RestAssured;
import utils.ConfigReader;

public class TestBase {
    private static TestBase instance = null;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static String sessionId;

    // ✅ Private Constructor (Singleton)
    protected TestBase() {
        // Prevent direct instantiation
    }

    // ✅ Singleton Instance Getter
    public static synchronized TestBase getInstance() {
        if (instance == null) {
            instance = new TestBase();
        }
        return instance;
    }

    // ✅ Ensure WebDriver is initialized before use
    public WebDriver getDriver() {
        if (driver.get() == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call initializeDriver() first.");
        }
        return driver.get();
    }

    // ✅ Initialize WebDriver based on browser type
    public void initializeDriver(String browser) {
        if (driver.get() == null) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized", "--incognito");
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    driver.set(new ChromeDriver(chromeOptions));
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--private");
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    driver.set(new EdgeDriver(edgeOptions));
                    break;

                case "safari":
                    driver.set(new SafariDriver());
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            // ✅ Store the session ID
            sessionId = ((RemoteWebDriver) driver.get()).getSessionId().toString();
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    // ✅ Quit WebDriver and clean up thread-local storage
    public void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    // ✅ Return current WebDriver session ID
    public static String getSessionId() {
        return sessionId;
    }

    // ✅ Get WebDriver from existing session (e.g., for Selenium Grid)
    public static WebDriver getDriverFromSession(String existingSessionId, String browser) {
        if (driver.get() == null) {
            try {
                URL remoteUrl = new URL("http://localhost:4444/wd/hub"); // Adjust Grid URL if needed
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName(browser.toLowerCase());

                RemoteWebDriver remoteDriver = new RemoteWebDriver(remoteUrl, capabilities);
                driver.set(remoteDriver);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create remote WebDriver instance.");
            }
        }
        return driver.get();
    }
    public static String captureScreenshot(String testName, WebDriver driver) {
        if (driver == null) {
            System.out.println("WebDriver instance is null. Cannot capture screenshot.");
            return null;
        }
        
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        
        String destPath = "./Screenshots/" + testName + ".png";
        File destination = new File(destPath);
        destination.getParentFile().mkdirs();
        
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved at: " + destPath);
            return destPath;
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public void setup() {
        RestAssured.baseURI = ConfigReader.getProperty("BaseURI");
    }
}
