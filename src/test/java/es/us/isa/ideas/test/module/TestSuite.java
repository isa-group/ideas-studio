package es.us.isa.ideas.test.module;

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.experimental.results.PrintableResult.testResult;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    es.us.isa.ideas.test.module.plaintext.TestSuite.class
})
public class TestSuite {
    
    private static String user = "";
	private static String pass = "";
	private static boolean testResult = true;
    private static final Logger LOG = Logger.getLogger(TestSuite.class.getName());
   
	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "#### Starting TestModules TestSuite...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "#### TestModules TestSuite finished");
	}
    
    @Test
	public void step01_goHomePage() {
//		testResult = IdeasStudioActions.goHomePage();
System.out.println("asjdoifjaosdjfoasidf");
		assertTrue(testResult);
	}

//	@Test
//	public void step02_loadSeleniumUserProperties() {
//		user = getSeleniumAutotesterUser();
//		pass = getSeleniumAutotesterPassword();
//
//		testResult = validatePropertyValues(user, pass);
//
//		assertTrue(testResult);
//	}
//
//	@Test
//	public void step03_loginWithSeleniumProperties() throws InterruptedException {
//
//		System.out.println(user);
//		System.out.println(pass);
//
//		if (validatePropertyValues(user, pass)) {
//			testResult = loginWithParams(user, pass);
//		} else {
//			LOG.info("Selenium user properties couldn\'t be loaded from " + getSeleniumPropFile());
//		}
//
//		assertTrue(testResult);
//	}
//	
//	@Test
//	public void step02_IAgreeAgreementTestModule() throws InterruptedException {
//		
//		LOG.info("TC02_IAgreeAgreementTestModule :: step02_IAgreeAgreementTestModule...");
//
//		IdeasStudioActions.executeCommands("testModule iagree-agreement-language");
//
//		boolean result = IdeasStudioActions.checkTestModuleOkResult(TestCase
//				.getWebDriver());
//
//		if (result) {
//			LOG.log(Level.INFO, "test OK");
//		} else {
//			LOG.log(Level.SEVERE, "test FALSE");
//		}
//
//		assertTrue(result);
//		
//		LOG.info("TC02_IAgreeAgreementTestModule :: step02_IAgreeAgreementTestModule finished");
//
//	}

}
