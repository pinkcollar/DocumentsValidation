import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;

//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class PartDocumentsValidation_AX2 {

    WebDriver driver = new ChromeDriver();
    CsvParcer csvParcer = new CsvParcer();

    String msdsFilePath = "src\\main\\resources\\msds_docs_legacyPartNumber.csv";
    // Instantiate a IEDriver class.
    //WebDriver driver=new InternetExplorerDriver();
    //String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
   // String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_AX2/4879940d-6e64-4140-977f-8a5e6c765b4b?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

    PartsPage partsPage = new PartsPage(driver);
    @Test
    public void test1() throws InterruptedException, IOException {
        driver.get(partsURL);
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        //login();
        driver.findElement(By.linkText("TransLink Azure AD - Infor AX2")).click();
        driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(6000);
        //Activate in case Send me a Push is not default action
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        //driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();

        Thread.sleep(20000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("EAM_4879940d-6e64-4140-977f-8a5e6c765b4b"))));
        //iFrame - EAM_4879940d-6e64-4140-977f-8a5e6c765b4b -- AX2
        //iFrame - EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2 -- DEV
        WebElement iframe =  driver.findElement(By.name("EAM_4879940d-6e64-4140-977f-8a5e6c765b4b"));
        driver.switchTo().frame(iframe);
        Thread.sleep(6000);
        //search field
        String searchFieldID = "uxpicktriggerfield-1157-inputEl";
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(searchFieldID))));
        //AX2 - uxpicktriggerfield-1157-inputEl
        //DEV - uxpicktriggerfield-1160-inputEl

        {
            WebElement element = driver.findElement(By.id("tabbar-1216-after-scroller"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("tabbar-1216-after-scroller")).click();
        driver.findElement(By.id("tabbar-1216-after-scroller")).click();
        driver.findElement(By.id("tabbar-1216-after-scroller")).click();
        Thread.sleep(6000);
        {
            WebElement element = driver.findElement(By.id("tab-1236-btnInnerEl"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }

        HashMap<String, String> documentDescriptionMap = csvParcer.getAttributeValueMap(msdsFilePath, "legacy_part_number", "Document") ;
        for (Map.Entry<String,String> entry: documentDescriptionMap.entrySet()
        ) {
            Thread.sleep(6000);
            driver.findElement(By.id(searchFieldID)).clear();
            driver.findElement(By.id(searchFieldID)).sendKeys(entry.getKey());
            String searchButtonID = "uxpicktriggerfield-1157-trigger-trigger";
            //Dev = uxpicktriggerfield-1160-trigger-trigger
            driver.findElement(By.id(searchButtonID)).click();
            Thread.sleep(6000);
            String documentNumber = driver.findElement(By.cssSelector(".x-tree-node-text:nth-child(4)")).getText().split(":")[0];
            Assertions.assertEquals(entry.getValue(), documentNumber);
        }

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

