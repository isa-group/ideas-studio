package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
    static final WebDriverWait wait = new WebDriverWait(driver, 10);
    static final JavascriptExecutor js = (JavascriptExecutor) driver;

    // Getters
    public static WebDriver getWebDriver() {
        return driver;
    }

    public static WebDriverWait getWebDriverWait() {
        return wait;
    }

    public static JavascriptExecutor getJs() {
        return js;
    }

    public T clickOnClickableElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException ex) {
            // nothing
        }

        element.click();
        return (T) this;
    }

    public T clickOnNotClickableElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException ex) {
            // nothing
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0," + element.getLocation().x + ")");
        element.click();
        return (T) this;
    }

    public T clickOnNotClickableLocator(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (NoSuchElementException ex) {
            // nothing
        }

        WebElement element = driver.findElement(locator);

        return clickOnNotClickableElement(element);
    }

    /**
     * Sets content into element field.
     *
     * @param element
     * @param content
     * @return
     */
    public T sendKeysWithWait(WebElement element, CharSequence... content) {

        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException ex) {
            // nothing
        }

        element.clear();
        element.sendKeys(content);

        return (T) this;

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
        
        try {
            wait.until(e);
        } catch (NoSuchElementException ex) {
            // nothing
        }
        

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
    public void close() {
        getWebDriver().close();
    }

}
