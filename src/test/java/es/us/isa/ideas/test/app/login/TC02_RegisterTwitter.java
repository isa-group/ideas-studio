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
import static org.junit.Assert.assertTrue;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC02_RegisterTwitter extends es.us.isa.ideas.test.utils.TestCase {
	
	private static final Logger LOG = Logger.getLogger(TC02_RegisterTwitter.class
			.getName());
	private static boolean testResult = true;
	private static String tw_user = "";
	private static String tw_pass = "";

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC02_RegisterTwitter...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC02_RegisterTwitter finished");
	}
	
	@After
	public void tearDownTest() {
		LOG.log(Level.INFO, "testResult value: {0}", testResult);
	}
	
	@Test
	public void step01_loadSeleniumTwitterProperties() {
		
		tw_user = TestCase.getSeleniumTwitterUser();
		tw_pass = TestCase.getSeleniumTwitterPassword();
		
		testResult = validatePropertyValues(tw_user, tw_pass);
		assertTrue(testResult);
		
	}
	
	@Test
	public void step02_loginWithTwitterSeleniumProperties() throws InterruptedException {
		testResult = IdeasStudioActions.registerTwitterUser(tw_user, tw_pass);
		assertTrue(testResult);
	}

}
