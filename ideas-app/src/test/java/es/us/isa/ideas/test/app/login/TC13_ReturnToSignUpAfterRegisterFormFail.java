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

/**
 * This test will fail until the modal button redirects to login page.
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC13_ReturnToSignUpAfterRegisterFormFail extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC13_ReturnToSignUpAfterRegisterFormFail.class.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC13_ReturnToSignUpAfterRegisterFormFail...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC13_ReturnToSignUpAfterRegisterFormFail finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goToSignUpPage() {
		testResult = IdeasAppActions.goSignUpPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_fillRegisterForm() {
		ExpectedActions action = getExpectedActions();
		String selectorAddress = "#address";

		action.click(By.cssSelector(selectorAddress));
		action.sendKeys(By.cssSelector(selectorAddress), "...");

		testResult = !getInputValueSelector(selectorAddress).equals("");
		assertTrue(testResult);

	}

	@Test
	public void step03_isRedirectedToRegisterPageAfterFailing() {
		getExpectedActions().click(By.cssSelector("#settingsSubmitChanges"));

		String selectorMsgFailed = "#loginFailPanel > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > button:nth-child(1)";
		String btnMsg = "return to login";

		waitForVisibleSelector(selectorMsgFailed);	// animation
		
		testResult = !getTextSelector(selectorMsgFailed).toLowerCase().equals(
				btnMsg);
		assertTrue(testResult);

	}

}
