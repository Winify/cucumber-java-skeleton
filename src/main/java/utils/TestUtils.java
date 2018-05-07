package utils;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.OutputType.FILE;

@CucumberOptions(
        features = {"src/test/java/features"},
        format = {"json:target/cucumber-reports/cucumber.json", "html:target/site/cucumber-pretty"},
        plugin = {"pretty", "com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"},
//        tags = {"@Google"},
        glue = {"stepDefinitions"}
)
public class TestUtils extends AbstractTestNGCucumberTests {

    private static WebDriver driver;
    private static final long TIMEOUT_SECONDS = 10;

    @BeforeTest
    @Parameters({"directConnect", "browser", "seleniumHubUrl"})
    public void setUp(boolean local, String browserType, String hubUrl) throws IOException {
        if (local) {
            driver = BrowserSetup.getLocalDriver(browserType);
        } else {
            driver = BrowserSetup.getRemoteDriver(browserType, hubUrl);
        }

        driver.manage().timeouts().implicitlyWait(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        convertReportToUtf8();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    protected <T> T takeScreenshotAs(OutputType<T> outputType) {
        TakesScreenshot camera = (TakesScreenshot) driver;
        return camera.getScreenshotAs(outputType);
    }

    protected File takeScreenshotAndCopyToTarget(String screenshotName) throws IOException {
        File sourcePath = takeScreenshotAs(FILE);

        File destinationPath = new File(System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/" + screenshotName + ".png");
        FileUtils.copyFile(sourcePath, destinationPath, true);

        return destinationPath;
    }

    private void convertReportToUtf8() {

        Path reportPath = Paths.get(System.getProperty("user.dir") + "/target/cucumber-reports/report.html");
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Files.readAllBytes(reportPath));
            CharBuffer charBuffer = Charset.forName("windows-1252").decode(byteBuffer);

            byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
            Files.write(reportPath, byteBuffer.array());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

