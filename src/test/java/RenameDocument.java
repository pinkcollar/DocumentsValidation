import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class RenameDocument {
    WebDriver driver = new ChromeDriver();
    String configFilePath = "AdministrationDocument.config";
    DocumentsAdministrationPage documentPage = new DocumentsAdministrationPage(driver, configFilePath);
    private static final String URL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSDOCU%26SYSTEM_FUNCTION_NAME%3DBSDOCU&LogicalId=lid://infor.eam.eam";

    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\oluneva\\IdeaProjects\\DocsValidation\\src\\main\\resources\\chromedriver.exe");

    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(20));

    PartsPage partsPage = new PartsPage(driver);
    AsureLogin asureLogin = new AsureLogin(driver);
    @Test
    public void test1() throws InterruptedException {
        documentPage.get();
        asureLogin.login(driver);
    }
}
