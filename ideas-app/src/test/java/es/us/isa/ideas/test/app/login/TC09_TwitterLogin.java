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

import es.us.isa.ideas.test.utils.IdeasAppActions;

/**
 * This test uses 'selenium.properties' to specify the Twitter user name and password.
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC09_TwitterLogin extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TC09_TwitterLogin.class
			.getName());
	private static boolean testResult = true;
	private static String tw_user = "";
	private static String tw_pass = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC09_TwitterLogin...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC09_TwitterLogin finished");
	}

	@Test
	public void step01_goHomePage() {
		testResult = IdeasAppActions.goHomePage();
		assertTrue(testResult);
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step02_loadTwitterSeleniumUserProperties() {
		tw_user = getSeleniumTwitterUser();
		tw_pass = getSeleniumTwitterPasswd();

		testResult = validatePropertyValues(tw_user, tw_pass);

		assertTrue(testResult);
	}

	@Test
	public void step03_loginWithTwitterSeleniumProperties() {
		testResult = IdeasAppActions
				.setUpFormDataTwitterLogin(tw_user, tw_pass);
		assertTrue(testResult);
	}

}
