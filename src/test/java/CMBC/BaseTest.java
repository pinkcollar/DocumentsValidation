package CMBC;

import CMBC_code.AsureLogin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
public class BaseTest {
    String env = "CMBC-DEV";
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set the path to the EdgeDriver executable
        System.setProperty("webdriver.edge.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\msedgedriver.exe");

        // Initialize EdgeDriver
        driver = new EdgeDriver();
        // Initialize the AsureLogin page object

    }
    @Test
    public void login(){
        String locatorsFile = "CMBC\\Asure-login-" + env +".properties";
        AsureLogin page = new AsureLogin(driver, locatorsFile).get();
        page.isLoaded();
        driver = new EdgeDriver();
        page.login(driver, env);
    }

//    @AfterClass
//    public void tearDownDriver(){
//        if(driver != null){
//            driver.quit();
//        }
//    }
}
