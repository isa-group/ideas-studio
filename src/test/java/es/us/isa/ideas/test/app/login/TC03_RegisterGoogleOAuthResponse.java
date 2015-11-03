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
 * [issue:#32] This test will fail until the user is not able to register by
 * Google because of a OAuth response error.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC03_RegisterGoogleOAuthResponse extends
		es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger
			.getLogger(TC03_RegisterGoogleOAuthResponse.class.getName());
	private static boolean testResult = true;
	private static String go_user = "";
	private static String go_pass = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC03_RegisterGoogleOAuthResponse...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC03_RegisterGoogleOAuthResponse finished");
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
	public void step02_loadSeleniumGoogleProperties() {

		go_user = TestCase.getSeleniumGoogleUser();
		go_pass = TestCase.getSeleniumGooglePasswd();

		testResult = validatePropertyValues(go_user, go_pass);
		assertTrue(testResult);

	}

	@Test
	public void step03_registerWithGoogleSeleniumProperties() {

		ExpectedActions action = TestCase.getExpectedActions();
		action.click(By.cssSelector("#go_signin > button"));

		testResult = IdeasAppActions.setUpFormDataGoogleLogin(go_user, go_pass);
		assertTrue(testResult);

	}

}
