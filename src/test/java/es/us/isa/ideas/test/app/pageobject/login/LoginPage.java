package es.us.isa.ideas.test.app.pageobject.login;

import es.us.isa.ideas.test.app.pageobject.PageObject;
import es.us.isa.ideas.test.app.pageobject.TestCase;
import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class LoginPage extends PageObject<LoginPage> {

    @FindBy(id = "username")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(className = "btn-twitter")
    WebElement twitterButton;

    @FindBy(className = "btn-google-plus")
    WebElement googleButton;

    static final Logger LOG = Logger.getLogger(LoginPage.class.getName());
    static final String URL = TestProperty.getBaseUrl();

    public static LoginPage navigateTo() {
        //TODO: automatically set base url.
        getWebDriver().get(URL);
        return PageFactory.initElements(getWebDriver(), LoginPage.class);
    }

    // click
    public LoginPage clickOnLogin() {
        clickOnClickableElement(loginButton);
        return PageFactory.initElements(getWebDriver(), LoginPage.class);
    }

    public RegisterSocialTwitterPage clickOnTwitter() {
        clickOnClickableElement(twitterButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialTwitterPage.class);
    }

    public RegisterSocialGooglePage clickOnGoogle() {
        clickOnClickableElement(googleButton);
        return PageFactory.initElements(getWebDriver(), RegisterSocialGooglePage.class);
    }

    // sendKeys
    public LoginPage typeUsername(CharSequence username) {
        usernameField.sendKeys(username);
        return PageFactory.initElements(getWebDriver(), LoginPage.class);
    }

    public LoginPage typePassword(CharSequence password) {
        passwordField.sendKeys(password);
        return PageFactory.initElements(getWebDriver(), LoginPage.class);
    }

    // functionalities
    public LoginPage login(String username, String password) {
        this.typeUsername(username)
            .typePassword(password)
            .clickOnLogin();

        return PageFactory.initElements(getWebDriver(), LoginPage.class);
    }

    // TESTS
    public static void testLogin(String username, String password) {
        new LoginTestCase().testLogin(username, password);
    }

    /**
     * This class implements all tests available in LoginPage.
     */
    private static class LoginTestCase extends TestCase {

        public void testLogin(String username, String password) {

            LoginPage page = LoginPage.navigateTo();

            PageObject.alertWindowConfirm(3);

            page.typeUsername(username)
                .typePassword(password)
                .clickOnLogin();

            WebElement lastElement = PageObject.getWebDriver().findElement(By.id("appFooter"));
            TEST_RESULT = page.getCurrentUrl().contains("editor") && lastElement != null;
            assertTrue(TEST_RESULT);
            LOG.log(Level.INFO, "test_result: {0}", TEST_RESULT);

            if (TEST_RESULT) {
                getWebDriver().navigate().refresh();
            }

        }

    }

}
