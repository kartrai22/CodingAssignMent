package Listeners;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Base.TestBase;
import Reports.ExtentManager;

public class TestListeners implements ITestListener {
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = ExtentManager.getTest();
    
    @Override
    public void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getName()));
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        
        if (testInstance instanceof TestBase) {
            WebDriver driver = ((TestBase) testInstance).getDriver();
            String screenshotPath = TestBase.captureScreenshot(result.getName(), driver);

            if (screenshotPath != null) {
                test.get().fail("Test Failed. Screenshot: " + test.get().addScreenCaptureFromPath(screenshotPath));
            } else {
                test.get().fail("Test Failed. Screenshot not available.");
            }
        } else {
            test.get().fail("Test Failed. WebDriver not available for screenshot.");
        }
    }
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
