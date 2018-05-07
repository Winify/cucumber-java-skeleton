package stepDefinitions;

import cucumber.api.java8.En;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pageObjects.GooglePage;
import utils.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class GoogleSteps extends TestUtils implements En {

    private GooglePage page;

    public GoogleSteps() {
        WebDriver driver = TestUtils.getDriver();

        Given("^(?:hogy )?a Google oldalára akarunk lépni$", () -> page = GooglePage.get(driver));

        When("^rákeresek a (.*) kifejezésre$", searchKey -> page.search((String) searchKey));

        Then("^(.*) lesz az oldal címe$", title -> assertThat(driver.getTitle(), is(equalTo(title))));

        Then("^látnom kell a keresési találatokat$", () -> assertThat(driver.findElement(By.cssSelector("div#search")).isDisplayed(), is(equalTo(Boolean.TRUE))));
    }
}
