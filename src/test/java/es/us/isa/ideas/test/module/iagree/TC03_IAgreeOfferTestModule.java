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
public class TC03_IAgreeOfferTestModule extends es.us.isa.ideas.test.utils.TestCase {

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC03_IAgreeOfferTestModule...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC03_IAgreeOfferTestModule finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goHomePage() {
		
		LOG.info("TC03_IAgreeOfferTestModule :: step01_goHomePage...");
		
		testResult = IdeasStudioActions.goHomePage();
		assertTrue(testResult);
		
		LOG.info("TC03_IAgreeOfferTestModule :: step01_goHomePage finished");
		
	}

	@Test
	public void step02_IAgreeOfferTestModule() throws InterruptedException {
		
		LOG.info("TC03_IAgreeOfferTestModule :: step02_IAgreeOfferTestModule...");

		LOG.log(Level.INFO, "testing iAgree Agreement Module");

		IdeasStudioActions.executeCommands("testModule iagree-offer-language");

		boolean result = IdeasStudioActions.checkTestModuleOkResult(TestCase.getWebDriver());

		if (result) {
			LOG.log(Level.INFO, "test OK");
		} else {
			LOG.log(Level.SEVERE, "test FALSE");
		}

		assertTrue(result);
		LOG.info("TC03_IAgreeOfferTestModule :: step02_IAgreeOfferTestModule finished");

	}

}
