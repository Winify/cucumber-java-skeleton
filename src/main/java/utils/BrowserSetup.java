package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

public class BrowserSetup {

    private enum BrowserType {
        FF("firefox", WebDriverManager.firefoxdriver(), DesiredCapabilities::firefox, FirefoxDriver::new),
        CHROME("chrome", WebDriverManager.chromedriver(), DesiredCapabilities::chrome, ChromeDriver::new),
        IE("ie", WebDriverManager.iedriver(), DesiredCapabilities::internetExplorer, InternetExplorerDriver::new),
        PHANTOM("phantomjs", WebDriverManager.phantomjs(), DesiredCapabilities::phantomjs, PhantomJSDriver::new),
        INVALID;

        private String code;
        private WebDriverManager driverManager;
        private Supplier<DesiredCapabilities> capabilitiesSupplier;
        private Supplier<WebDriver> driverSupplier;

        BrowserType() {
            this(null, WebDriverManager.chromedriver(), DesiredCapabilities::chrome, ChromeDriver::new);
        }

        BrowserType(String code, WebDriverManager driverPropertyName, Supplier<DesiredCapabilities> capabilitiesSupplier, Supplier<WebDriver> driverSupplier) {
            this.code = code;
            this.driverManager = driverPropertyName;
            this.capabilitiesSupplier = capabilitiesSupplier;
            this.driverSupplier = driverSupplier;
        }

        public WebDriver toDriver() {
            return driverSupplier.get();
        }

        public WebDriverManager getDriverManager() {
            return driverManager;
        }

        public DesiredCapabilities toCapabilities() {
            return capabilitiesSupplier.get();
        }

        public static BrowserType fromCode(String code) {
            for (BrowserType type : values()) {
                if (code.equals(type.code)) {
                    return type;
                }
            }

            return INVALID;
        }
    }

    private static WebDriver getWebDriver(String browserTypeCode) {

        BrowserType browserType = BrowserType.fromCode(browserTypeCode);

        if (browserType == BrowserType.INVALID) {
            System.err.println("Browser [" + browserTypeCode + "] is invalid, Launching Chrome as browser of choice...");
        }

        browserType.getDriverManager().setup();

        return browserType.toDriver();
    }

    private static DesiredCapabilities getBrowserCapabilities(String browserTypeCode) {

        BrowserType browserType = BrowserType.fromCode(browserTypeCode);

        if (browserType == BrowserType.INVALID) {
            System.out.println("Browser [" + browserTypeCode + "] is invalid, Launching Chrome as browser of choice...");
        }

        return browserType.toCapabilities();
    }

    public static WebDriver getLocalDriver(String browser) {
        return getWebDriver(browser);
    }

    static RemoteWebDriver getRemoteDriver(String browserType, String hubUrl) throws IOException {

        DesiredCapabilities capabilities = getBrowserCapabilities(browserType);

        return new RemoteWebDriver(new URL(hubUrl), capabilities);
    }

}
