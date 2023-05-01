
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;

public class DocumentsAdministrationPage extends AbstractBasePage<DocumentsAdministrationPage>  {

    private static final String URL = "https://mingle-portal.ca1.inforcloudsuite.com/TRANSLINK_DEV/637fada2-ade6-4c51-ac37-5ef13b91bdb2?favoriteContext=FROMEMAIL%3DSHAREPOINT%26USER_FUNCTION_NAME%3DBSDOCU%26SYSTEM_FUNCTION_NAME%3DBSDOCU&LogicalId=lid://infor.eam.eam";
    public DocumentsAdministrationPage(WebDriver driver, String locatorsFile) {
        super(driver, locatorsFile);
    }

    @Override
    protected void isLoaded() throws Error {
        driver.get(URL);
        super.isLoaded();
    }




}
