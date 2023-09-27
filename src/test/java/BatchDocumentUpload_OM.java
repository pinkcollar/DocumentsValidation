//BatchDocumentUpload

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;

public class BatchDocumentUpload_OM {
    String documentUploadPage = ConfigurationManager.getInstance("AX2").getProperty("DEV_ENV");
    //WebDriver driver = new ChromeDriver();
    WebDriver driver = new EdgeDriver();
    public BatchDocumentUpload_OM() throws IOException {
    }

    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

    PartsPage partsPage = new PartsPage(driver);

    String msdsFilePath = ConfigurationManager.getInstance("AX2").getProperty("msdsFilePath");;
    String loadedDocsFilePath = ConfigurationManager.getInstance("AX2").getProperty("loadedDocsFilePath");
    String uploadDocsPath = "c:\\Users\\oluneva\\OneDrive - TransLink\\Vendor Documents\\21Tech\\Data-21Tech\\Inventory Management Data\\Data Modelling and Design\\D5 UAT\\Data Source\\MSDS\\";
    Iterable<CSVRecord> records;

    @Test
    public void batchDocumentUpload() throws InterruptedException, AWTException, IOException {
        //get file names to load
        HashSet<String> setOfFileNames =  getSetOfFileNamesToLoad(msdsFilePath, "\uFEFFName");
        HashSet<String> setOfLoadedFilesFullNames =  getSetOfFileNamesToLoad(loadedDocsFilePath,"File Path" );
        HashSet<String> shortNames = new HashSet<>();
        for (String fullName:setOfLoadedFilesFullNames
             ) {String shortNAme = fullName.split("_")[0] + ".pdf";
            shortNames.add(shortNAme);
        }
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
        HashSet<String> batchesFilePaths = getBatchUploadPaths(setOfFileNames,uploadDocsPath);
        //add files to load:

        for (String filesPath: batchesFilePaths
        ) {
            JavascriptExecutor jsx = (JavascriptExecutor) driver;

            //click add and load files

            //driver.findElement(By.id("lovfield-1287-inputEl")).sendKeys("BCRTC");
            WebElement addButton = driver.findElement(By.id("uxselectfile-1268-button-btnWrap"));//Add button
            jsx.executeScript("arguments[0].click();", addButton);
            Thread.sleep(6000);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection str = new StringSelection(filesPath);
            clipboard.setContents(str, null);


            //c:\Users\oluneva\OneDrive - TransLink\Vendor Documents\21Tech\Data-21Tech\Inventory Management Data\Data Modelling and Design\D5 UAT\Data Source\MSDS\

            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);


           Thread.sleep(30000);
            //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='In Queue']"))));
            driver.findElement(By.id("button-1270-btnInnerEl")).click();//click Upload
            //uxselectfile-1996-button
            Thread.sleep(20000);
            //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Uploaded']"))));
            WebElement refreshButton = driver.findElement(By.id("button-1289-btnInnerEl"));//Refresh button
            jsx.executeScript("arguments[0].click();", refreshButton);
            Thread.sleep(10000);
        }


        driver.close();
        driver.quit();
        //"msds0013" "msds0007" "msds0007B" "msds0008" "msds0008b" "msds0009" "msds0010" "msds0011" "msds0012"
    }

    @Test
    public void test2() throws  IOException {
        HashSet<String> setOfFileNames = getSetOfFileNamesToLoad(msdsFilePath, "\uFEFFName");
        getBatchUploadPaths(setOfFileNames, uploadDocsPath);

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

    //add files to load:

    public HashSet<String> getSetOfFileNamesToLoad(String filePath, String columnName) throws IOException {
        records = CsvParcer.getCSVrecordsIterator(filePath);
        CsvParcer csvParcer = new CsvParcer();
        HashSet<String> setOfFileNames = csvParcer.getRecordsSet(filePath, columnName);
        System.out.println("Number files to load: " + setOfFileNames.size());
        return setOfFileNames;
    }

    public HashSet<String> getSetOfUploadedFiles(String filePath) throws IOException {
        HashSet<String> setOfLoadedFilesFullNames = getSetOfFileNamesToLoad(filePath, "File Path");
        HashSet<String> shortNames = new HashSet<>();
        for (String fullName : setOfLoadedFilesFullNames
        ) {
            String shortNAme = fullName.split("_")[0].toLowerCase() + ".pdf";
            shortNames.add(shortNAme);
        }
        return shortNames;
    }
    public HashSet<String> getBatchUploadPaths(HashSet<String> setOfFileNames, String uploadDocsPath) throws IOException {
        HashSet<String> batchesFilePaths = new HashSet<>();
        HashSet<String> loadedFiles = getSetOfUploadedFiles(loadedDocsFilePath);

        String batchFilesToLoad = "";
        int batchSize = 0;
        int fileNumber = 0;
            for (String fileName : setOfFileNames
            ) {
                if(loadedFiles.contains(fileName.toLowerCase())) continue;
                batchFilesToLoad = batchFilesToLoad + "\"" + fileName + "\"" + " ";
                batchSize++;
                fileNumber++;
                if (batchSize == 10 || fileNumber == setOfFileNames.size()) {
                    batchesFilePaths.add(uploadDocsPath + batchFilesToLoad);
                    System.out.println(batchesFilePaths);

                    System.out.println("Files to load: " + batchFilesToLoad);
                    batchFilesToLoad = "";
                    batchSize = 0;
                }
            }
        return batchesFilePaths;
    }


}




