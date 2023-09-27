import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class BaseTest {
    String env = "AX2";
    WebDriver driver;
    @BeforeMethod
    public void setUp(Method method) {
        this.driver = new EdgeDriver();
    }

    @Test
    public void login(){
        String locatorsFile = "Asure-login-" + env +".properties";
        AsureLogin page = new AsureLogin(driver, locatorsFile).get();
        page.isLoaded();
        page.login(driver, env);
    }

    @AfterClass
    public void tearDownDriver(){
        if(driver != null){
            driver.quit();
        }
    }
}
