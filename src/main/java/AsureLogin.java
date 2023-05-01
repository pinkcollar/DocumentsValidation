import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class AsureLogin {
    WebDriver driver;

    public AsureLogin(WebDriver driver) {
        this.driver = driver;
    }

    public void login(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        driver.findElement(By.linkText("TransLink Azure AD - Infor DEV")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("i0116"))));
        driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        driver.findElement(By.id("idSIButton9")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();
    }
}
