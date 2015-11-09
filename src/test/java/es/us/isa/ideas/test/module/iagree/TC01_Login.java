package es.us.isa.ideas.test.module.iagree;

import static org.junit.Assert.assertTrue;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_Login extends es.us.isa.ideas.test.utils.TestCase {

	private static String user = "";
	private static String pass = "";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC01_Login...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC01_Login finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goHomePage() {
		testResult = IdeasStudioActions.goHomePage();
		assertTrue(testResult);
	}

	@Test
	public void step02_loadSeleniumUserProperties() {
		user = getSeleniumAutotesterUser();
		pass = getSeleniumAutotesterPassword();

		testResult = validatePropertyValues(user, pass);

		assertTrue(testResult);
	}

	@Test
	public void step03_loginWithSeleniumProperties() throws InterruptedException {

		System.out.println(user);
		System.out.println(pass);

		if (validatePropertyValues(user, pass)) {
			testResult = loginWithParams(user, pass);
		} else {
			LOG.info("Selenium user properties couldn\'t be loaded from " + getSeleniumPropFile());
		}

		assertTrue(testResult);
	}

}
