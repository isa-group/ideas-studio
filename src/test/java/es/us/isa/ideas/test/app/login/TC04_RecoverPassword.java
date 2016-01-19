package es.us.isa.ideas.test.app.login;

import es.us.isa.ideas.test.utils.ExpectedActions;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import static es.us.isa.ideas.test.utils.TestCase.getAutotesterEmail;
import static es.us.isa.ideas.test.utils.TestCase.getAutotesterEmailPassword;
import static es.us.isa.ideas.test.utils.TestCase.getCurrentUrl;
import static es.us.isa.ideas.test.utils.TestCase.getExpectedActions;
import static es.us.isa.ideas.test.utils.TestCase.getWebDriver;
import static es.us.isa.ideas.test.utils.TestCase.waitForVisibleSelector;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC05_RecoverPassword extends es.us.isa.ideas.test.utils.TestCase {

    private static final Logger LOG = Logger.getLogger(TC05_RecoverPassword.class
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
    public void step02_resetPasswordResponseWithoutException() {

        waitForVisibleSelector("#dontRememberLogin > a:nth-child(3)");
        getJs().executeScript("jQuery('#dontRememberLogin > a:nth-child(3)').click();");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC05_RecoverPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        waitForVisibleSelector("#email");
        getJs().executeScript("jQuery('#email').val('"+ email +"');"); // avoid #email not clickable
        
        waitForVisibleSelector("#submit");
        getJs().executeScript("jQuery('#submit').click();");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TC05_RecoverPassword.class.getName()).log(Level.SEVERE, null, ex);
        }

        waitForVisibleSelector("#pagesContent > h2");
        testResult = getWebDriver()
                .findElement(By.cssSelector("#pagesContent > h2")).getText()
                .toLowerCase().equals("Thank you for using E3!");

        assertTrue(testResult);

    }

    @Test
    public void step04_loginToGmail() {

        getWebDriver().get("https://www.gmail.com");

        waitForVisibleSelector("#Email");

        ExpectedActions action = getExpectedActions();
        action.sendKeys(By.cssSelector("#Email"), getAutotesterEmail());
        action.click(By.cssSelector("#next"));
        action.sendKeys(By.cssSelector("#Passwd"), getAutotesterEmailPassword());
        action.click(By.cssSelector("#signIn"));

        testResult = getCurrentUrl().contains("mail.google.com/mail/u");
        assertTrue(testResult);

    }

    @Test
    public void step06_copyUserPassword() {

        getWebDriver().get("https://www.gmail.com");

        String selectorEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
        waitForVisibleSelector(selectorEmail);
        getExpectedActions().click(By.cssSelector(selectorEmail));

        LOG.info("Opening \'Welcome to IDEAS\' email");

        waitForVisibleSelector(".gs");
        String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
                + "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
        String password = (String) getJs().executeScript(scriptCopyPass);

        LOG.info("Copying user password \'" + password + "\'");

        testResult = !password.equals("");

        assertTrue(testResult);

    }

    @Test
    public void step07_loginWithCopiedPassword() throws InterruptedException {

        Thread.sleep(2000);

        waitForVisibleSelector(".gs");
        String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
                + "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
        password = (String) getJs().executeScript(scriptCopyPass);

        Thread.sleep(2000);

        setAutotesterPassword(password);

        testResult = loginWithParams(getAutotesterUser(), password);
        assertTrue(testResult);

    }
}
