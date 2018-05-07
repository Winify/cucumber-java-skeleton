package stepDefinitions.support;

import com.cucumber.listener.Reporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.OutputType.BYTES;

public class Hooks extends TestUtils {

    @After
    public void afterScenario(Scenario scenario) throws IOException {

        if (!scenario.isFailed()) {
            return;
        }

        // Take screenshot for cucumber-reporting
        byte[] screenshotByte = takeScreenshotAs(BYTES);
        scenario.embed(screenshotByte, "image/png");

        // Take screenshot for cucumber-extentsreport
        String scenarioName = scenario.getName().replaceAll(" ", "_");
        File screenshotFile = takeScreenshotAndCopyToTarget(scenarioName);

        Reporter.addScreenCaptureFromPath("./screenshots/" + screenshotFile.getName());
    }

}

