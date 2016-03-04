package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RegisterSocialGooglePage extends PageObject<RegisterSocialGooglePage> {

    @FindBy(id = "Email")
    WebElement usernameField;

    @FindBy(id = "Passwd")
    WebElement passwordField;

    @FindBy(id = "next")
    WebElement nextButton;

    @FindBy(id = "signIn")
    WebElement loginButton;

    @FindBy(id = "goToApp")
    WebElement goToAppButton;

    @FindBy(id = "submit_approve_access")
    WebElement submitApproveAccessButton;

    static final Logger LOG = Logger.getLogger(RegisterSocialGooglePage.class.getName());

    public static RegisterSocialGooglePage navigateTo() {
        logout();
        PageFactory.initElements(getWebDriver(), LoginPage.class).clickOnGoogle();
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnNext() {
        nextButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnSignIn() {
        loginButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnGoToApp() {
        clickOnNotClickableElement(goToAppButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnSubmitApproveAccess() {
        clickOnClickableElement(submitApproveAccessButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typeUsername(CharSequence goUser) {
        sendKeysWithWait(usernameField, goUser);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typePassword(CharSequence goPass) {
        sendKeysWithWait(passwordField, goPass);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public static void testGoogleSocialRegister(String goUser, String goPass) {
        new GoogleRegisterTestCase().testGoogleSocialRegister(goUser, goPass);
    }

    public static void testGoogleSocialLogin(String goUser, String goPass) {
        new GoogleRegisterTestCase().testGoogleSocialLogin(goUser, goPass);
    }
    
    public static void testGoogleSocialLogout() {
        new GoogleRegisterTestCase().testGoogleSocialLogout();
    }

    private static class GoogleRegisterTestCase extends TestCase {

        public void testGoogleSocialRegister(String goUser, String goPass) {

            RegisterSocialGooglePage page = RegisterSocialGooglePage.navigateTo();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            page.typeUsername(goUser)
                    .clickOnNext()
                    .typePassword(goPass)
                    .clickOnSignIn();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            try {
                page.clickOnSubmitApproveAccess();
            } catch (NoSuchElementException ex) {
                // nothing
            }

            try {
                page.clickOnGoToApp();
            } catch (NoSuchElementException ex) {
                // nothing
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            TEST_RESULT = page.getCurrentUrl().contains("app/editor");

            if (TEST_RESULT) {
                new EditorPage().consoleEchoCommand("User logged with Google account \"" + goUser + "\".");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

        /**
         * This method logs with an already register Google account. The only
         * difference between this method and "testGoogleSocialRegister" is that
         * it doesn't expect to a "goToApp" button.
         *
         * @param goUser
         * @param goPass
         */
        public void testGoogleSocialLogin(String goUser, String goPass) {

            RegisterSocialGooglePage page = RegisterSocialGooglePage.navigateTo();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            page.typeUsername(goUser)
                    .clickOnNext()
                    .typePassword(goPass)
                    .clickOnSignIn();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            try {
                page.clickOnSubmitApproveAccess();
            } catch (NoSuchElementException ex) {
                // nothing
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }

            TEST_RESULT = page.getCurrentUrl().contains("app/editor");

            if (TEST_RESULT) {
                new EditorPage().consoleEchoCommand("User logged with Google account \"" + goUser + "\".");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }
        
        public void testGoogleSocialLogout() {

            RegisterSocialGooglePage page = RegisterSocialGooglePage.navigateTo();
            By locator = By.xpath("//*[@id=\"gb\"]/div[1]/div[1]/div[2]/div[4]/div[1]/a/span");
            PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            
            try {
                Thread.sleep(2000);  
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterSocialGooglePage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            page.clickOnNotClickableLocator(locator);
            
            locator = By.xpath("//*[@id=\"gb_71\"]");
            PageObject.getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            
            try {
                Thread.sleep(2000);  
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterSocialGooglePage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            page.clickOnNotClickableLocator(locator);
            
            try {
                Thread.sleep(5000);  
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterSocialGooglePage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TEST_RESULT = page.getCurrentUrl().contains("accounts.google.com");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);
            
        }
    }

}
