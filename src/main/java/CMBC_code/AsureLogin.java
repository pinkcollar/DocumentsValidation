package CMBC_code;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class AsureLogin extends AbstractBasePage<AsureLogin> {
    String URL = "https://ca1.eam.hxgnsmartcloud.com/web/base/logindisp?tenant=BVBWV1707339191_DEV";

     //System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");

    // Initialize EdgeDriver
    WebDriver driver = new EdgeDriver();
    public AsureLogin(WebDriver driver, String locatorsFile) {
        super(driver, locatorsFile);
    }

    @Override
    protected void load() {
        driver.get(URL);
    }

    @Override
    public void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        System.out.println(url);
        Assert.assertTrue(url.contains("https://ca1.eam.hxgnsmartcloud.com/web/base/ssoservlet"));
    }
    public void login(WebDriver driver, String env) {
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        //driver.findElement(By.linkText("TransLink Azure AD - Infor "+env)).click();
    }
}
