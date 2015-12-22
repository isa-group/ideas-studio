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

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC03_Login extends es.us.isa.ideas.test.utils.TestCase {

	private static final Logger LOG = Logger.getLogger(TC03_Login.class
			.getName());
	private static boolean testResult = true;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC08_Login...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC08_Login finished");
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
		String user = getSeleniumAutotesterUser();
		String pass = getSeleniumAutotesterPassword();

		testResult = validatePropertyValues(user, pass);

		assertTrue(testResult);
	}

	@Test
	public void step03_loginWithSeleniumProperties() throws InterruptedException {
		String user = getSeleniumAutotesterUser();
		String pass = getSeleniumAutotesterPassword();

		testResult = false;

		if (validatePropertyValues(user, pass)) {
			testResult = loginWithParams(user, pass);
		} else {
			LOG.info("Selenium user properties couldn\'t be loaded from "
					+ getSeleniumPropFile());
		}

		assertTrue(testResult);
	}
}
