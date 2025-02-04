package PerformaceTest;

import CMBC_code.CsvParcer;
import PerofrmanceTest.ConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.time.Duration.ofSeconds;

//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class R5MetersReadingUpdate {

     WebDriver driver = new EdgeDriver();
    CsvParcer csvParcer = new CsvParcer();

    //String tablesFilePAth = "src\\main\\resources\\CMBC\\CMBC_PRDEAM_Reports_Tables_Analysis.csv";
    //String primaryKeys = "src\\main\\resources\\CMBC\\EAM_12.1_PrimaryKeys_revised.csv";
    private static String environment = ConfigReader.get("environment");
    private static String loginPage = ConfigReader.get("loginPage_" + environment);
    private static String startMenuId = ConfigReader.get("startMenuId_" + environment);
    private static String azureTable = ConfigReader.get("azureTable_" + environment);


    public R5MetersReadingUpdate() throws IOException {
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
    public static void GoThroughMenu(WebDriver driver, String loginPage) throws InterruptedException {

            // Open the login page
            driver.get(loginPage);

            // Initialize WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, ofSeconds(40));

            // Wait and sleep to ensure page load
            Thread.sleep(10000);

            // Locate and click the start menu

        WebElement startMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[@id='" + startMenuId + "']")));
        startMenu.click();

            // Wait and locate the Work menu
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Work']")));
            WebElement WorkMenu = driver.findElement(By.xpath("//span[text()='Work']"));
            WorkMenu.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table[" + azureTable + "]/tbody/tr/td/div/span[text()='Azure']")));

            WebElement azureFolder = driver.findElement(
                By.xpath("//table[" + azureTable + "]/tbody/tr/td/div/span[text()='Azure']"));
            azureFolder.click();           // Sleep to ensure actions are completed
            Thread.sleep(2000);
    }
    //#treeview-1057-record-49 > tbody > tr > td > div
    //html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div[2]/table[41]/tbody/tr/td/div/span
    @Test
    public void metersReadingUpdate() throws InterruptedException {
        GoThroughMenu(driver, loginPage);

        // Initial check for 'pmWorkOrdersButton'

        // Path to the CSV file
        String filename = "c:\\Users\\oluneva\\Desktop\\CMBC\\Performance Test\\Minor_PPM_Objects_Meter_Due.csv";
        String columnName = "ppo_object";
        HashSet<String> equipmentsSet = (HashSet) readColumnFromCsv(filename, columnName);

        // Navigate to 'Batch Meter Readings'
        WebElement metersScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Batch Meter Readings']"));
        metersScreen.click();

        int batch = 0;

        for (String equipment : equipmentsSet) {
            batch++;

            // Check for 'pmWorkOrdersButton'
            checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));
            checkAndClickButton(driver, By.id("button-1013-btnInnerEl"));

            WebElement equipmentField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(2)")));
            doubleClickAndInsertValue(driver, equipmentField, equipment);

            WebElement differenceCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(6) > div > div")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", differenceCheckbox);

            try {
                differenceCheckbox.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", differenceCheckbox);
            }

            WebElement newValueField = driver.findElement(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(8) > div"));
            doubleClickAndInsertValue(driver, newValueField, "8000");

            WebElement updateMetersButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > div > div > div > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > a > span > span > span:nth-of-type(2)")));

            System.out.println(updateMetersButton + ": " + updateMetersButton.getText());
            if (batch == 10) {
                try {
                    updateMetersButton.click();
                } catch (ElementClickInterceptedException e) {
                    // Click the corresponding button in case of interception
                    checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));

                    // Reset batch and retry the update
                    batch = 0;
                    updateMetersButton.click();
                }
                batch = 0;
                String expectedText = "records were processed successfully.";
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[3]/div/div/div/div/div/div[1]/div/span[2]"), expectedText));
            }
        }
    }

    @Test
    public void metersReadingUpdateImproved() throws InterruptedException {
        GoThroughMenu(driver, loginPage);

        // Initial check for 'pmWorkOrdersButton'

        // Path to the CSV file
        String filename = "C:\\Users\\oluneva1\\IdeaProjects\\DocumentsValidation\\src\\main\\resources\\Minor_PPM_Objects_Meter_Due.csv";
        String columnName = "ppo_object";
        HashSet<String> equipmentsSet = (HashSet) readColumnFromCsv(filename, columnName);

        // Navigate to 'Batch Meter Readings'
        WebElement metersScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Batch Meter Readings']"));
        metersScreen.click();
        //while (true) {
        int batch = 0;

        for (String equipment : equipmentsSet) {
            batch++;

            // Check for 'pmWorkOrdersButton'
            checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));
            checkAndClickButton(driver, By.id("button-1013-btnInnerEl"));

            String equipmentSelector = ConfigReader.get("equipmentSelector_" + environment);

            // Replace the placeholder with the actual batch value
            equipmentSelector = equipmentSelector.replace("{batch}", String.valueOf(batch));

            WebElement equipmentField;

            if (equipmentSelector.startsWith("/")) {  // XPath
                equipmentField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(equipmentSelector)));
            } else {  // CSS Selector
                equipmentField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(equipmentSelector)));
            }
            doubleClickAndInsertValue(driver, equipmentField, equipment);

            // XPath for the difference checkbox
            String checkboxXPath = "/html/body/div[1]/div/div[2]/div/div/div/div/div[1]/div/div/div/div/div[3]/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div[2]/table[" + batch + "]/tbody/tr/td[6]/div/div";

