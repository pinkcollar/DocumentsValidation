import net.bytebuddy.description.annotation.AnnotationDescription;
import org.asynchttpclient.util.Assertions;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class AsureLogin extends AbstractBasePage<AsureLogin> {
    String URL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_AX2/4879940d-6e64-4140-977f-8a5e6c765b4b?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    public AsureLogin(WebDriver driver, String locatorsFile) {
        super(driver, locatorsFile);
    }

    @Override
    protected void load() {
        driver.get(URL);
    }

    @Override
    protected void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("inforcloudsuite"));
    }

    public void login(WebDriver driver, String env) {
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        driver.findElement(By.linkText("TransLink Azure AD - Infor "+env)).click();
//        if(driver is chrome) {
//            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("i0116"))));
//            driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
//            driver.findElement(By.id("idSIButton9")).click();
//            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
//            driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();
//        }
    }
}
