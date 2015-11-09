package es.us.isa.ideas.test.module.plaintext;

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

import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC06_RemoveWorkspace extends es.us.isa.ideas.test.utils.TestCase {

	private static String workspaceName = "Workspace";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TC06_RemoveWorkspace.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC06_RemoveWorkspace...");
		logout();
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC06_RemoveWorkspace finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goEditorPage() {

		LOG.info("TC06_RemoveWorkspace :: step01_goEditorPage...");

		testResult = IdeasStudioActions.goEditorPage();
		assertTrue(testResult);

		LOG.info("TC06_RemoveWorkspace :: step01_goEditorPage finished");

	}

	@Test
	public void step02_removeWorkspace() throws InterruptedException {

		LOG.info("TC06_RemoveWorkspace :: step02_removeWorkspace...");

		if (IdeasStudioActions.executeCommands("deleteCurrentWorkspace")) {

			TestCase.getWebDriver()
					.get(TestCase.getWebDriver().getCurrentUrl()); // refreshing
	
			TestCase.getExpectedActions().click(By.id("menuToggler"));
			
			testResult = getSelectorLength("#workspacesNavContainer li") > 0;
		
		}
		
		if (testResult) {
			LOG.info("\t :: Workspace \"" + workspaceName
					+ "\" was successfully removed.");
			echoCommandApi("Workspace \"" + workspaceName
					+ "\" was successfully removed.");
		}
		
		assertTrue(testResult);
		LOG.info("TC06_RemoveWorkspace :: step02_removeWorkspace finished");
		
	}

}
