import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.time.Duration.*;

public class PartsPage {
    WebDriver driver;

    public PartsPage(WebDriver driver) {
        this.driver = driver;
    }

    private By moreSpan = By.xpath("//span[contains(@title, 'More')]");

    public String getTitle() {
        return driver.getTitle();
    }

    public WebElement getSearchField() {
            return getWebElement(By.xpath("//*[@id='uxpicktriggerfield-1160-inputEl']"));
            //uxpicktriggerfield-1160-inputEl
        ////*[@id="uxpicktriggerfield-1160-inputEl"]
   }

    public WebElement getWebElement(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(60));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            throw new RuntimeException(locator + " element was not found", e);
        }
    }
    public WebElement getMoreSpan(){
        return getWebElement(moreSpan);
    }
    String URL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
}
