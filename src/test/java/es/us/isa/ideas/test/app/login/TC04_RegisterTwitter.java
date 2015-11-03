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
import es.us.isa.ideas.test.utils.TestCase;

/**
 * 
 * @author feserafim
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC04_RegisterTwitter extends es.us.isa.ideas.test.utils.TestCase {
	
	private static final Logger LOG = Logger.getLogger(TC04_RegisterTwitter.class
			.getName());
	private static boolean testResult = true;
	private static String tw_user = "";
	private static String tw_pass = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC04_RegisterTwitter...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC04_RegisterTwitter finished");
	}
	
	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}
	
	@Test
	public void step01_loadSeleniumTwitterProperties() {
		
		tw_user = TestCase.getSeleniumTwitterUser();
		tw_pass = TestCase.getSeleniumTwitterPasswd();
		
		testResult = validatePropertyValues(tw_user, tw_pass);
		assertTrue(testResult);
		
	}
	
	@Test
	public void step02_loginWithTwitterSeleniumProperties() {
		testResult = IdeasAppActions.registerTwitterUser(tw_user, tw_pass);
		assertTrue(testResult);
	}

}
