package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceManagerPage;
import es.us.isa.ideas.test.app.utils.IdeasURLType;
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
    @FindBy(xpath = "//*[@id=\"loginOKPanel\"]/div/div/div[1]/h4")
    WebElement modalSuccessHeaderTitle;
    
    @FindBy(css = "#loginFailPanel > div > div > div.modal-header > h4")
    WebElement modalErrorHeaderTitle;
    
    @FindBy(css = "#loginFailPanel > div > div > div.modal-footer > button")
    WebElement modalErrorCloseButton;
    
    @FindBy(id = "statusPanel")
    WebElement registerFormValidation;
    
    @FindBy(className = "goToApp")
    WebElement goToAppButton;

    static final Logger LOG = Logger.getLogger(RegisterPage.class.getName());
    static final String URL = TestProperty.getBaseUrl() + IdeasURLType.SETTINGS_NEW_USER_URL;

    public static RegisterPage navigateTo() {
        PageObject.getWebDriver().get(URL);
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // click
    public RegisterPage clickOnSaveChanges() {
        clickOnNotClickableElement(saveChangesButton);
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
    
    public EditorPage clickOnModalErrorCloseButton() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkspaceManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        clickOnNotClickableElement(modalErrorCloseButton);

        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    public RegisterSocialGooglePage clickOnGoToApp() {
        clickOnNotClickableElement(goToAppButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
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
            PageObject.waitForElementVisible(page.modalErrorHeaderTitle, 10);
            
            TEST_RESULT = element.getText().equals("Sign up error");
            Assert.assertTrue(TEST_RESULT);
            
            page.clickOnModalErrorCloseButton();
            
            TEST_RESULT = currentPageContainsURLType(IdeasURLType.SETTINGS_NEW_USER_URL);
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
                Thread.sleep(1000); // animation
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            page.clickOnSaveChanges();

            // Modal confirmation
            PageObject.waitForElementVisible(page.registerFormValidation, 10);
            PageObject.waitForElementVisible(page.modalSuccessHeaderTitle, 10);
            String validationText = page.registerFormValidation.getText();
            String modalText = page.modalSuccessHeaderTitle.getText();
            TEST_RESULT = modalText.contains("Account created successfully") && !validationText.contains("The email address you entered is already in use");
            Assert.assertTrue(TEST_RESULT);

            // Email login
            PageObject.getWebDriver().get("https://www.gmail.com");
            RegisterSocialGooglePage pageGoogle = PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
            pageGoogle.clickOnSignInDifferentAccountAnchor()
                .clickOnSignInAddNewAccountAnchor()
                .typeUsername(email)
                .clickOnNext()
                .typePassword(emailPass)
                .clickOnSignIn();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }
            
            TEST_RESULT = RegisterSocialGooglePage.confirmEmail();
            Assert.assertTrue(TEST_RESULT);

            // Open generated password email
            String password = RegisterSocialGooglePage.getPasswordInEmail();
            TEST_RESULT = !password.equals("");
            Assert.assertTrue(TEST_RESULT);

            // Updated test password in properties file
            TestProperty.setTestRegisterUserPassword(password);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Delete current email
            pageGoogle.clickOnDeleteEmailButton()
                .expectDifferentUrl();
            
            // Login with generated password
            LoginPage.testLogin(user, password);

            TEST_RESULT = PageObject.currentPageContainsURLType(IdeasURLType.EDITOR_URL);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

    }

}
