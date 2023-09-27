import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class PartDocumentsValidation_TST {

    //WebDriver driver = new ChromeDriver();
    WebDriver driver = new EdgeDriver();
    CsvParcer csvParcer = new CsvParcer();

    String msdsFilePath = "src\\main\\resources\\msds_docs_legacyPartNumber.csv";
    // Instantiate a IEDriver class.

    //String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    //String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DSSPART%26SYSTEM_FUNCTION_NAME%3DSSPART&LogicalId=lid://infor.eam.eam";
    String partsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_TST";
    @BeforeAll
    public static void setProperty() {
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");
        System.setProperty("webdriver.edge.driver", "src\\main\\resources\\msedgedriver.exe");
    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));
    PartsPage partsPage = new PartsPage(driver);
    Map<String, Object> vars;


    public String waitForWindow(int timeout) {
        vars = new HashMap<String, Object>();
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> whNow = driver.getWindowHandles();

        return whNow.iterator().next();
    }
    @Test
    public void test1() throws InterruptedException, IOException {
        driver.get(partsURL);
        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        //login();
       driver.findElement(By.linkText("TransLink Azure AD - Infor TST")).click();
       //No need with EDGE browser
        //driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
        //driver.findElement(By.id("idSIButton9")).click();
       // Thread.sleep(10000);
       // wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
        //driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();

        Thread.sleep(10000);

        {
            WebElement element = driver.findElement(By.id("rNavUsrBtn"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.cssSelector("#mhdrAppBtn > .nm-icons")).click();
        String originalWindow = driver.getWindowHandle();

//Check we don't have other windows open already
        assert driver.getWindowHandles().size() == 1;
//Click the link which opens in a new window
        driver.findElement(By.cssSelector("#b2dd004b-dc76-473c-8f3b-6872955c69ca-menu > #icdeskSClk")).click();

        Thread.sleep(6000);
        Set<String> windowHandles = driver.getWindowHandles();
        waitForWindow(2000);

//        vars.put("root", driver.getWindowHandle());

//Loop through until we find a new window handle
        for (String windowHandle : driver.getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

//Wait for the new tab to finish loading content
        wait.until(titleContains("HxGN EAM"));

        driver.findElement(By.id("appheadericon-1038-btnEl")).click();//click Start Center

        wait.until(presenceOfElementLocated(By.xpath("//span[contains(text(),'Materials')]")));// wait for Materials menu

        driver.findElement(By.xpath("//span[contains(text(),'Materials')]")).click();// Click on Materials

        wait.until(presenceOfElementLocated(By.xpath("//span[contains(text(),'Parts')]")));// wait for parts

        driver.findElement(By.xpath("//span[contains(text(),'Parts')]")).click(); // click on Parts

        String searchFieldID = "uxpicktriggerfield-1162-inputEl";
        driver.findElement(By.id(searchFieldID)).click();
        driver.findElement(By.id(searchFieldID)).sendKeys("A02-00-0102-393");
        driver.findElement(By.id(searchFieldID)).sendKeys(Keys.ENTER);
        driver.findElement(By.id("tab-1234-btnInnerEl")).click(); // open Documents tab

        driver.findElement(By.xpath("//span[contains(text(),'No Document')]")).click();

        HashMap<String, String> documentDescriptionMap = csvParcer.getAttributeValueMap(msdsFilePath, "legacy_part_number", "Document") ;
        for (Map.Entry<String,String> entry: documentDescriptionMap.entrySet()
        ) {
            Thread.sleep(6000);
            driver.findElement(By.id(searchFieldID)).clear();
           driver.findElement(By.id(searchFieldID)).sendKeys(entry.getKey());


            driver.findElement(By.id(searchFieldID)).sendKeys(Keys.ENTER);
            Thread.sleep(6000);
            String documentNumber = driver.findElement(By.xpath("//span[contains(text(),'"+ entry.getValue()  + ":')]")).getText();
            Assertions.assertTrue( documentNumber.contains(entry.getValue()));
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


}

