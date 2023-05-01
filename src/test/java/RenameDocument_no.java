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

import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
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
public class RenameDocument_no {

    //document.querySelector("#uxpicktriggerfield-1160-inputEl")
//


        WebDriver driver = new ChromeDriver();
    private static final String documentsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSDOCU%26SYSTEM_FUNCTION_NAME%3DBSDOCU&LogicalId=lid://infor.eam.eam";


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

            driver.get(documentsURL);
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
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("uxpicktriggerfield-1137-inputEl"))));//searchField
            driver.findElement(By.id("uxpicktriggerfield-1137-inputEl")).sendKeys("1063");
            driver.findElement(By.id("uxpicktriggerfield-1137-trigger-trigger")).click();//click search
            Thread.sleep(6000);
            System.out.println(driver.findElement(By.id("textfield-1201-inputEl")).getText());//read documentDescription
            //validationHere

            driver.findElement(By.id("textfield-1201-inputEl")).sendKeys("Renamed_with_auto_script.pdf");
            driver.findElement(By.id("button-1103-btnIconEl")).click();//saveButton

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



