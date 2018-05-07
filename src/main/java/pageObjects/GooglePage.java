package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.PageUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class GooglePage extends PageUtils {

    @FindBy(name = "q")
    private WebElement searchBar;

    private GooglePage(WebDriver driver) {
        super(driver);
    }

    public static GooglePage get(WebDriver driver) {
        String url = "http://www.google.com";
        driver.get(url);

        assertThat(driver.getTitle(), is(equalTo("Google")));

        return new GooglePage(driver);
    }

    public void search(String searchKey) {

        searchBar.clear();
        searchBar.sendKeys(searchKey);

        searchBar.submit();
    }
}
