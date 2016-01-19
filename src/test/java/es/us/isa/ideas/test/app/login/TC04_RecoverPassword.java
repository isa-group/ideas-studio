package es.us.isa.ideas.test.app.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import static es.us.isa.ideas.test.utils.TestCase.deleteCurrentGmailEmail;
import static es.us.isa.ideas.test.utils.TestCase.getAutotesterEmail;
import static es.us.isa.ideas.test.utils.TestCase.getCurrentUrl;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.getJs;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC04_RecoverPassword extends es.us.isa.ideas.test.utils.TestCase {

    private static final Logger LOG = Logger.getLogger(TC04_RecoverPassword.class
            .getName());
    private static boolean testResult = true;
    private static String email = "";
    private static String password = "";

    @BeforeClass
    public static void setUp() throws InterruptedException {
        LOG.log(Level.INFO, "Init TC05_RecoverPassword...");
        logout();
    }

    @AfterClass
    public static void tearDown() throws InterruptedException {
        LOG.log(Level.INFO, "TC05_RecoverPassword finished");
        logout();
    }

    @After
    public void tearDownTest() {
        LOG.log(Level.INFO, "testResult value: {0}", testResult);
    }

    @Test
    public void step01_goToHomePage() {
        testResult = IdeasStudioActions.goHomePage();
        assertTrue(testResult);
    }

    @Test
    public void step01_loadSeleniumUserEmailProperty() {
        email = getAutotesterEmail();
        testResult = validatePropertyValues(email);
        assertTrue(testResult);
    }
    
    @Test
    public void step02_loadResetPasswordPage() {
        
        waitForVisibleSelector("#dontRememberLogin");
        List<WebElement> elements = getWebDriver().findElements(By.cssSelector("#dontRememberLogin > a"));
        if (elements.size() > 1) {
            elements.get(1).click();
            testResult = getWebDriver().getCurrentUrl().contains("/security/useraccount/resetPassword");
        } else {
            testResult = false;
        }
        
        assertTrue(testResult);
        
    }

    @Test
    public void step03_resetPasswordSubmit() throws InterruptedException {
        
        waitForVisibleSelector("#email");
        getJs().executeScript("jQuery('#email').val('"+ email +"');"); // avoid #email not clickable
        getJs().executeScript("jQuery('#submit').click();");
        
        Thread.sleep(2000);

        waitForVisibleSelector("#pagesContent > h2");
        testResult = getWebDriver()
                .findElement(By.cssSelector("#pagesContent > h2")).getText()
                .toLowerCase().contains("thank you for using e3");

        assertTrue(testResult);

    }

    @Test
    public void step04_loginToGmail() throws InterruptedException {

        getWebDriver().get("https://www.gmail.com");

//        waitForVisibleSelector("#Email");
//
//        getExpectedActions().sendKeys(By.cssSelector("#Email"), getAutotesterEmail());
//        getExpectedActions().click(By.cssSelector("#next"));
//        getExpectedActions().sendKeys(By.cssSelector("#Passwd"), getAutotesterEmailPassword());
//        getExpectedActions().click(By.cssSelector("#signIn"));

        Thread.sleep(2000);

        testResult = getCurrentUrl().contains("mail.google.com/mail/u");
        assertTrue(testResult);

    }
    
    @Test
    public void step06_validateRegisterByEmail() throws InterruptedException {

        String selectorConfirmationEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
        waitForVisibleSelector(selectorConfirmationEmail);
        getExpectedActions().click(By.cssSelector(selectorConfirmationEmail)); // opening

        waitForVisibleSelector(".ads");
        String urlConfirmation = (String) getJs()
                .executeScript(
                        "return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

        Thread.sleep(1000);
        deleteCurrentGmailEmail();

        Thread.sleep(2000);
        getWebDriver().get(urlConfirmation);

        String selectorModalTitle = "#message > div > div > div.modal-header > h4";
        waitForVisibleSelector(selectorModalTitle);
        String modalTitle = TestCase.getWebDriver()
                .findElement(By.cssSelector(selectorModalTitle)).getText();

        testResult = "Account validated successfully".equals(modalTitle);
        assertTrue(testResult);

    }

    @Test
    public void step07_copyUserPassword() throws InterruptedException {

        getWebDriver().get("https://www.gmail.com");

        String selectorEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
        waitForVisibleSelector(selectorEmail);
        getExpectedActions().click(By.cssSelector(selectorEmail));

        LOG.info("Opening \'Welcome to IDEAS\' email");

        waitForVisibleSelector(".gs");
        String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
                + "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
        String pass = (String) getJs().executeScript(scriptCopyPass);

        LOG.log(Level.INFO, "Copying user password ''{0}''", pass);

        testResult = !pass.equals("");
        assertTrue(testResult);

    }

    @Test
    public void step08_loginWithCopiedPassword() throws InterruptedException {

        Thread.sleep(2000);

        waitForVisibleSelector(".gs");
        String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
                + "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
        password = (String) getJs().executeScript(scriptCopyPass);

        setAutotesterPassword(password);

        Thread.sleep(1000);
        deleteCurrentGmailEmail();

        Thread.sleep(2000);

        testResult = loginWithParams(getAutotesterUser(), password);
        assertTrue(testResult);

    }
}
