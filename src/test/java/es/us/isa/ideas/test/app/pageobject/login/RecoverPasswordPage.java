package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.TestCase;
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

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RecoverPasswordPage extends RegisterSocialGooglePage {

    // FORM
    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "submit")
    WebElement submitButton;
    
    @FindBy(xpath = "//*[@id=\"loginLoader\"]/div/h2")
    WebElement confirmationTitle;

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
        emailField.sendKeys(email);
        return PageFactory.initElements(getWebDriver(), RecoverPasswordPage.class);
    }

    // Tests
    public static void testRecoverPassword(CharSequence email, CharSequence emailPass, String user) {
        new RegisterTestCase().testRecoverPassword(email, emailPass, user);
    }

    private static class RegisterTestCase extends TestCase {
        
        public void testRecoverPassword(CharSequence email, CharSequence emailPass, String user) {

            RecoverPasswordPage pageRecover = RecoverPasswordPage.navigateTo();
            RegisterSocialGooglePage pageGoogle = PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
            
            PageObject.waitForElementVisible(pageRecover.emailField, 10);
            pageRecover.typeEmail(email)
                .clickOnSubmit();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecoverPasswordPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Modal confirmation
            PageObject.waitForElementVisible(pageRecover.confirmationTitle, 10);
            TEST_RESULT = pageRecover.confirmationTitle.getText().contains("Please check your email");
            Assert.assertTrue(TEST_RESULT);

            TEST_RESULT = RegisterSocialGooglePage.confirmEmail();
            Assert.assertTrue(TEST_RESULT);
            
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
