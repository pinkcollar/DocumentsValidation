import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static io.ous.jtoml.impl.Token.TokenType.Key;
import static java.time.Duration.ofSeconds;

public class DocumentsUpload {

    //document.querySelector("#uxpicktriggerfield-1160-inputE

    String documentUploadPage = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSIMPT%26SYSTEM_FUNCTION_NAME%3DBSIMPT&LogicalId=lid://infor.eam.eam";
        WebDriver driver = new ChromeDriver();

        @BeforeAll
        public static void setProperty() {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

        }

        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

        PartsPage partsPage = new PartsPage(driver);


        @Test
        public void test1() throws InterruptedException, AWTException, IOException {

            driver.get(documentUploadPage);
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            //login();
            driver.findElement(By.linkText("TransLink Azure AD - Infor DEV")).click();
            driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
            driver.findElement(By.id("idSIButton9")).click();
            Thread.sleep(13000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
            driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();
            Thread.sleep(20000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"))));
            WebElement iframe =  driver.findElement(By.name("EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2"));
            driver.switchTo().frame(iframe);
            Thread.sleep(6000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("tab-1132-btnInnerEl"))));//Batch Document Upload tab
            driver.findElement(By.id("tab-1132-btnInnerEl")).click();

            driver.findElement(By.id("lovfield-1287-inputEl")).sendKeys("BCRTC");

            Thread.sleep(1000);

            String uploadDocsPath = "c:\\Users\\oluneva\\OneDrive - TransLink\\Vendor Documents\\21Tech\\Data-21Tech\\Inventory Management Data\\Data Modelling and Design\\D5 UAT\\Data Source\\MSDS\\\"msds0007.pdf\" \"msds0008.pdf";
            //files to add "\"msds0007.PDF" "msds0008.PDF""

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection str = new StringSelection(uploadDocsPath);
            clipboard.setContents(str, null);

            WebElement element = driver.findElement(By.id("uxselectfile-1268-button-btnWrap"));//Add button
            JavascriptExecutor jsx = (JavascriptExecutor) driver;

            jsx.executeScript("arguments[0].click();", element);
            Thread.sleep(6000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            Thread.sleep(6000);
            Thread.sleep(10000);
            driver.findElement(By.id("button-1270-btnInnerEl")).click();//click Upload
            //uxselectfile-1996-button
            Thread.sleep(10000);
            driver.close();
            driver.quit();
            //"msds0013" "msds0007" "msds0007B" "msds0008" "msds0008b" "msds0009" "msds0010" "msds0011" "msds0012" 
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
        //imgs_to_load.csv
        //msds_to_load.csv

    }



