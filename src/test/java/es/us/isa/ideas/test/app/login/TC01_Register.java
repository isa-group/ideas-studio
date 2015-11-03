package es.us.isa.ideas.test.app.login;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasAppActions;
import es.us.isa.ideas.test.utils.TestCase;

/**
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_Register extends TestCase {

	private static final Logger LOG = Logger.getLogger(TC01_Register.class
			.getName());
	private static boolean testResult = true;
	private static String name = "";
	private static String email = "";
	private static String phone = "";
	private static String address = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC01_Register...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC01_Register finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goHomePage() {
		testResult = IdeasAppActions.goHomePage();
		assertTrue(testResult);
	}

	@Test
	public void step02_loadAutoTesterProperties() {
		name = getSeleniumAutotesterUser();
		email = getSeleniumUserEmail();
		phone = getSeleniumUserPhone();
		address = getSeleniumUserAddress();

		testResult = validatePropertyValues(name, email, phone, address);

		assertTrue(testResult);
	}

	@Test
	public void step03_register() {
		testResult = IdeasAppActions.registerUser(name, email, phone, address);
		assertTrue(testResult);
	}

	@Test
	public void step04_loginToGmail() {

		getWebDriver().get("gmail.com");

		waitForVisibleSelector("#Email");

		ExpectedActions action = getExpectedActions();
		action.sendKeys(By.cssSelector("#Email"), "ideas.isa.us@gmail.com");
		action.click(By.cssSelector("#next"));
		action.sendKeys(By.cssSelector("#Passwd"), "ideas.isa.us%GOOGLE");
		action.click(By.cssSelector("#signIn"));

		testResult = getCurrentUrl().contains("mail.google.com/mail/u");
		assertTrue(testResult);

	}

	@Test
	public void step05_validateRegisterByEmail() {
		
		String selectorConfirmationEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
		waitForVisibleSelector(selectorConfirmationEmail);
		getExpectedActions().click(By.cssSelector(selectorConfirmationEmail)); // opening

		waitForVisibleSelector(".ads");
		String urlConfirmation = (String) getJs()
				.executeScript(
						"return document.getElementsByClassName('ads')[0].textContent.match(/http.+code=[a-zA-Z0-9\\-]+/i)[0]");

		getWebDriver().get(urlConfirmation);

		// Thread.sleep(3000);

		String selectorModalTitle = "#message > div > div > div.modal-header > h4";
		waitForVisibleSelector(selectorModalTitle);
		String modalTitle = TestCase.getWebDriver()
				.findElement(By.cssSelector(selectorModalTitle)).getText();

		testResult = "Account validated successfully".equals(modalTitle);

		assertTrue(testResult);

	}

	@Test
	public void step06_copyUserPassword() {

		ExpectedActions action = getExpectedActions();

		getWebDriver().get("gmail.com");

		String selectorEmail = "#\\3a 2 > div > div > div.UI tbody tr:first-child td:nth-child(4)";
		waitForVisibleSelector(selectorEmail);
		action.click(By.cssSelector(selectorEmail));

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
		String scriptCopyPass = "var str=document.getElementsByClassName('gs')[0].textContent;"
				+ "return str.match(/([0-9a-zA-Z]+-)+([0-9a-zA-Z]+)/i)[0];";
		String password = (String) getJs().executeScript(scriptCopyPass);

		testResult = loginWithParams(name, password);

		assertTrue(testResult);

	}

}
