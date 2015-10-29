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
 * This test will fail until resetting password produces an exception
 * [issue:#27]
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC12_ResetPassword extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TC12_ResetPassword.class
			.getName());
	private static boolean testResult = true;
	private static String email = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC12_ResetPassword...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC12_ResetPassword finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goToHomePage() {
		testResult = IdeasAppActions.goHomePage();
		assertTrue(testResult);
	}

	@Test
	public void step01_loadSeleniumUserEmailProperty() {
		email = TestCase.getSeleniumUserEmail();
		testResult = validatePropertyValues(email);
		assertTrue(testResult);
	}

	@Test
	public void step02_resetPasswordResponseWithoutException() {

		ExpectedActions action = TestCase.getExpectedActions();
		String selectorMsgError = "#pagesContent > p:nth-child(1) > code";

		LOG.log(Level.INFO, "Loading \'don\'t remember password\' page");
		action.click(By.cssSelector("#dontRememberLogin > a:nth-child(3)"));
		LOG.log(Level.INFO, "introducing email to reset: " + email);
		action.sendKeys(By.cssSelector("#email"), email);
		LOG.log(Level.INFO, "submitting form");
		action.click(By.cssSelector("#submit"));

		waitForVisibleSelector(selectorMsgError);
		boolean containsException = TestCase.getWebDriver()
				.findElement(By.cssSelector(selectorMsgError)).getText()
				.toLowerCase().contains("exception".toLowerCase());

		testResult = !containsException;

		assertTrue(testResult);

	}
}
