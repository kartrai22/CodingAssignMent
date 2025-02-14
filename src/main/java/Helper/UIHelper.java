package Helper;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import Exception.UIExceptions;

public class UIHelper {
	private WebDriver driver;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(UIHelper.class);

	public UIHelper(WebDriver driver) {
		this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	}

	public WebElement findElementWithExceptionHandling(By locator) {
		try {
			logger.info("Attempting to locate element: " + locator);
			return driver.findElement(locator);
		} catch (NoSuchElementException e) {
			logger.error("Element not found: " + locator, e);
			throw new UIExceptions("Element not found: " + locator, e);
		}
	}
}
