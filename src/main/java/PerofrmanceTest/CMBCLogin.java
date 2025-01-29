package PerofrmanceTest;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class CMBCLogin extends AbstractBasePage<CMBCLogin> {
    String URL = "https://ca1.eam.hxgnsmartcloud.com/sso/samlconnect?service=https%3A%2F%2Fca1.eam.hxgnsmartcloud.com%3A443%2Fweb%2Fbase%2Fssoservlet%3Ftenant%3DBVBWV1707339191_DEV";

    public CMBCLogin(WebDriver driver, String locatorsFile) {
        super(driver, locatorsFile);
        // Ensure driver is not null
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance cannot be null");
        }
    }

    @Override
    protected void load() {
        driver.get(URL);
    }

    @Override
    public void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        System.out.println(url);
        Assert.assertTrue(url.contains("ssoservlet"));
    }

    public void login(String env) {
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-test-id='olga.luneva@translink.ca']")));
        driver.findElement(By.cssSelector("div[data-test-id='olga.luneva@translink.ca']")).click();
    }
}
