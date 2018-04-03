package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getJs;
import static es.us.isa.ideas.test.app.pageobject.PageObject.getWebDriver;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.utils.IdeasURLType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.NoSuchElementException;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class RegisterSocialGooglePage extends RegisterPage {

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
    //
    // User menu
    @FindBy(xpath = "//*[@id=\"gb\"]/div[2]/div[1]/div[2]/div[4]/div[1]/a")
    WebElement userMenuButton;
    
    @FindBy(xpath = "//*[@id=\"gb_71\"]")
    WebElement userMenuLogoutButton;
    
    // Email list
    @FindBy(css = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)")
    WebElement confirmationEmail;
    
    // Email
    @FindBy(xpath = "//*[@id=\":5\"]/div[2]/div[1]/div/div[2]/div[3]/div/div")
    WebElement deleteEmailButton;
    
    @FindBy(className = "ads")
    WebElement emailConfirmationContainer;
    
    @FindBy(className = "gs")
    WebElement emailPasswordContainer;
    
    @FindBy(xpath = "//*[@id=\"account-chooser-link\"]")
    WebElement signInDifferentAccountAnchor;
    
    @FindBy(xpath = "//*[@id=\"account-chooser-add-account\"]")
    WebElement signInAddNewAccountAnchor;

    static final Logger LOG = Logger.getLogger(RegisterSocialGooglePage.class.getName());

    public static RegisterSocialGooglePage navigateTo() {
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

    public RegisterSocialGooglePage clickOnSignInDifferentAccountAnchor() {
        clickOnClickableElement(signInDifferentAccountAnchor);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnSignInAddNewAccountAnchor() {
        clickOnClickableElement(signInAddNewAccountAnchor);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage clickOnSubmitApproveAccess() {
        clickOnClickableElement(submitApproveAccessButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }
    
    public RegisterSocialGooglePage clickOnUserMenuButton() {
        clickOnNotClickableElement(userMenuButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }
    
    public RegisterSocialGooglePage clickOnUserMenuLogoutButton() {
        clickOnNotClickableElement(userMenuLogoutButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }
    
    public RegisterSocialGooglePage clickOnConfirmationEmail() {
        clickOnClickableElement(confirmationEmail);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }
    
    public RegisterSocialGooglePage clickOnDeleteEmailButton() {
        clickOnClickableElement(deleteEmailButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typeUsername(CharSequence goUser) {
        usernameField.sendKeys(goUser);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typePassword(CharSequence goPass) {
        passwordField.sendKeys(goPass);
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
    
    public static boolean confirmEmail() {
        
        RegisterPage pageRegister = PageFactory.initElements(PageObject.getWebDriver(), RegisterPage.class);
        RegisterSocialGooglePage pageGoogle = PageFactory.initElements(PageObject.getWebDriver(), RegisterSocialGooglePage.class);
        boolean result = false;
        
        PageObject.getWebDriver().get("https://www.gmail.com");
        result = getWebDriver().getCurrentUrl().contains("mail.google.com");
        Assert.assertTrue(result);

        // Open email
        PageObject.waitForElementVisible(pageGoogle.confirmationEmail, 10);
        pageGoogle.clickOnConfirmationEmail()
            .expectDifferentUrl();

        // Parse url confirmation
        PageObject.waitForElementVisible(pageGoogle.emailConfirmationContainer, 10);
        String urlConfirmation = (String) getJs()
            .executeScript(
                "return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

        // Delete current email
        pageGoogle.clickOnDeleteEmailButton()
            .expectDifferentUrl();

        // Go to confirmation url
        PageObject.getWebDriver().get(urlConfirmation);
        PageObject.alertWindowConfirm(3);

        pageRegister.clickOnGoToApp()
            .expectDifferentUrl();

        result = PageObject.currentPageContainsURLType(IdeasURLType.LOGIN_URL);
        Assert.assertTrue(result);
        
        return result;
    }
    
    public static String getPasswordInEmail() {
        
        RegisterSocialGooglePage pageGoogle = PageFactory.initElements(PageObject.getWebDriver(), RegisterSocialGooglePage.class);
        
        PageObject.getWebDriver().get("https://www.gmail.com");
        pageGoogle.clickOnConfirmationEmail()
            .expectDifferentUrl();

        // Copy parsed password
        PageObject.waitForElementVisible(pageGoogle.emailPasswordContainer, 10);
        String jsCopyPass = "" + 
            "var str=document.getElementsByClassName('gs')[0].textContent;" +
            "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
        
        return (String) getJs().executeScript(jsCopyPass);
    }

    private static class GoogleRegisterTestCase extends TestCase {

        public void testGoogleSocialRegister(String goUser, String goPass) {

            RegisterSocialGooglePage page = RegisterSocialGooglePage.navigateTo();

            PageObject.waitForElementVisible(page.usernameField, 10);

            page.typeUsername(goUser)
                .clickOnNext()
                .typePassword(goPass)
                .clickOnSignIn();

//            PageObject.waitForElementVisible(page.submitApproveAccessButton, 10);
            
//            try {
//                Thread.sleep(5000); // allow button animation
//            } catch (InterruptedException ex) {
//                Logger.getLogger(RegisterSocialGooglePage.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            try {
//                page.clickOnSubmitApproveAccess();
//            } catch (NoSuchElementException ex) {
//                // nothing
//            }
//
//            try {
//                page.clickOnGoToApp();
//            } catch (NoSuchElementException ex) {
//                // nothing
//            }
//
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                LOG.severe(ex.getMessage());
            }
            
            EditorPage.navigateTo()
                .expectDifferentUrl();

            TEST_RESULT = PageObject.currentPageContainsURLType(IdeasURLType.EDITOR_URL);

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

            RegisterSocialGooglePage page = PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
            
            PageObject.getWebDriver().get("https://gmail.com");
            
            
            page.clickOnUserMenuButton()
                .clickOnUserMenuLogoutButton();
            
            page.expectDifferentUrl();
            
            TEST_RESULT = page.getCurrentUrl().contains("accounts.google.com");
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);
            
        }
    }

}
