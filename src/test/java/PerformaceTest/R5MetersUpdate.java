package PerformaceTest;

import CMBC_code.CsvParcer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.io.Serial;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import static java.time.Duration.ofSeconds;

//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class R5MetersUpdate {

     WebDriver driver = new EdgeDriver();
    CsvParcer csvParcer = new CsvParcer();

    //String tablesFilePAth = "src\\main\\resources\\CMBC\\CMBC_PRDEAM_Reports_Tables_Analysis.csv";
    //String primaryKeys = "src\\main\\resources\\CMBC\\EAM_12.1_PrimaryKeys_revised.csv";

    String loginPage = "https://ca1.eam.hxgnsmartcloud.com/web/base/logindisp?tenant=BVBWV1707339191_DEV";

    public R5MetersUpdate() throws IOException {
    }
    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.edge.driver", "src\\main\\resources\\msedgedriver.exe");
    }

    @BeforeMethod
    public void setUp() {
        // Initialize EdgeDriver
        driver = new EdgeDriver();
    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(10));
    public static void performActions(WebDriver driver, String loginPage) {
        try {
            // Open the login page
            driver.get(loginPage);

            // Initialize WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(40));

            // Wait and sleep to ensure page load
            Thread.sleep(20000);

            // Locate and click the start menu
            WebElement startMenu = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='appheadericon-1043-btnEl']")));
            startMenu.click();

            // Wait and locate the Work menu
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Work']")));
            WebElement WorkMenu = driver.findElement(By.xpath("//span[text()='Work']"));
            WorkMenu.click();

            // Locate and click the Azure folder
            WebElement AzureFolder = driver.findElement(By.xpath("//table[36]/tbody/tr/td/div/span[text()='Azure']"));
            AzureFolder.click();

            // Sleep to ensure actions are completed
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void metersUpdate() throws InterruptedException {
        performActions(driver, loginPage);
        WebElement MetersScreen = driver.findElement(By.xpath("//table[38]/tbody/tr/td/div/span[text()='Meters']"));
        MetersScreen.click();
        Thread.sleep(3000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div/div/div/div[1]/div/div/div/div/div/div[1]/div/div[2]/input")));
        WebElement SearchMetersField = driver.findElement((By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div/div/div/div[1]/div/div/div/div/div/div[1]/div/div[2]/input")));
        SearchMetersField.sendKeys("PERFORMANCETEST");
        WebElement SearchButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div/div/div/div[1]/div/div/div/div/div/div[1]/div/div[3]"));
        SearchButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#uxnumber-1215-inputEl")));
        WebElement maxValueElement = driver.findElement(By.cssSelector("#uxnumber-1215-inputEl"));
        maxValueElement.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String maxValueText = (String) js.executeScript("return arguments[0].value;", maxValueElement);
        System.out.println("MaxValue: " +  maxValueText);
        int currentValue = Integer.parseInt(maxValueText);
        int testInterval = currentValue+100;
        WebElement SaveButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[2]/div/div/a[2]/span/span/span[1]"));
        for (int i = currentValue+1; i < testInterval; i++) {
            maxValueElement.clear();
            maxValueElement.sendKeys(String.valueOf(i));
            SaveButton.click();
            Thread.sleep(2000);
        }
    }

    @Test
    public void partUpdate() throws InterruptedException {
        performActions(driver, loginPage);
        WebElement PartScreen = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div[2]/table[41]/tbody/tr/td/div/span"));
        PartScreen.click();
        Thread.sleep(3000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div[3]/div/div/div[2]/div/div/div/div[1]/div/div/input")));
        WebElement DescriptionField =  driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div[3]/div/div/div[2]/div/div/div/div[1]/div/div/input"));
        DescriptionField.sendKeys("PERFORMANCETEST");
        WebElement RunButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[1]/div/div/div/div/div/div[1]/div/div/a[3]/span/span/span[2]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", RunButton);

        // Optionally, use JavaScript to click if standard click fails
        js.executeScript("arguments[0].click();", RunButton);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tableview-1176-record-1170']/tbody/tr/td[contains(@class, 'x-grid-cell') and contains(@class, 'x-grid-td') and contains(@class, 'x-grid-cell-gridcolumn-1178') and contains(@class, 'x-wrap-cell') and contains(@class, 'x-unselectable')]/div")));
        WebElement partRecord = driver.findElement(By.xpath("//*[@id='tableview-1176-record-1170']/tbody/tr/td[contains(@class, 'x-grid-cell') and contains(@class, 'x-grid-td') and contains(@class, 'x-grid-cell-gridcolumn-1178') and contains(@class, 'x-wrap-cell') and contains(@class, 'x-unselectable')]/div"));


        Actions actions = new Actions(driver);
        actions.doubleClick(partRecord).perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[3]/div/div/div/div/div/div[1]/div[1]/div[2]/div/div/div/div/div/div/div[1]/div/div/div/div/div/table/tbody/tr/td[1]/div/table/tbody/tr[1]/td/div/div/div/div[2]/div[2]/div/div/textarea")));

        WebElement longDescription = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div[3]/div/div/div/div/div/div[1]/div[1]/div[2]/div/div/div/div/div/div/div[1]/div/div/div/div/div/table/tbody/tr/td[1]/div/table/tbody/tr[1]/td/div/div/div/div[2]/div[2]/div/div/textarea"));
        longDescription.click();

        WebElement SaveButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[2]/div/div/a[2]"));

        int testInterval = 100;
        //WebElement SaveButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[2]/div/div/a[2]/span/span/span[1]"));
        for (int i = 1; i < testInterval; i++) {
            longDescription.clear();
            longDescription.sendKeys(String.valueOf(i));
            SaveButton.click();
            Thread.sleep(2000);
        }
    }


}