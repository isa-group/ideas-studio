package es.us.isa.ideas.test.app.pageobject;

import es.us.isa.ideas.test.app.utils.IdeasURLType;
import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 * @param <T>
 */
public class PageObject<T> {

    static final WebDriver driver = new ChromeDriver();
    static final Timeouts timeouts = driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    static final JavascriptExecutor js = (JavascriptExecutor) driver;

    private static final Logger LOG = Logger.getLogger(TestProperty.class.getName());

    // Getters
    public static WebDriver getWebDriver() {
        return driver;
    }

    public static JavascriptExecutor getJs() {
        return js;
    }

    // Functionalities
    public static void navigateTo(String relativeUrl) {

        String absoluteUrl = TestProperty.getBaseUrl() + relativeUrl;
        String currentUrl = getWebDriver().getCurrentUrl();

        if (!currentUrl.contains(absoluteUrl)) {
            System.out.println("driver location : " + absoluteUrl);
            getWebDriver().get(absoluteUrl);
        }

    }

    public T clickOnClickableElement(WebElement element) {
        PageObject.waitForElementVisible(element, 10);
        element.click();
        return (T) this;
    }

    public T clickOnNotClickableElement(WebElement element) {
        PageObject.waitForElementVisible(element, 10);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
        return (T) this;
    }

    public T clickOnNotClickableLocator(By locator) {
        WebElement element = driver.findElement(locator);
        PageObject.waitForElementVisible(element, 10);
        return clickOnNotClickableElement(element);
    }

    public T expectDifferentUrl() {

        final String previousURL = getWebDriver().getCurrentUrl();
        ExpectedCondition e;
        e = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return (!d.getCurrentUrl().equals(previousURL));
            }
        };

        return (T) this;

    }

    /**
     * Logout user.
     *
     * @return
     */
    public static PageObject logout() {
        getWebDriver().get(TestProperty.getBaseUrl() + "/j_spring_security_logout");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PageObject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return PageFactory.initElements(driver, PageObject.class);
    }

    /**
     * Get the current URL from a PageObject instance.
     *
     * @return
     */
    public String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public String getStatusCode(String url) {

        String ret = "";
        Object statusCode = (Object) getJs().executeScript(""
            + "var res;"
            + "jQuery.ajax({"
            + "  url: '" + url + "',"
            + "  data: {},"
            + "  async: false,"
            + "  complete: function(xhr, statusText){"
            + "    res = xhr.status;"
            + "  }"
            + "});"
            + "return res;");

        if (statusCode != null) {
            ret += statusCode.toString();
        }
        return ret;

    }

    /**
     * Close current WebDriver.
     */
    public static void close() {
        getWebDriver().close();
    }
    
    public static void waitForElementVisible(WebElement element, int seconds) {
        new WebDriverWait(PageObject.getWebDriver(), seconds)
            .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Tries to confirm an alert message window in the specified time.
     *
     * @param seconds
     */
    public static void alertWindowConfirm(int seconds) {

        try {

            // Wait 3 seconds
            new WebDriverWait(PageObject.getWebDriver(), seconds)
                .until(ExpectedConditions.alertIsPresent());

            // Get the Alert
            Alert alert = PageObject.getWebDriver().switchTo().alert();
            alert.accept();

        } catch (NoAlertPresentException | TimeoutException ex) {
            LOG.severe(ex.getMessage());
        }

    }
    
    public static boolean currentPageContainsURLType(IdeasURLType urlType) {
        return PageObject.getWebDriver().getCurrentUrl().contains(urlType.toString());
    }

}
