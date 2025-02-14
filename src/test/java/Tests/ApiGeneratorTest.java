package Tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Base.TestBase;
import Listeners.TestListeners;
import pages.ApiGeneratorPage;
import utils.ConfigReader;
@Listeners(TestListeners.class)

public class ApiGeneratorTest {
    private WebDriver driver;
    private ApiGeneratorPage apiPage;

    @BeforeClass
    public void setUp() {
        // Initialize WebDriver using TestBase
        TestBase.getInstance().initializeDriver(ConfigReader.getProperty("browser"));  
        driver = TestBase.getInstance().getDriver();
        apiPage = new ApiGeneratorPage(driver); // Pass driver to Page Object
    }

    @Test
    public void testApiGeneration() {
        apiPage.openPage();
        apiPage.addColumns();
        apiPage.setColumnValues();
        apiPage.setNumberOfRows();
        apiPage.generateAPI();
        apiPage.fetchGetURI();
        apiPage.tableDataExtractorIntoJson();
        
        // Assert API generation success
       // Assert.assertTrue(apiPage.isDisplayed(By.xpath("//h3[text()='API generated successfully']")), "API not generated.");
    }

    @AfterClass
    public void tearDown() {
        // Quit WebDriver after test execution
        TestBase.getInstance().quitDriver();
    }
}
