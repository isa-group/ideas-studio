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
public class TC04_IAgreeTemplateTestModule extends es.us.isa.ideas.test.utils.TestCase {

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC04_IAgreeTemplateTestModule...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC04_IAgreeTemplateTestModule finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goEditorPage() {
		LOG.info("TC04_IAgreeTemplateTestModule :: step01_goEditorPage...");

		testResult = IdeasStudioActions.goEditorPage();
		assertTrue(testResult);

		LOG.info("TC04_IAgreeTemplateTestModule :: step01_goEditorPage finished");
	}

	@Test
	public void step02_IAgreeTemplateTestModule() throws InterruptedException {

		LOG.info("TC04_IAgreeTemplateTestModule :: step02_IAgreeAgreementTestModule...");

		IdeasStudioActions.executeCommands("testModule iagree-template-language");

		boolean result = IdeasStudioActions.checkTestModuleOkResult(TestCase.getWebDriver());

		if (result) {
			LOG.log(Level.INFO, "test OK");
		} else {
			LOG.log(Level.SEVERE, "test FALSE");
		}

		assertTrue(result);
		LOG.info("TC04_IAgreeTemplateTestModule :: step02_IAgreeAgreementTestModule finished");

	}

}
