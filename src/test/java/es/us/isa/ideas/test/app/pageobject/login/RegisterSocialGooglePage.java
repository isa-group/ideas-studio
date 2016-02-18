package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.testcase.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import static org.junit.Assert.assertTrue;

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

    public RegisterSocialGooglePage typeUsername(CharSequence goUser) {
        sendKeysWithWait(usernameField, goUser);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    public RegisterSocialGooglePage typePassword(CharSequence goPass) {
        sendKeysWithWait(passwordField, goPass);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }
    
    public static void testGoogleSocialLogin(String goUser, String goPass) {
        new GoogleRegisterTestCase().testGoogleSocialLogin(goUser, goPass);
    }
    
    private static class GoogleRegisterTestCase extends TestCase {

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

            TEST_RESULT = page.getCurrentUrl().contains("app/editor");

            if (TEST_RESULT) {
                new EditorPage().consoleEchoCommand("User logged with Google account \"" + goUser + "\".");
            }

            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);
            assertTrue(TEST_RESULT);

        }

    }

}
