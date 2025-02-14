package pages;

import org.json.JSONArray;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Exception.UIExceptions;
import utils.ConfigReader;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiGeneratorPage {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;
	private UIExceptions uiExceptions;

	private BasePage base;

	// Locators
	private By addColumnButton = By.xpath("//p[contains(text(),'Add Column')]");
	private By iframe = By.xpath("//iframe[@title='REST API Generator']");
	private By rowInput = By.xpath("//input[@placeholder='Enter rows']");
	private By generateApiButton = By.xpath("//p[text()='Generate API']");
	private By successMessage = By.xpath("//h3[text()='API generated successfully']");
	private By dataTypeDD = By.className("ant-cascader-menu-item");
	private String columnInputXPath = "//input[@id='input_name--{}']";
	private String dataTypeInputXPath = "(//input[@value=''])[{}]";
	private By getURI = By.xpath("//a[contains(text(),'retool')]");
	private By columnTitle = By.xpath("//span[@class='column-title']");
	private By Rows = By.className("inner-cell-container");

	// Constructor
	public ApiGeneratorPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.js = (JavascriptExecutor) driver;
		this.base = new BasePage();
	}

	// Open the API Generator page
	public void openPage() {
		driver.manage().window().maximize();
		driver.get(ConfigReader.getProperty("url"));
		js.executeScript("window.scrollBy(0,332)");
		driver.switchTo().frame(driver.findElement(iframe));
	}

	// Add columns dynamically
	public void addColumns() {
		int columnCount = Integer.parseInt(ConfigReader.getProperty("columns.count"));
		wait.until(ExpectedConditions.elementToBeClickable(addColumnButton));
		for (int i = 0; i < columnCount - 1; i++) {
			driver.findElement(addColumnButton).click();
		}
	}

	// Set column values
	public void setColumnValues() {
		List<String> columnNames = Arrays.asList(ConfigReader.getProperty("columns.names").split(","));
		List<String> options = Arrays.asList(ConfigReader.getProperty("columns.options").split(","));
		List<String> subOptions = Arrays.asList(ConfigReader.getProperty("columns.subOptions").split(","));

		System.out.println(columnNames);
		System.out.println(options);
		System.out.println(subOptions);

		for (int index = 0; index < columnNames.size(); index++) {
			driver.findElement(By.xpath("//input[@id='input_name--" + index + "']")).click();
			driver.findElement(By.xpath("//input[@id='input_name--" + index + "']")).sendKeys(Keys.CONTROL + "A");
			driver.findElement(By.xpath("//input[@id='input_name--" + index + "']")).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath("//input[@id='input_name--" + index + "']")).sendKeys(columnNames.get(index));
			int index1 = index + 1;
			driver.findElement(By.xpath("(//input[@value=''])[" + index1 + "]")).click();
			driver.findElement(By.xpath("(//input[@value=''])[" + index1 + "]")).sendKeys(options.get(index));
			List<WebElement> ele = driver.findElements(dataTypeDD);
			for (WebElement el : ele) {
				if (el.getText().contains(subOptions.get(index))) {
					el.click();
				}
			}
		}
	}

	public void fetchGetURI() {
		try {
			String URI = driver.findElement(getURI).getText().trim();
			System.out.println(URI);
			base.writeToConfig("BaseURI", URI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new UIExceptions("issue in writing Base URI" + e.getMessage());
		}

	}

	// Set number of rows
	public void setNumberOfRows() {
		String rows = ConfigReader.getProperty("rows.count");
		WebElement rowField = wait.until(ExpectedConditions.visibilityOfElementLocated(rowInput));
		rowField.click();
		rowField.sendKeys(Keys.CONTROL + "A");
		rowField.sendKeys(Keys.BACK_SPACE);
		rowField.sendKeys(rows);

	}

	// Generate API
	public void generateAPI() {
		driver.findElement(generateApiButton).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
	}

	// Verify API generation
	public boolean isApiGenerated() {
		return driver.findElement(successMessage).isDisplayed();
	}

	public void tableDataExtractorIntoJson() {
		List<WebElement> Headers = driver.findElements(columnTitle);
		List<String> columnNames = new ArrayList<>();
		for (WebElement header : Headers) {
			columnNames.add(header.getText().trim());
		}
		System.out.println("coloumnNames" + columnNames);
		List<WebElement> rows = driver.findElements(Rows);
		List<Map<String, String>> tableData = new ArrayList<>();

		if (rows.size() % 5 != 0) {
			System.out.println("Warning: rows count is not a multiple of 5. Some extra data might be present.");
		}

		for (int i = 0; i + 4 < rows.size(); i += 5) {
			Map<String, String> rowData = new LinkedHashMap<>();
			boolean isValidRow = true;

			for (int j = 0; j < 5 && (i + j) < rows.size(); j++) {
				String cellText = rows.get(i + j).getText().trim();

				// Check for unwanted API-related data
				if (cellText.contains("GET") || cellText.contains("POST") || cellText.contains("PUT")
						|| cellText.contains("PATCH") || cellText.contains("/XYF6uf/data")) {
					isValidRow = false; // Mark row as invalid
				}

				rowData.put(columnNames.get(j), cellText);
			}

			if (isValidRow) { // Only add valid data rows
				tableData.add(rowData);
				System.out.println("Extracted Row: " + rowData);
			} else {
				System.out.println("Skipped Invalid Row: " + rowData);
			}

		}

        JSONArray jsonArray = new JSONArray(tableData);

        // Write JSON to file
        try (FileWriter file = new FileWriter("API Preview.json")) {
            file.write(jsonArray.toString(4)); // Pretty print JSON
            System.out.println("Data successfully written to tableData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
