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
import org.openqa.selenium.support.ui.ExpectedConditions;

import es.us.isa.ideas.test.utils.ExpectedActions;
import es.us.isa.ideas.test.utils.IdeasStudioActions;
import es.us.isa.ideas.test.utils.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC02_CreateWorkspace extends es.us.isa.ideas.test.utils.TestCase {

	private static String workspaceName = "Workspace";

	private static boolean testResult = true;
	private static final Logger LOG = Logger.getLogger(TestCase.class.getName());

	@BeforeClass
	public static void setUp() throws InterruptedException {
		LOG.log(Level.INFO, "Init TC02_CreateWorkspace...");
	}

	@AfterClass
	public static void tearDown() {
		LOG.log(Level.INFO, "TC02_CreateWorkspace finished");
	}

	@After
	public void tearDownTest() {
		LOG.info("testResult value: " + testResult);
	}

	@Test
	public void step01_goEditorPage() {
		testResult = IdeasStudioActions.goEditorPage();
		assertTrue(testResult);
	}

	@Test
	public void step02_createWorkspace() {
		
		ExpectedActions action = TestCase.getExpectedActions();
		
		action.getWait()
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuToggler")));

		LOG.info("testCreateWorkspace :: Creating a workspace...");

		action.click(By.id("menuToggler"));
		action.click(By.className("addWorkspace"));

		LOG.info("\t :: Inserting name \"" + workspaceName + "\" for workspace.");

		action.sendKeys(By.cssSelector("input.form-control.focusedInput"), workspaceName);
		action.click(By.linkText("Create"));

//		Thread.sleep(500);
		
		waitForVisibleSelector("#appMainContentBlocker");
		action.click(By.cssSelector("#appMainContentBlocker"));

		LOG.info("\t :: Workspace \"" + workspaceName + "\" was successfully created.");
		echoCommandApi("Workspace \"" + workspaceName + "\" was successfully created.");
	}

}
