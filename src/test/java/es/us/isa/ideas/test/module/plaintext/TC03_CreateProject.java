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
public class TC03_CreateProject extends es.us.isa.ideas.test.utils.TestCase {

	private static String projectName = "Project";

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
	public void step02_createProject() {
		LOG.info("testCreateProject :: Creating a project...");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}

		TestCase.getExpectedActions().click(By.cssSelector("div#editorSidePanelHeaderAddProject div.dropdown-toggle"));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}
		
		TestCase.getExpectedActions().click(By.linkText("Create Project"));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}

		LOG.info("\t :: Inserting \"" + projectName + "\" as name project.");

		TestCase.getExpectedActions().sendKeys(By.cssSelector("input.form-control.focusedInput"), projectName);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}
		
		TestCase.getExpectedActions().click(By.linkText("Create"));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}
		
		// Refreshing browser
		getWebDriver().navigate().refresh();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.severe(e.getMessage());
		}
		
		waitForVisibleSelector("#projectsTree > ul > li:nth-child(1) > span > a");
		testResult = projectName.equals(getTextSelector("#projectsTree > ul > li:nth-child(1) > span > a"));
		
		String msg = "";
		if (testResult) {
			msg += "\t :: Project \"" + projectName + "\" was successfully created.";
		}
		
		LOG.info(msg);
		echoCommandApi(msg);
		assertTrue(testResult);
		
	}

}
