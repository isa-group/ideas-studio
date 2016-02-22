package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.testcase.PageObject;
import es.us.isa.ideas.test.app.pageobject.editor.EditorPage;
import static es.us.isa.ideas.test.app.pageobject.login.RegisterPage.LOG;
import static es.us.isa.ideas.test.app.pageobject.testcase.PageObject.getWebDriverWait;
import es.us.isa.ideas.test.app.pageobject.testcase.TestCase;
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
public class RegisterSocialTwitterPage extends PageObject<RegisterSocialTwitterPage> {

    @FindBy(id = "username_or_email")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "allow")
    WebElement loginButton;

    static final Logger LOG = Logger.getLogger(RegisterSocialTwitterPage.class.getName());

    public static RegisterSocialTwitterPage navigateTo() {
        //TODO: automatically set base url.
        logout();
        PageFactory.initElements(getWebDriver(), LoginPage.class).clickOnTwitter();
        return PageFactory.initElements(getWebDriver(), RegisterSocialTwitterPage.class);
    }

    public RegisterSocialTwitterPage clickOnLogin() {
        loginButton.click();
        return PageFactory.initElements(getWebDriver(), RegisterSocialTwitterPage.class);
    }

    public RegisterSocialTwitterPage typeUsername(String username) {
        usernameField.sendKeys(username);
        return PageFactory.initElements(getWebDriver(), RegisterSocialTwitterPage.class);
    }

    public RegisterSocialTwitterPage typePassword(String password) {
        passwordField.sendKeys(password);
        return PageFactory.initElements(getWebDriver(), RegisterSocialTwitterPage.class);
    }

    public static void testTwitterSocialLogin(String twUser, String twPass) {
        new TwitterRegisterTestCase().testRegister(twUser, twPass);
    }

    public EditorPage login(String twUser, String twPass) {

        return PageFactory.initElements(getWebDriver(), EditorPage.class);
    }

    private static class TwitterRegisterTestCase extends TestCase {

        public void testRegister(String twUser, String twPass) {

            RegisterSocialTwitterPage page = RegisterSocialTwitterPage.navigateTo()
                .typeUsername(twUser)
                .typePassword(twPass)
                .clickOnLogin();

        }

    }

}
