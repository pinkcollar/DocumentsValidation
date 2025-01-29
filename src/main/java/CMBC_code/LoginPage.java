package CMBC_code;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver) {
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
