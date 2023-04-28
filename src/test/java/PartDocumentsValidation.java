import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class PartDocumentsValidation {

    WebDriver driver = new ChromeDriver();



    // Instantiate a IEDriver class.
    //WebDriver driver=new InternetExplorerDriver();
    //String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

    PartsPage partsPage = new PartsPage(driver);
    @Test
    public void test1() throws InterruptedException {

        driver.get(partsURL);
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        //login();
        driver.findElement(By.linkText("TransLink Azure AD - Infor DEV")).click();
        driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        driver.findElement(By.id("idSIButton9")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();

        Thread.sleep(20000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"))));
        WebElement iframe =  driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"));
        driver.switchTo().frame(iframe);
        Thread.sleep(6000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("uxpicktriggerfield-1160-inputEl"))));
        driver.findElement(By.id("uxpicktriggerfield-1160-inputEl")).sendKeys("A02-00-0102-393");
        driver.findElement(By.id("uxpicktriggerfield-1160-trigger-trigger")).click();
        Thread.sleep(6000);
        System.out.println(driver.findElement(By.id("textfield-1348-inputEl")).getText());
        {
            WebElement element = driver.findElement(By.id("textfield-1348-inputEl"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.cssSelector("#ext-element-13 > .x-tab-wrap")).click();
        driver.findElement(By.id("menuitem-1251-textEl")).click();
        driver.findElement(By.cssSelector(".x-tree-node-text:nth-child(4)")).click();
        //Element: [[ChromeDriver: chrome on WINDOWS (48c856e5532e7b2be3905704cfeb8b3f)] -> name: EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2]




        driver.close();
        driver.quit();
    }

    private void checkUrl(String expectedUrl) {
        String actualURL = driver.getCurrentUrl();
        Assertions.assertEquals(actualURL, expectedUrl);
    }
    private void findPartNumber(String expectedLegacyValue) {
        partsPage.getSearchField().sendKeys(expectedLegacyValue);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("textfield-1348-inputEl"))));//legacyNumber
        findPartNumber(expectedLegacyValue);
        WebElement actualLegacyNumber = driver.findElement(By.id("textfield-1348-inputEl"));
        Assertions.assertEquals(actualLegacyNumber, expectedLegacyValue);
    }
    private void login() {
        driver.findElement(By.linkText("TransLink Azure AD - Infor DEV")).click();
        driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        driver.findElement(By.id("idSIButton9")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();
    }

}

