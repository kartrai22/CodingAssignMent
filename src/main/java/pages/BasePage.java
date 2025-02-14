package pages;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.TestBase;

public class BasePage extends TestBase {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

	public BasePage() {
		 this.driver = getDriver();  // Get the WebDriver from TestBase
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        this.js = (JavascriptExecutor) driver;
	}

	protected void click(By locator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
		} catch (TimeoutException | NoSuchElementException e) {
			throw new RuntimeException("Element not found or not clickable: " + locator, e);
		}
	}

	protected void sendKeys(By locator, String text) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			element.click();
			element.sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE, text);
		} catch (TimeoutException | NoSuchElementException e) {
			throw new RuntimeException("Element not found or not visible: " + locator, e);
		}
	}

	protected boolean isDisplayed(By locator) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
		} catch (TimeoutException | NoSuchElementException e) {
			return false;
		}
	}
	
	
	  public void writeToConfig(String key, String value) {
	        try {
	            // Load existing properties
	            Properties prop = new Properties();
	            FileInputStream fileInput = new FileInputStream(CONFIG_FILE_PATH);
	            prop.load(fileInput);
	            fileInput.close();

	            // Update the property
	            prop.setProperty(key, value);

	            // **Manually write to file to prevent escaping**
	            BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE_PATH));
	            for (String propKey : prop.stringPropertyNames()) {
	                writer.write(propKey + "=" + prop.getProperty(propKey)); // No escaping
	                writer.newLine();
	            }
	            writer.close();

	            System.out.println("Successfully saved: " + key + " = " + value);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
