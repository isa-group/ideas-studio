package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.testcase.PageObject;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import es.us.isa.ideas.test.app.pageobject.testcase.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
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

    public static RegisterPage navigateTo() {
        //TODO: automatically set base url.
        getWebDriver().get(TestProperty.getBaseUrl() + "/settings/user");
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // click
    public RegisterPage clickOnSaveChanges() {
        saveChangesButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterPage.class);
    }

    // sendKeys
    public RegisterPage typeName(CharSequence name) {
        sendKeysWithWait(nameField, name);
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
    public static void testRegister(CharSequence name, CharSequence email,
        CharSequence phone, CharSequence address) {
        new RegisterTestCase().testRegister(name, email, phone, address);
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
        public void testRegister(CharSequence name, CharSequence email,
            CharSequence phone, CharSequence address) {

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

            WebElement element = page.modalErrorHeaderTitle;
            getWebDriverWait().until(ExpectedConditions.visibilityOf(element));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            TEST_RESULT = element.getText().equals("Sign up error");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            Assert.assertTrue(TEST_RESULT);

        }

    }

}
