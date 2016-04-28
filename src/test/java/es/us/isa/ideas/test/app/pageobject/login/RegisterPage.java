package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
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
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RegisterPage extends PageObject<RegisterPage> {

    // FORM
    @FindBy(id = "name")
    WebElement nameField;

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "phone")
    WebElement phoneField;

    @FindBy(id = "address")
    WebElement addressField;

    @FindBy(id = "settingsSubmitChanges")
    WebElement saveChangesButton;

    // MODAL
    @FindBy(css = "#loginFailPanel > div > div > div.modal-header > h4")
    WebElement modalErrorHeaderTitle;

    static final Logger LOG = Logger.getLogger(RegisterPage.class.getName());
    static final String URL = TestProperty.getBaseUrl() + "/settings/user";

    public static RegisterPage navigateTo() {
        getWebDriver().get(URL);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // click
    public RegisterPage clickOnSaveChanges() {
        saveChangesButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // sendKeys
    public RegisterPage typeName(CharSequence name) {
        nameField.sendKeys(name);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    public RegisterPage typeEmail(CharSequence email) {
        emailField.sendKeys(email);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    public RegisterPage typePhone(CharSequence phone) {
        phoneField.sendKeys(phone);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    public RegisterPage typeAddress(CharSequence address) {
        addressField.sendKeys(address);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // others
    public WebElement getModalErrorHeaderTitle() {
        return modalErrorHeaderTitle;
    }

    // Tests
    public static void testRegisterWithErrors(CharSequence name, CharSequence email,
        CharSequence phone, CharSequence address) {
        new RegisterTestCase().testRegisterWithErrors(name, email, phone, address);
    }

    public static void testRegister(CharSequence name, CharSequence email, CharSequence emailPass,
        CharSequence phone, CharSequence address, String user) {
        new RegisterTestCase().testRegister(name, email, emailPass, phone, address, user);
    }

    private static class RegisterTestCase extends TestCase {

        /**
         * This test expects to show a modal error message.
         *
         * @param name
         * @param email
         * @param phone
         * @param address
         */
        public void testRegisterWithErrors(CharSequence name, CharSequence email,
            CharSequence phone, CharSequence address) {

            RegisterPage page = RegisterPage.navigateTo()
                .typeName(name)
                .typeEmail(email)
                .typePhone(phone)
                .typeAddress(address)
                .clickOnSaveChanges();

            WebElement element = page.modalErrorHeaderTitle;
            new WebDriverWait(PageObject.getWebDriver(), 10)
                .until(ExpectedConditions.visibilityOf(element));
            
            TEST_RESULT = element.getText().equals("Sign up error");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

        public void testRegister(CharSequence name, CharSequence email, CharSequence emailPass,
            CharSequence phone, CharSequence address, String user) {

            RegisterPage page = RegisterPage.navigateTo()
                .typeName(name)
                .typeEmail(email)
                .typePhone(phone)
                .typeAddress(address);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnSaveChanges();

            // Modal confirmation
            By locator = By.id("statusPanel");
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String statusPanelText = getWebDriver().findElement(locator).getText();
            TEST_RESULT = !statusPanelText.contains("The email address you entered is already in use");
            Assert.assertTrue(TEST_RESULT);

            // Email login
            getWebDriver().get("https://www.gmail.com");
            locator = By.id("Email");
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class)
                .typeUsername(email)
                .clickOnNext()
                .typePassword(emailPass)
                .clickOnSignIn();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }
            TEST_RESULT = getWebDriver().getCurrentUrl().contains("mail.google.com");
            Assert.assertTrue(TEST_RESULT);

            // Open email
            String selectorConfirmationEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
            locator = By.cssSelector(selectorConfirmationEmail);
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            getWebDriver().findElement(locator).click(); // opening
            locator = By.cssSelector(".ads");
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Parse url confirmation
            String urlConfirmation = (String) getJs()
                .executeScript(
                    "return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Delete current email
            locator = By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div");
            getWebDriver().findElement(locator).click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Go to confirmation url
            getWebDriver().get(urlConfirmation);
            String selectorModalTitle = "#message > div > div > div.modal-header > h4";
            locator = By.cssSelector(selectorModalTitle);
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String modalTitle = getWebDriver().findElement(locator).getText();

            TEST_RESULT = "Account validated successfully".equals(modalTitle);
            Assert.assertTrue(TEST_RESULT);

            // Open generated password email
            getWebDriver().get("https://www.gmail.com");
            String selectorEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
            locator = By.cssSelector(selectorEmail);
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            getWebDriver().findElement(locator).click();

            // Copy parsed password
            locator = By.cssSelector(".gs");
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Delete current email
            locator = By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div");
            getWebDriver().findElement(locator).click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Login with generated password
            LoginPage.testLogin(user, password);

            TEST_RESULT = page.getCurrentUrl().contains("app/editor");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

    }

}
