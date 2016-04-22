package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriverWait;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RecoverPasswordPage extends PageObject<RecoverPasswordPage> {

    // FORM
    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "submit")
    WebElement submitButton;

    // MODAL
    static final Logger LOG = Logger.getLogger(RecoverPasswordPage.class.getName());
    static final String URL = TestProperty.getBaseUrl() + "/security/useraccount/resetPassword";

    public static RecoverPasswordPage navigateTo() {
        getWebDriver().get(URL);
        return PageFactory.initElements(getWebDriver(), RecoverPasswordPage.class);
    }

    // click
    public RecoverPasswordPage clickOnSubmit() {
        clickOnClickableElement(submitButton);
        return PageFactory.initElements(getWebDriver(), RecoverPasswordPage.class);
    }

    // sendKeys
    public RecoverPasswordPage typeEmail(CharSequence email) {
        sendKeysWithWait(emailField, email);
        return PageFactory.initElements(getWebDriver(), RecoverPasswordPage.class);
    }

    // Tests
    public static void testRecoverPassword(CharSequence email, CharSequence emailPass, String user) {
        new RegisterTestCase().testRecoverPassword(email, emailPass, user);
    }

    private static class RegisterTestCase extends TestCase {
        
        public void testRecoverPassword(CharSequence email, CharSequence emailPass, String user) {

            RecoverPasswordPage page = RecoverPasswordPage.navigateTo().typeEmail(email);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            page.clickOnSubmit();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Modal confirmation
            By locator = By.cssSelector("#pagesContent > h2");
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

            String result = getWebDriver().findElement(locator).getText().toLowerCase();
            TEST_RESULT = result.contains("thank you for using e3");
            Assert.assertTrue(TEST_RESULT);

            // Email login
            getWebDriver().get("https://www.gmail.com");
//            locator = By.id("Email");
//            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                LOG.severe(ex.getMessage());
//            }
//            PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class)
//                .typeUsername(email)
//                .clickOnNext()
//                .typePassword(emailPass)
//                .clickOnSignIn();
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                LOG.severe(ex.getMessage());
//            }
//            TEST_RESULT = getWebDriver().getCurrentUrl().contains("mail.google.com");
//            Assert.assertTrue(TEST_RESULT);

            // Open email
            String selectorConfirmationEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
            locator = By.cssSelector(selectorConfirmationEmail);
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            getWebDriver().findElement(locator).click(); // opening
            locator = By.cssSelector(".ads");
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

            // Parse url confirmation
            String urlConfirmation = (String) getJs()
                .executeScript(
                    "return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Delete current email
            locator = By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div");
            getWebDriver().findElement(locator).click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Go to confirmation url
            getWebDriver().get(urlConfirmation);
            String selectorModalTitle = "#message > div > div > div.modal-header > h4";
            locator = By.cssSelector(selectorModalTitle);
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            String modalTitle = getWebDriver().findElement(locator).getText();

            TEST_RESULT = "Account validated successfully".equals(modalTitle);
            Assert.assertTrue(TEST_RESULT);

            // Open generated password email
            getWebDriver().get("https://www.gmail.com");
            String selectorEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
            locator = By.cssSelector(selectorEmail);
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            getWebDriver().findElement(locator).click();

            // Copy parsed password
            locator = By.cssSelector(".gs");
            getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
                + "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
            String password = (String) getJs().executeScript(scriptCopyPass);

            TEST_RESULT = !password.equals("");
            Assert.assertTrue(TEST_RESULT);

            // Updated test password in properties file
            TestProperty.setTestUserPassword(password);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Delete current email
            locator = By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div");
            getWebDriver().findElement(locator).click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Login with generated password
            LoginPage.testLogin(user, password);

            TEST_RESULT = page.getCurrentUrl().contains("app/editor");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

    }

}
