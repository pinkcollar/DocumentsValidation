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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
public class RenameDocument_no {

    //document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
    String msdsFilePath = "src\\main\\resources\\msds_desc.csv";

    WebDriver driver = new ChromeDriver();
   // private static final String documentsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSDOCU%26SYSTEM_FUNCTION_NAME%3DBSDOCU&LogicalId=lid://infor.eam.eam";
    private static final String documentsURL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_AX2/4879940d-6e64-4140-977f-8a5e6c765b4b?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSDOCU%26SYSTEM_FUNCTION_NAME%3DBSDOCU&LogicalId=lid://infor.eam.eam";
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
    CsvParcer csvParcer = new CsvParcer();

        @Test
        public void test1() throws InterruptedException, IOException {
            driver.get(documentsURL);
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(120));
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);


            driver.findElement(By.linkText("TransLink Azure AD - Infor AX2")).click();
            //TransLink Azure AD - Infor DEV
            driver.findElement(By.id("i0116")).sendKeys("olga.luneva@translink.ca");
            driver.findElement(By.id("idSIButton9")).click();
            Thread.sleep(10000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Send Me a Push ']"))));
            driver.findElement(By.xpath("//*[text()='Send Me a Push ']")).click();

            Thread.sleep(20000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("EAM_4879940d-6e64-4140-977f-8a5e6c765b4b"))));
            //iFrame - EAM_4879940d-6e64-4140-977f-8a5e6c765b4b -- AX2
            //iFrame - EAM_637fada2-ade6-4c51-ac37-5ef13b91bdb2 -- DEV
            WebElement iframe =  driver.findElement(By.name("EAM_4879940d-6e64-4140-977f-8a5e6c765b4b"));
            driver.switchTo().frame(iframe);
            HashMap<String, String> documentDescriptionMap = csvParcer.getAttributeValueMap(msdsFilePath, "Original_FileName", "desc") ;
            for (Map.Entry<String,String> entry: documentDescriptionMap.entrySet()
            ) {
                Thread.sleep(6000);
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("uxpicktriggerfield-1137-inputEl"))));//searchField
                driver.findElement(By.id("uxpicktriggerfield-1137-inputEl")).clear();
                driver.findElement(By.id("uxpicktriggerfield-1137-inputEl")).sendKeys(entry.getKey());
                driver.findElement(By.id("uxpicktriggerfield-1137-trigger-trigger")).click();//click search
                Thread.sleep(6000);
                System.out.println(driver.findElement(By.id("textfield-1202-inputEl")).getText() );//read documentDescription //msds0274
                //validationHere

                driver.findElement(By.id("textfield-1202-inputEl")).sendKeys(entry.getValue()+"_" + entry.getKey().split("_")[0]);
                driver.findElement(By.id("button-1103-btnIconEl")).click();//saveButton
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



