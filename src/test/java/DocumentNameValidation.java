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
public class DocumentNameValidation {

    WebDriver driver = new ChromeDriver();



    // Instantiate a IEDriver class.
    //WebDriver driver=new InternetExplorerDriver();
    //String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV";
    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

    PartsPage partsPage = new PartsPage(driver);
    @Test
    public void test1() throws InterruptedException {

        driver.get(partsURL);
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        //login();
        driver.findElement(By.linkText("TransLink Azure AD - Infor DEV")).click();
        driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        driver.findElement(By.id("idSIButton9")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("mhdrAppBtn"))));
        driver.findElement(By.cssSelector("#mhdrAppBtn > .nm-icons")).click();
        driver.findElement(By.cssSelector("#\\36 37fada2-ade6-4c51-ac37-5ef13b91bdb2-menu > #icdeskSClk")).click();

       wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"))));
        WebElement iframe =  driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"));
       driver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("appheadericon-1034-btnEl"))));
        driver.findElement(By.id("appheadericon-1034-btnEl")).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("*[@id='treeview-1049-record-6']/tbody/tr/td/div/span"))));
        driver.findElement(By.xpath("*[@id='treeview-1049-record-6']/tbody/tr/td/div/span")).click();



//        driver.findElement(By.cssSelector(".x-grid-item-focused .x-tree-node-text")).click();
//        WebElement iframe2 =  driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"));
//        driver.switchTo().frame(iframe2);
//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("appheadericon-1034-btnEl"))));
//
//
//
//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("appheadericon-1034-btnEl"))));
//        driver.findElement(By.id("appheadericon-1034-btnEl")).click();
//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("mhdrSite"))));
//        driver.findElement(By.id("mhdrSite")).click();//EAM menu
//        driver.findElement(By.linkText("Materials")).click();
//        driver.findElement(By.linkText("Parts")).click();
        //driver.navigate().refresh();
        //checkUrl("https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2");
        //https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam




//        WebElement iframe= driver.findElement(By.xpath("//iframe[@name='EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2']"));
//        driver.switchTo().frame(iframe);


//        driver.findElement(By.id("textfield-1348-inputEl")).sendKeys("A04-02-0104-810");
//        String expectedLegacyValue = "A02-00-0102-393";
//        findPartNumber(expectedLegacyValue);
//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("uxpicktriggerfield-1160-inputWrap"))));

//TransLink Azure AD - Infor DEV


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