// Wait until the checkbox is clickable
            WebElement differenceCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXPath)));

            try {
                // Check if the checkbox is already selected
                if (!differenceCheckbox.isSelected()) {
                    differenceCheckbox.click(); // If not selected, click it
                }
            } catch (Exception e) {
                // In case of an exception (e.g., ElementNotInteractableException), use JavaScript to click
                if (!differenceCheckbox.isSelected()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", differenceCheckbox);
                }
            }

            // XPath for the new value field
            String newValueXPath = "/html/body/div[1]/div/div[2]/div/div/div/div/div[1]/div/div/div/div/div[3]/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div[2]/table[" + batch + "]/tbody/tr/td[8]/div";

// Locate the new value field
            WebElement newValueField = driver.findElement(By.xpath(newValueXPath));

// Double-click and insert the value "8000"
            doubleClickAndInsertValue(driver, newValueField, "8000");

            String updateMetersButtonXPath = "/html/body/div[1]/div/div[2]/div/div/div/div/div[1]/div/div/div/div/div[3]/div/div/div/div/div/div/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div/div[1]/div/div/a/span/span/span[2]";

// Wait until the button is clickable
            WebElement updateMetersButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(updateMetersButtonXPath)
            ));
            System.out.println(updateMetersButton + ": " + updateMetersButton.getText());
            if (batch == 15) {
                try {
                    updateMetersButton.click();
                } catch (ElementClickInterceptedException e) {
                    // Click the corresponding button in case of interception
                    checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));
                    // Reset batch and retry the update
                    batch = 0;
                    updateMetersButton.click();
                }
                batch = 0;
                String expectedText = "records were processed successfully.";
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[3]/div/div/div/div/div/div[1]/div/span[2]"), expectedText));
            }
        }
       // }
    }

    @Test
    public void metersReadingUpdateTRN() throws InterruptedException, IOException {
        GoThroughMenu(driver, loginPage);

        // Path to the CSV file
        String filename = "c:\\Users\\oluneva\\Downloads\\Merged_Odometer_Data.csv";
        String objCodeColumn = "OBJ_CODE";
        String diffColumn = "DIFF";
        HashMap<String, String> equipmentsMap = (HashMap) readColumnsFromCsv(filename, objCodeColumn, diffColumn);

        // Navigate to 'Batch Meter Readings'
        WebElement metersScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Batch Meter Readings']"));
        metersScreen.click();


            int batch = 0;

            for (Map.Entry<String, String> entry : equipmentsMap.entrySet()) {
                batch++;
                String equipment = entry.getKey();
                String diffValue = entry.getValue();

                // Check for 'pmWorkOrdersButton'
                checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));
                checkAndClickButton(driver, By.id("button-1013-btnInnerEl"));

                WebElement equipmentField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(2)")));
                doubleClickAndInsertValue(driver, equipmentField, equipment);

                WebElement differenceCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(6) > div > div")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", differenceCheckbox);

                try {
                    // Check if the checkbox is already selected
                    if (!differenceCheckbox.isSelected()) {
                        differenceCheckbox.click(); // If not selected, click it
                    }
                } catch (Exception e) {
                    // In case of an exception, use JavaScript to click
                    if (!differenceCheckbox.isSelected()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", differenceCheckbox);
                    }
                }

                WebElement newValueField = driver.findElement(
                        By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(" + batch + ") > tbody > tr > td:nth-of-type(8) > div"));
                doubleClickAndInsertValue(driver, newValueField, diffValue);

                WebElement updateMetersButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > div > div > div > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > a > span > span > span:nth-of-type(2)")));

                System.out.println(updateMetersButton + ": " + updateMetersButton.getText());
                if (batch == 15) {
                    try {
                        updateMetersButton.click();
                    } catch (ElementClickInterceptedException e) {
                        checkAndClickButton(driver, By.xpath("//*[@id='button-1013-btnWrap']"));
                        batch = 0;
                        updateMetersButton.click();
                    }
                    batch = 0;
                    String expectedText = "records were processed successfully.";
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[3]/div/div/div/div/div/div[1]/div/span[2]"), expectedText));
                }
            }

    }

    public static HashMap<String, String> readColumnsFromCsv(String filename, String keyColumn, String valueColumn) throws IOException {
        HashMap<String, String> columnMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Read the header line
            String[] headers = line.split(",");
            int keyIndex = -1;
            int valueIndex = -1;

            // Find the index of the columns
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(keyColumn)) {
                    keyIndex = i;
                }
                if (headers[i].trim().equalsIgnoreCase(valueColumn)) {
                    valueIndex = i;
                }
            }

            if (keyIndex == -1 || valueIndex == -1) {
                throw new IllegalArgumentException("Required columns not found in CSV file.");
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > keyIndex && values.length > valueIndex) {
                    columnMap.put(values[keyIndex].trim(), values[valueIndex].trim());
                }
            }
        }
        return columnMap;
    }

    @Test
    public void GenerateWOs() throws InterruptedException {
        GoThroughMenu(driver, loginPage);
        // Initial check for 'pmWorkOrdersButton'
        // Navigate to 'Batch Meter Readings'
        WebElement GenerateWOsScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Generate WOs']"));
        GenerateWOsScreen.click();

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div/div/div/div/div/div/div[2]/div/div/div[2]/div[2]/div/table/tbody/tr/td/div/div/div/div[3]/div/div/a/span/span/span[2]")));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(80));
        // Scroll to the button using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        // Click the button using JavaScript to bypass WebDriver's checks
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        // Wait until the element is present and visible in the DOM
        WebElement targetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div")));
       // targetElement.click();

        String baseXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[%d]/tbody/tr/td[2]/div/div";
        int shift = 0;


        while(true) {
            // Iterate over the table numbers to generate and click checkboxes
            for (int i = 1+shift; i <= 5+shift; i++) { // Assuming there are 3 tables
                // Generate the XPath for the current table
                String xpath = String.format(baseXPath, i+shift);
                // Wait for the checkbox to be visible
                WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                // Scroll to the checkbox (if necessary)
                //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
                // Check the checkbox if it is not already checked
                if (!checkbox.isSelected()) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                    checkbox.click();
                } else {
                   shift++;
                }
            }
            //Scroll to the last record to load next 50 records to the screen
            WebElement lastRecord = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[50]/tbody/tr/td[2]/div/div"));
            // Scroll to the element using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", lastRecord);
            Thread.sleep(1000);
            //Scroll back
            WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div"));
            js.executeScript("arguments[0].scrollIntoView(true);", element1);
            Thread.sleep(1000);
            String buttonXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/table/tbody/tr/td/div/div/div/div[1]/div/div/a/span/span/span[2]";

            // Wait for the button to be visible
            WebElement Generatebutton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));

            // Scroll to the button (if necessary)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Generatebutton);

            // Click the button
            try {
                button.click();
            } catch (Exception e) {
                // Fallback to JavaScript click if the standard click fails
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Generatebutton);
            }
            String elementXPath = "/html/body/div[6]/div/div[2]/div/div/a[1]/span/span/span[2]";
            checkAndClickButton(driver, By.xpath(elementXPath));
        }
    }

    @Test
    public void GenerateWOs2() throws InterruptedException {
        GoThroughMenu(driver, loginPage);
        // Initial check for 'pmWorkOrdersButton'
        // Navigate to 'Batch Meter Readings'
        WebElement GenerateWOsScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Generate WOs']"));
        GenerateWOsScreen.click();

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div/div/div/div/div/div/div[2]/div/div/div[2]/div[2]/div/table/tbody/tr/td/div/div/div/div[3]/div/div/a/span/span/span[2]")));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(80));
        // Scroll to the button using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        // Click the button using JavaScript to bypass WebDriver's checks
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

     //Path for record on Generate WO screen
        String baseXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[%d]/tbody/tr/td[2]/div/div";
        //String baseXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div";
        int startRecord = 1;
        while(true) {
            // Iterate over the table numbers to generate and click checkboxes
            for (int i = startRecord; i <= startRecord+10; i++) { // Assuming there are 3 tables
                // Error Window path
                String elementXPath = "/html/body/div[6]/div/div[2]/div/div/a[1]/span/span/span[2]";
                checkAndClickButton(driver, By.xpath(elementXPath));
                String xpath = String.format(baseXPath, i);
                // Wait for the checkbox to be visible
                WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                // Scroll to the checkbox (if necessary)
                //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
                // Check the checkbox if it is not already checked
                if (!checkbox.isSelected()) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                    checkbox.click();
                } else {
                    startRecord++;
                }
            }
            //Scroll to the last record to load next 50 records to the screen
            WebElement lastRecord = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[50]/tbody/tr/td[2]/div/div"));
            // Scroll to the element using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", lastRecord);
            Thread.sleep(1000);
            //Scroll back
            WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div"));
            js.executeScript("arguments[0].scrollIntoView(true);", element1);
            Thread.sleep(1000);
            String buttonXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/table/tbody/tr/td/div/div/div/div[1]/div/div/a/span/span/span[2]";

            // Wait for the button to be visible
            WebElement Generatebutton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));

            // Scroll to the button (if necessary)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Generatebutton);

            // Click the button
            try {
                button.click();
            } catch (Exception e) {
                // Fallback to JavaScript click if the standard click fails
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Generatebutton);
            }

        }
    }

    @Test
    public void GenerateWOs3() throws InterruptedException {
        GoThroughMenu(driver, loginPage);
        // Initial check for 'pmWorkOrdersButton'
        // Navigate to 'Batch Meter Readings'
        WebElement GenerateWOsScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Generate WOs']"));
        GenerateWOsScreen.click();

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div/div/div/div/div/div/div[2]/div/div/div[2]/div[2]/div/table/tbody/tr/td/div/div/div/div[3]/div/div/a/span/span/span[2]")));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(80));
        // Scroll to the button using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        // Click the button using JavaScript to bypass WebDriver's checks
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        WebElement targetElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div")));
        // targetElement.click();

        String baseXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[%d]/tbody/tr/td[2]/div/div";
        int shift = 0;
        HashSet<Integer> shiftSize = new HashSet<>();

        while(true) {
            // Iterate over the table numbers to generate and click checkboxes
            for (int i = 1+shiftSize.size(); i <= 11+shiftSize.size(); i++) { // Assuming there are 3 tables
                // Generate the XPath for the current table
                String xpath = String.format(baseXPath, i+shift);
                // Wait for the checkbox to be visible
                WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                // Scroll to the checkbox (if necessary)
                //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
                // Check the checkbox if it is not already checked
                if (!checkbox.isSelected()) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                    checkbox.click();
                } else {
                    shiftSize.add(shift++);
                }
            }
            //Scroll to the last record to load next 50 records to the screen
            WebElement lastRecord = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[50]/tbody/tr/td[2]/div/div"));
            // Scroll to the element using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", lastRecord);
            Thread.sleep(1000);
            //Scroll back
            WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[2]/table[1]/tbody/tr/td[2]/div/div"));
            js.executeScript("arguments[0].scrollIntoView(true);", element1);
            Thread.sleep(1000);
            String buttonXPath = "/html/body/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div/div/div/div[3]/div/div/div/div[1]/div[2]/div[2]/div/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/table/tbody/tr/td/div/div/div/div[1]/div/div/a/span/span/span[2]";

            // Wait for the button to be visible
            WebElement Generatebutton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));

            // Scroll to the button (if necessary)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Generatebutton);

            // Click the button
            try {
                button.click();
            } catch (Exception e) {
                // Fallback to JavaScript click if the standard click fails
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Generatebutton);
            }
            String elementXPath = "/html/body/div[6]/div/div[2]/div/div/a[1]/span/span/span[2]";
            checkAndClickButton(driver, By.xpath(elementXPath));
        }
    }

    // Method to check and click a button if present
    private void checkAndClickButton(WebDriver driver, By locator) {
        try {
            WebElement button = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            button.click();
        } catch (TimeoutException e) {
            System.out.println("TimeoutException: Element not clickable " + locator);
            // Continue execution even if timeout occurs
        } catch (NoSuchElementException | ElementNotInteractableException e) {
            System.out.println(e.getClass().getSimpleName() + ": Element not interactable or not found " + locator);
            // Element not found or not interactable, proceed with the test
        }
    }

    @Test
    public void metersReadingUpdateWithoutBatches() throws InterruptedException {
        GoThroughMenu(driver, loginPage);
        String filename = "c:\\Users\\oluneva\\Desktop\\CMBC\\Performance Test\\Minor_PPM_Objects_Meter_Due.csv"; // Replace with the actual CSV file path
        String columnName = "ppo_object";
        HashSet<String> equipmentsSet = (HashSet) readColumnFromCsv(filename, columnName);
        WebElement MetersScreen = driver.findElement(By.xpath("//span[@class='x-tree-node-text ' and text()='Batch Meter Readings']"));
        MetersScreen.click();

        Thread.sleep(3000);
        int batch = 0;
        // Now you are inside the iframe, locate the element
        for (String equipment: equipmentsSet
        ) {
            batch ++;
            WebElement equipmentField1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(1) > tbody > tr > td:nth-of-type(2)")));
            //equipmentField1.submit();
            doubleClickAndInsertValue(driver,equipmentField1, equipment);

            WebElement DiffrenceCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(1) > tbody > tr > td:nth-of-type(6) > div > div")));

            // Scroll the element into view if it is not already
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DiffrenceCheckbox);
            Thread.sleep(2000);
            // Click the button using JavaScript if normal click fails
            try {
                DiffrenceCheckbox.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", DiffrenceCheckbox);
            }

            WebElement newValueField = driver.findElement(By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(2) > table:nth-of-type(1) > tbody > tr > td:nth-of-type(8) > div"));
            doubleClickAndInsertValue(driver,newValueField, "8000" );

            WebElement UpdateMetersButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("html > body > div:nth-of-type(1) > div > div:nth-of-type(2) > div > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div > div > div > div:nth-of-type(3) > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > div > div > div > div > div > div > div > div > div > div > div > div:nth-of-type(1) > div > div > a > span > span > span:nth-of-type(2)")));

            System.out.println(UpdateMetersButton + ": " + UpdateMetersButton.getText());
            try {

                    UpdateMetersButton.click();

            } catch(ElementClickInterceptedException e){

                // Click the corresponding button
                WebElement pmWorkOrdersButton = driver.findElement(By.xpath("//*[@id='button-1013-btnWrap']"));
                pmWorkOrdersButton.click();
                // Adding a short sleep to ensure the action completes before next steps
                // Attempt to click the UpdateMetersButton again
                UpdateMetersButton.click();
            }
            String expectedText = "records were processed successfully.";
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[3]/div/div/div/div/div/div[1]/div/span[2]"), expectedText));

        }
    }

    public Set<String> readColumnFromCsv(String filename, String columnName) {
        Set<String> resultSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerRead = false;
            int columnIndex = -1;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (!headerRead) {
                    for (int i = 0; i < columns.length; i++) {
                        if (columns[i].trim().equals(columnName)) {
                            columnIndex = i;
                            break;
                        }
                    }
                    headerRead = true;
                } else if (columnIndex >= 0 && columnIndex < columns.length) {
                    resultSet.add(columns[columnIndex].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public void doubleClickAndInsertValue(WebDriver driver, WebElement element, String value) {
        try {
            // Wait for the visibility of the element

            // Perform double-click action
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();

            // Insert the value
            actions.sendKeys(value).perform();

            // Optionally, perform other actions as needed (e.g., submit form)
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void partUpdate() throws InterruptedException {
        GoThroughMenu(driver, loginPage);
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